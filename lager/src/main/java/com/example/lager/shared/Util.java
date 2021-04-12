package com.example.lager.shared;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Util{
    public String generateHash(String uniqueProperty){
       try {
           MessageDigest hashFunction = MessageDigest.getInstance("SHA3-256");
           byte[] digest= hashFunction.digest(uniqueProperty.getBytes());
           return digest.toString().substring(3);
       }catch (NoSuchAlgorithmException e)  {
           e.printStackTrace();
           return "";
       }
    }
}
