package com.uniClub.Club.clubService.impl;

import com.uniClub.Club.clubDto.ClubMemberStatsResponse;
import com.uniClub.Club.clubDto.ClubRequestDto;
import com.uniClub.Club.clubDto.ClubResponseDTO;
import com.uniClub.Club.clubEntity.ClubEntity;
import com.uniClub.enums.*;
import com.uniClub.member.memberEntity.ClubMembership;
import com.uniClub.member.memberEntity.Member;
import com.uniClub.user.userEntity.UserEntity;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.FileStorageException;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.Club.clubMapper.ClubMapper;
import com.uniClub.Club.clubRepository.ClubRepository;
import com.uniClub.member.memberRepository.ClubMemberShipRepository;
import com.uniClub.member.memberRepository.MemberRepository;
import com.uniClub.user.userRepository.UserRepository;
import com.uniClub.Club.clubService.IClubService;
import com.uniClub.util.pageable.PageUtil;
import com.uniClub.util.pageable.PageableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ClubServiceImpl implements IClubService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ClubMemberShipRepository  clubMemberShipRepository;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper, UserRepository userRepository, MemberRepository memberRepository, ClubMemberShipRepository clubMemberShipRepository) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.clubMemberShipRepository = clubMemberShipRepository;
    }


    @LoggableOperation(OperationType.CREATE_CLUB)
    @Transactional
    @Override
    public ClubResponseDTO createClub(ClubRequestDto dto) {
        ClubEntity entity = clubMapper.toEntity(dto);

        UserEntity presidentUser = null;
        if (dto.getPresidentId() != null) {
            presidentUser = userRepository.findById(dto.getPresidentId())
                    .orElseThrow(() -> new BaseException(
                            new ErrorMessage(MessageType.USER_NOT_FOUND, "Başkan bulunamadı")
                    ));
            entity.setPresident(presidentUser);
        }

        ClubEntity saved = clubRepository.save(entity);

        // 🔥 KRİTİK KISIM
        if (presidentUser != null) {
            Member presidentMember = memberRepository.findByUser(presidentUser)
                    .orElseThrow(() -> new BaseException(
                            new ErrorMessage(MessageType.MEMBER_NOT_FOUND, "Başkanın member kaydı yok")
                    ));

            ClubMembership cm = new ClubMembership();
            cm.setClub(saved);
            cm.setMember(presidentMember);
            cm.setRole(ClubRole.PRESIDENT);
            cm.setStatus(ClubMembershipStatus.APPROVED);
            cm.setJoinedAt(LocalDate.now());

            clubMemberShipRepository.save(cm);
        }

        return clubMapper.toResponseDTO(saved);
    }


    @Override
    @Transactional(readOnly = true)
    @LoggableOperation(OperationType.FIND_CLUB)
    public ClubResponseDTO getClubById(Long id) {
        Member currentMember = getCurrentMember();

        ClubEntity club = getClubWithId(id);
        ClubResponseDTO dto = clubMapper.toResponseDTO(club);
        dto.setMembershipStatus(resolveMembershipStatus(club, currentMember));
        return dto;
    }
    @Override
    @Transactional(readOnly = true)
    @LoggableOperation(OperationType.FIND_ALL_CLUBS)
    public List<ClubResponseDTO> getAllClubs() {
        return clubRepository.findAll()
                .stream()
                .map(clubMapper::toResponseDTO).
                toList();
    }
    @LoggableOperation(OperationType.FIND_ACTIVE_CLUB)
    @Override
    public List<ClubResponseDTO> getActiveClubs() {
        Member currentMember = getCurrentMember();

        return clubRepository.findAllByStatus(StatusEnum.ACTIVE)
                .stream()
                .map(club -> {
                    ClubResponseDTO dto = clubMapper.toResponseDTO(club);
                    dto.setMembershipStatus(resolveMembershipStatus(club, currentMember));
                    return dto;
                })
                .toList();

    }

    @Transactional(readOnly = true)
    @Override
    @LoggableOperation(OperationType.FIND_ALL_CLUBS)
    public Page<ClubResponseDTO> getAllPaged(Pageable pageable) {
        Page<ClubEntity> page = clubRepository.findAll(pageable);
        Member currentMember = getCurrentMember();

        return page.map(club -> {
            ClubResponseDTO dto = clubMapper.toResponseDTO(club);
            dto.setMembershipStatus(
                    resolveMembershipStatus(club, currentMember)
            );
            return dto;
        });

    }
    @LoggableOperation(OperationType.FIND_ALL_CLUBS)
    @Override
    public Page<ClubResponseDTO> getActiveClubsPaged(Pageable pageable) {
        return clubRepository.findAllByStatus(StatusEnum.ACTIVE, pageable)
                .map(clubMapper::toResponseDTO);
    }

    @Override
    @Transactional
    @LoggableOperation(OperationType.UPDATE_CLUB)
    public ClubResponseDTO updateClub(Long id, ClubRequestDto dto) {
        if (dto.getLogoUrl() != null && dto.getLogoUrl().isBlank()) {
            dto.setLogoUrl(null);
        }
        ClubEntity existing = getClubWithId(id);

        clubMapper.updateEntity(existing, dto);
        if (dto.getPresidentId() != null) {
            UserEntity president = userRepository.findById(dto.getPresidentId())
                    .orElseThrow(() -> new BaseException(
                            new ErrorMessage(MessageType.USER_NOT_FOUND, "Başkan bulunamadı")
                    ));
            existing.setPresident(president);
        }

        ClubEntity updated = clubRepository.save(existing);

        return clubMapper.toResponseDTO(updated);
    }
    @LoggableOperation(OperationType.DEACTIVATE_CLUB)
    @Override
    public void deactivateClub(Long id) {
        ClubEntity entity = getClubWithId(id);
        entity.setStatus(StatusEnum.SUSPENDED);
        clubRepository.save(entity);
    }

    @LoggableOperation(OperationType.ACTIVATE_CLUB)
    public ClubResponseDTO activateClub(Long id) {
        ClubEntity entity = getClubWithId(id);
        entity.setStatus(StatusEnum.ACTIVE);
        return clubMapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    @LoggableOperation(OperationType.DELETE_CLUB)
    public void deleteClubById(Long id) {
        ClubEntity existing = getClubWithId(id);
        existing.setStatus(StatusEnum.TERMINATED);
        clubRepository.save(existing);
    }

    @Transactional
    @LoggableOperation(OperationType.ASSIGN_PRESIDENT)
    @Override
    public ClubResponseDTO assignPresident(Long clubId, UUID userId) {
        ClubEntity club = getClubWithId(clubId);
        UserEntity president = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.USER_NOT_FOUND, "Başkan bulunamadı.")
                ));
        club.setPresident(president);
        ClubEntity updated = clubRepository.save(club);
        return clubMapper.toResponseDTO(updated);
    }

    @Override
    public Long totalClubs() {
        return clubRepository.count();
    }
    @Transactional(readOnly = true)
    @LoggableOperation(OperationType.FIND_ALL_CLUBS)
    @Override
    public Page<ClubResponseDTO> getAllPaged(Pageable pageable, String filter) {

        Page<ClubEntity> page;
        if (filter == null || filter.isBlank()) {
            page = clubRepository.findAll(pageable);
        }else{
            page = clubRepository.searchByClubNameOrShortName(filter, pageable);
        }
        return page.map(clubMapper::toResponseDTO);
    }



    @Override
    public List<ClubMemberStatsResponse> getTopClubsByMemberCount() {
        List<ClubEntity> clubs = clubRepository.findAll();

        return clubs.stream()
                .map(c -> new ClubMemberStatsResponse(
                        c.getId(),
                        c.getClubName(),
                        c.getMemberships() != null ? c.getMemberships().size() : 0
                ))
                .sorted((a,b) -> Integer.compare(b.getMemberCount(), a.getMemberCount()))
                .limit(5)
                .toList();
    }

    @Override
    public PageableEntity<ClubResponseDTO> searchClubsByName(String name, Pageable pageable) {

        Page<ClubEntity> page = clubRepository.searchByClubNameOrShortName(name, pageable);

        Member currentMember = getCurrentMember();

        List<ClubResponseDTO> dtoList = page.getContent().stream()
                .map(club -> {
                    ClubResponseDTO dto = clubMapper.toResponseDTO(club);
                    dto.setMembershipStatus(resolveMembershipStatus(club, currentMember));
                    return dto;
                })
                .toList();


        return PageUtil.toPageableResponse(page, dtoList);
    }





    private ClubEntity getClubWithId(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() ->
                        new BaseException(new ErrorMessage(
                                MessageType.CLUB_NOT_FOUND,
                                "Kulüp bulunamadı. ID = " + id
                        ))
                );
    }
    public String saveBase64Logo(Long clubId, String fileName, String base64Content) {

        try {
            System.out.println("🟡 [LOGO-UPLOAD] clubId = " + clubId);
            System.out.println("🟡 [LOGO-UPLOAD] fileName = " + fileName);
            System.out.println("🟡 [LOGO-UPLOAD] base64 length = " + base64Content.length());
            ClubEntity club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new BaseException(
                            new ErrorMessage(MessageType.CLUB_NOT_FOUND, clubId.toString())
                    ));

            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);

            Path uploadDir = Paths.get("uploads/clubs");
            Files.createDirectories(uploadDir);

            String newFileName = "club_" + clubId + "_" + fileName;
            Path filePath = uploadDir.resolve(newFileName);

            Files.write(filePath, decodedBytes);

            String logoUrl = "http://localhost:8080/uploads/clubs/" + newFileName;

            // 🔥 DB UPDATE – ASIL KRİTİK NOKTA
            club.setLogoUrl(logoUrl);
            clubRepository.save(club);
            System.out.println("🟢 [LOGO-UPLOAD] DB AFTER logoUrl = " + club.getLogoUrl());

            return logoUrl;

        } catch (Exception e) {
            throw new FileStorageException("Kulüp logosu yüklenemedi", e);
        }
    }

    @Override
    public Long activeClubCount() {
        return clubRepository.countByStatus(StatusEnum.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public ClubResponseDTO getMyClub() {
        // 1. Giriş yapan kullanıcıyı (UserEntity) alalım
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Bu kullanıcının başkan olduğu kulübü arayalım
        return clubRepository.findByPresidentId(currentUser.getId())
                .map(club -> {
                    ClubResponseDTO dto = clubMapper.toResponseDTO(club);
                    // Başkan olduğu için statüsü zaten APPROVED'dır ama yine de set edelim
                    dto.setMembershipStatus(MembershipViewStatus.APPROVED);
                    return dto;
                })
                .orElse(null); // Eğer başkan olduğu kulüp yoksa null döner
    }


    private MembershipViewStatus resolveMembershipStatus(
            ClubEntity club,
            Member member
    ) {
        Optional<ClubMembership> membership =
                clubMemberShipRepository.findByClubAndMember(club, member);

        return membership.map(clubMembership -> switch (clubMembership.getStatus()) {
            case PENDING -> MembershipViewStatus.PENDING;
            case APPROVED -> MembershipViewStatus.APPROVED;
            case REJECTED -> MembershipViewStatus.REJECTED;
            case LEFT -> MembershipViewStatus.NOT_MEMBER;
        }).orElse(MembershipViewStatus.NOT_MEMBER);

    }

    private Member getCurrentMember() {
        UserEntity user = (UserEntity) org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return memberRepository.findByUser(user)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(MessageType.MEMBER_NOT_FOUND, "Member bulunamadı")
                ));
    }

}
