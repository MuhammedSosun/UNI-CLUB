package com.uniClub.controller.memberController.impl;

import com.uniClub.controller.controller.RestBaseController;
import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.memberDto.JoinRequestDto;
import com.uniClub.service.memberService.IClubMembershipService;
import org.springframework.http.ResponseEntity;
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

}
