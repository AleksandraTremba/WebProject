package ee.taltech.iti0302.okapi.backend.components;

import ee.taltech.iti0302.okapi.backend.dto.records.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecordsMapper {

    RecordsMapper INSTANCE = Mappers.getMapper(RecordsMapper.class);

    @Mapping(source = "numberOfCustomers", target = "numberOfCustomers")
    RecordsDTO toDTO(Records records);

    @Mapping(source = "numberOfCustomers", target = "numberOfCustomers")
    Records toEntity(RecordsDTO dto);
}
