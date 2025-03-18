package org.mql.jdbc;

import java.sql.Connection;

abstract public class DataSource {
	private String driver;
	private String brigde;
	private String host;
	private String source;
	private String user;
	private String password;
	
	public DataSource() {

	}
	public DataSource(String driver, String brigde, String host, String source, String user, String password) {
		super();
		this.driver = driver;
		this.brigde = brigde;
		this.host = host;
		this.source = source;
		this.user = user;
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getBrigde() {
		return brigde;
	}

	public void setBrigde(String brigde) {
		this.brigde = brigde;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	abstract public Connection getConnection();
}
