package com.ibizabroker.lms;

import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initUsers(UsersRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        // ... (Phần initUsers giữ nguyên, không thay đổi) ...
        return args -> {
            Role roleAdmin = roleRepository.findByRoleName("ROLE_ADMIN").orElseGet(() -> {
                Role r = new Role();
                r.setRoleName("ROLE_ADMIN");
                return roleRepository.save(r);
            });
            Role roleUser = roleRepository.findByRoleName("ROLE_USER").orElseGet(() -> {
                Role r = new Role();
                r.setRoleName("ROLE_USER");
                return roleRepository.save(r);
            });

            if (userRepository.findByUsername("admin").isEmpty()) {
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setName("Admin User");
                admin.setEmail("admin@lms.com");
                admin.getRoles().add(roleAdmin);
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                Users u = new Users();
                u.setUsername("user");
                u.setPassword(passwordEncoder.encode("user123"));
                u.setName("Normal User");
                u.setEmail("user@lms.com");
                u.getRoles().add(roleUser);
                userRepository.save(u);
            }
        };
    }
}