package ee.taltech.iti0302.okapi.backend.components;

import ee.taltech.iti0302.okapi.backend.dto.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecordsMapper {

    RecordsMapper INSTANCE = Mappers.getMapper(RecordsMapper.class);

    RecordsDTO toDTO(Records records);
    Records toEntity(RecordsDTO dto);
}
