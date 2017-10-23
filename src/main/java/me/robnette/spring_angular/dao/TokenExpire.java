package me.robnette.spring_angular.dao;

import me.robnette.spring_angular.util.Constant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Entity
public class TokenExpire {
    @Id
    private String code;
    private Date expireAt;

    public TokenExpire() {
    }

    public TokenExpire(String code) {
        this.code = code;
        expireAt = new Date();
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
    @PreUpdate
    public void tokenSetDate(){
        expireAt = new Date(new Date().getTime() + Constant.TOKEN_TIMEOUT_INMILISECOND);
    }
}
