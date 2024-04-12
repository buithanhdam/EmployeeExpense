package vn.edu.hcmuaf.fit.employeeexpense.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpGenerator {
    private int len = 8;

    public String createOtp(){
        String result = "";
        String numbers = "0123456789";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numChar = numbers + Small_chars;
        Random rd = new Random();
        //String [] otp = new String[len];

        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            //otp[i] = numbers.charAt(rd.nextInt(numbers.length()));
            result += numChar.charAt(rd.nextInt(numChar.length()-1)) + "";

        }

        return result;
    }
}
