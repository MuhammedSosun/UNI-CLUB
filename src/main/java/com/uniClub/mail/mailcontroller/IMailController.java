package com.uniClub.mail.mailcontroller;

import com.uniClub.util.controller.RootEntity;
import com.uniClub.mail.mailDto.ForgotPasswordRequest;
import com.uniClub.mail.mailDto.PasswordResetCodeRequest;
import com.uniClub.mail.mailDto.VerifyCodeRequest;

public interface IMailController {
    RootEntity<?> forgotPassword(ForgotPasswordRequest request);
    RootEntity<?> verify(VerifyCodeRequest request);
    RootEntity<?> resetPassword(PasswordResetCodeRequest request);
    RootEntity<?> VerifyAccount(VerifyCodeRequest request);
}
