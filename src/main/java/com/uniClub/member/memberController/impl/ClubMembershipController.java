package com.uniClub.member.memberController.impl;

import com.uniClub.Club.clubDto.ClubResponseDTO; // 1. IMPORT EKLENDİ
import com.uniClub.util.controller.RestBaseController;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.member.memberDto.ClubMemberDto;
import com.uniClub.member.memberDto.JoinRequestDto;
import com.uniClub.member.memberDto.UpdateMemberRoleRequest;
import com.uniClub.member.memberDto.UpdateMemberStatusRequest;
import com.uniClub.member.memberService.IClubMembershipService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member-ship-clubs")
public class ClubMembershipController extends RestBaseController {

    private final IClubMembershipService membershipService;

    public ClubMembershipController(IClubMembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping("/{clubId}/join-requests")
    public RootEntity<Void> requestJoin(@PathVariable Long clubId, Authentication auth) {
        membershipService.requestJoin(clubId, auth.getName());
        return ok(null);
    }

    // 2) Admin -> pending list
    @GetMapping("/{clubId}/join-requests")
    public RootEntity<List<JoinRequestDto>> pendingRequests(@PathVariable Long clubId, Authentication auth) {
        return ok(membershipService.listPendingRequests(clubId, auth.getName()));
    }

    // 3) Admin -> approve
    @PostMapping("/{clubId}/join-requests/{memberId}/approve")
    public RootEntity<Void> approve(@PathVariable Long clubId,
                                    @PathVariable Long memberId,
                                    Authentication auth) {
        membershipService.approveRequest(clubId, memberId, auth.getName());
        return ok(null);
    }

    // 4) Admin -> reject
    @PostMapping("/{clubId}/join-requests/{memberId}/reject")
    public RootEntity<Void> reject(@PathVariable Long clubId,
                                   @PathVariable Long memberId,
                                   Authentication auth) {
        membershipService.rejectRequest(clubId, memberId, auth.getName());
        return ok(null);
    }

    // 5) Member -> leave club
    @PostMapping("/{clubId}/leave")
    public RootEntity<Void> leaveClub(@PathVariable Long clubId, Authentication auth) {
        membershipService.leaveClub(clubId, auth.getName());
        return ok(null);
    }

    @GetMapping("/{clubId}/members")
    public RootEntity<List<ClubMemberDto>> getAllMembers(
            @PathVariable Long clubId,
            Authentication authentication
    ) {
        return RootEntity.ok(
                membershipService.listClubMembers(
                        clubId,
                        authentication.getName()
                )
        );
    }

    // 🔹 Sadece onaylı üyeler
    @GetMapping("/{clubId}/members/approved")
    public RootEntity<List<ClubMemberDto>> getApprovedMembers(
            @PathVariable Long clubId,
            Authentication authentication
    ) {
        return RootEntity.ok(
                membershipService.listApprovedMembers(
                        clubId,
                        authentication.getName()
                )
        );
    }

    @PutMapping("/{clubId}/members/{memberId}/status")
    public RootEntity<Void> updateMemberStatus(
            @PathVariable Long clubId,
            @PathVariable Long memberId,
            @RequestBody UpdateMemberStatusRequest request,
            Authentication authentication
    ) {
        membershipService.updateMemberStatus(
                clubId,
                memberId,
                request.status(),
                authentication.getName()
        );
        return ok(null);
    }

    // =========================
    // 🔹 ROLE GÜNCELLEME
    // =========================
    @PutMapping("/{clubId}/members/{memberId}/role")
    public RootEntity<Void> updateMemberRole(
            @PathVariable Long clubId,
            @PathVariable Long memberId,
            @RequestBody UpdateMemberRoleRequest request,
            Authentication authentication
    ) {
        membershipService.updateMemberRole(
                clubId,
                memberId,
                request.role(),
                authentication.getName()
        );
        return ok(null);
    }

    // 🔥🔥🔥 YENİ EKLENEN ENDPOINT 🔥🔥🔥
    // Frontend buradan "Benim başkan olduğum kulübü getir" diyecek.
    @GetMapping("/my-club")
    public RootEntity<ClubResponseDTO> getMyClub() {
        return ok(membershipService.getMyClub());
    }

}