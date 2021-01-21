package com.cea.jwt.model;

import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Component
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;
	private String nameUser;
	private String ip;
	private String token;
	private String observation;
	private String roles;
	private Date dtGeneration;

	public User() {
	}

	public User(String login, String nameUser, String ip, String token, String observation, String roles, Date dtGeneration) {
		this.login = login;
		this.nameUser = nameUser;
		this.ip = ip;
		this.token = token;
		this.observation = observation;
		this.roles = roles;
		this.dtGeneration = dtGeneration;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public Date getDtGeneration() {
		return dtGeneration;
	}

	public void setDtGeneration(Date dtGeneration) {
		this.dtGeneration = dtGeneration;
	}
}
