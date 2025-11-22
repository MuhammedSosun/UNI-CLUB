package com.uniClub.controller.clubController;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;

import java.util.List;

public interface IClubController {

    RootEntity<ClubResponseDTO> createClub(ClubRequestDto requestDto);
    RootEntity<ClubResponseDTO> getClubById(Long id);
    RootEntity<PageableEntity<ClubResponseDTO>> getAllPaged(PageableRequest pageableRequest);
    RootEntity<ClubResponseDTO> updateClub(Long id, ClubRequestDto requestDto);
    RootEntity<Void> deleteClub(Long id);
}
