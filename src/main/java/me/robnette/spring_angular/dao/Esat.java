package me.robnette.spring_angular.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class Esat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="id_esat")
    private Long idEsat;

    @Column(name="code_esat")
    @JsonProperty("code")
    private String codeEsat;

    private String nom;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_esat", referencedColumnName = "id_esat")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private List<EsatContact> esatContacts;

    public Long getIdEsat() {
        return idEsat;
    }

    public void setIdEsat(Long idEsat) {
        this.idEsat = idEsat;
    }

    public String getCodeEsat() {
        return codeEsat;
    }

    public void setCodeEsat(String codeEsat) {
        this.codeEsat = codeEsat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

//    public List<EsatContact> getEsatContacts() {
//        return esatContacts;
//    }
//
//    public void setEsatContacts(List<EsatContact> esatContacts) {
//        this.esatContacts = esatContacts;
//    }
}
