package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.JwtRequest;
import com.ibizabroker.lms.entity.JwtResponse;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;
    private final UsersRepository userDao;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /** API đang được JwtController gọi: tạo JwtResponse (user + token) */
    public JwtResponse createJwtToken(JwtRequest jwtRequest) {
        final String username = jwtRequest.getUsername();
        final String password = jwtRequest.getPassword();

        // 1) Xác thực (ném BadCredentialsException nếu sai)
        authenticate(username, password);

        // 2) Tải user + details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Users user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 3) Claims an toàn (KHÔNG dùng Map.of vì cấm null)
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        // SỬA: Đổi "uid" thành "userId" cho nhất quán
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        // name có thể null -> fallback về username
        String safeName = (user.getName() != null && !user.getName().isBlank())
                ? user.getName()
                : user.getUsername();
        claims.put("name", safeName);

        // 4) Tạo token
        String token = jwtUtil.generateToken(userDetails, claims);

        // 5) Trả về response
        return new JwtResponse(user, token);
    }

    /** Dùng khi controller muốn trả Map { "token": "<...>" } */
    public String loginAndIssueToken(String username, String password) {
        // 1) auth
        authenticate(username, password);

        // 2) load details (đủ để ký token) và cả user object để lấy ID
        UserDetails ud = userDetailsService.loadUserByUsername(username);
        Users user = userDao.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found after auth: " + username));


        // 3) claims gọn: roles + username + userId
        Map<String, Object> extra = new HashMap<>();
        extra.put("roles", ud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        extra.put("username", ud.getUsername());
        // THÊM DÒNG NÀY ĐỂ GẮN USERID VÀO TOKEN
        extra.put("userId", user.getUserId());

        return jwtUtil.generateToken(ud, extra);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (DisabledException e) {
            throw new BadCredentialsException("User disabled", e);
        } catch (BadCredentialsException e) {
            throw e; // để controller trả 401
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }
    }
}