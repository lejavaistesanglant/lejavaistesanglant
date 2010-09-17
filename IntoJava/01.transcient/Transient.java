/*
 * Transient.java
 *
 * lancer ce programme avec java -ea Transcient
 * Il retourne erreur d'assertion sur l'attribut vert
 *
 * A quoi sert Transient ?
 * Transient n'est pas sérializé !!
 * il est utilisé pour ne pas sérializer un attribut
 * 	- lorsque c'est un mot de passe par exemple
 * 	- lorsque que cela n'a pas d'intérêt
 * 	- lorsque l'on ne veut pas écrire sur le disque ou distribuer cet attribut
 */

import java.io.*;

public class Transient implements Serializable {

	private String rouge = "#FF0000";
	transient String vert = "#00FF00";
	public String bleu = "#0000FF";

	public  Transient(String r, String v, String b){
	rouge=r;
	vert=v;
	bleu=b;
	}

	public String toString(){
		return rouge+","+vert+","+bleu;
	}	
	
	public static void main(String args[]) throws java.io.IOException, java.lang.ClassNotFoundException {

		System.out.println("Début du programme");

		Transient t1=new Transient("#FF0000","#00FF00","#0000FF");
		File f=new File("objet.txt");
		FileOutputStream fout=new FileOutputStream(f);
		ObjectOutputStream oos=new ObjectOutputStream(fout);
		FileInputStream fin=new FileInputStream(f);
		ObjectInputStream ois=new ObjectInputStream(fin);

		System.out.println("Ecriture d'un object Transient dans objet.txt");
		oos.writeObject(t1);

		System.out.println("Lecture de objet.txt");
		Transient t2 = new Transient("#F00","#0F0","#00F");
		t2=(Transient) ois.readObject();

		System.out.println("Objet t1");
		System.out.println(t1.toString());
		System.out.println("Objet t2");
		System.out.println(t2.toString());

		assert t1.rouge.equals(t2.rouge);
		assert t1.bleu.equals(t2.bleu);
		//transient n'est pas sérializé, donc différent
		assert t1.vert.equals(t2.vert);
		System.out.println("Fin du programme");

	}



}
