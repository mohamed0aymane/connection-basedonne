package org.mql.jdbc;

import org.mql.biblio.models.Author;

public class Exemples {
	private Database db;
	public Exemples() {
	
		initMySQL();
		exp01();
		
	}
	
	void initMySQL() {
		 MySQLDataSource ds=new MySQLDataSource("biblio");
		 db=new Database(ds); 
	}

	 void exp01() {
		 String data[][]=db.select("Authors");
		 print(data,1,3);
	 }
	 
	  void exp02() {
		  String data[][]=db.select("documents");
			 print(data,100,110);			
		}
	  void exp03() {
		  String data[][]=db.selectByKeyword("documents","Title" , "java");
			 print(data);			
		}
	  void exp04() {
		  String data[][]=db.selectByKeyword("documents","Title" , "C++");
			 print(data);			
		}
	  
	  void exp05() {
		  Author a1 = new Author(16100,"James Gosling",1955);
		boolean result= db.insert("Authors", a1.getId() ,a1.getName() ,a1.getYearBorn());
		if(result) System.out.println("Auteur bien enregistré");
		else System.out.println("Erreur d'insertion d'auteur");
		}
	  
	  void exp06() {
		  Author a1 = new Author(16101,"Erich Gamma",1961);
		boolean result= db.insert("Authors",a1);
		if(result) System.out.println("Auteur bien enregistré");
		else System.out.println("Erreur d'insertion d'auteur");
		}
	  
	  void print(String data[][] ,int min,int max) {
			 System.out.println("Nombre de lignes :" + (data.length-1));
			 
			 for (int i = 0; i < data[0].length; i++) {
				System.out.print(data[0][i]+ "\t");
			}
			 
			 System.out.println();
			 
			 for (int row = min; row <= max; row++) {
				 for (int col=0; col < data[row].length; col++) {

						System.out.print(data[row][col]+"\t");
				 }
				System.out.println(); 
			}
			 
		 }
		 
		 void print(String data[][]) {
			 print(data,1,data.length-1);
			 
		 }
	public static void main(String[] args) {
		new Exemples();
	}
	
}
