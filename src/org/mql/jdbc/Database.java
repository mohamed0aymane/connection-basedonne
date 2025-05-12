package org.mql.jdbc;



import java.lang.reflect.Field;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Database {
	private DataSource dataSource;
	
	/*on doit fait comme ca lors d'appel car elle depend d'un objet de type MySQLDataSource 
	 * DataSource ds = new MySQLDataSource(...);
	Connection conn = ds.getConnection();*/
	private Connection db;
    

    //initialise la connexion avec une source de donnees
    public Database(DataSource dataSource) {
    	this.dataSource=dataSource;
    	db=dataSource.getConnection();
    }
    
    
    //Change la source de donnes
    public void setDataSource(DataSource dataSource) {
  		this.dataSource = dataSource;
  		db=dataSource.getConnection();
  	}
     //recupere la source de donnees actuelle 
    public DataSource getDataSource() {
  		return dataSource;
  	}
      
      //retourne  plusieurs lignes et colonnes par exemple :la premiere ligne etcontient les noms de colonnes 
    public String[][] select(String tableName) {
    	String request = "SELECT * FROM " + tableName;
    	return query(request);
    }
    
    public String[][] query(String request) {
    
    	 try {	/*db:est la connection a la base de donne
    	    	 * createStatement(...) :cree un objet qui permet d'executer des requetes SQL.
    	    	 * TYPE.SCROLL_INSENSITIVE: permet de se deplacer dans les resultats(avant/arriere),
    	    	   		sans etre affecte par les changements dans la base pendant l'execution
    	    	 *  CONCUR_READ_ONLY signifie que les données ne peuvent pas être modifiées via le ResultSet.
    	    	 */
           Statement sql = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           ResultSet rs = sql.executeQuery(request);
           int rows,cols;
           
           rs.last();
           rows=rs.getRow();//retourne le numero de la ligne actuelle (donc ici le nombre total de lignes)
           rs.beforeFirst();//revient avant la premiere ligne pour pouvoir iterer proprement ensuite
           
           
           //D'ici comment refactoring des datas:c'est l'extraction dynamique des donnes au lieu d'extraction manuelle des donnes ligne par ligne
	           ResultSetMetaData rsm= rs.getMetaData();//obtenir les informations sur les colonnes (noms,types...)
	           cols=rsm.getColumnCount();
	           
	           
	           String data[][]= new String[rows+1][cols];//initialisation du tableau 2D de retour /row+1 :car la premiere ligne (index 0) va contenir les noms de colonnes
	           
	           // Remplissage des noms de colonnes
	           for (int i = 0; i < cols; i++) {
				data[0][i]=rsm.getColumnName(i+1);
					//remplit la ligne 0 du tableau avec les noms des colonnes
					//getColumnName(i+1) car les colonnes dans JDBC sont indexxes a partir de 1(pas 0)
	           }
	           
	           // Remplissage des données ligne par ligne
	           int row=0;
	           while (rs.next()) {		//avance a la ligne suivante dans le resultat
	        	   row++;				//incremente pour ecrire a la ligne 1,2...
	        	   for(int col=0;col <cols;col++ ) {
	        		   data[row][col]=rs.getString(col +1);// cette ligne s'appele refactoring
	        		   			//getString(col + 1) récupère la valeur d'une colonne (toujours indexée à partir de 1).
	        		   			//data[row][col] contient la valeur convertie en String.
	        	   }
	           }
           //jusqu'a ici
           sql.close();
           return data;
       } catch (Exception e) {
           System.out.println("Erreur : " + e.getMessage());
       }
		return null;
    }
    
    //cherche  une table selon un mot-cle LIKE
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
			Statement sql=db.createStatement();//creer un statement a partir de la connexion db.
			int result = sql.executeUpdate(query);//executeUpdate() exécute la requête SQL construite.
			if(result == 1) return true;
			return false;
		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage());	
			return false;
		} 
	
		
	}
    //  Nouvelle méthode pour UPDATE
    public boolean update(String tableName,String updateKey, Object updateValue ,String conditionKey, Object conditionValue) {
    	String query = String.format(
    			"UPDATE %s SET %s = '%s' WHERE %s = '%s'",
    			tableName, updateKey, updateValue, conditionKey, conditionValue
    	);
    	
    	try {
    		Statement sql = db.createStatement();
    		int result = sql.executeUpdate(query);
    		sql.close();
    		if(result == 1) return true;
			return false;
    	} catch (Exception e) {
    		System.out.println("Erreur : " + e.getMessage());
    		return false;
    	}
    }
    
    //public <T> boolean delete(String tableName, String conditionKey,T conditionValue) : Si on veut travailler generiquement on utilise T:type generique ou Object(sauf object accept le tous)
    public  boolean delete(String tableName, String conditionKey,Object conditionValue) {
    	String query = String.format(
    			"DELETE FROM %s WHERE %s = '%s'",
    			tableName, conditionKey, conditionValue
    	);
    	
    	try {
    		Statement sql = db.createStatement();
    		int result = sql.executeUpdate(query);
    		sql.close();
    		return result > 0;
    	} catch (Exception e) {
    		System.out.println("Erreur : " + e.getMessage());
    		return false;
    	}
    }
    //===========================================Utilisant la reflexion du prof  =========================================//
    public boolean insert(String tableName,Object model) {
    	  try {
              Field[] fields = model.getClass().getDeclaredFields();
              Object row[]= new Object[fields.length];//creation d'une table generique
              
              for (int i = 0; i < row.length; i++) {
                  fields[i].setAccessible(true);
                  row[i]=fields[i].get(model);
                  fields[i].setAccessible(false);
              }
              return insert(tableName,row);
    	  } catch (Exception e) {
              System.out.println("Erreur d'insertion par réflexion : " + e.getMessage());
              return false;
          }
    }
    
    //=======================================================================================//
    
    // en utilisant les streams(flux) 
    public boolean insertStream(String tableName,Object... row) {
    	if(row ==null|| row.length==0) return false;
    	try {
    		
    		String values=Arrays.stream(row)
    							.map(value->"'" + value + "'") //chaque element est entoure de guillemets simples
    							.collect(Collectors.joining(","));//les valeurs sont separees par des virgules
    		 
    		
    		String query = String.format("INSERT INTO %s VALUES(%s)", tableName, values);
    	    System.out.println("Query générée : " + query); // Debug

    	     Statement sql = db.createStatement();
    	     int result = sql.executeUpdate(query);
    	     sql.close();

    	    if(result == 1) return true;
 			return false;
    		
    	}catch (Exception e) {
    		System.out.println("Erreur : "+ e.getMessage());	
			return false;
		}
    }
 

    //===========================================Utilisant la reflexion et Refactoring =========================================//

    //Refactoring String builder avec Reflexion
    public boolean insertRefactoring(String tableName, Object model) {
        try {
            Field[] fields = model.getClass().getDeclaredFields();
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                String fieldName = fields[i].getName();
                Object value = fields[i].get(model);

                // Mappage manuel
                String columnName = null;
                if (fieldName.equalsIgnoreCase("id")) columnName = "Au_ID";
                else if (fieldName.equalsIgnoreCase("name")) columnName = "Author";
                else if (fieldName.equalsIgnoreCase("yearBorn")) columnName = "Year_Born";

                if (columnName != null) {
                    columns.append(columnName);
                    values.append("'").append(value).append("'");

                    if (i < fields.length - 1) {
                        columns.append(", ");
                        values.append(", ");
                    }
                }
            }
            
            String query = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                tableName,
                columns.toString(),
                values.toString()
            );
            
            System.out.println("Query générée : " + query);
            
            Statement sql = db.createStatement();
            int result = sql.executeUpdate(query);
            sql.close();
            
            return result == 1;
        } catch (Exception e) {
            System.out.println("Erreur d'insertion par réflexion : " + e.getMessage());
            return false;
        }
    }
 
    public boolean updateRefacoring(String tableName, Object model) {
        try {
            Field[] fields = model.getClass().getDeclaredFields();//recupere tous les champs declares dans la classe de l'objet model
            StringBuilder setClause = new StringBuilder();//construire dynamiquement la clause set de la requete sql
            String primaryKeyField = "id";     // id du class Author
            String primaryKeyColumn = "Au_ID"; // id réel dans la base de données
            Object idValue = null;	//stockera la valeur de la cle primaire 
            boolean hasFieldsToUpdate = false; // Flag pour vérifier si des champs ont été ajoutés

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(model);

                if (fieldName.equalsIgnoreCase("id")) {
                    idValue = value;
                    continue;
                }

                if (fieldName.equalsIgnoreCase("name")) {
                    setClause.append("Author")  // Correspond à la colonne dans la DB
                             .append(" = '")
                             .append(value)
                             .append("', ");
                    hasFieldsToUpdate = true;
                } else if (fieldName.equalsIgnoreCase("yearBorn")) {
                    setClause.append("Year_Born") // Correspond à la colonne dans la DB
                             .append(" = '")
                             .append(value)
                             .append("', ");
                    hasFieldsToUpdate = true;
                }
            }

            if (idValue == null) {
                throw new RuntimeException("Clé primaire '" + primaryKeyField + "' non trouvée dans l'objet.");
            }

            // Si aucun champ n'a été ajouté à la clause SET, renvoyer une erreur
            if (!hasFieldsToUpdate) {
                throw new RuntimeException("Aucun champ à mettre à jour.");
            }

            // Retirer la dernière virgule de la clause SET
            if (setClause.length() > 0) {
                setClause.setLength(setClause.length() - 2);
            }

            String query = String.format(
                "UPDATE %s SET %s WHERE %s = '%s'",
                tableName,
                setClause.toString(),
                primaryKeyColumn, 
                idValue
            );//UPDATE Author SET Author = 'Nom', Year_Born = '1990' WHERE Au_ID = '5'

            System.out.println("Query générée : " + query);

            Statement sql = db.createStatement();
            int result = sql.executeUpdate(query);
            sql.close();

            return result == 1;
        } catch (Exception e) {
            System.out.println("Erreur de mise à jour par réflexion : " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteRefactoring(String tableName, Object model) {
        try {
            Field[] fields = model.getClass().getDeclaredFields();
            String primaryKeyField = "id";     // id du modèle
            String primaryKeyColumn = "Au_ID"; // Nom réel de la colonne de la clé primaire dans la base de données
            Object idValue = null;

            // Récupérer la valeur de la clé primaire
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(model);//recupere la valeur du champ dans l'objet (par ex 5 si id=5)

                if (fieldName.equalsIgnoreCase(primaryKeyField)) {
                    idValue = value;
                    break;  // On arrête dès qu'on trouve la clé primaire
                }
            }

            if (idValue == null) {
                throw new RuntimeException("Clé primaire '" + primaryKeyField + "' non trouvée dans l'objet.");
            }
            
            // Construire la requête DELETE :String.format("format", valeurs...)
            String query = String.format(
                "DELETE FROM %s WHERE %s = '%s'",
                tableName,
                primaryKeyColumn,
                idValue
            );

            System.out.println("Query générée : " + query);

            // Exécuter la requête
            Statement sql = db.createStatement();
            int result = sql.executeUpdate(query);
            sql.close();

            return result == 1;  // Retourner true si une ligne a été supprimée, sinon false
        } catch (Exception e) {
            System.out.println("Erreur de suppression par réflexion : " + e.getMessage());
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
  
    
}
