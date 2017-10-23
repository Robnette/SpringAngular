package me.robnette.spring_angular.controller;

import me.robnette.spring_angular.dao.AppUser;
import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.model.UserPojo;
import me.robnette.spring_angular.security.SecurityUser;
import me.robnette.spring_angular.service.AppUserService;
import me.robnette.spring_angular.util.Util;
import me.robnette.spring_angular.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class AppUserRestController {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppUserService appUserService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<AppUser> users() {
		return appUserRepository.findAll();
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{uid}", method = RequestMethod.GET)
	public ResponseEntity<AppUser> userByUid(@PathVariable String uid) {
		AppUser appUser = appUserRepository.findOneByUid(uid);
		if (appUser == null) {
			return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
		}
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{uid}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteUser(@PathVariable String uid) {
		SecurityUser securityUser = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (securityUser.getUid().equals(uid)) {
			throw new ForbiddenException("You cannot delete your account");
		}

		AppUser appUser = appUserRepository.findOneByUid(uid);
		if (appUser == null) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} else {
			appUserRepository.delete(appUser);
			return new ResponseEntity<String>(HttpStatus.OK);
		}

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody UserPojo userPojo) {
		AppUser appUser = appUserService.addUser(userPojo);
		return new ResponseEntity<AppUser>(appUserRepository.save(appUser), HttpStatus.CREATED);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public AppUser updateUser(@RequestBody UserPojo userPojo) {
		AppUser appUser = appUserService.updateUser(userPojo);

//		AppUser appUser = appUserRepository.findOneByUid(userPojo.getUid());
//		if(appUser == null){
//			throw new ForbiddenException("User unknown");
//		}
//		if (! appUser.getUsername().equals(userPojo.getUsername()) && appUserRepository.findOneByUsername(userPojo.getUsername()) != null) {
//			throw new ForbiddenException("Username already exist");
//		}
//		appUser.updateUser(userPojo);
//		return appUserRepository.save(appUser);
		return appUser;
	}

}
