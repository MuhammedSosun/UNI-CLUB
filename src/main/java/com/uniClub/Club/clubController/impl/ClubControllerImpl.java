package com.uniClub.Club.clubController.impl;

import com.uniClub.Club.clubController.IClubController;
import com.uniClub.util.controller.RestBaseController;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.Club.clubDto.ClubLogoUploadRequest;
import com.uniClub.Club.clubDto.ClubMemberStatsResponse;
import com.uniClub.Club.clubDto.ClubRequestDto;
import com.uniClub.Club.clubDto.ClubResponseDTO;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.Club.clubService.IClubService;
import com.uniClub.util.pageable.PageUtil;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // bak filtreli bu istek gönderince filter için bir şey yazmak lazım örneğin spor**
    @GetMapping
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
    @GetMapping("/search")
    public RootEntity<PageableEntity<ClubResponseDTO>> searchClubsByName(
            String name,
            @PageableDefault(sort = {}) Pageable pageable
    ) {
        return ok(clubService.searchClubsByName(name, pageable));
    }
    @GetMapping("/count")
    @Override
    public RootEntity<Long> activeClubCount() {
        return ok(clubService.activeClubCount());
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


    @PostMapping("/logo/upload")
    public RootEntity<String> uploadLogo(@RequestBody ClubLogoUploadRequest request) {

        if (request.getBase64Content() == null || request.getBase64Content().isBlank()) {
            throw new BaseException(
                    new ErrorMessage(MessageType.FILE_NOT_FOUND, "Base64 içerik boş")
            );
        }

        String logoUrl = clubService.saveBase64Logo(
                request.getClubId(),
                request.getFileName(),
                request.getBase64Content()
        );

        return ok(logoUrl);
    }
    // ClubControllerImpl.java içine:

    @GetMapping("/my-club")
    public RootEntity<ClubResponseDTO> getMyClub() {
        // Servis null dönerse frontend'de "null" olarak işleriz, hata fırlatmaya gerek yok
        return ok(clubService.getMyClub());
    }

}
