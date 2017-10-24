package me.robnette.spring_angular.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import me.robnette.spring_angular.dao.AppUserRole;
import me.robnette.spring_angular.dao.TokenExpire;
import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.repository.TokenExpireRepository;
import me.robnette.spring_angular.security.SecurityUser;
import me.robnette.spring_angular.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Configurable
public class TokenService {
    @Autowired
    private TokenExpireRepository tokenRepository;

    public String createToken(String login, List<AppUserRole> appUserRolesList, String uid){
        List<String> roles = new ArrayList<>();
        for(AppUserRole appUserRoles : appUserRolesList){
            roles.add(appUserRoles.getRole());
        }
        String token = Jwts.builder()
                .setSubject(login)
                .claim(Constant.AUTHORITIES_KEY, roles)
                .claim(Constant.UID_KEY, uid)
                .setIssuedAt(new Date())
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(Constant.SIGNATURE_ALGORITHM, Constant.JWT_SECRET)
                .compact();
        TokenExpire tokenExpire = new TokenExpire(token);
        tokenRepository.save(tokenExpire);
        return token;
    }

    public Authentication getAuthentication(String token){
        TokenExpire tokenExpire = tokenRepository.findOneByCode(token);
        Long currentTimeInMilisecond = new Date().getTime();
        if(tokenExpire == null){
            throw new ForbiddenException("Token expired");
        }
        if(tokenExpire.getExpireAt().getTime() < currentTimeInMilisecond) {
            tokenRepository.delete(tokenExpire);
            throw new ForbiddenException("Token expired");
        }

        tokenExpire.setNewExpireDate();
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

    public void deleteToken(String token){
        TokenExpire tokenExpire = tokenRepository.findOneByCode(token);
        if(tokenExpire != null){
            tokenRepository.delete(tokenExpire);
        }
    }
}
