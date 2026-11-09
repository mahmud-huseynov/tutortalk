    package nl.fontys.s3.controller;

    import jakarta.validation.Valid;
    import lombok.AllArgsConstructor;
    import nl.fontys.s3.business.tutor.*;
    import nl.fontys.s3.domain.tutor.*;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/tutors")
    @AllArgsConstructor
    @CrossOrigin
    public class TutorsController {
        private final GetTutorsUseCase getTutorsUseCase;
        private final GetTutorUseCase getTutorUseCase;
        private final CreateTutorUseCase createTutorUseCase;
        private final UpdateTutorUseCase updateTutorUseCase;
        private final DeleteTutorUseCase deleteTutorUseCase;

        @GetMapping
        public ResponseEntity<GetTutorsResponse> getTutors() {
            return ResponseEntity.ok(getTutorsUseCase.getTutors());
        }

        @GetMapping("{id}")
        public ResponseEntity<Tutor> getTutor(@PathVariable(value = "id") final long id) {
            final GetTutorResponse tutorResponse = getTutorUseCase.getTutorById(id);
            if (tutorResponse.getTutor() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(tutorResponse.getTutor());
        }

        @PostMapping()
        public ResponseEntity<CreateTutorResponse> createTutor(@RequestBody @Valid CreateTutorRequest request) {
            CreateTutorResponse response = createTutorUseCase.createTutor(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @PutMapping("{id}")
        public ResponseEntity<Void> updateTutor(@PathVariable("id") long id,
                                                   @RequestBody @Valid UpdateTutorRequest request) {
            updateTutorUseCase.updateTutor(id, request);
            return ResponseEntity.noContent().build();
        }
        @DeleteMapping("{tutorId}")
        public ResponseEntity<Void> deleteTutor(@PathVariable int tutorId) {
            deleteTutorUseCase.deleteTutor(tutorId);
            return ResponseEntity.noContent().build();
        }
    }
