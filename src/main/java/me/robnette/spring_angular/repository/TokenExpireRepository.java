package me.robnette.spring_angular.repository;

import me.robnette.spring_angular.dao.TokenExpire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface TokenExpireRepository extends CrudRepository<TokenExpire, Long> {

    public TokenExpire findOneByCode(String token);

    @Query("select t from TokenExpire t where t.expireAt < ?1")
    public List<TokenExpire> findAllBefore(Date now);
}
