package nl.fontys.s3.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.business.user.GetUserUseCase;
import nl.fontys.s3.persistence.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final GetUserUseCase getUserUseCase;

    @GetMapping("/username/{username}")
    public UserEntity getUserByUsername(@PathVariable String username) {
        return getUserUseCase.findByUsername(username);
    }
}
