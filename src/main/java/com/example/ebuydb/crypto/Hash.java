package com.example.ebuydb.crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static String SHA256(String text){
        String hex;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Change this to UTF-16 if needed
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            hex = String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            hex = "Algorithm SHA-256 does not exists";
        }
        return hex;
    }
}
