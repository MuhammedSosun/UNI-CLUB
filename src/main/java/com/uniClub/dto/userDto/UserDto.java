package com.uniClub.dto.userDto;

import com.uniClub.entity.baseEntity.DtoBase;
import com.uniClub.enums.Role;
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
