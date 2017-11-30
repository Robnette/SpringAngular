package me.robnette.spring_angular.repository;

import me.robnette.spring_angular.dao.EsatContact;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EsatContactRepository extends CrudRepository<EsatContact, Long> {
    public EsatContact findOneByIdEsatContact(Long id);
    public List<EsatContact> findByIdEsat(Long idEsat);
}
