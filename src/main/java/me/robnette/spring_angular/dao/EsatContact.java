package me.robnette.spring_angular.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class EsatContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_esat_contact")
    @JsonProperty("uid")
    private Long idEsatContact;

    @Column(name="id_esat")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long idEsat;

    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    @Column(nullable = false)
    private String telephone;
    @Column(nullable = false)
    private String mobile;
    @Column(nullable = false)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    public Long getIdEsatContact() {
        return idEsatContact;
    }

    public void setIdEsatContact(Long idEsatContact) {
        this.idEsatContact = idEsatContact;
    }

    public Long getIdEsat() {
        return idEsat;
    }

    public void setIdEsat(Long idEsat) {
        this.idEsat = idEsat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
