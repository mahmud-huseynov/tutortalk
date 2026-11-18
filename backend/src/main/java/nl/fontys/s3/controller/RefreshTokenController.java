package nl.fontys.s3.controller;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.business.impl.auth.LoginUseCaseImpl;
import nl.fontys.s3.business.auth.RefreshTokenService;
import nl.fontys.s3.domain.auth.LoginResponse;
import nl.fontys.s3.persistence.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/refresh_tokens")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final LoginUseCaseImpl loginUseCase;

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshAccessToken(@RequestParam("refreshToken") String refreshToken) {
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().build();
        }
        UserEntity user = refreshTokenService.getUserByRefreshToken(refreshToken);
        String newAccessToken = loginUseCase.generateAccessToken(user);
        return ResponseEntity.ok(LoginResponse.builder().accessToken(newAccessToken).build());
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validateRefreshToken(@RequestParam("refreshToken") String refreshToken) {
        boolean isValid = refreshTokenService.validateRefreshToken(refreshToken);
        return ResponseEntity.ok(Map.of("isValid", isValid));
    }

}
