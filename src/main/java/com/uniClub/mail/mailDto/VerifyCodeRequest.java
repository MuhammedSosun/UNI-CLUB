package com.uniClub.dto.mailDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCodeRequest {

    private String email;

    private String code;
}
