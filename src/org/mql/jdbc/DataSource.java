package org.mql.jdbc;

import java.sql.Connection;

//classe abstraite qui définit les infos communes pour se connecter à une base de données.
abstract public class DataSource {
	private String driver; //nom du driver JDBC
	private String brigde; //protocole JDBC (ex: jdbc:mysql:)
	private String host;   //adresse de l'hote (serveur de base de donnees)
	private String source; //nom de la base de donnees
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
	
	// Méthode abstraite pour obtenir une connexion (sera implémentée dans les classes filles)
	abstract public Connection getConnection();
}
