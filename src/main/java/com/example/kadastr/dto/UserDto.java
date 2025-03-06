package com.example.kadastr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String parentName;
    private RoleDto role;

}
