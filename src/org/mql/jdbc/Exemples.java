package org.mql.jdbc;

import org.mql.biblio.models.Author;

public class Exemples {
	private Database db;

	public Exemples() {

		initMySQL();
		
		
		exp01();

	}

	void initMySQL() {
		MySQLDataSource ds = new MySQLDataSource("biblio");
		db = new Database(ds);
	}

	void exp01() {
		String data[][] = db.select("Authors");
		//print(data, 1, 6249);
		print(data);
	}

	void exp02() {
		String data[][] = db.select("documents");
		print(data, 100, 110);
	}

	void exp03() {
		String data[][] = db.selectByKeyword("documents", "Title", "java");
		print(data);
	}

	void exp04() {
		String data[][] = db.selectByKeyword("documents", "Title", "C++");
		print(data);
	}

	// utilisant insert(String tableName,Object... row)
	void exp05() {
		Author a1 = new Author(16101, "Erich Gamma", 1961);
		boolean result = db.insert("Authors", a1.getId(), a1.getName(), a1.getYearBorn());
		if (result)
			System.out.println("Auteur bien enregistré");
		else
			System.out.println("Erreur d'insertion d'auteur");
	}

	// update(String tableName,String updateKey, Object updateValue ,String conditionKey, Object conditionValue)
	void exp05_1() {
		Author a1 = new Author(16103, "James Arthur", 1961);
		boolean result = db.update("Authors", "Author", a1.getName(), "Au_ID", a1.getId());
		if (result)
			System.out.println("Auteur bien modifier");
		else
			System.out.println("Erreur du modification d'auteur");
	}

	// delete(String tableName, String conditionKey, Object conditionValue)
	void exp05_2() {
		boolean result = db.delete("Authors", "Au_ID",16100);
		if (result)
			System.out.println("Auteur bien supprime");
		else
			System.out.println("Erreur du suppression d'auteur");
	
	}

	// insertStream(String tableName,Object... row)
	void exp06() {
		Author a1 = new Author(16100, "James Gosling", 1955);
		boolean result = db.insertStream("Authors", a1.getId(), a1.getName(), a1.getYearBorn());// db.insertStream(16102,"James
																								// Gosling",1955);
		if (result)
			System.out.println("Auteur bien enregistré");
		else
			System.out.println("Erreur d'insertion d'auteur");
	}

						// Reflexion
	//insert(String tableName,Object model
	void exp07() {
		Author a1 = new Author(16101, "Erich Gamma", 1961);
		boolean result = db.insertRefactoring("Authors", a1);
		if (result)
			System.out.println("Auteur bien enregistré");
		else
			System.out.println("Erreur d'insertion d'auteur");
	}
	
	
	// insertRefactoring(String tableName,Object model) en utilisant String Builder Avec Reflexion
	void exp08() {
		Author a1 = new Author(16102, "Erich Gamma", 1961);
		boolean result = db.insertRefactoring("Authors", a1);
		if (result)
			System.out.println("Auteur bien enregistré");
		else
			System.out.println("Erreur d'insertion d'auteur");
	}

	// updateRefactoring(String tableName, Object model)
	void exp07_2() {
		Author a = new Author(16100, "Erich Gamma gr", 1962); // valeurs modifiées
		boolean result = db.updateRefacoring("Authors", a);

		if (result) {
			System.out.println("Auteur mis à jour avec succès");
		} else {
			System.out.println("Erreur lors de la mise à jour de l'auteur");
		}
	}

	// deleteRefactoring(String tableName, Object model)
	void exp07_3() {
		Author author = new Author();
		author.setId(16101); // L'ID de l'auteur à supprimer

		boolean isDeleted = db.deleteRefactoring("Authors", author);
		if (isDeleted) {
			System.out.println("L'auteur a été supprimé avec succès.");
		} else {
			System.out.println("Erreur lors de la suppression de l'auteur.");
		}

	}

	void print(String data[][], int min, int max) {
		System.out.println("Nombre de lignes :" + (data.length - 1));
		// Affiche les titres de colonnes (lignes data[0]), séparés par des tabulations
		// \t.
		for (int i = 0; i < data[0].length; i++) {
			System.out.print(data[0][i] + "\t");
		}

		System.out.println();
		// Affiche les lignes de données, depuis l’indice min jusqu’à max inclus. Chaque
		// cellule est affichée avec \t entre les colonnes.
		for (int row = min; row <= max; row++) {
			for (int col = 0; col < data[row].length; col++) {

				System.out.print(data[row][col] + "\t");
			}
			System.out.println();
		}

	}

	// C’est une surcharge de la méthode précédente.
	// Elle appelle automatiquement la méthode principale en
	// affichant toutes les lignes sauf l’en-tête (de la 1ère ligne data[1] à
	// la dernière ligne data[data.length-1]).
	void print(String data[][]) {
		print(data, 1, data.length - 1);

	}

	public static void main(String[] args) {
		new Exemples();
	}

}
