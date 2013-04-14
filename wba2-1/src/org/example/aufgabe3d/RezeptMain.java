package org.example.aufgabe3d;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;

public class RezeptMain {

	    public static void main(String[] args) throws JAXBException, IOException {
	        //xml_File in Datei speichern
	        File file = new File("/Users/Butterfly/git/wba2_phase1/wba2-1/src/aufgabe3/aufgabe3a.xml");

	        //jaxb Objekt erstellen
	        JAXBContext jc = JAXBContext.newInstance(Rezepte.class);
	        //Unmarshaller
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        //Marshaller
	        Marshaller marshaller = jc.createMarshaller();
	        //Rezepte unmarshallen
	        Rezepte rezept = (Rezepte) unmarshaller.unmarshal(file);

	        //Menue
	        String eingabe;
	        int auswahl;
	        eingabe = JOptionPane.showInputDialog("Was moechten Sie tun: \n1. XML ausgeben\n2.Kommentar hinzufuegen \n3.beenden");
	        auswahl = Integer.parseInt(eingabe);
	        if (auswahl == 1) {
	            ausgabeRezept(rezept);
	        } else if (auswahl == 2) {
	            insertKommentar(rezept, file, marshaller);
	        } else if (auswahl == 3) {
	            System.exit(0);
	        } else
	            System.out.println("Eingabe falsch!");

	    }
	    
	    private static void ausgabeRezept(Rezepte rezept) {
	        for (int i = 0; i < rezept.getRezept().size(); i++) {
	            //Verfasser
	            System.out.println("\n \n REZEPT");
	            System.out.println("Titel: " + rezept.getRezept().get(i).getTitel());
	            System.out.println("Bild_Rezept: " + rezept.getRezept().get(i).getBild());

	            //Zutaten
	            System.out.println("Zutaten:");
	            for (int j = 0; j < rezept.getRezept().get(i).getZutaten().getZutat().size(); j++) {
	                System.out.println(rezept.getRezept().get(i).getZutaten().getZutat().get(j).getName() + " " + rezept.getRezept().get(i).getZutaten().getZutat().get(j).getMenge() + " " + rezept.getRezept().get(i).getZutaten().getZutat().get(j).getEinheit());

	            }

	            //Zubereitung
	            System.out.println("\n \n"+ rezept.getRezept().get(i).getZubereitung().arbeitszeit.getDauer() + " " + rezept.getRezept().get(i).getZubereitung().arbeitszeit.getEinheit() + " Arbeitszeit");
	            if (rezept.getRezept().get(i).getZubereitung().ruhezeit != null) {
	                System.out.println(rezept.getRezept().get(i).getZubereitung().ruhezeit.getDauer() + " " + rezept.getRezept().get(i).getZubereitung().ruhezeit.getEinheit()+ " Ruhezeit");
	            }
	            if (rezept.getRezept().get(i).getZubereitung().brennwert != null) {
	                System.out.println("Brennwert: "+ rezept.getRezept().get(i).getZubereitung().getBrennwert() + " kcal");
	            }
	            System.out.println("Schwierigkeitsgrad: " + rezept.getRezept().get(i).getZubereitung().getSchwierigkeitsgrad());

	            //Kommentare
	            
	            if (rezept.getRezept().get(i).getBenutzerkommentare().kommentar != null) {
	                System.out.println();
	                System.out.println("Kommentare: ");
	                for (int k = 0; k <= rezept.getRezept().get(i).getBenutzerkommentare().getKommentar().size() - 1; k++) {
	                    System.out.println("Autor: " + rezept.getRezept().get(i).getBenutzerkommentare().getKommentar().get(k).getVerfasser());
	                    System.out.println("Datum: " + rezept.getRezept().get(i).getBenutzerkommentare().getKommentar().get(k).getDatum());
	                    System.out.println("Uhezeit: " + rezept.getRezept().get(i).getBenutzerkommentare().getKommentar().get(k).getUhrzeit());
	                    System.out.println("Kommentar: " + rezept.getRezept().get(i).getBenutzerkommentare().getKommentar().get(k).getText());
	                    System.out.println();
	                }
	                
	            }
	        }
	    }
	    
	    private static void insertKommentar(Rezepte rezepte, File datei, Marshaller m) throws IOException, JAXBException {

	        // FileWriter initialisieren
	        Writer w = new FileWriter(datei);
	        String eingabe;
	        String name;
	        String comment;
	        int ein;
	        eingabe = JOptionPane.showInputDialog("Welche Datei moechten Sie erweitern: ");
	        ein = Integer.parseInt(eingabe);
	        if (ein > rezepte.getRezept().size()) {
	            System.out.println("Rezept nicht vorhanden!");
	            System.exit(1);
	        }


	        Rezepte.Rezept.Benutzerkommentare.Kommentar newcomment = new Rezepte.Rezept.Benutzerkommentare.Kommentar();
	        name = JOptionPane.showInputDialog("Name: ");
	        newcomment.setVerfasser(name);
	        // Automatische generierung der Systemzeit
	        Timestamp aktTime = new Timestamp(System.currentTimeMillis());
	        System.out.println("Datum: " + aktTime);
	        comment = JOptionPane.showInputDialog("Ihr Kommentar: ");
	        newcomment.setText(comment);

	        // Formatierung der XML-Datei
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        // Kommentar hinzufuegen
	        rezepte.getRezept().get(ein - 1).getBenutzerkommentare().getKommentar().add(rezepte.getRezept().get(ein - 1).getBenutzerkommentare().getKommentar().size(), newcomment);
	        m.marshal(rezepte, w);
	    }
}
