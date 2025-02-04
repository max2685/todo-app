package org.app;

import java.util.UUID;

public class Utils {

    private static String getUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateRandomEmail() {
        return String.format("%s@%s", getUniqueId(), "todoapp.com");
    }

    public static String generateRandomPassword() {
        return String.format("%s@PAS!", getUniqueId());
    }
}

