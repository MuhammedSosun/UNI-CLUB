package com.uniClub.controller.memberController.impl;

import com.uniClub.controller.controller.RestBaseController;
import com.uniClub.controller.controller.RootEntity;
import com.uniClub.controller.memberController.IMemberController;
import com.uniClub.dto.memberDto.MemberRequest;
import com.uniClub.dto.memberDto.MemberResponse;
import com.uniClub.entity.userEntity.UserEntity;
import com.uniClub.service.memberService.IMemberService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
