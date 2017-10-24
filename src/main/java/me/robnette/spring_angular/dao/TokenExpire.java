package me.robnette.spring_angular.dao;

import me.robnette.spring_angular.util.Constant;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TokenExpire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Date expireAt;

    public TokenExpire() {
    }

    public TokenExpire(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    @PrePersist
    public void setNewExpireDate(){
        expireAt = new Date(new Date().getTime() + Constant.TOKEN_TIMEOUT_INMILISECOND);
    }
}
