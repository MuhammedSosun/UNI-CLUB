package com.uniClub.controller.mailcontroller;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.mailDto.ForgotPasswordRequest;
import com.uniClub.dto.mailDto.PasswordResetCodeRequest;
import com.uniClub.dto.mailDto.VerifyCodeRequest;

public interface IMailController {
    RootEntity<?> forgotPassword(ForgotPasswordRequest request);
    RootEntity<?> verify(VerifyCodeRequest request);
    RootEntity<?> resetPassword(PasswordResetCodeRequest request);
    RootEntity<?> VerifyAccount(VerifyCodeRequest request);
}
