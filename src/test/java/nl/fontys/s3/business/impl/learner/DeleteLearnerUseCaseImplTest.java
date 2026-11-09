package nl.fontys.s3.business.impl.learner;

import nl.fontys.s3.business.exception.InvalidLearnerException;
import nl.fontys.s3.persistence.LearnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteLearnerUseCaseImplTest {

    @Mock
    private LearnerRepository learnerRepositoryMock;

    @InjectMocks
    private DeleteLearnerUseCaseImpl deleteLearnerUseCase;

    @Test
    void deleteLearner_learnerFound_shouldDelete() {
        when(learnerRepositoryMock.existsById(1L)).thenReturn(true);

        doNothing().when(learnerRepositoryMock).deleteById(anyLong());

        deleteLearnerUseCase.deleteLearner(1L);

        verify(learnerRepositoryMock).deleteById(1L);
    }

    @Test
    void deleteLearner_learnerNotFound_shouldThrowException() {
        assertThrows(InvalidLearnerException.class, () -> deleteLearnerUseCase.deleteLearner(1L));
    }
}
