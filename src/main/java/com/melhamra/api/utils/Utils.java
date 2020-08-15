package com.melhamra.api.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private Random random = new SecureRandom();
    private String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateStringId(int length){
        StringBuilder id = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            id.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new String(id);
    }

}
