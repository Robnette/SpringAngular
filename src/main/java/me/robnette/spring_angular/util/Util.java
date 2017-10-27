package me.robnette.spring_angular.util;

import me.robnette.spring_angular.dao.AppUserRole;
import me.robnette.spring_angular.dao.TokenExpire;
import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.repository.TokenExpireRepository;
import me.robnette.spring_angular.security.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {
    public static String getUid(){
        return java.util.UUID.randomUUID().toString();
    }

    public static void passwordCheck(String password) {
        if(! password.matches(Constant.PASSWORD_REGEX)){
            throw new ForbiddenException("Password not match (Minimum 8 characters in length. " +
                    "Contains the following items: " +
                    "Uppercase Letters, " +
                    "Lowercase Letters, " +
                    "Numbers and" +
                    "Symbols");
        }
    }

    public static String passwordEncode(String password) {
        try{
            String base = password.concat(Constant.PASSWORD_SALT);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
