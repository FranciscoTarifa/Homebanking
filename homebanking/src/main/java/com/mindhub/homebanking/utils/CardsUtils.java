package com.mindhub.homebanking.utils;

public final class CardsUtils {
    public static int getCcvRandomNumber() {
        int ccvRandomNumber = (int) ((Math.random() * (999-100)) + 100);
        return ccvRandomNumber;
    }

    public static String getRandomCardNumber() {
        String randomCardNumber = (int) ((Math.random() * (9999-1000)) + 1000) + " " + (int) ((Math.random() * (9999-1000)) + 1000) + " " +(int) ((Math.random() * (9999-1000)) + 1000) + " "+ (int) ((Math.random() * (9999-1000)) + 1000);
        return randomCardNumber;
    }
}
