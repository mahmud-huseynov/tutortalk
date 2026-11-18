package nl.fontys.s3.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.learner.*;
import nl.fontys.s3.domain.learner.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/learners")
@AllArgsConstructor
public class LearnersController {
    private final GetLearnersUseCase getLearnersUseCase;
    private final GetLearnerUseCase getLearnerUseCase;
    private final CreateLearnerUseCase createLearnerUseCase;
    private final UpdateLearnerUseCase updateLearnerUseCase;
    private final DeleteLearnerUseCase deleteLearnerUseCase;
    private final LearnerStatisticsUseCase learnerStatisticsUseCase;

    @RolesAllowed({"LEARNER", "TUTOR", "ADMIN"})
    @GetMapping
    public ResponseEntity<GetLearnersResponse> getLearners() {
        return ResponseEntity.ok(getLearnersUseCase.getLearners());
    }

    @RolesAllowed({"LEARNER", "TUTOR", "ADMIN"})
    @GetMapping("{id}")
    public ResponseEntity<Learner> getLearner(@PathVariable(value = "id") final long id) {
        final GetLearnerResponse learnerResponse = getLearnerUseCase.getLearnerById(id);
        if (learnerResponse.getLearner() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(learnerResponse.getLearner());
    }

    @RolesAllowed({"LEARNER", "ADMIN"})
    @PostMapping()
    public ResponseEntity<CreateLearnerResponse> createLearner(@RequestBody @Valid CreateLearnerRequest request) {
        CreateLearnerResponse response = createLearnerUseCase.createLearner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RolesAllowed({"LEARNER", "ADMIN"})
    @PutMapping("{id}")
    public ResponseEntity<Void> updateLearner(@PathVariable("id") long id,
                                            @RequestBody @Valid UpdateLearnerRequest request) {
        updateLearnerUseCase.updateLearner(id, request);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("{learnerId}")
    public ResponseEntity<Void> deleteLearner(@PathVariable int learnerId) {
        deleteLearnerUseCase.deleteLearner(learnerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getLearnerCount() {
        long count = learnerStatisticsUseCase.getNumberOfLearners();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
