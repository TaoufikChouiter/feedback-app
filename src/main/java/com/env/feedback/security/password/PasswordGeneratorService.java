package com.env.feedback.security.password;

import org.passay.*;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class PasswordGeneratorService {
    private static final List<CharacterRule> RULES = List.of(
            new CharacterRule(EnglishCharacterData.UpperCase, 2),
            new CharacterRule(EnglishCharacterData.LowerCase, 2),
            new CharacterRule(EnglishCharacterData.Digit, 2),
            new CharacterRule(EnglishCharacterData.Special, 2)
    );

    public String generate(int i) {
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(12, RULES);
    }
}

