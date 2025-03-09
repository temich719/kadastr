package com.example.kadastr.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.kadastr.util.StringsStorage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Size(min = 3, max = 40, message = USERNAME_VALIDATOR_MESSAGE)
    private String username;
    @Size(min = 8, max = 80, message = PASSWORD_VALIDATOR_MESSAGE)
    private String password;

}
