package ru.shchetinin.lab1p.config;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordHasher {
    public static String hashPassword(String password) {
        return DigestUtils.sha384Hex(password);
    }
}
