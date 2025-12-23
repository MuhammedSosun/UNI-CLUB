package com.uniClub.controller.mailcontroller.impl;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.controller.mailcontroller.IMailController;
import com.uniClub.dto.mailDto.ForgotPasswordRequest;
import com.uniClub.dto.mailDto.PasswordResetCodeRequest;
import com.uniClub.dto.mailDto.VerifyCodeRequest;
import com.uniClub.service.mailService.IPasswordResetService;
import com.uniClub.service.userService.IAuthenticateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailControlerImpl extends RootEntity implements IMailController {

    private final IPasswordResetService passwordResetService;
    private final IAuthenticateService authenticateService;

    public MailControlerImpl(IPasswordResetService passwordResetService, IAuthenticateService authenticateService) {
        this.passwordResetService = passwordResetService;
        this.authenticateService = authenticateService;
    }

    @PostMapping("/forgot-password")
    @Override
    public RootEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendResetCode(request.getEmail());
        return ok("Kod email adresinize gönderildi.");
    }


    @PostMapping("/verify-code")
    @Override
    public RootEntity<?> verify(@RequestBody VerifyCodeRequest request) {
        passwordResetService.verifyCode(request.getEmail(), request.getCode());
        return ok("Kod Doğrulandı.");
    }


    @PostMapping("/reset-password")
    @Override
    public RootEntity<?> resetPassword(@RequestBody PasswordResetCodeRequest request) {
        passwordResetService.resetPassword(request);
        return ok("Şifre Başarıyla Değiştirildi");
    }


    @PostMapping("/verify")
    @Override
    public RootEntity<?> VerifyAccount(@RequestBody VerifyCodeRequest request) {
        return ok(authenticateService.verifyCode(request));
    }
}
