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

    private final static SignatureAlgorithm signatureAlgorithm = Constant.SIGNATURE_ALGORITHM;

    public static String getUid(){
        return java.util.UUID.randomUUID().toString();
    }

    public static String createToken(String login, List<AppUserRole> appUserRolesList, String uid){
        List<String> roles = new ArrayList<>();
        for(AppUserRole appUserRoles : appUserRolesList){
            roles.add(appUserRoles.getRole());
        }
        return Jwts.builder()
                .setSubject(login)
                .claim(Constant.AUTHORITIES_KEY, roles)
                .claim(Constant.UID_KEY, uid)
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm, Constant.JWT_SECRET)
                .compact();
    }

    public static Authentication getAuthentication(String token, TokenExpireRepository tokenRepository){
        TokenExpire tokenExpire = tokenRepository.findOneByCode(token);
        Long currentTimeInMilisecond = new Date().getTime();
        if(tokenExpire == null){
            throw new ForbiddenException("Token expired");
        }
        if(tokenExpire.getExpireAt().getTime() < currentTimeInMilisecond) {
            tokenRepository.delete(tokenExpire);
            throw new ForbiddenException("Token expired");
        }

        tokenExpire.setExpireAt(new Date());
        tokenRepository.save(tokenExpire);

        Claims claims = Jwts.parser().setSigningKey(Constant.JWT_SECRET).parseClaimsJws(token).getBody();
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

        List<String> roles = (List<String>) claims.get(Constant.AUTHORITIES_KEY);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        SecurityUser principal = new SecurityUser(claims.getSubject(), "", authorities, (String)claims.get("uid"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                principal, "", authorities);
        return usernamePasswordAuthenticationToken;
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

    public static String passwordEncode(String rawPassword){
        return sha256(rawPassword);
    }

    private static String sha256(String password) {
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
