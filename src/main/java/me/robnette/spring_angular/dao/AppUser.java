package me.robnette.spring_angular.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import me.robnette.spring_angular.model.UserPojo;
import me.robnette.spring_angular.util.Util;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
public class AppUser implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long id;
	private String name;

	@Column(unique = true)
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "app_user_id", referencedColumnName = "id")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<AppUserRole> appUserRoles;

	@Column(unique = true)
	private String uid;

	public AppUser() {
	}

	/**
	 * for create only
	 * */
	public AppUser(UserPojo userPojo) {
		if(uid != null){
			throw new RuntimeException("Bad way for create user !");
		}
		Util.passwordCheck(userPojo.getPassword());

		name = userPojo.getName();
		username = userPojo.getUsername();
		password = Util.passwordEncode(userPojo.getPassword());
	}

	/**
	 * for update
	 */
	public void updateUser(UserPojo userPojo){
		if(uid == null){
			throw new RuntimeException("Bad way for update user !");
		}
		Util.passwordCheck(userPojo.getPassword());

		name = userPojo.getName();
		username = userPojo.getUsername();
		password = Util.passwordEncode(userPojo.getPassword());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AppUserRole> getAppUserRoles() {
		return appUserRoles;
	}

	public void setAppUserRoles(List<AppUserRole> appUserRoles) {
		this.appUserRoles = appUserRoles;
	}

	@JsonProperty("roles")
	public List<String> getRoles(){
		List<String> roles = new ArrayList<>();
		for(AppUserRole role : appUserRoles){
			roles.add(role.getRole());
		}
		return roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
	for (AppUserRole role : appUserRoles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@PrePersist
	public void userPrePersist() {
		uid = Util.getUid();
	}
//	@PostPersist
//	public void userPostPersist() {
//		System.out.println("Listening User Post Persist : " + getName());
//	}
//	@PostLoad
//	public void userPostLoad() {
//		System.out.println("Listening User Post Load : " + getName());
//	}
//	@PreUpdate
//	public void userPreUpdate() {
//		System.out.println("Listening User Pre Update : " + getName());
//	}
//	@PostUpdate
//	public void userPostUpdate() {
//		System.out.println("Listening User Post Update : " + getName());
//	}
//	@PreRemove
//	public void userPreRemove() {
//		System.out.println("Listening User Pre Remove : " + getName());
//	}
//	@PostRemove
//	public void userPostRemove() {
//		System.out.println("Listening User Post Remove : " + getName());
//	}
}
