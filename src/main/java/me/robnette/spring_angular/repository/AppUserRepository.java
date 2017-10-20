package me.robnette.spring_angular.repository;

import me.robnette.spring_angular.dao.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface AppUserRepository extends CrudRepository<AppUser, Long> {
	public AppUser findOneByUid(String uid);
	public AppUser findOneByUsername(String username);
	public List<AppUser> findAll();
}
