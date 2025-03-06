package com.example.kadastr.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Size(min = 3, max = 40, message = "Username must be between 3 and 40 symbols!")
    private String username;
    @Size(min = 8, max = 80, message = "Password must be between 8 and 80 symbols!")
    private String password;
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 symbols!")
    private String name;
    @Size(min = 2, max = 20, message = "Surname must be between 2 and 20 symbols!")
    private String surname;
    @Size(min = 2, max = 20, message = "Parent name must be between 2 and 20 symbols!")
    private String parentName;
    private RoleDto role;

}
