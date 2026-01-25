package com.uniClub.user.userDto;

import com.uniClub.baseEntity.DtoBase;
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
    private String email;
    private Role role;



}
