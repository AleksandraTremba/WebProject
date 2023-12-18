package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.RecordsMapper;
import ee.taltech.iti0302.okapi.backend.components.RecordsMapperImpl;
import ee.taltech.iti0302.okapi.backend.dto.records.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.enums.RecordType;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class RecordsServiceTest {
    @Mock
    private RecordsRepository recordsRepository;
    @Spy
    private RecordsMapper recordsMapper = new RecordsMapperImpl();
    @InjectMocks
    private RecordsService recordsService;

    private Records buildRecords() {
        Records records = new Records();
        records.setNumberOfCustomers(1);
        records.setNumberOfTimers(2);
        records.setNumberOfGroups(3);
        records.setNumberOfTasks(4);
        return records;
    }

    private RecordsDTO buildRecordsDTO(int customers, int timers, int groups, int tasks) {
        return RecordsDTO.builder()
                .id(1L)
                .numberOfCustomers(customers)
                .numberOfTimers(timers)
                .numberOfGroups(groups)
                .numberOfTasks(tasks)
                .build();
    }


    // #### GET RECORDS ####

    @Test
    void getRecordsSuccessfully() {
        //Given
        Records records = buildRecords();
        given(recordsRepository.findById(1L)).willReturn(Optional.of(records));
        RecordsDTO dto = buildRecordsDTO(1, 2, 3, 4);

        //When
        RecordsDTO response = recordsService.getRecords();

        //Then
        then(recordsRepository).should().findById(1L);
        assertEquals(dto, response);
    }

    // #### GET CUSTOMER AMOUNTS ####
    @Test
    void getCustomersAmountSuccessfully() {
        //Given
        Records records = buildRecords();
        given(recordsRepository.findById(1L)).willReturn(Optional.of(records));

        //When
        String response = recordsService.getCustomersAmount();

        //Then
        then(recordsRepository).should().findById(1L);
        assertEquals("1", response);
    }

    // #### GET TIMERS AMOUNT ####
    @Test
    void getTimersAmountSuccessfully() {
        //Given
        Records records = buildRecords();
        given(recordsRepository.findById(1L)).willReturn(Optional.of(records));

        //When
        String response = recordsService.getTimersNumber();

        //Then
        then(recordsRepository).should().findById(1L);
        assertEquals("2", response);
    }

    // #### GET GROUPS AMOUNT ####
    @Test
    void getGroupsAmountSuccessfully() {
        //Given
        Records records = buildRecords();
        given(recordsRepository.findById(1L)).willReturn(Optional.of(records));

        //When
        String response = recordsService.getGroupsNumber();

        //Then
        then(recordsRepository).should().findById(1L);
        assertEquals("3", response);
    }


    // #### GET TASKS AMOUNT ####
    @Test
    void getTasksAmountSuccessfully() {
        //Given
        Records records = buildRecords();
        given(recordsRepository.findById(1L)).willReturn(Optional.of(records));

        //When
        String response = recordsService.getTasksNumber();

        //Then
        then(recordsRepository).should().findById(1L);
        assertEquals("4", response);
    }

    @Test
    void testUpdateRecordsCustomer() {
        long recordsDataId = 1L;
        Records existingRecords = new Records();
        existingRecords.setNumberOfCustomers(5);
        Mockito.when(recordsRepository.findById(eq(recordsDataId))).thenReturn(Optional.of(existingRecords));

        recordsService.updateRecords(RecordType.CUSTOMERS);

        Mockito.verify(recordsRepository).save(existingRecords);
        assertEquals(6, existingRecords.getNumberOfCustomers());
    }

    @Test
    void testUpdateRecordsNewRecord() {
        long recordsDataId = 1L;
        Mockito.when(recordsRepository.findById(eq(recordsDataId))).thenReturn(Optional.empty());

        recordsService.updateRecords(RecordType.CUSTOMERS);

        Mockito.verify(recordsRepository).save(any(Records.class));
    }

    @Test
    void testUpdateRecordsTimers() {
        long recordsDataId = 1L;
        Records existingRecords = new Records();
        existingRecords.setNumberOfTimers(5);
        Mockito.when(recordsRepository.findById(eq(recordsDataId))).thenReturn(Optional.of(existingRecords));

        recordsService.updateRecords(RecordType.TIMERS);

        Mockito.verify(recordsRepository).save(existingRecords);
        assertEquals(6, existingRecords.getNumberOfTimers());
    }

    @Test
    void testUpdateRecordsTasks() {
        long recordsDataId = 1L;
        Records existingRecords = new Records();
        existingRecords.setNumberOfTasks(8);
        Mockito.when(recordsRepository.findById(eq(recordsDataId))).thenReturn(Optional.of(existingRecords));

        recordsService.updateRecords(RecordType.TASKS);

        Mockito.verify(recordsRepository).save(existingRecords);
        assertEquals(9, existingRecords.getNumberOfTasks());
    }

    @Test
    void testUpdateRecordsGroups() {
        long recordsDataId = 1L;
        Records existingRecords = new Records();
        existingRecords.setNumberOfGroups(3);
        Mockito.when(recordsRepository.findById(eq(recordsDataId))).thenReturn(Optional.of(existingRecords));

        recordsService.updateRecords(RecordType.GROUPS);

        Mockito.verify(recordsRepository).save(existingRecords);
        assertEquals(4, existingRecords.getNumberOfGroups());
    }

}
