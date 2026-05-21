package kz.iitu.Library.web.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.iitu.Library.service.OteuIslamAuthService;
import kz.iitu.Library.web.dto.request.OteuIslamLoginRequest;
import kz.iitu.Library.web.dto.request.OteuIslamRegisterRequest;
import kz.iitu.Library.web.dto.response.OteuIslamAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Registration and login")
public class OteuIslamAuthController {

    private final OteuIslamAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<OteuIslamAuthResponse> register(
            @Valid @RequestBody OteuIslamRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<OteuIslamAuthResponse> login(
            @Valid @RequestBody OteuIslamLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}