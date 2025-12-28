package com.uniClub.service.mailService.impl;

import com.uniClub.dto.mailDto.PasswordResetCodeRequest;
import com.uniClub.entity.mailEntity.PasswordResetCode;
import com.uniClub.entity.userEntity.UserEntity;
import com.uniClub.enums.OperationType;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.repository.mailRepository.PasswordResetCodeRepository;
import com.uniClub.repository.userRepository.UserRepository;
import com.uniClub.service.mailService.IPasswordResetService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetServiceImpl implements IPasswordResetService {

    private final PasswordResetCodeRepository codeRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder encoder;

    public PasswordResetServiceImpl(PasswordResetCodeRepository codeRepository, UserRepository userRepository, JavaMailSender mailSender, PasswordEncoder encoder) {
        this.codeRepository = codeRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.encoder = encoder;
    }

    private String generateCode(){
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    @LoggableOperation(OperationType.SEND_CODE)
    @Override
    public void sendResetCode(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.EMAIL_ALREADY_EXISTS, "Email kayıtlı değil!")
                ));

        codeRepository.invalidateAllCodes(email);

        String code = generateCode();

        PasswordResetCode resetCode = new PasswordResetCode();
        resetCode.setEmail(email);
        resetCode.setCode(code);
        resetCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        codeRepository.save(resetCode);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("UniClub Şifre Sıfırlama Kodu");
        mailMessage.setText("Şifre sıfırlama kodunuz: " + code + "\nBu kod 10 dakika geçerlidir.");
        mailSender.send(mailMessage);
    }

    @Override
    public void verifyCode(String email, String code) {
        PasswordResetCode reset = codeRepository.findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.CODE_IS_ERRORS, "Kod hatalı!")
                ));

        if (reset.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BaseException(
                    new ErrorMessage(MessageType.CODE_TIME_IS_EXPIRES_DATE, "Kodun süresi dolmuş!")
            );
        }
    }

    @Transactional
    @Override
    public void resetPassword(PasswordResetCodeRequest request) {

        PasswordResetCode reset = codeRepository.findByEmailAndCodeAndUsedFalse(
                request.getEmail(), request.getCode()
        ).orElseThrow(() -> new BaseException(
                new ErrorMessage(MessageType.CODE_IS_ERRORS, "Kod hatalı!")
        ));

        if (reset.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BaseException(
                    new ErrorMessage(MessageType.CODE_TIME_IS_EXPIRES_DATE, "Kodun süresi dolmuş!")
            );
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BaseException(
                    new ErrorMessage(MessageType.PASSWORD_IS_NOT_SAME, "Şifreler uyuşmuyor!")
            );
        }

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.EMAIL_ALREADY_EXISTS, "Email kayıtlı değil!")
                ));

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);

        reset.setUsed(true);
        codeRepository.save(reset);
    }
}
