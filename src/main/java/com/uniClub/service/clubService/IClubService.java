package com.uniClub.service.clubService;

import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClubService {

    ClubResponseDTO createClub(ClubRequestDto clubRequestDto);
    ClubResponseDTO getClubById(Long id);
    List<ClubResponseDTO> getAllClubs();
    Page<ClubResponseDTO> getAllPaged(Pageable pageable);
    ClubResponseDTO updateClub(Long id,ClubRequestDto clubRequestDto);
    void deleteClubById(Long id);
}
