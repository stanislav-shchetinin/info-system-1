package ru.shchetinin.lab1p.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.codec.digest.DigestUtils;

@ApplicationScoped
public class PasswordHash {

    public String hash(String password) {

        return DigestUtils.sha384Hex(password);
    }

    public boolean verify(String password, String hash) {

        return hash.equals(hash(password));
    }
}
