package me.robnette.spring_angular.controller;

import me.robnette.spring_angular.dao.AppUser;
import me.robnette.spring_angular.dao.TokenExpire;
import me.robnette.spring_angular.model.UserPojo;
import me.robnette.spring_angular.repository.TokenExpireRepository;
import me.robnette.spring_angular.security.SecurityUser;
import me.robnette.spring_angular.service.AppUserService;
import me.robnette.spring_angular.util.Util;
import me.robnette.spring_angular.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
public class HomeRestController {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private TokenExpireRepository tokenRepository;


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody UserPojo userPojo) {
		AppUser appUser = appUserService.addUser(userPojo);
		return new ResponseEntity<AppUser>(appUser, HttpStatus.CREATED);
	}


	@RequestMapping("/user")
	public AppUser user() {
		SecurityUser securityUser = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String uid = securityUser.getUid();
		return appUserRepository.findOneByUid(uid);
	}


	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password,
                                                     HttpServletResponse response) throws IOException {
		String token = null;
		AppUser appUser = appUserRepository.findOneByUsername(username);
		Map<String, Object> tokenMap = new HashMap<String, Object>();

		String passwordEncode = Util.passwordEncode(password);

		if (appUser != null && appUser.getPassword().equals(passwordEncode)) {
			token = Util.createToken(username, appUser.getAppUserRoles(), appUser.getUid());
			TokenExpire tokenExpire = new TokenExpire(token);
			tokenRepository.save(tokenExpire);
			tokenMap.put("token", token);
			tokenMap.put("user", appUser);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
		} else {
			tokenMap.put("token", null);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
		}

	}
}
