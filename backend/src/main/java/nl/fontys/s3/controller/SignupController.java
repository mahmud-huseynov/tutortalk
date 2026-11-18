package nl.fontys.s3.controller;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.business.auth.SignupUseCase;
import nl.fontys.s3.domain.auth.SignupRequest;
import nl.fontys.s3.domain.auth.SignupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
    private final SignupUseCase signupUseCase;

    @PostMapping("/tutor")
    public ResponseEntity<SignupResponse> signupTutor(@RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = signupUseCase.signupTutor(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponse);
    }

    @PostMapping("/learner")
    public ResponseEntity<SignupResponse> signupLearner(@RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = signupUseCase.signupLearner(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponse);
    }
}
