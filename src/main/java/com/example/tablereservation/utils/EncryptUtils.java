package com.example.tablereservation.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptUtils {

    // 비밀번호를 암호화 하는 함수
    public static String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    // 비밀 번호의 일치 여부를 반환하는 함수
    public static boolean equalPassword(String password, String encryptedPassword){

        return BCrypt.checkpw(password, encryptedPassword);

    }
}
