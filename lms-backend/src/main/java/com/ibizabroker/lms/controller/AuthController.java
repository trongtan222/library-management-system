package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dto.RegisterRequest;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils; // <-- SỬA LỖI TẠI ĐÂY: BỔ SUNG IMPORT
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

  private final UsersRepository usersRepo;
  private final RoleRepository roleRepo;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;

  public AuthController(UsersRepository usersRepo,
                        RoleRepository roleRepo,
                        PasswordEncoder encoder,
                        AuthenticationManager authManager,
                        JwtUtil jwtUtil) {
    this.usersRepo = usersRepo;
    this.roleRepo = roleRepo;
    this.encoder = encoder;
    this.authManager = authManager;
    this.jwtUtil = jwtUtil;
  }

  // ===== REGISTER =====
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

  @PostMapping(
      value = "/authenticate",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest req) {
    if (!StringUtils.hasText(req.username()) || !StringUtils.hasText(req.password())) {
      return ResponseEntity.badRequest().build();
    }

    String username = req.username().trim().toLowerCase();

    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, req.password())
    );

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
}

