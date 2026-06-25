package com.library.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    private static String generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    //password hash
    public static String hashPassword(String password, String salt){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //combine pass and salt
            String combined = password + salt;
            md.update(combined.getBytes());
            byte[] hashBytes = md.digest();
            return Base64.getEncoder().encodeToString(hashBytes);
        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //create first password
    public static String createNewHash(String password){
        String salt = generateSalt();
        String hash = hashPassword(password, salt);

        return salt + ":" + hash;
    }

    //check password when login
    public static boolean verifyPassword(String inputPassword, String storedHashWithSalt){
        //separate hash and salt
        String[] parts = storedHashWithSalt.split(":");
        String salt = parts[0];
        String storedHash = parts[1];

        //hash input password with this salt
        String newHash = hashPassword(inputPassword, salt);

        //return true if equals
        return newHash.equals(storedHash);
    }
}
