package com.uniClub.user.userController;

import com.uniClub.util.controller.RestBaseController;
import com.uniClub.util.controller.RootEntity;
import com.uniClub.enums.Role;
import com.uniClub.user.userDto.*;
import com.uniClub.user.userService.IAuthenticateService;
import com.uniClub.util.pageable.PageUtil;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")

public class RestAuthenticationControllerImpl extends RestBaseController{

    private final IAuthenticateService authenticateService;

    public RestAuthenticationControllerImpl(IAuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/register")
    public RootEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ok(authenticateService.register(request));
    }
    @PostMapping("/authenticate")
    
    public RootEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ok(authenticateService.authenticate(request));
    }

    @PostMapping("/refreshToken")
    
    public RootEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ok(authenticateService.refreshToken(refreshTokenRequest));
    }
    @PutMapping("/users/{userId}/role")
    
    public RootEntity<UserDto> updateUserRole(@PathVariable UUID userId, @RequestParam Role newRole) {
        return ok(authenticateService.updateUserRole(userId, newRole));
    }
    @GetMapping("/all/users")
    
    public RootEntity<List<UserDto>> allUsers() {
        return ok(authenticateService.allUsers());
    }
    @GetMapping("/users/paged")
    public RootEntity<PageableEntity<UserDto>> getUsersPaged(PageableRequest pageableRequest) {
        Pageable pageable = PageUtil.toPageable(pageableRequest);
        Page<UserDto> page = authenticateService.getUsersPaged(pageable, pageableRequest.getFilter());
        return ok(PageUtil.toPageableResponse(page, page.getContent()));
    }

    @GetMapping("/filter/users")
    
    public RootEntity<List<UserDto>> searchUsers(@RequestParam(required = false) String filter) {
        return ok(authenticateService.searchUsers(filter));
    }

    @PostMapping("/logout")
    
    public ResponseEntity<Void> logout(Authentication authentication) {
        if (authentication != null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok().build();
        }
        authenticateService.logout(authentication.getName());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();

    }
}
