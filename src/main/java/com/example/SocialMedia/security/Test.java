package com.example.SocialMedia.security;

import java.security.SecureRandom;
import java.util.Base64;

public class Test {
    public static String generateRandomKey() {
        // Create a SecureRandom instance to generate a secure key
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];  // 256 bits = 32 bytes
        secureRandom.nextBytes(key); // Fill the byte array with random values

        // Encode the generated key into Base64 format
        return Base64.getEncoder().encodeToString(key);
    }

    public static void main(String[] args) {
        System.out.printf(generateRandomKey());
    }
}
