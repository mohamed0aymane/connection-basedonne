package org.mql.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLDataSource extends DataSource {
	public static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
	public static final String MYSQL_BRIDGE="jdbc:mysql:";
	
	public MySQLDataSource(String driver, String brigde, String host, String source, String user, String password) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, host, source, user, password);
	}

	public MySQLDataSource(String source, String user, String password) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", source, user, password);
	}

	public MySQLDataSource(String source, String user) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", source, user, "");
	}

	public MySQLDataSource(String source) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", source, "root", "");
	}
	public MySQLDataSource()  {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", "mysql", "root", "");
		}
	@Override
	public Connection getConnection() {
		  try {
			  // Chargement du pilote JDBC
	            Class<?> cls = Class.forName(getDriver());
	            String url = getBrigde() + "//" + getHost() + "/" + getSource() + "/";
	            
   	            Connection db = DriverManager.getConnection(url, getUser(), getPassword());
	            System.out.println("Connexion établie avec succès !");
	            return db;
	        } catch (Exception e) {
	            System.out.println("Erreur : " + e.getMessage());
	  		  return null;

	        }
	}

}
