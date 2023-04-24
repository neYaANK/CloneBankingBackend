package me.neyaank.clonebankingbackend.security.utils;

import java.security.SecureRandom;

public class Util {
    public static String generateSecureCode(int max) {
        SecureRandom rand = new SecureRandom();
        String pin = String.valueOf(rand.nextInt(max));
        return pin;
    }
}
