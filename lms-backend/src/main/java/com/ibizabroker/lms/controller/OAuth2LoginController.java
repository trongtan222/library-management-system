package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class OAuth2LoginController {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public OAuth2LoginController(UsersRepository usersRepository, RoleRepository roleRepository, 
                                JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint nhận Google OAuth token từ frontend, tạo/update user và trả về JWT
     * Frontend sẽ gọi Google OAuth trực tiếp, sau đó gửi idToken lên đây
     */
    @PostMapping("/google")
    public ResponseEntity<Map<String, String>> googleLogin(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String name = payload.get("name");
        String googleId = payload.get("googleId");
        String picture = payload.get("picture");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email không hợp lệ"));
        }

        // Tìm hoặc tạo user
        Users user = usersRepository.findByUsername(email).orElseGet(() -> {
            Users newUser = new Users();
            newUser.setUsername(email);
            newUser.setEmail(email);
            newUser.setName(name != null ? name : email);
            
            // Set random password (user không cần biết, chỉ dùng OAuth)
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            
            // Default role USER - lấy từ database
            Role userRole = roleRepository.findByRoleName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role ROLE_USER không tồn tại trong database"));
            newUser.addRole(userRole);
            
            // Lưu Google ID và avatar
            newUser.setGoogleId(googleId);
            newUser.setAvatar(picture);
            
            return usersRepository.save(newUser);
        });

        // Nếu user đã tồn tại, cập nhật Google info
        if (user.getGoogleId() == null || !user.getGoogleId().equals(googleId)) {
            user.setGoogleId(googleId);
            user.setAvatar(picture);
            usersRepository.save(user);
        }

        // Generate JWT với proper UserDetails và claims
        List<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
        
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("roles", roles);
        
        String token = jwtUtil.generateToken(userDetails, claims);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "username", user.getUsername(),
            "name", user.getName(),
            "role", roles.isEmpty() ? "ROLE_USER" : roles.get(0)
        ));
    }
}
