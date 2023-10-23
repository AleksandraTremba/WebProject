package ee.taltech.iti0302.okapi.backend.components;

import ee.taltech.iti0302.okapi.backend.dto.TimerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import org.springframework.stereotype.Component;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TimerMapper {
    TimerMapper INSTANCE = Mappers.getMapper(TimerMapper.class);

    TimerDTO toDTO(Timer timer);
    Timer toEntity(TimerDTO dto);

}
