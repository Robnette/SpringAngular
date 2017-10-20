package me.robnette.spring_angular.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {

    private String uid;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String uid) {
        super(username, password, authorities);
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "---------------------------------- SecurityUser{" +
                "uid='" + uid + '\'' +
                '}';
    }
}
