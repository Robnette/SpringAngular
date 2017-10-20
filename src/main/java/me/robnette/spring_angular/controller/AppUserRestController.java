package me.robnette.spring_angular.controller;

import me.robnette.spring_angular.dao.AppUser;
import me.robnette.spring_angular.exception.ForbiddenException;
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<AppUser> users() {
//		SecurityUser securityUser = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		System.out.println(securityUser);
		return appUserRepository.findAll();
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<AppUser> userById(@PathVariable Long id) {
		AppUser appUser = appUserRepository.findOne(id);
		if (appUser == null) {
			return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
		}
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
		AppUser appUser = appUserRepository.findOne(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedUsername = auth.getName();
		if (appUser == null) {
			return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
		} else if (appUser.getUsername().equalsIgnoreCase(loggedUsername)) {
			throw new ForbiddenException("You cannot delete your account");
		} else {
			appUserRepository.delete(appUser);
			return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
		}

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
			throw new ForbiddenException("Username already exist");
		}

		Util.passwordCheck(appUser.getPassword());

		appUser.setPassword(Util.passwordEncode(appUser.getPassword()));
		appUser.setUid(Util.getUid());

		return new ResponseEntity<AppUser>(appUserRepository.save(appUser), HttpStatus.CREATED);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public AppUser updateUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null
				&& appUserRepository.findOneByUsername(appUser.getUsername()).getId() != appUser.getId()) {
			throw new ForbiddenException("Username already exist");
		}

		Util.passwordCheck(appUser.getPassword());

		appUser.setPassword(Util.passwordEncode(appUser.getPassword()));

		return appUserRepository.save(appUser);
	}

}
