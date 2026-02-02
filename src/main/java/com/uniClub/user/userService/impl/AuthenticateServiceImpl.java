package com.uniClub.user.userService.impl;

import com.uniClub.user.event.UserRegisteredEvent;
import com.uniClub.mail.mailDto.VerifyCodeRequest;
import com.uniClub.mail.mailEntity.Verification;
import com.uniClub.member.memberEntity.Member;
import com.uniClub.user.userEntity.RefreshToken;
import com.uniClub.user.userEntity.UserEntity;
import com.uniClub.enums.OperationType;
import com.uniClub.enums.Role;
import com.uniClub.enums.StatusEnum;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.user.userMapper.RefreshTokenMapper;
import com.uniClub.user.userMapper.UserMapper;
import com.uniClub.mail.mailRepository.VerificationRepository;
import com.uniClub.member.memberRepository.MemberRepository;
import com.uniClub.user.userDto.*;
import com.uniClub.user.userRepository.RefreshTokenRepository;
import com.uniClub.user.userRepository.UserRepository;
import com.uniClub.security.JwtService;
import com.uniClub.mail.mailService.IPasswordResetService;
import com.uniClub.mail.mailService.IVerificationAccount;
import com.uniClub.user.userService.IAuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AuthenticateServiceImpl implements IAuthenticateService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final IPasswordResetService resetService;
    private final IVerificationAccount verificationAccount;
    private final VerificationRepository verificationRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AuthenticateServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, AuthenticationProvider authenticationProvider, JwtService jwtService, RefreshTokenRepository refreshTokenRepository, IPasswordResetService resetService, IVerificationAccount verificationAccount, VerificationRepository verificationRepository, MemberRepository memberRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.resetService = resetService;
        this.verificationAccount = verificationAccount;
        this.verificationRepository = verificationRepository;
        this.memberRepository = memberRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }
    private RefreshToken createRefreshToken(UserEntity user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        return refreshToken;
    }
    @Transactional
    @LoggableOperation(OperationType.REGISTER)
    @Override
    public String register(RegisterRequest request) {
        String email = request.getEmail().trim();
        String username = email.split("@")[0];

        UserEntity existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (existingUser != null) {

            if (!existingUser.isActive()) {
                verificationAccount.sendVerificationCode(email);
                return "Bu e-posta ile daha önce kayıt olunmuş fakat doğrulanmamış. Yeni doğrulama kodu gönderildi.";
            }
            throw new BaseException(
                    new ErrorMessage(MessageType.USER_ALREADY_EXISTS,
                            "Bu e-posta adresi zaten sistemde kayıtlı.")
            );
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setUsername(username);
        userDto.setPassword(request.getPassword());
        userDto.setRole(Role.USER);

        log.info("REGISTER REQUEST -> " + request.getEmail() + " / " + request.getPassword());
        UserEntity user = UserMapper.toEntity(userDto, bCryptPasswordEncoder);
        userRepository.save(user);

        Member member = new Member();

        member.setUser(user);
        member.setStatus(StatusEnum.INCOMPLETED);
        member.setUniversity("Yalova Üniversitesi");

        memberRepository.save(member);
        applicationEventPublisher.publishEvent(
                new UserRegisteredEvent(email)
        );
       // verificationAccount.sendVerificationCode(email);

        return "Kayıt başarılı. Doğrulama kodu e-posta adresinize gönderildi.";
    }

    @Transactional
    @Override
    public String verifyCode(VerifyCodeRequest request) {

        Verification verification = verificationRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.CODE_NOT_FOUND, "Kod bulunamadı!")
                ));

        if (verification.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new BaseException(
                    new ErrorMessage(MessageType.CODE_TIME_IS_EXPIRES_DATE, "Kod zaman aşımına uğradı")
            );
        }

        if (!verification.getCode().equals(request.getCode())) {
            throw new BaseException(
                    new ErrorMessage(MessageType.CODE_IS_ERRORS, "Doğrulama kodu yanlış")
            );
        }

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.USER_NOT_FOUND, "Kullanıcı bulunamadı")
                ));

        // ✅ USER AKTİF
        user.setActive(true);

        // ✅ MEMBER COMPLETED
        Member member = memberRepository.findByUser(user).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.MEMBER_NOT_FOUND, user.getUsername()))
        );
        member.setStatus(StatusEnum.ACTIVE);

        // ✅ VERIFICATION TEMİZLE
        verificationRepository.delete(verification);

        return "Doğrulama başarılı. Artık giriş yapabilirsiniz.";
    }


    @Transactional
    @LoggableOperation(OperationType.LOGIN)
    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {

        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserEntity user = userRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(()-> new UsernameNotFoundException("Username not found"));

            if (!user.isActive()) {
                throw new BaseException(
                        new ErrorMessage(MessageType.ACCOUNT_NOT_VERIFIED, "Hesabınız doğrulanmamış!"));
            }

            String accessToken = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenRepository.save(RefreshTokenMapper.generate(user));

            // 👇 GÜNCELLENEN KISIM: ID, Username ve Role bilgisini de gönderiyoruz
            return new AuthResponse(
                    accessToken,
                    refreshToken.getRefreshToken(),
                    user.getId(),       // userId
                    user.getUsername(), // username
                    user.getRole()      // role
            );

        } catch (Exception e) {
            log.error("LOGIN FAILED username={}, reason={}", authRequest.getUsername(), e.getMessage(), e);
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
    @Transactional
    @LoggableOperation(OperationType.REFRESH_TOKEN)
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(()-> new UsernameNotFoundException("Refresh token not found"));

        if (!isValid(refreshToken.getExpiredDate())){
            throw new UsernameNotFoundException("Invalid refresh token");
        }

        UserEntity user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenRepository.save(RefreshTokenMapper.generate(user));

        // 👇 GÜNCELLENEN KISIM
        return new AuthResponse(
                accessToken,
                newRefreshToken.getRefreshToken(),
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    @Transactional
    @Override
    public UserDto updateUserRole(UUID userId, Role newRole) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND, "Username not found"))
        );
        user.setRole(newRole);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> allUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDto).toList();
    }

    @Override
    public Page<UserDto> getUsersPaged(Pageable pageable, String filter) {
        Page<UserEntity> page;
        if (filter == null || filter.isBlank()) {
            page = userRepository.findAll(pageable);
        } else {
            page = userRepository.searchUsersPaged(filter, pageable);
        }

        return page.map(UserMapper::toDto);
    }

    @Override
    public List<UserDto> searchUsers(String filter) {
        if (filter == null || filter.isBlank()) {
            return userRepository.findAll()
                    .stream()
                    .map(UserMapper::toDto)
                    .toList();
        }
        List<UserEntity> users = userRepository.searchUsers(filter);
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void logout(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                ()-> new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND, username)));
        refreshTokenRepository.deleteAllByUserId(user.getId());
    }

    private boolean isValid(Date expiredDate){
        return expiredDate.after(new Date());
    }
}
