package com.uniClub.mapper.clubMapper;

import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import com.uniClub.entity.clubEntity.ClubEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClubMapper {

    // CREATE işlemi için map
    @Mapping(target = "president", ignore = true)  // foreign key elle set edilecek
    @Mapping(target = "memberships", ignore = true)
    @Mapping(target = "events", ignore = true)
    ClubEntity toEntity(ClubRequestDto dto);

    // Response için map
    @Mapping(target = "presidentId", source = "president.id")
    @Mapping(target = "presidentUsername", source = "president.username")
    ClubResponseDTO toResponseDTO(ClubEntity entity);

    // UPDATE işlemi incremental olarak
    @Mapping(target = "president", ignore = true) // update’te de foreign key elle set edilecek
    @Mapping(target = "memberships", ignore = true)
    @Mapping(target = "events", ignore = true)
    void updateEntity(@MappingTarget ClubEntity entity, ClubRequestDto dto);
}
