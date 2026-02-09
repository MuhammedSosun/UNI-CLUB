package com.uniClub.member.memberController;

import com.uniClub.util.controller.RestBaseController;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.member.memberDto.MemberRequest;
import com.uniClub.member.memberDto.MemberResponse;
import com.uniClub.user.userEntity.UserEntity;
import com.uniClub.member.memberService.IMemberService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberControllerImpl extends RestBaseController{

    private final IMemberService memberService;

    public MemberControllerImpl(IMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    
    public RootEntity<MemberResponse> getMyProfile(@AuthenticationPrincipal UserEntity user) {
        return ok(memberService.getMyProfile(user));
    }
    @PutMapping("/me")
    
    public RootEntity<MemberResponse> updateMyProfile(@Valid @RequestBody MemberRequest request,
                                                      @AuthenticationPrincipal UserEntity user) {
        return ok(memberService.updateMyProfile(request,user));
    }
    @GetMapping("/count")
    
    public RootEntity<Long> activeMember() {
        return ok(memberService.activeMemberCount());
    }
    @GetMapping
     // Eğer IMemberController'a da eklediysen  kalabilir, yoksa sil
    public RootEntity<Page<MemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter
    ) {
        // Sayfalama ayarları (ID'ye göre tersten sırala - en yeniler en üstte)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return ok(memberService.getAllMembers(pageable, filter));
    }

    @GetMapping("/{id}")
    
    public RootEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        return ok(memberService.getMemberById(id));
    }
}
