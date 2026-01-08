package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dto.ForgotPasswordRequest;
import com.ibizabroker.lms.dto.RegisterRequest;
import com.ibizabroker.lms.dto.ResetPasswordRequest;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.util.JwtUtil;
import com.ibizabroker.lms.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus; // ⭐️ ĐÃ THÊM
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException; // ⭐️ ĐÃ THÊM
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Đảm bảo đã có /api
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UsersRepository usersRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordResetService passwordResetService;

    public AuthController(UsersRepository usersRepo,
                          RoleRepository roleRepo,
                          PasswordEncoder encoder,
                          AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          PasswordResetService passwordResetService) {
        this.usersRepo = usersRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.passwordResetService = passwordResetService;
    }

    // ===== REGISTER =====
    @SuppressWarnings("null")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        String username = req.getUsername().trim().toLowerCase();
        if (usersRepo.existsByUsernameIgnoreCase(username)) {
            return ResponseEntity.status(409).body(Map.of("message", "Username is already taken."));
        }

        Users u = new Users();
        u.setName(req.getName().trim());
        u.setUsername(username);
        u.setPassword(encoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        u.setStudentClass(req.getStudentClass());
        u.setPhoneNumber(req.getPhoneNumber());

        Role userRole = roleRepo.findByRoleName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setRoleName("ROLE_USER");
            return roleRepo.save(r);
        });
        u.addRole(userRole);

        Users saved = usersRepo.save(u);
        return ResponseEntity.created(URI.create("/users/" + saved.getUserId())).build();
    }

    // ===== AUTHENTICATE =====
    public static record LoginRequest(String username, String password) {}
    public static record LoginResponse(String token, Integer userId, String name, List<String> roles) {}

    @SuppressWarnings("null")
    @PostMapping(
            value = "/authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    // ⭐️ SỬA: Đổi kiểu trả về thành ResponseEntity<?>
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest req) {
        if (!StringUtils.hasText(req.username()) || !StringUtils.hasText(req.password())) {
            return ResponseEntity.badRequest().build();
        }

        String username = req.username().trim().toLowerCase();
        Authentication auth;

        // ⭐️ SỬA: Thêm try-catch để bắt lỗi sai mật khẩu
        try {
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, req.password())
            );
        } catch (BadCredentialsException e) {
            // Trả về 401 (Unauthorized) thay vì sập 500
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(Map.of("message", "Tên đăng nhập hoặc mật khẩu không đúng"));
        }
        // ⭐️ KẾT THÚC SỬA

        // Code bên dưới chỉ chạy nếu đăng nhập thành công
        Users u = usersRepo.findByUsernameWithRolesIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found after auth"));

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", u.getUserId());
        claims.put("roles", roles);

        UserDetails principal = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(principal, claims);

        return ResponseEntity.ok(new LoginResponse(token, u.getUserId(), u.getName(), roles));
    }

    // ===== FORGOT PASSWORD =====
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.initiateReset(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Nếu email tồn tại, hướng dẫn khôi phục đã được gửi."));
    }

    // ===== RESET PASSWORD =====
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công."));
    }
}