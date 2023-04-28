package me.neyaank.clonebankingbackend.security.utils;

import java.security.SecureRandom;

public class Util {
    public static String generateSecureCode(int max, int length) {
        SecureRandom rand = new SecureRandom();
        String pin = String.valueOf(rand.nextInt(max));
        while (pin.length() < length) pin = "0" + pin;
        return pin;
    }
}
