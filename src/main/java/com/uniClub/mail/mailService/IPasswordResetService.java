package com.uniClub.mail.mailService;

import com.uniClub.mail.mailDto.PasswordResetCodeRequest;

public interface IPasswordResetService {

    void sendResetCode(String email);

    void verifyCode(String email, String code);

    void resetPassword(PasswordResetCodeRequest request);

}
