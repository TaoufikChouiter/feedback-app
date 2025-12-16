package com.env.feedback.security;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class PasswordGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordGenerator() {
    }

    public static String generate(int length) {
        if (length < 8) throw new IllegalArgumentException("Password length must be >= 8");

        String upper = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lower = "abcdefghijkmnopqrstuvwxyz";
        String digits = "23456789";
        String symbols = "!@#$%&*";

        String all = upper + lower + digits + symbols;
        StringBuilder sb = new StringBuilder(length);

        // guarantee at least one of each type
        sb.append(upper.charAt(RANDOM.nextInt(upper.length())));
        sb.append(lower.charAt(RANDOM.nextInt(lower.length())));
        sb.append(digits.charAt(RANDOM.nextInt(digits.length())));
        sb.append(symbols.charAt(RANDOM.nextInt(symbols.length())));

        for (int i = 4; i < length; i++) {
            sb.append(all.charAt(RANDOM.nextInt(all.length())));
        }

        List<Character> pwdChars = sb.chars()
                .mapToObj(c -> (char)c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars, RANDOM);

        StringBuilder password = new StringBuilder();
        pwdChars.forEach(password::append);
        return password.toString();
    }

}