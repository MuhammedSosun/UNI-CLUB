package com.uniClub.Club.clubController;

import com.uniClub.util.controller.RootEntity;
import com.uniClub.Club.clubDto.ClubMemberStatsResponse;
import com.uniClub.Club.clubDto.ClubRequestDto;
import com.uniClub.Club.clubDto.ClubResponseDTO;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IClubController {

    RootEntity<ClubResponseDTO> createClub(ClubRequestDto requestDto);

    RootEntity<List<ClubResponseDTO>> getActiveClubs();

    RootEntity<ClubResponseDTO> getClubById(Long id);

    RootEntity<PageableEntity<ClubResponseDTO>> getActiveClubsPaged(PageableRequest pageableRequest);

    RootEntity<Void> deactivateClub(Long id);

    RootEntity<ClubResponseDTO> activateClub(Long id);

    RootEntity<PageableEntity<ClubResponseDTO>> getAllPaged(PageableRequest pageableRequest);

    RootEntity<ClubResponseDTO> updateClub(Long id, ClubRequestDto requestDto);

    RootEntity<Void> deleteClub(Long id);

    RootEntity<ClubResponseDTO> assignPresident(Long clubId, UUID userId);

    RootEntity<Long> totalClubs();

    RootEntity<List<ClubMemberStatsResponse>> getTopByMembers();

    RootEntity<PageableEntity<ClubResponseDTO>> searchClubsByName(String name, Pageable pageable);
}
