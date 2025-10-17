package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dto.UserCreateDto;
import com.ibizabroker.lms.dto.UserDto;
import com.ibizabroker.lms.dto.UserUpdateDto;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UsersRepository usersRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return usersRepository.findAll().stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setUserId(user.getUserId());
            dto.setName(user.getName());
            dto.setUsername(user.getUsername());
            dto.setRoles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    // === BỔ SUNG API TẠO MỚI USER TẠI ĐÂY ===
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        if (usersRepository.existsByUsernameIgnoreCase(userCreateDto.getUsername())) {
            throw new IllegalStateException("Username '" + userCreateDto.getUsername() + "' is already taken.");
        }

        Users user = new Users();
        user.setName(userCreateDto.getName());
        user.setUsername(userCreateDto.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));

        Set<Role> roles = userCreateDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new NotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        
        if (roles.isEmpty()) {
            roles.add(roleRepository.findByRoleName("ROLE_USER")
                    .orElseThrow(() -> new NotFoundException("Default role ROLE_USER not found.")));
        }
        
        user.setRoles(roles);

        Users savedUser = usersRepository.save(user);
        
        UserDto dto = new UserDto();
        dto.setUserId(savedUser.getUserId());
        dto.setName(savedUser.getName());
        dto.setUsername(savedUser.getUsername());
        dto.setRoles(savedUser.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDto userDetails) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));

        user.setName(userDetails.getName());
        user.setUsername(userDetails.getUsername());

        if (userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
            Set<Role> newRoles = new HashSet<>();
            for (String roleName : userDetails.getRoles()) {
                Role role = roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền " + roleName));
                newRoles.add(role);
            }
            user.setRoles(newRoles);
        }

        Users updatedUser = usersRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));

        usersRepository.delete(user);
        return ResponseEntity.ok(Map.of("deleted", Boolean.TRUE));
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));

        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
        
        return ResponseEntity.ok(Map.of("newPassword", newPassword));
    }
}