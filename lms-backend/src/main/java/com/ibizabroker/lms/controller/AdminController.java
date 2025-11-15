package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dto.UserCreateDto;
import com.ibizabroker.lms.dto.UserDto;
import com.ibizabroker.lms.dto.UserUpdateDto;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.service.UserService; // Import service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Thêm
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor // Thêm
public class AdminController {

    private final UserService userService; // Chỉ inject service

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        Users user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDto userDetails) {
        Users updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("deleted", Boolean.TRUE));
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable Integer id) {
        Map<String, String> response = userService.resetPassword(id);
        return ResponseEntity.ok(response);
    }
}