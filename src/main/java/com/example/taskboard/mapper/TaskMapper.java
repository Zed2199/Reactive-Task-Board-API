package com.example.taskboard.mapper;

import com.example.taskboard.domain.Task;
import com.example.taskboard.web.dto.CreateTaskRequest;
import com.example.taskboard.web.dto.TaskResponse;
import com.example.taskboard.web.dto.UpdateTaskRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(CreateTaskRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateTaskRequest request, @MappingTarget Task entity);

    TaskResponse toResponse(Task task);
}
