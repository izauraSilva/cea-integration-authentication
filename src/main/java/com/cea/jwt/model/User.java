package com.cea.jwt.model;

import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Component
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String username;
	private String perfil;
	private String ip;
	private String token;
	private String observation;
	private Date dtGeneration;

	public User() {
	}

	public User(String username, String perfil, String ip, String token, String observation, Date dtGeneration) {
		this.username = username;
		this.perfil = perfil;
		this.ip = ip;
		this.token = token;
		this.observation = observation;
		this.dtGeneration = dtGeneration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
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

	public Date getDtGeneration() {
		return dtGeneration;
	}

	public void setDtGeneration(Date dtGeneration) {
		this.dtGeneration = dtGeneration;
	}
}
