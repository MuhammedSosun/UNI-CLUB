package com.uniClub.dto.notificationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uniClub.entity.baseEntity.DtoBase;
import com.uniClub.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDtoResponse extends DtoBase {

    private String id;

    private String title;

    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private NotificationType type;



}
