package org.mql.jdbc;



import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Database {
	private DataSource dataSource;
    private Connection db;

    public Database(DataSource dataSource) {
    	this.dataSource=dataSource;
    	db=dataSource.getConnection();
    }
    public String[][] select(String tableName) {
    	String request = "SELECT * FROM " + tableName;
    	return query(request);
    }
    public String[][] query(String request) {
    	 try {
           Statement sql = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           ResultSet rs = sql.executeQuery(request);
           int rows,cols;
           rs.last();
           rows=rs.getRow();
           rs.beforeFirst();
          
           ResultSetMetaData rsm= rs.getMetaData();
           cols=rsm.getColumnCount();
           String data[][]= new String[rows+1][cols];
           
           for (int i = 0; i < cols; i++) {
			data[0][i]=rsm.getColumnName(i+1);
           }
           
           int row=0;
           while (rs.next()) {
        	   row++;
        	   for(int col=0;col <cols;col++ ) {
        		   data[row][col]=rs.getString(col +1);
        	   }
           }
           sql.close();
           return data;
       } catch (Exception e) {
           System.out.println("Erreur : " + e.getMessage());
       }
		return null;
    }
    

    public String[][] selectByKeyword(String tableName , String key , Object value) {
    	String request = "SELECT * FROM " + tableName + " WHERE " + key + " LIKE '%" + value + "%'";
    	return query(request);
    }
    
    public boolean insert(String tableName,Object... row) {
    	 //	"comment améliorer ce code utilisant les flux ?
        //StringBuilder , pensez a String.format...ect
    	String query="INSERT INTO " + tableName + " VALUES('" + row[0] + "'";
		for(int i=1;i<row.length;i++) {
			query +=", '" + row[i] + "'";
		}
		query +=")";
		try {
			Statement sql=db.createStatement();
			int result = sql.executeUpdate(query);
			if(result == 1) return true;
			return false;
		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage());	
			return false;
		} 
	
		
	}
    
   
    //hibernet qui fait cettes complixcite , au dessous
    
    //probleme d'ordre le type des champs (exemple  un publicher continet un autre object Author)...ect
//    public boolean insert(String tableName , Object model) {
//   
//    	try {
//			Field fields[] = model.getClass().getDeclaredFields();
//			Object row[] = new Object[fields.length];
//			
//			for (int i = 0; i <row.length; i++) {
//				fields[i].setAccessible(true);
//				row[i]	= fields[i].get(model);
//				fields[i].setAccessible(false);
//
//			}
//			//return insert(tableName, model);
//		} catch (Exception e) {
//			System.out.println("Erreur : "+ e.getMessage());
//			return false;
//		}
//    }
//   
    
    
    
//    public void oldTestSelect() {
//        try {
//            // Un Statement est un moteur d'exécution des requêtes SQL
//            Statement sql = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            
//            // Types de requêtes SQL :
//            // - DDL (Data Definition Language) : utilisé pour modifier la structure de la base de données (ALTER, CREATE, DROP, etc.).
//            // - DML (Data Manipulation Language) : utilisé pour manipuler les données (INSERT, UPDATE, DELETE, SELECT).
//            //   -> Il existe deux catégories : 
//            //      * Requêtes de modification : INSERT, UPDATE, DELETE.
//            //      * Requêtes de sélection : SELECT.
//            // - DCL (Data Control Language) : utilisé pour gérer les privilèges et les permissions (GRANT, REVOKE, etc.).
//
//            // Exécution des requêtes :
//            // - sql.executeQuery() : utilisé pour exécuter des requêtes SELECT (DML).
//            // - sql.executeUpdate() : utilisé pour exécuter des requêtes INSERT, UPDATE, DELETE (DML).
//            // - sql.execute() : utilisé pour exécuter des requêtes DDL (CREATE, ALTER, DROP, etc.).
//
//            // Exécution d'une requête de sélection
//            ResultSet rs = sql.executeQuery("SELECT * FROM Authors");
//            
//            // Parcours du résultat et affichage des valeurs des colonnes
//            while (rs.next()) {
//                System.out.println(rs.getString(1) + " - " + rs.getString(2));
//            }
//        } catch (Exception e) {
//            System.out.println("Erreur : " + e.getMessage());
//        }
//    }
    public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		db=dataSource.getConnection();
	}
    
    public DataSource getDataSource() {
		return dataSource;
	}
    
}
