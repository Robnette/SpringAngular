package me.robnette.spring_angular.controller;

import me.robnette.spring_angular.dao.AppUser;
import me.robnette.spring_angular.model.LoginPojo;
import me.robnette.spring_angular.model.UserPojo;
import me.robnette.spring_angular.repository.TokenExpireRepository;
import me.robnette.spring_angular.security.SecurityUser;
import me.robnette.spring_angular.service.AppUserService;
import me.robnette.spring_angular.service.TokenService;
import me.robnette.spring_angular.util.Constant;
import me.robnette.spring_angular.util.Util;
import me.robnette.spring_angular.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class HomeRestController {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private TokenExpireRepository tokenRepository;
	@Autowired
	private TokenService tokenService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

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
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginPojo logiPojo){
		Map<String, Object> tokenMap = new HashMap<String, Object>();
		if(logiPojo.getUsername() == null || logiPojo.getPassword() == null){
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
		}

		log.trace("---------- trace ------------- authenticate Log");
		log.debug("---------- debug ------------- authenticate Log");
		log.info("---------- info ------------- authenticate Log");
		log.warn("---------- warn ------------- authenticate Log");
		log.error("---------- error ------------- authenticate Log");

		AppUser appUser = appUserRepository.findOneByUsername(logiPojo.getUsername());
		String passwordEncode = Util.passwordEncode(logiPojo.getPassword());

		if (appUser != null && appUser.getPassword().equals(passwordEncode)) {
			String token = tokenService.createToken(logiPojo.getUsername(), appUser.getAppUserRoles(), appUser.getUid());
			tokenMap.put("token", token);
			tokenMap.put("user", appUser);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
		} else {
//			tokenMap.put("token", null);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<String> logout(HttpServletRequest request) {
		String authHeader = request.getHeader(Constant.AUTHORIZATION_HEADER);
		if(authHeader != null && authHeader.startsWith(Constant.BEARER_AUTHENTICATION)) {
			String token = authHeader.substring(Constant.BEARER_AUTHENTICATION.length());
			tokenService.deleteToken(token);
		}
		return new ResponseEntity<String>("Logout", HttpStatus.OK);
	}
}
