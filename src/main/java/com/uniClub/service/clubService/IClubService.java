package com.uniClub.service.clubService;

import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IClubService {

    ClubResponseDTO createClub(ClubRequestDto clubRequestDto);
    ClubResponseDTO getClubById(Long id);
    List<ClubResponseDTO> getAllClubs();
    List<ClubResponseDTO> getActiveClubs();
    Page<ClubResponseDTO> getAllPaged(Pageable pageable);
    Page<ClubResponseDTO> getActiveClubsPaged(Pageable pageable);
    ClubResponseDTO updateClub(Long id,ClubRequestDto clubRequestDto);
    void deactivateClub(Long id);
    ClubResponseDTO activateClub(Long id);
    void deleteClubById(Long id);
    public ClubResponseDTO assignPresident(Long clubId, UUID userId);
}
