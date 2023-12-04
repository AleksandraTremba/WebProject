package ee.taltech.iti0302.okapi.backend.components;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ee.taltech.iti0302.okapi.backend.dto.GroupDTO;
import ee.taltech.iti0302.okapi.backend.entities.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDTO toDTO(Group group);
    Group toEntity(GroupDTO dto);
}

