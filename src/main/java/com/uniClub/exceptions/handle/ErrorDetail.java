package com.uniClub.exceptions.handle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail<E> {

    private String path;

    private LocalDateTime createTime;

    private String host;

    private E message;
}
