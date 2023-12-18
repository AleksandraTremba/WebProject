package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.dto.affirmation.AffirmationDTO;
import ee.taltech.iti0302.okapi.backend.entities.Affirmation;
import ee.taltech.iti0302.okapi.backend.repository.AffirmationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AffirmationServiceTest {

    @Mock
    private AffirmationRepository affirmationRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AffirmationService affirmationService;

    private AffirmationDTO buildAffirmationDTO() {
        return AffirmationDTO.builder()
                .id(1L)
                .affirmation("Affirmation")
                .build();
    }

    private Affirmation buildAffirmation() {
        return new Affirmation();
    }

    @Test
    void testGetAffirmation_ExistingAffirmation() {
        long affirmationId = 1L;
        Affirmation mockAffirmation = buildAffirmation();

        Mockito.when(affirmationRepository.findById(affirmationId)).thenReturn(Optional.of(mockAffirmation));

        String result = affirmationService.getAffirmation();

        assertNull(result);
    }

    @Test
    void testGetAffirmation_NonExistingAffirmation() {
        Affirmation mockAffirmation = buildAffirmation();

        Mockito.when(affirmationRepository.findById(1L)).thenReturn(Optional.of(mockAffirmation));

        String result = affirmationService.getAffirmation();

        assertNull(result);
    }

    @Test
    void testRequestAffirmation_Success() {
        when(affirmationRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(affirmationRepository.save(any(Affirmation.class))).thenReturn(new Affirmation());

        affirmationService.requestAffirmation();

        verify(affirmationRepository, times(1)).findById(any(Long.class));
        verify(affirmationRepository, times(1)).save(any(Affirmation.class));
    }

    @Test
    void testRequestAffirmation_ExistingAffirmation() {
        Affirmation existingAffirmation = buildAffirmation();

        when(affirmationRepository.findById(any(Long.class))).thenReturn(Optional.of(existingAffirmation));
        when(affirmationRepository.save(any(Affirmation.class))).thenReturn(existingAffirmation);

        affirmationService.requestAffirmation();

        verify(affirmationRepository, times(1)).findById(any(Long.class));
        verify(affirmationRepository, times(1)).save(any(Affirmation.class));
    }
}