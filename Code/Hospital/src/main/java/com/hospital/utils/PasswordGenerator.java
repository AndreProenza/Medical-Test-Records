package com.hospital.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public final class PasswordGenerator {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";


    /**
     * This method will generate a password using secure random and a combination
     * of upper and lower case, numbers and punctuation.
     *
     * @param length the length of the password that will be generated
     * @return the generated password or empty string on length less or equal to 0
     * 
     * based on https://stackoverflow.com/questions/19743124/java-password-generator
     */
    public static String generate(int length) {
        if (length <= 0) {
            return "";
        }

        // Variables
        StringBuilder password = new StringBuilder(length);
        SecureRandom random = new SecureRandom();

        // Collect the categories to use
        List<String> charCategories = new ArrayList<>(4);
        
        // add the possible characters
        charCategories.add(LOWER);
        charCategories.add(UPPER);
        charCategories.add(DIGITS);
        charCategories.add(PUNCTUATION);

        // Create the password
        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return new String(password);
    }
}
