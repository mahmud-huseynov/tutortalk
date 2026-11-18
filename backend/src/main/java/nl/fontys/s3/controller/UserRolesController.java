package nl.fontys.s3.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.user.GetUserRoleUseCase;
import nl.fontys.s3.persistence.entity.UserRoleEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-roles")
@AllArgsConstructor
public class UserRolesController {

    private final GetUserRoleUseCase getUserRoleUseCase;

    @RolesAllowed({"LEARNER", "TUTOR", "ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<UserRoleEntity> getUserRole(@PathVariable("id") long userId) {
        UserRoleEntity userRole = getUserRoleUseCase.findRoleByUserId(userId);
        if (userRole == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRole);
    }
}
