package com.uniClub.service.mailService.impl;

import com.uniClub.entity.mailEntity.Verification;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.repository.mailRepository.VerificationRepository;
import com.uniClub.service.mailService.IVerificationAccount;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationAccountImpl implements IVerificationAccount {

    private final JavaMailSender javaMailSender;
    private final VerificationRepository verificationRepository;


    public VerificationAccountImpl(JavaMailSender javaMailSender, VerificationRepository verificationRepository) {
        this.javaMailSender = javaMailSender;
        this.verificationRepository = verificationRepository;
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    @Override
    public void sendVerificationCode(String email) {

        try {

            String code = generateCode();
            verificationRepository.deleteByEmail(email);

            Verification verification = new Verification();

            verification.setEmail(email);
            verification.setCode(code);
            verification.setExpireAt(LocalDateTime.now().plusMinutes(5));
            verificationRepository.save(verification);



            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("UniClub Doğrulama Kodu");
            message.setText("Doğrulama Kodunuz: " + code);


            javaMailSender.send(message);

        }catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.CODE_IS_ERRORS, e.getMessage()));
        }
    }
}
