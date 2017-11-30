package me.robnette.spring_angular.repository;

import me.robnette.spring_angular.dao.Esat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EsatRepository extends CrudRepository<Esat, Long> {
    public Esat findOneByCodeEsat(String codeEsat);
    public List<Esat> findAll();
}
