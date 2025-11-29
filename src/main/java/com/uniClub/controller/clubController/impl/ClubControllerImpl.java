package com.uniClub.controller.clubController.impl;

import com.uniClub.controller.clubController.IClubController;
import com.uniClub.controller.controller.RestBaseController;
import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.clubDto.ClubMemberStatsResponse;
import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import com.uniClub.service.clubService.IClubService;
import com.uniClub.util.pageable.PageUtil;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clubs")
public class ClubControllerImpl extends RestBaseController implements IClubController {

    private final IClubService clubService;

    public ClubControllerImpl(IClubService clubService) {
        this.clubService = clubService;
    }

    // -------------------- CREATE --------------------
    @PostMapping
    @Override
    public RootEntity<ClubResponseDTO> createClub(@Valid @RequestBody ClubRequestDto requestDto) {
        return ok(clubService.createClub(requestDto));
    }

    // -------------------- GET BY ID --------------------
    @GetMapping("/{id}")
    @Override
    public RootEntity<ClubResponseDTO> getClubById(@PathVariable Long id) {
        return ok(clubService.getClubById(id));
    }

    // -------------------- PAGED GET ALL --------------------
    @GetMapping("")
    public RootEntity<PageableEntity<ClubResponseDTO>> getAllPaged(@Valid PageableRequest pageableRequest) {

        Pageable pageable = PageUtil.toPageable(pageableRequest);

        Page<ClubResponseDTO> page =
                clubService.getAllPaged(pageable, pageableRequest.getFilter());

        return ok(PageUtil.toPageableResponse(page, page.getContent()));
    }


    // -------------------- UPDATE --------------------
    @PutMapping("/{id}")
    @Override
    public RootEntity<ClubResponseDTO> updateClub(
            @PathVariable Long id,
            @Valid @RequestBody ClubRequestDto requestDto
    ) {
        return ok(clubService.updateClub(id, requestDto));
    }

    // -------------------- DELETE (SOFT) --------------------
    @DeleteMapping("/{id}")
    @Override
    public RootEntity<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClubById(id);
        return ok(null);
    }

    // -------------------- ASSIGN PRESIDENT --------------------
    @PatchMapping("/{id}/assign-president/{userId}")
    @Override
    public RootEntity<ClubResponseDTO> assignPresident(
            @PathVariable Long id,
            @PathVariable UUID userId
    ) {
        return ok(clubService.assignPresident(id, userId));
    }
    @GetMapping("/total")
    @Override
    public RootEntity<Long> totalClubs() {
        return ok(clubService.totalClubs());
    }

    @Override
    @GetMapping("/stats/top-by-members")
    public RootEntity<List<ClubMemberStatsResponse>> getTopByMembers() {
        return ok(clubService.getTopClubsByMemberCount());
    }


    // -------------------- ACTIVE CLUBS --------------------
    @GetMapping("/active")
    public RootEntity<List<ClubResponseDTO>> getActiveClubs() {
        return ok(clubService.getActiveClubs());
    }

    // -------------------- ACTIVE CLUBS PAGED --------------------
    @GetMapping("/active/paged")
    public RootEntity<PageableEntity<ClubResponseDTO>> getActiveClubsPaged(PageableRequest pageableRequest) {

        Pageable pageable = PageUtil.toPageable(pageableRequest);
        Page<ClubResponseDTO> page = clubService.getActiveClubsPaged(pageable);

        return ok(PageUtil.toPageableResponse(page, page.getContent()));
    }

    // -------------------- DEACTIVATE --------------------
    @PatchMapping("/{id}/deactivate")
    public RootEntity<Void> deactivateClub(@PathVariable Long id) {
        clubService.deactivateClub(id);
        return ok(null);
    }

    // -------------------- ACTIVATE --------------------
    @PatchMapping("/{id}/activate")
    public RootEntity<ClubResponseDTO> activateClub(@PathVariable Long id) {
        return ok(clubService.activateClub(id));
    }
}
