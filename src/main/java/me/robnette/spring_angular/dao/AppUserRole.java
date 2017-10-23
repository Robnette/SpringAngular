package me.robnette.spring_angular.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class AppUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    @Column(name="app_user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long appUserId;
    private String role;

    public AppUserRole() {
    }

    public AppUserRole(Long appUserId, String role) {
        this.appUserId = appUserId;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getRole() {
        return role;
    }

    public void setRoles(String role) {
        this.role = role;
    }
}
