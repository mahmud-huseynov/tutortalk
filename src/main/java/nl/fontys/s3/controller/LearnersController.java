package nl.fontys.s3.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.learner.*;
import nl.fontys.s3.domain.learner.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/learners")
@AllArgsConstructor
@CrossOrigin
public class LearnersController {
    private final GetLearnersUseCase getLearnersUseCase;
    private final GetLearnerUseCase getLearnerUseCase;
    private final CreateLearnerUseCase createLearnerUseCase;
    private final UpdateLearnerUseCase updateLearnerUseCase;
    private final DeleteLearnerUseCase deleteLearnerUseCase;

    @GetMapping
    public ResponseEntity<GetLearnersResponse> getLearners() {
        return ResponseEntity.ok(getLearnersUseCase.getLearners());
    }

    @GetMapping("{id}")
    public ResponseEntity<Learner> getLearner(@PathVariable(value = "id") final long id) {
        final GetLearnerResponse learnerResponse = getLearnerUseCase.getLearnerById(id);
        if (learnerResponse.getLearner() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(learnerResponse.getLearner());
    }

    @PostMapping()
    public ResponseEntity<CreateLearnerResponse> createLearner(@RequestBody @Valid CreateLearnerRequest request) {
        CreateLearnerResponse response = createLearnerUseCase.createLearner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateLearner(@PathVariable("id") long id,
                                            @RequestBody @Valid UpdateLearnerRequest request) {
        updateLearnerUseCase.updateLearner(id, request);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{learnerId}")
    public ResponseEntity<Void> deleteLearner(@PathVariable int learnerId) {
        deleteLearnerUseCase.deleteLearner(learnerId);
        return ResponseEntity.noContent().build();
    }
}
