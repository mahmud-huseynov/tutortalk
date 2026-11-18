package nl.fontys.s3.business.auth;

import nl.fontys.s3.domain.auth.SignupRequest;
import nl.fontys.s3.domain.auth.SignupResponse;

public interface SignupUseCase {
    SignupResponse signupTutor(SignupRequest signupRequest);
    SignupResponse signupLearner(SignupRequest signupRequest);
}