package com.example.kadastr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Data transfer object of authentication response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;

}
