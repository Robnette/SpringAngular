package me.robnette.spring_angular.repository;

import me.robnette.spring_angular.dao.AppUserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppUserRolesRepository extends CrudRepository<AppUserRole, Long> {
}
