package ee.taltech.iti0302.okapi.backend.services;
import ee.taltech.iti0302.okapi.backend.components.TimerMapper;
import ee.taltech.iti0302.okapi.backend.components.TimerMapperImpl;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerInitDTO;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerDTO;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerResetDTO;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import ee.taltech.iti0302.okapi.backend.exceptions.ApplicationRuntimeException;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;
import ee.taltech.iti0302.okapi.backend.repository.TimerRepository;
import ee.taltech.iti0302.okapi.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private CustomerService customerService;
    @Mock
    private RecordsService recordsService;
    @Mock
    private TimerRepository timerRepository;
    @Spy
    private TimerMapper timerMapper = new TimerMapperImpl();
    @InjectMocks
    private TimerService timerService;

    // ###### BUILDING TIMER ######

    private TimerDTO buildTimerDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, Integer runningTime, Long remaining) {
        return TimerDTO.builder()
                .id(id)
                .startTime(startTime)
                .endTime(endTime)
                .runningTime(runningTime)
                .remainingTime(remaining)
                .build();
    }

    private Timer buildTimer(LocalDateTime startTime, LocalDateTime endTime, Integer runningTime, Long remaining) {
        return new Timer();
    }



    // ###### CREATING TIMER ######

    @Test
    void createWithEmptyCustomerThrowsError() {
        // Given
        Long customerId = null;

        assertThrows(NullPointerException.class, () -> timerService.createTimer(customerId));
    }

    @Test
    void createTimerSuccessfully() {
        // Given
        Long customerId = 1L;

        // use ArgumentCaptor to capture the timer instance passed to save
        ArgumentCaptor<Timer> timerCaptor = ArgumentCaptor.forClass(Timer.class);

        // When
        Long timerId = timerService.createTimer(customerId);

        // Then
        //verify that save was called with the expected timer instance
        verify(timerRepository).save(timerCaptor.capture());

        // Assert the captured Timer instance
        Timer capturedTimer = timerCaptor.getValue();
        assertEquals(customerId, capturedTimer.getCustomerId());
    }

    // ###### STARTING TIMER ######

    @Test
    void startTimerTimerNotFound() {
        TimerDTO dto = null;

        assertThrows(NullPointerException.class, () -> timerService.startTimer(null));
    }

    @Test
    void startTimerSuccessfully() {
        // Given
        TimerDTO timerDTO = buildTimerDTO(1L, null, null, 10, 10L);

        Timer timer = timerMapper.toEntity(timerDTO);


        given(timerRepository.findById(timerDTO.getId())).willReturn(Optional.of(timer));

        // When
        TimerDTO resultDTO = timerService.startTimer(timerDTO);

        // Then
        assertNotNull(resultDTO);
        assertNotNull(resultDTO.getStartTime());
        assertNotNull(resultDTO.getEndTime());
        assertEquals(10, resultDTO.getRunningTime());
        assertEquals(0L, resultDTO.getRemainingTime());
    }


    // ###### STOPPING TIMER ######

    @Test
    void stopTimerTimerNotFound() {
        Long timerId = null;

        assertThrows(NullPointerException.class, () -> timerService.stopTimer(timerId));
    }

    @Test
    void stopTimerSuccessfully() {
        // Given
        Long timerId = 1L;
        TimerDTO timerDTO = buildTimerDTO(timerId, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), 300, 0L);

        Timer savedTimer = timerMapper.toEntity(timerDTO);

        given(timerRepository.findById(timerId)).willReturn(Optional.of(savedTimer));

        // When
        TimerDTO resultDTO = timerService.stopTimer(timerId);

        // Then
        assertNotNull(resultDTO);
        assertNotNull(resultDTO.getRemainingTime());
        assertTrue(resultDTO.getRemainingTime() > 0);

        //verify that save was called with the updated timer instance
        ArgumentCaptor<Timer> timerCaptor = ArgumentCaptor.forClass(Timer.class);
        verify(timerRepository).save(timerCaptor.capture());

        Timer updatedTimer = timerCaptor.getValue();
        assertNotNull(updatedTimer.getEndTime());
        assertEquals(updatedTimer.getRemainingTime(), ChronoUnit.SECONDS.between(LocalDateTime.now(), updatedTimer.getEndTime()));
    }

    // ###### RESET TIMER ######

    @Test
    void resetTimerSuccessfully() {
        // Given
        TimerResetDTO resetDTO = new TimerResetDTO(1L, 1L);
        TimerDTO timerDTO = buildTimerDTO(1L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), 300, 0L);
        Timer existingTimer = timerMapper.toEntity(timerDTO);
        existingTimer.setCustomerId(resetDTO.getCustomerId());

        given(timerRepository.findById(resetDTO.getId())).willReturn(Optional.of(existingTimer));
        given(timerRepository.save(any(Timer.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        TimerDTO resultDTO = timerService.resetTimer(resetDTO);

        // Then
        assertNotNull(resultDTO);

        ArgumentCaptor<Timer> timerCaptor = ArgumentCaptor.forClass(Timer.class);
        verify(timerRepository).save(timerCaptor.capture());

        Timer updatedTimer = timerCaptor.getValue();
        assertNotNull(updatedTimer);
    }

    @Test
    void resetTimerTimerNotFound() {
        TimerResetDTO resetDTO = new TimerResetDTO(null, 1L);

        assertThrows(NullPointerException.class, () -> timerService.resetTimer(resetDTO));
    }

    // ###### TIMER DELETION ######
    @Test
    void deleteTimerSuccessfully() {
        // Given
        Long timerId = 1L;

        // When
        timerService.deleteTimer(timerId);

        // Then
        // Verify that deleteById was called with the expected ID
        verify(timerRepository).deleteById(eq(timerId));
    }

    @Test
    void testGetTimerByIdExists() {
        Timer timer = buildTimer(LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), 300, 0L);

        Mockito.when(timerRepository.findById(eq(1L))).thenReturn(Optional.of(timer));

        TimerDTO result = timerService.getTimerById(1L);

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    void testGetTimerByIdNotExists() {
        Mockito.when(timerRepository.findById(eq(2L))).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> timerService.getTimerById(2L));
    }

}

