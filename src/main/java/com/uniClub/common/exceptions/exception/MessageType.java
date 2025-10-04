package com.uniClub.common.exceptions.exception;

import lombok.Getter;

@Getter
public enum MessageType {
    NO_RECORD_EXIST("1000","RECORD_IS_NOT_EXIST"),
    GENERAL_EXCEPTION("9999","GENERALLY_EXCEPTION");


    private String code;

    private String message;

    MessageType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
