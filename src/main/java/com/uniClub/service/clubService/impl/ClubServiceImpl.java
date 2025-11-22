package com.uniClub.service.clubService.impl;

import com.uniClub.dto.clubDto.ClubRequestDto;
import com.uniClub.dto.clubDto.ClubResponseDTO;
import com.uniClub.entity.clubEntity.ClubEntity;
import com.uniClub.enums.OperationType;
import com.uniClub.enums.StatusEnum;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.mapper.clubMapper.ClubMapper;
import com.uniClub.repository.clubRepository.ClubRepository;
import com.uniClub.service.clubService.IClubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClubServiceImpl implements IClubService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
    }

    @Override
    @LoggableOperation(OperationType.CREATE_CLUB)
    public ClubResponseDTO createClub(ClubRequestDto clubRequestDto) {
        if (clubRepository.existsByClubName(clubRequestDto.getClubName())) {
            throw new BaseException(new ErrorMessage(
                    MessageType.DUPLICATE_RECORD,
                    "Bu kulüp ismi zaten kayıtlı: " + clubRequestDto.getClubName()
            ));
        }
        ClubEntity entity = clubMapper.toEntity(clubRequestDto);
        ClubEntity saved =  clubRepository.save(entity);
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
    @Transactional(readOnly = true)
    @Override
    @LoggableOperation(OperationType.FIND_ALL_CLUBS)
    public Page<ClubResponseDTO> getAllPaged(Pageable pageable) {
        Page<ClubEntity> page = clubRepository.findAll(pageable);
        return page.map(clubMapper::toResponseDTO);
    }

    @Override
    @Transactional
    @LoggableOperation(OperationType.UPDATE_CLUB)
    public ClubResponseDTO updateClub(Long id, ClubRequestDto dto) {
        ClubEntity existing = getClubWithId(id);

        existing.setClubName(dto.getClubName());
        existing.setShortName(dto.getShortName());
        existing.setDescription(dto.getDescription());
        existing.setLogoUrl(dto.getLogoUrl());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setInstagram(dto.getInstagram());
        existing.setFoundationDate(dto.getFoundationDate());
        existing.setApproved(dto.getApproved());
        existing.setStatus(dto.getStatus());

        ClubEntity updated = clubRepository.save(existing);

        return clubMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    @LoggableOperation(OperationType.DELETE_CLUB)
    public void deleteClubById(Long id) {
        ClubEntity existing = getClubWithId(id);
        existing.setStatus(StatusEnum.Terminated);
        clubRepository.save(existing);
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
