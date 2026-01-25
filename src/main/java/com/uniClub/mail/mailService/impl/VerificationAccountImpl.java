package com.uniClub.mail.mailService.impl;

import com.uniClub.mail.mailEntity.Verification;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.mail.mailRepository.VerificationRepository;
import com.uniClub.mail.mailService.IVerificationAccount;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Log4j2
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
    @Transactional
    @Override
    public void sendVerificationCode(String email) {

        log.info("MAIL START -> {}", email);

        String code = generateCode();
        verificationRepository.deleteByEmail(email);

        Verification verification = new Verification();
        verification.setEmail(email);
        verification.setCode(code);
        verification.setExpireAt(LocalDateTime.now().plusMinutes(5));
        verificationRepository.save(verification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("UniClub <vestvivallc77@gmail.com>");
        message.setTo(email);
        message.setSubject("UniClub | Doğrulama Kodu");
        message.setText("""
            Merhaba,

            UniClub hesabınızı doğrulamak için kodunuz:

            """ + code + """

           Bu kod 5 dakika geçerlidir.

           UniClub Ekibi
           """);

        javaMailSender.send(message);
    }

}
