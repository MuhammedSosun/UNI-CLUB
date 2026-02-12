package com.uniClub.user.listener;

import com.uniClub.mail.mailService.IVerificationAccount;
import com.uniClub.user.event.UserRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Log4j2
public class UserRegisteredListener {

    private final IVerificationAccount verificationAccount;

    public UserRegisteredListener(IVerificationAccount verificationAccount) {
        this.verificationAccount = verificationAccount;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserRegisteredEvent event) {
        log.info("EVENT RECEIVED -> UserRegisteredEvent for {}", event.email());
        verificationAccount.sendVerificationCode(event.email());
    }

}
