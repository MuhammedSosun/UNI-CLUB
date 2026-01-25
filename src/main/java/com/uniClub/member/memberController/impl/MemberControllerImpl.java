package com.uniClub.member.memberController.impl;

import com.uniClub.util.controller.RestBaseController;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.member.memberController.IMemberController;
import com.uniClub.member.memberDto.MemberRequest;
import com.uniClub.member.memberDto.MemberResponse;
import com.uniClub.user.userEntity.UserEntity;
import com.uniClub.member.memberService.IMemberService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberControllerImpl extends RestBaseController implements IMemberController {

    private final IMemberService memberService;

    public MemberControllerImpl(IMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    @Override
    public RootEntity<MemberResponse> getMyProfile(@AuthenticationPrincipal UserEntity user) {
        return ok(memberService.getMyProfile(user));
    }
    @PutMapping("/me")
    @Override
    public RootEntity<MemberResponse> updateMyProfile(@Valid @RequestBody MemberRequest request,
                                                      @AuthenticationPrincipal UserEntity user) {
        return ok(memberService.updateMyProfile(request,user));
    }
}
