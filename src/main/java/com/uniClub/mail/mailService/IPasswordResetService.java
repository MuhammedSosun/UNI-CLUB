package com.uniClub.service.mailService;

import com.uniClub.dto.mailDto.PasswordResetCodeRequest;

public interface IPasswordResetService {

    void sendResetCode(String email);

    void verifyCode(String email, String code);

    void resetPassword(PasswordResetCodeRequest request);

}
