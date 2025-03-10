package com.example.kadastr.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.kadastr.util.StringsStorage.ROLE_VALIDATOR_MESSAGE;

//Data transfer object of Role entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    @Pattern(regexp = "ROLE_ADMIN|ROLE_JOURNALIST|ROLE_SUBSCRIBER", message = ROLE_VALIDATOR_MESSAGE)
    private String name;

}
