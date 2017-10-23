package me.robnette.spring_angular.service;

import me.robnette.spring_angular.dao.AppUser;
import me.robnette.spring_angular.dao.AppUserRole;
import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.model.UserPojo;
import me.robnette.spring_angular.repository.AppUserRepository;
import me.robnette.spring_angular.repository.AppUserRolesRepository;
import me.robnette.spring_angular.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Configurable
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserRolesRepository appUserRolesRepository;

    public AppUser addUser(UserPojo userPojo){
        if (appUserRepository.findOneByUsername(userPojo.getUsername()) != null) {
            throw new ForbiddenException("Username already exist");
        }
        Util.passwordCheck(userPojo.getPassword());

        AppUser appUser = new AppUser(userPojo);
        appUserRepository.save(appUser);

        //set default "USER" roles
        List<String> roles = userPojo.getRoles();
        if(! roles.contains("USER")) {
            roles.add("USER");
        }
        AppUserRole tmp;
        List<AppUserRole> appUserRoles = new ArrayList<>();
        for(String role : roles){
            tmp = new AppUserRole(appUser.getId(), role);
            appUserRolesRepository.save(tmp);
            appUserRoles.add(tmp);
        }
        appUser.setAppUserRoles(appUserRoles);

        return appUser;
    }

    public AppUser updateUser(UserPojo userPojo){
        AppUser appUser = appUserRepository.findOneByUid(userPojo.getUid());
        if(appUser == null){
            throw new ForbiddenException("User unknown");
        }
        if (! appUser.getUsername().equals(userPojo.getUsername()) && appUserRepository.findOneByUsername(userPojo.getUsername()) != null) {
            throw new ForbiddenException("Username already exist");
        }
        appUser.updateUser(userPojo);
        appUserRepository.save(appUser);

        List<String> roles = userPojo.getRoles();
        List<AppUserRole> appUserRoles = appUser.getAppUserRoles();
        Iterator<AppUserRole> appUserRoleIterator = appUserRoles.iterator();
        while(appUserRoleIterator.hasNext()){
            AppUserRole appUserRole = appUserRoleIterator.next();
            if(! roles.contains(appUserRole.getRole())){
                appUserRoleIterator.remove();
                appUserRolesRepository.delete(appUserRole.getId());
            }else{
                roles.remove(appUserRole.getRole());
            }
        }
        for(String role : roles){
            AppUserRole appUserRole = new AppUserRole(appUser.getId(), role);
            appUserRolesRepository.save(appUserRole);
            appUserRoles.add(appUserRole);
        }

        appUser.setAppUserRoles(appUserRoles);

        return appUser;
    }
}
