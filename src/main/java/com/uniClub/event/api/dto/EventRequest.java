package com.uniClub.event.api.dto;

import com.uniClub.common.baseEntity.DtoBase;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest extends DtoBase {

    @NotBlank(message = "Etkinlik başlığı boş olamaz.")
    @Size(max = 255, message = "Etkinlik başlığı en fazla 255 karakter olabilir.")
    private String title;

    @Size(max = 2000, message = "Açıklama en fazla 2000 karakter olabilir.")
    private String description;

    @NotNull(message = "Etkinlik tarihi belirtilmelidir.")
    @Future(message = "Etkinlik tarihi geçmiş bir tarih olamaz.")
    private LocalDateTime eventDate;

    @NotBlank(message = "Etkinlik konumu boş olamaz.")
    @Size(max = 255, message = "Konum en fazla 255 karakter olabilir.")
    private String location;

    @NotNull(message = "Etkinliği oluşturan kullanıcı belirtilmelidir.")
    private Long createdByUserId;
}
