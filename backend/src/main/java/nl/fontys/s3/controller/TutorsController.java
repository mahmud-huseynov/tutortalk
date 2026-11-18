package nl.fontys.s3.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.tutor.*;
import nl.fontys.s3.domain.tutor.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tutors")
@AllArgsConstructor
public class TutorsController {
    private final GetTutorsUseCase getTutorsUseCase;
    private final GetTutorUseCase getTutorUseCase;
    private final CreateTutorUseCase createTutorUseCase;
    private final UpdateTutorUseCase updateTutorUseCase;
    private final DeleteTutorUseCase deleteTutorUseCase;

    @RolesAllowed({"LEARNER", "TUTOR", "ADMIN"})
    @GetMapping
    public ResponseEntity<GetTutorsResponse> getTutors() {
        return ResponseEntity.ok(getTutorsUseCase.getTutors());
    }

    @RolesAllowed({"LEARNER", "TUTOR", "ADMIN"})
    @GetMapping("{id}")
    public ResponseEntity<Tutor> getTutor(@PathVariable(value = "id") final long id) {
        final GetTutorResponse tutorResponse = getTutorUseCase.getTutorById(id);
        if (tutorResponse == null || tutorResponse.getTutor() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(tutorResponse.getTutor());
    }

    @PostMapping()
    public ResponseEntity<CreateTutorResponse> createTutor(@RequestBody @Valid CreateTutorRequest request) {
        CreateTutorResponse response = createTutorUseCase.createTutor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @RolesAllowed({"TUTOR", "ADMIN"})
    @PutMapping("{id}")
    public ResponseEntity<Void> updateTutor(@PathVariable("id") long id,
                                               @RequestBody @Valid UpdateTutorRequest request) {
        updateTutorUseCase.updateTutor(id, request);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("{tutorId}")
    public ResponseEntity<Void> deleteTutor(@PathVariable int tutorId) {
        deleteTutorUseCase.deleteTutor(tutorId);
        return ResponseEntity.noContent().build();
    }

}
