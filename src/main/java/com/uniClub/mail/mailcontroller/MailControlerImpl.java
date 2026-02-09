package com.uniClub.mail.mailcontroller;

import com.uniClub.util.controller.RootEntity;
import com.uniClub.mail.mailDto.ForgotPasswordRequest;
import com.uniClub.mail.mailDto.PasswordResetCodeRequest;
import com.uniClub.mail.mailDto.VerifyCodeRequest;
import com.uniClub.mail.mailService.IPasswordResetService;
import com.uniClub.user.userService.IAuthenticateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailControlerImpl extends RootEntity{

    private final IPasswordResetService passwordResetService;
    private final IAuthenticateService authenticateService;

    public MailControlerImpl(IPasswordResetService passwordResetService, IAuthenticateService authenticateService) {
        this.passwordResetService = passwordResetService;
        this.authenticateService = authenticateService;
    }

    @PostMapping("/forgot-password")
    
    public RootEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendResetCode(request.getEmail());
        return ok("Kod email adresinize gönderildi.");
    }


    @PostMapping("/verify-code")
    
    public RootEntity<?> verify(@RequestBody VerifyCodeRequest request) {
        passwordResetService.verifyCode(request.getEmail(), request.getCode());
        return ok("Kod Doğrulandı.");
    }


    @PostMapping("/reset-password")
    
    public RootEntity<?> resetPassword(@RequestBody PasswordResetCodeRequest request) {
        passwordResetService.resetPassword(request);
        return ok("Şifre Başarıyla Değiştirildi");
    }


    @PostMapping("/verify")
    
    public RootEntity<?> VerifyAccount(@RequestBody VerifyCodeRequest request) {
        return ok(authenticateService.verifyCode(request));
    }
}
