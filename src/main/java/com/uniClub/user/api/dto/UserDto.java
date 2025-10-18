package com.uniClub.user.api.dto;

import com.uniClub.common.baseEntity.DtoBase;
import com.uniClub.user.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends DtoBase {

    private UUID id;
    private String username;
    private String password;
    private Role role;



}
