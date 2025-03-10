package com.example.kadastr.security.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class PasswordGenerator {

    //data set from which password will be generated
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

    public char[] generatePassword() {
        SecureRandom secureRandom = new SecureRandom();
        int len = getRandomPasswordLen();
        char[] password = new char[len];

        for (int i = 0; i < len; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            password[i] = CHARACTERS.charAt(index);
        }

        return password;
    }

    //generate password length between 8 and 80 symbols
    private int getRandomPasswordLen() {
        Random random = new Random();
        int min = 8;
        int max = 80;
        return random.nextInt(max - min + 1) + min;
    }

}
