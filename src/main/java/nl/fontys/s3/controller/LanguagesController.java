package nl.fontys.s3.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.language.*;
import nl.fontys.s3.domain.language.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/languages")
@AllArgsConstructor
@CrossOrigin
public class LanguagesController {
    private final GetLanguagesUseCase getLanguagesUseCase;
    private final GetLanguageUseCase getLanguageUseCase;
    private final CreateLanguageUseCase createLanguageUseCase;
    private final UpdateLanguageUseCase updateLanguageUseCase;
    private final DeleteLanguageUseCase deleteLanguageUseCase;

    @GetMapping
    public ResponseEntity<GetLanguagesResponse> getLanguages() {
        return ResponseEntity.ok(getLanguagesUseCase.getLanguages());
    }

    @GetMapping("{id}")
    public ResponseEntity<Language> getLanguage(@PathVariable(value = "id") final long id) {
        final GetLanguageResponse languageResponse = getLanguageUseCase.getLanguageById(id);
        if (languageResponse.getLanguage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(languageResponse.getLanguage());
    }

    @PostMapping()
    public ResponseEntity<CreateLanguageResponse> createLanguage(@RequestBody @Valid CreateLanguageRequest request) {
            CreateLanguageResponse response = createLanguageUseCase.createLanguage(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateLanguage(@PathVariable("id") long id,
                                               @RequestBody @Valid UpdateLanguageRequest request) {
        updateLanguageUseCase.updateLanguage(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{languageId}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable int languageId) {
        deleteLanguageUseCase.deleteLanguage(languageId);
        return ResponseEntity.noContent().build();
    }





}
