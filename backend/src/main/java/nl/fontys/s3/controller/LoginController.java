package nl.fontys.s3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.business.auth.LoginUseCase;
import nl.fontys.s3.domain.auth.LoginRequest;
import nl.fontys.s3.domain.auth.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
}
