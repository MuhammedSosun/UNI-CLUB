package com.uniClub.dto.clubDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubLogoUploadRequest {

    private Long clubId;          // hangi kulüp
    private String fileName;      // logo.png
    private String base64Content; // base64 string
}
