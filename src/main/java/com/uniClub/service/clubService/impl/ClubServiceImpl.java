package com.uniClub.service.clubService.impl;

import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import com.uniClub.entity.clubEntity.ClubEntity;
import com.uniClub.entity.userEntity.UserEntity;
import com.uniClub.enums.OperationType;
import com.uniClub.enums.StatusEnum;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.mapper.clubMapper.ClubMapper;
import com.uniClub.repository.clubRepository.ClubRepository;
import com.uniClub.repository.userRepository.UserRepository;
import com.uniClub.service.clubService.IClubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClubServiceImpl implements IClubService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final UserRepository userRepository;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
        this.userRepository = userRepository;
    }

    @Override
    @LoggableOperation(OperationType.CREATE_CLUB)
    public ClubResponseDTO createClub(ClubRequestDto dto) {
        ClubEntity entity = clubMapper.toEntity(dto);

        if (dto.getPresidentId() != null) {
            UserEntity president = userRepository.findById(dto.getPresidentId()).orElseThrow(()-> new BaseException(
                    new ErrorMessage(MessageType.USER_NOT_FOUND, "Başkan bulunamadı")
            ));
            entity.setPresident(president);
        }
        ClubEntity saved = clubRepository.save(entity);
        return clubMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @LoggableOperation(OperationType.FIND_CLUB)
    public ClubResponseDTO getClubById(Long id) {
        return clubMapper.toResponseDTO(getClubWithId(id));
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
        return clubRepository.findAllByStatus(StatusEnum.Active)
                .stream()
                .map(clubMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    @LoggableOperation(OperationType.FIND_ALL_CLUBS)
    public Page<ClubResponseDTO> getAllPaged(Pageable pageable) {
        Page<ClubEntity> page = clubRepository.findAll(pageable);
        return page.map(clubMapper::toResponseDTO);
    }
    @LoggableOperation(OperationType.FIND_ALL_CLUBS)
    @Override
    public Page<ClubResponseDTO> getActiveClubsPaged(Pageable pageable) {
        return clubRepository.findAllByStatus(StatusEnum.Active, pageable)
                .map(clubMapper::toResponseDTO);
    }

    @Override
    @Transactional
    @LoggableOperation(OperationType.UPDATE_CLUB)
    public ClubResponseDTO updateClub(Long id, ClubRequestDto dto) {
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
        entity.setStatus(StatusEnum.Suspended);
        clubRepository.save(entity);
    }

    @LoggableOperation(OperationType.ACTIVATE_CLUB)
    public ClubResponseDTO activateClub(Long id) {
        ClubEntity entity = getClubWithId(id);
        entity.setStatus(StatusEnum.Active);
        return clubMapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    @LoggableOperation(OperationType.DELETE_CLUB)
    public void deleteClubById(Long id) {
        ClubEntity existing = getClubWithId(id);
        existing.setStatus(StatusEnum.Terminated);
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

    private ClubEntity getClubWithId(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() ->
                        new BaseException(new ErrorMessage(
                                MessageType.CLUB_NOT_FOUND,
                                "Kulüp bulunamadı. ID = " + id
                        ))
                );
    }


}
