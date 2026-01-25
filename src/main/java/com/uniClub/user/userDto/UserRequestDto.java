package com.uniClub.dto.userDto;

import com.uniClub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private String username;
    private String email;
    private String password;
    private String role;
}
