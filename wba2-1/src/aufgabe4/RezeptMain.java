package aufgabe4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class RezeptMain {

    public static void main(String[] args) throws JAXBException, IOException {
        //xml_File in Datei speichern
        File file = new File("/Users/Butterfly/git/wba2_phase1/wba2-1/src/aufgabe3/aufgabe3a.xml");
       

        //jaxb Objekt erstellen
        JAXBContext jc = JAXBContext.newInstance(Rezepte.class);
        //Unmarshaller, der XML-Dokumente in Java-Objekte konvertiert
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        //Marshaller, der Java-Objekte in XML-Dokumente konvertieren kann
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
            System.out.println("REZEPT");
            System.out.println("Titel: " + rezept.getRezept().get(i).getTitel());
            System.out.println("Bild_Rezept: " + rezept.getRezept().get(i).getBild() +"\n");

            //Zutaten
            System.out.println("\nZutaten: ");
            for (int j = 0; j < rezept.getRezept().get(i).getZutaten().getZutat().size(); j++) {
            	System.out.println(rezept.getRezept().get(i).getZutaten().getZutat().get(j).menge +" "+ rezept.getRezept().get(i).getZutaten().getZutat().get(j).einheit +" "+ rezept.getRezept().get(i).getZutaten().getZutat().get(j).name);
            }

            //Zubereitung
            System.out.println("\nZubereitung: ");
            System.out.println(rezept.getRezept().get(i).getZubereitung().arbeitszeit.getDauer() + " " + rezept.getRezept().get(i).getZubereitung().arbeitszeit.getEinheit() + " Arbeitszeit");
            if (rezept.getRezept().get(i).getZubereitung().ruhezeit != null) {
                System.out.println(""+ rezept.getRezept().get(i).getZubereitung().ruhezeit.getDauer() + " " + rezept.getRezept().get(i).getZubereitung().ruhezeit.getEinheit()+ " Ruhezeit");
            }
            if (rezept.getRezept().get(i).getZubereitung().brennwert != null) {
                System.out.println("Brennwert: "+ rezept.getRezept().get(i).getZubereitung().getBrennwert().wert + " kcal");
            }
            System.out.println("Schwierigkeitsgrad: " + rezept.getRezept().get(i).getZubereitung().getSchwierigkeitsgrad().grad);
            
            System.out.println("\nBeschreibung: ");
            
            for(int m = 0; m < rezept.getRezept().get(i).getZubereitung().getBeschreibung().schritt.size(); m++ ){ 
            	System.out.println(rezept.getRezept().get(i).getZubereitung().getBeschreibung().schritt.get(m) );
            }

            System.out.println();
            
            //Kommentare
            
            if (rezept.getRezept().get(i).getNutzerkommentare().kommentar != null) {
                System.out.println("Kommentare: ");
                for (int k = 0; k <= rezept.getRezept().get(i).getNutzerkommentare().getKommentar().size() - 1; k++) {
                    System.out.println("Autor: " + rezept.getRezept().get(i).getNutzerkommentare().getKommentar().get(k).verfasser);
                    System.out.println("Datum: " + rezept.getRezept().get(i).getNutzerkommentare().getKommentar().get(k).datum);
                    System.out.println("Kommentar: " + rezept.getRezept().get(i).getNutzerkommentare().getKommentar().get(k).text +"\n");
                }
            }
            else {
            	System.out.println("Keine Kommentare vorhanden!");
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


        Rezepte.Rezept.Nutzerkommentare.Kommentar newcomment = new Rezepte.Rezept.Nutzerkommentare.Kommentar();
        name = JOptionPane.showInputDialog("Name: ");
        newcomment.setVerfasser(name);
        
    	// Datum und Uhrzeit
        GregorianCalendar gCalendar = new GregorianCalendar();
        Date currentDate = new Date();
        gCalendar.setTime(currentDate);
        XMLGregorianCalendar xmlCalendar = null;
        try {
        xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
        }
        newcomment.setDatum(xmlCalendar);
        
        comment = JOptionPane.showInputDialog("Ihr Kommentar: ");
        newcomment.setText(comment);

        // Formatierung der XML-Datei
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // Kommentar hinzufuegen
        rezepte.getRezept().get(ein - 1).getNutzerkommentare().getKommentar().add(rezepte.getRezept().get(ein - 1).getNutzerkommentare().getKommentar().size(), newcomment);
        m.marshal(rezepte, w);
    }
}
