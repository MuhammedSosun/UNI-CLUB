package com.uniClub.common.exceptions.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private MessageType messageType;
    private String ofStatic;


    public String prepareErrorMessage(){
        StringBuilder builder = new StringBuilder();
        if(messageType != null && messageType.getMessage() != null){
            builder.append(messageType.getMessage());
        }
        if(ofStatic != null){
            builder.append(" : " + ofStatic);
        }
        return builder.toString();
    }
}
