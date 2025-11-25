package com.uniClub.controller.clubController.impl;

import com.uniClub.controller.clubController.IClubController;
import com.uniClub.controller.controller.RestBaseController;
import com.uniClub.controller.controller.RootEntity;
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
    @PostMapping
    @Override
    public RootEntity<ClubResponseDTO> createClub(@Valid @RequestBody ClubRequestDto requestDto) {
        return ok(clubService.createClub(requestDto));
    }
    @GetMapping("/{id}")
    @Override
    public RootEntity<ClubResponseDTO> getClubById(@PathVariable Long id) {
        return ok(clubService.getClubById(id));
    }

    @GetMapping
    @Override
    public RootEntity<PageableEntity<ClubResponseDTO>> getAllPaged(@Valid PageableRequest pageableRequest) {
        Pageable pageable = PageUtil.toPageable(pageableRequest);
        Page<ClubResponseDTO> page = clubService.getAllPaged(pageable);

        PageableEntity<ClubResponseDTO> response  = PageUtil.toPageableResponse(page,page.getContent());
        return ok(response);
    }
    @PutMapping("/{id}")
    @Override
    public RootEntity<ClubResponseDTO> updateClub(@PathVariable Long id,@Valid @RequestBody ClubRequestDto requestDto) {
        return ok(clubService.updateClub(id, requestDto));
    }
    @DeleteMapping("/{id}")
    @Override
    public RootEntity<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClubById(id);
        return ok(null);
    }

    @PatchMapping("/{id}/assign-president/{userId}")
    @Override
    public RootEntity<ClubResponseDTO> assignPresident( @PathVariable Long id,
                                                        @PathVariable UUID userId) {
        return ok(clubService.assignPresident(id,userId));
    }

    @GetMapping("/active")
    public RootEntity<List<ClubResponseDTO>> getActiveClubs() {
        return ok(clubService.getActiveClubs());
    }

    @GetMapping("/active/page")
    public RootEntity<PageableEntity<ClubResponseDTO>> getActiveClubsPaged(PageableRequest pageableRequest) {
        Pageable pageable = PageUtil.toPageable(pageableRequest);
        Page<ClubResponseDTO> page = clubService.getActiveClubsPaged(pageable);

        return ok(PageUtil.toPageableResponse(page, page.getContent()));
    }

    @PatchMapping("/{id}/deactivate")
    public RootEntity<Void> deactivateClub(@PathVariable Long id) {
        clubService.deactivateClub(id);
        return ok(null);
    }

    @PatchMapping("/{id}/activate")
    public RootEntity<ClubResponseDTO> activateClub(@PathVariable Long id) {
        return ok(clubService.activateClub(id));
    }

}
