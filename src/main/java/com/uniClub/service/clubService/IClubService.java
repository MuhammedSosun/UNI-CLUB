package com.uniClub.service.clubService;

import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;

import java.util.List;

public interface IClubService {

    ClubResponseDTO createClub(ClubRequestDto clubRequestDto);
    ClubResponseDTO getClubById(Long id);
    List<ClubResponseDTO> getAllClubs();
    ClubResponseDTO updateClub(Long id,ClubRequestDto clubRequestDto);
    void deleteClubById(Long id);
}
