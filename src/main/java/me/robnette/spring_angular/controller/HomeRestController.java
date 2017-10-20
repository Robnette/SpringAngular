package me.robnette.spring_angular.controller;

import me.robnette.spring_angular.dao.AppUser;
import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.util.Util;
import me.robnette.spring_angular.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
public class HomeRestController {
	@Autowired
	private AppUserRepository appUserRepository;


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
			throw new ForbiddenException("Username already exist");
		}

		Util.passwordCheck(appUser.getPassword());

		appUser.setPassword(Util.passwordEncode(appUser.getPassword()));
		appUser.setUid(Util.getUid());

		List<String> roles = new ArrayList<>();
		roles.add("USER");
		appUser.setRoles(roles);
		return new ResponseEntity<AppUser>(appUserRepository.save(appUser), HttpStatus.CREATED);
	}


	@RequestMapping("/user")
	public AppUser user(Principal principal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedUsername = auth.getName();
		return appUserRepository.findOneByUsername(loggedUsername);
	}


	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password,
                                                     HttpServletResponse response) throws IOException {
		String token = null;
		AppUser appUser = appUserRepository.findOneByUsername(username);
		Map<String, Object> tokenMap = new HashMap<String, Object>();

		String passwordEncode = Util.passwordEncode(password);

		if (appUser != null && appUser.getPassword().equals(passwordEncode)) {
			token = Util.createToken(username, appUser.getRoles(), appUser.getUid());
			tokenMap.put("token", token);
			tokenMap.put("user", appUser);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
		} else {
			tokenMap.put("token", null);
			return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
		}

	}
}
