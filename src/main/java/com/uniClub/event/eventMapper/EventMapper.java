package com.uniClub.event.eventMapper;

import com.uniClub.event.eventDto.EventParticipantDto;
import com.uniClub.event.eventDto.EventRequest;
import com.uniClub.event.eventDto.EventResponse;
import com.uniClub.event.eventEntity.Event;
import com.uniClub.enums.ParticipationStatus; // 🔥 Enum Importu Eklendi
import com.uniClub.event.eventEntity.EventParticipation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EventMapper {

    // REQUEST -> ENTITY
    @Mapping(target = "clubs", ignore = true)
    @Mapping(target = "participantCount", ignore = true)
    @Mapping(target = "eventParticipations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Event toEventEntity(EventRequest eventRequest);

    // ENTITY -> RESPONSE
    @Mapping(target = "joined", expression = "java(checkIfJoined(event))")
    @Mapping(target = "clubIds", expression = "java(mapClubIds(event))")
    // 🔥 YENİ: Başvuru durumunu (PENDING, APPROVED vs.) DTO'ya ekle
    @Mapping(target = "participationStatus", expression = "java(getParticipationStatus(event))")
    EventResponse toEventResponse(Event event);

    void updateEventFromRequest(EventRequest eventRequest, @MappingTarget Event event);

    // --- YARDIMCI METODLAR ---

    default Set<Long> mapClubIds(Event event) {
        if (event.getClubs() == null) return Set.of();
        return event.getClubs().stream()
                .map(c -> c.getId())
                .collect(Collectors.toSet());
    }

    // KULLANICI KATILDI MI (Herhangi bir başvurusu var mı?)
    default boolean checkIfJoined(Event event) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) return false;

            String currentUsername = authentication.getName();
            if ("anonymousUser".equals(currentUsername)) return false;

            if (event.getEventParticipations() == null) return false;

            return event.getEventParticipations().stream()
                    .anyMatch(ep ->
                            ep.getMember() != null &&
                                    ep.getMember().getUser() != null &&
                                    ep.getMember().getUser().getUsername().equals(currentUsername)
                    );
        } catch (Exception e) {
            return false;
        }
    }

    // 🔥 YENİ METOD: Katılım Durumunu (Enum) Döndür
    default ParticipationStatus getParticipationStatus(Event event) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) return null;

            String currentUsername = authentication.getName();
            if ("anonymousUser".equals(currentUsername)) return null;

            if (event.getEventParticipations() == null) return null;

            // Kullanıcının kaydını bul ve statüsünü döndür
            return event.getEventParticipations().stream()
                    .filter(ep ->
                            ep.getMember() != null &&
                                    ep.getMember().getUser() != null &&
                                    ep.getMember().getUser().getUsername().equals(currentUsername)
                    )
                    .findFirst()
                    .map(ep -> ep.getStatus()) // EventParticipation entity içindeki getStatus()
                    .orElse(null); // Kaydı yoksa null döner
        } catch (Exception e) {
            return null;
        }
    }
    // EventMapper interface içine ekle:
    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "member.user.username", target = "username")
    @Mapping(source = "member.user.email", target = "email")
    EventParticipantDto toParticipantDto(EventParticipation participation);
}