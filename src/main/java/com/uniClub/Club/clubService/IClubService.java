package com.uniClub.Club.clubService;

import com.uniClub.Club.clubDto.ClubMemberStatsResponse;
import com.uniClub.Club.clubDto.ClubRequestDto;
import com.uniClub.Club.clubDto.ClubResponseDTO;
import com.uniClub.util.pageable.PageableEntity;
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
    Long totalClubs();
    Page<ClubResponseDTO> getAllPaged(Pageable pageable, String filter);
    List<ClubMemberStatsResponse> getTopClubsByMemberCount();
    PageableEntity<ClubResponseDTO> searchClubsByName(String name, Pageable pageable);
    String saveBase64Logo(Long clubId, String fileName, String base64Content);
}
