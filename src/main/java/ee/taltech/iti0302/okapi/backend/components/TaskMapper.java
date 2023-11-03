package ee.taltech.iti0302.okapi.backend.components;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ee.taltech.iti0302.okapi.backend.dto.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDTO toDTO(Task task);
    Task toEntity(TaskDTO dto);
}
