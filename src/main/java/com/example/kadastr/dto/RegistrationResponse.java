package com.example.kadastr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Data transfer object of registration response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {

    private String generatedPassword;

}
