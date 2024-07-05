package vn.edu.hcmuaf.fit.employeeexpense.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpGenerator {

    public String createOtp() {
        StringBuilder result = new StringBuilder();
        String numbers = "0123456789876543210";
        Random rd = new Random();

        for (int i = 0; i < 6; i++) {
            result.append(numbers.charAt(rd.nextInt(numbers.length() - 1)));
        }

        return result.toString();
    }
}
