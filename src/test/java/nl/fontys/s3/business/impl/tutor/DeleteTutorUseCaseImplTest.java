package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.persistence.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTutorUseCaseImplTest {

    @Mock
    private TutorRepository tutorRepositoryMock;

    @InjectMocks
    private DeleteTutorUseCaseImpl deleteTutorUseCase;

    @Test
    void deleteTutor_tutorFound_shouldDelete() {
        when(tutorRepositoryMock.existsById(1L)).thenReturn(true);

        doNothing().when(tutorRepositoryMock).deleteById(anyLong());

        deleteTutorUseCase.deleteTutor(1L);

        verify(tutorRepositoryMock).deleteById(1L);
    }

    @Test
    void deleteTutor_tutorNotFound_shouldThrowException() {
        assertThrows(InvalidTutorException.class, () -> deleteTutorUseCase.deleteTutor(1L));
    }
}
