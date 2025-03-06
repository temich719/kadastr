package com.example.kadastr.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    @Pattern(regexp = "ROLE_ADMIN|ROLE_JOURNALIST|ROLE_SUBSCRIBER", message = "Role can be only from list - [ROLE_ADMIN, ROLE_JOURNALIST, ROLE_SUBSCRIBER]!")
    private String name;

}
