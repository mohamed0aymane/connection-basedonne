package org.mql.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

//cette classe permet de se connecter a une base de donne MySQL
public class MySQLDataSource extends DataSource {
	public static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
	public static final String MYSQL_BRIDGE="jdbc:mysql:";
	
	// Constructeur complet (tu fournis tout manuellement)
	public MySQLDataSource(String driver, String brigde, String host, String source, String user, String password) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, host, source, user, password);
	}
	
	
	// Constructeur où on fournit juste la base, user et mot de passe (host=localhost par défaut)
	public MySQLDataSource(String source, String user, String password) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", source, user, password);
	}
	
	
	// Constructeur avec juste base et user (mot de passe vide)
	public MySQLDataSource(String source, String user) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", source, user, "");
	}
	
	// Constructeur avec juste base (user=root et mot de passe vide)
	public MySQLDataSource(String source) {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", source, "root", "");
	}
	
	
	// Constructeur sans rien : base = mysql, user = root, mot de passe vide
	public MySQLDataSource()  {
		super(MYSQL_DRIVER, MYSQL_BRIDGE, "Localhost", "mysql", "root", "");
		}
	
	
	
	/*La méthode getConnection() ici sert à établir une connexion avec une base de données MySQL via JDBC.
	 *  C’est une méthode clé dans tout projet Java 
	 * qui utilise une base de données relationnelle.
	 * charger le driver,construit l'url,tente d'etablir la connexion avec DriverManager*/
	
	/*si la methode etait static on peut l'appeler comme ca :
	 Connection conn = MySQLDataSource.getConnection();
	 */
	@Override
	public Connection getConnection() {
		  try {
			  // Cela charge le driver JDBC MySQL (ex: "com.mysql.cj.jdbc.Driver")
			  // pour que DriverManager sache comment parler à MySQL.
	            Class<?> cls = Class.forName(getDriver());
	            String url = getBrigde() + "//" + getHost() + "/" + getSource() + "/";// url: jdbc:mysql://Localhost/mysql/

	         
	            // Se connecter à la base de données
   	            Connection db = DriverManager.getConnection(url, getUser(), getPassword());
	            System.out.println("Connexion établie avec succès !");
	            return db;
	        } catch (Exception e) {
	            System.out.println("Erreur : " + e.getMessage());
	  		  return null;

	        }
	}

}
