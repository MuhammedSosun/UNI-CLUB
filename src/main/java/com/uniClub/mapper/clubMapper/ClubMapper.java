package com.uniClub.mapper.clubMapper;

import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import com.uniClub.entity.clubEntity.ClubEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClubMapper {
    ClubEntity toEntity(ClubRequestDto dto);
    ClubResponseDTO toResponseDTO(ClubEntity entity);
}
