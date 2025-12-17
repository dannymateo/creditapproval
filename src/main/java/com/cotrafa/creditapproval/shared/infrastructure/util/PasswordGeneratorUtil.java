package com.cotrafa.creditapproval.shared.infrastructure.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PasswordGeneratorUtil {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "@#$%^&+=!.*";
    private static final String ALL_CHARS = LOWER + UPPER + DIGITS + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    public String generateStrongPassword() {
        int length = 12; // Longitud segura por defecto
        StringBuilder password = new StringBuilder(length);

        password.append(getRandomChar(LOWER));
        password.append(getRandomChar(UPPER));
        password.append(getRandomChar(DIGITS));
        password.append(getRandomChar(SPECIAL));

        for (int i = 4; i < length; i++) {
            password.append(getRandomChar(ALL_CHARS));
        }

        return shuffleString(password.toString());
    }

    private char getRandomChar(String source) {
        return source.charAt(random.nextInt(source.length()));
    }

    private String shuffleString(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        StringBuilder result = new StringBuilder();
        for (Character c : characters) {
            result.append(c);
        }
        return result.toString();
    }
}