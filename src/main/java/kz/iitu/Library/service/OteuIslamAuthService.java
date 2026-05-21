package kz.iitu.Library.service;

import kz.iitu.Library.domain.entity.OteuIslamUser;
import kz.iitu.Library.domain.enums.OteuIslamRole;
import kz.iitu.Library.exception.OteuIslamBadRequestException;
import kz.iitu.Library.repository.OteuIslamUserRepository;
import kz.iitu.Library.security.OteuIslamJwtUtil;
import kz.iitu.Library.web.dto.request.OteuIslamLoginRequest;
import kz.iitu.Library.web.dto.request.OteuIslamRegisterRequest;
import kz.iitu.Library.web.dto.response.OteuIslamAuthResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OteuIslamAuthService {

    private static final Logger log = LoggerFactory.getLogger(OteuIslamAuthService.class);

    private final OteuIslamUserRepository userRepository;
    private final OteuIslamJwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public OteuIslamAuthResponse register(OteuIslamRegisterRequest request) {
        log.info("Registering user: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new OteuIslamBadRequestException("Email already registered: " + request.getEmail());
        }

        OteuIslamUser user = OteuIslamUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(OteuIslamRole.USER)
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        log.info("User registered successfully: {}", request.getEmail());
        return new OteuIslamAuthResponse(token, user.getEmail(), user.getRole().name());
    }

    public OteuIslamAuthResponse login(OteuIslamLoginRequest request) {
        log.info("Login attempt: {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        OteuIslamUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new OteuIslamBadRequestException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        log.info("Login successful: {}", request.getEmail());
        return new OteuIslamAuthResponse(token, user.getEmail(), user.getRole().name());
    }
}