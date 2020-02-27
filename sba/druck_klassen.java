package sba;

//import java.awt.*;
//import java.awt.event.*;
//import java.awt.print.*;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFileChooser;

import javax.swing.JOptionPane;
//import java.net.URL;

//import java.util.*;

/**
 * <p>
 * Überschrift: Schulbuchaktion
 * </p>
 * <p>
 * Beschreibung: Programm für die Eingabe und Bearbeitung für
 * Schulbuchreferenten
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003/04 A.Knapp
 * </p>
 * <p>
 * Organisation: AGI
 * </p>
 * 
 * @author Andreas Knapp
 * @version 1.2
 */

public class druck_klassen extends Frame {

	public druck_klassen(String sjstr) {
		int lang0 = 0;
		int buch_count = 0, buch_ges = 0;// Anzahl der bestellen Bücher bzgl.
											// Klasse
		double htmp = 0;// Variable um die Klassen-Bestellsumme zu berechnen u.
						// ausgeben
		double rtmp = 0;// Variable um die R-Bestellsumme zu berechnen u.
						// ausgegeben
		double xtmp = 0;// Variable um den SBX-Bestellwert zu berechnen
		double uewtmp = 0;// Variable um den UEW-Bestellwert zu berechenen u.
							// ausgeben
		String Tab = "\t", Tab1 = "\t", Tab2 = "\t", Tab3 = "\t", TabNr = "", Tabh = "";

		// Datumseingabe
		String date = JOptionPane.showInputDialog(null, "Datum:",
				"Datumseingabe", JOptionPane.QUESTION_MESSAGE);
		try {
			int dateint = date.compareTo("");
			if (dateint < 0 || dateint > 0)
				date = "vom " + date;
		} catch (java.lang.Exception ex) {
			date = "";
		}
		// Infotexteingabe
		String text = JOptionPane.showInputDialog(null, "Text:", "Info Text",
				JOptionPane.QUESTION_MESSAGE);
		try {
			int textint = text.compareTo("");
			if (textint < 0 || textint > 0)
				text = text + "\n \n";
		} catch (java.lang.Exception ex) {
			text = "";
		}
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		String pfadundNameohneEndung = null;
	        // Dialog zum Oeffnen von Dateien anzeigen	
	        int rueckgabeWert = chooser.showSaveDialog(null);
	        /* Abfrage, ob auf "Öffnen" geklickt wurde */
	        if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
	        {
			pfadundNameohneEndung = chooser.getSelectedFile().getAbsolutePath();
			// Ausgabe der ausgewaehlten Datei
	            	//tg System.out.println("Die zu öffnende Datei ist: " + pfadundNameohneEndung + ".doc");
		    
	        }

		int klcb_row = klcb.getItemCount();
		int j = 0;

		try {// try 0
			FileWriter klassenfile = new FileWriter(pfadundNameohneEndung+ ".doc");

			for (j = 0; j < klcb_row; j++) {
				klcb.setSelectedIndex(j);
				klassecb = (String) klcb.getSelectedItem();

				lang0 = klassecb.length();
				if (lang0 < 1) { // Anfang If-Abfrage 0
					output.setText("Bitte eine Klasse eingeben");
				} else {
					// lehrer_a();
					try { // try 1
						ConnectDB conobj = new ConnectDB();
						Connection con = conobj.getDBconnection();
						Statement stmt = con.createStatement();
						klassestr = klassecb;
						ResultSet rs = stmt.executeQuery("SELECT * FROM "
								+ klassestr + " ORDER BY Fach");

						klassenfile
								.write("LISTE FÜR DEN KV ZUR AUSGABE DER BESTELLTEN BÜCHER "
										+ date + "\n\n");
						klassenfile.write(text);
						klassenfile.write("KLASSENLISTE FÜR " + klassestr
								+ " zur Schulbuchaktion (" + (char) 64
								+ " A.Knapp)\n\n");
						klassenfile
								.write("Das Ergebnis der Abfrage für die "
										+ klassestr
										+ " Klasse:\nNr.\t\tKurztitel d. Buches\t\t\tPreis\tFach\tAnz.\tLeh.\tWert\t\tW\n\n"
										+

										"-------------------------------------------------------------------------------------\n\n");
						while (rs.next()) {
							String b = rs.getString(2);
							int Nr_laenge = b.length();
							if (Nr_laenge < 6)
								TabNr = "\t\t";
							else
								TabNr = "\t";
							int bl = b.length();
							bl = 7 - bl;
							if (bl > 0)
								Tab = "\t";
							String c = rs.getString(3);
							int cl = c.length();
							if (cl > 35)
								c = c.substring(0, 35);
							cl = c.length();
							cl = 35 - cl;
							if (cl >= 30)
								Tab1 = "\t\t\t\t\t\t";
							else if (cl >= 24 && cl < 30)
								Tab1 = "\t\t\t\t\t";
							else if (cl >= 18 && cl < 24)
								Tab1 = "\t\t\t\t";
							else if (cl >= 12 && cl < 18)
								Tab1 = "\t\t\t";
							else if (cl >= 6 && cl < 12)
								Tab1 = "\t\t";
							else if (cl < 6)
								Tab1 = "\t";
							String d = rs.getString(4);
							String e = rs.getString(5);
							String f = rs.getString(6);
							String g = rs.getString(7);
							double h = rs.getDouble(8);
							String hstr = "" + h;
							int hint = hstr.length();
							if (hint < 6)
								Tabh = "\t\t";
							else
								Tabh = "\t";
							String i = rs.getString(9);

							klassenfile.write(b + TabNr + c + Tab1 + d + Tab2
									+ e + Tab3 + f + "\t" + g + "\t" + h + Tabh
									+ i + "\n");

							int fach_rk = e.compareTo("RK");
							int fach_re = e.compareTo("RE");
							boolean fach_x = e.startsWith("X");
							boolean titel_uew = c.startsWith("UeW");
							if (fach_rk == 0 || fach_re == 0)
								rtmp = rtmp + h;
							else if (fach_x == true)
								xtmp = xtmp + h;
							else if (titel_uew == true)
								uewtmp = uewtmp + h;
							else
								htmp = htmp + h;
							buch_count++;
							buch_ges++;
						}

						double wert1 = (rtmp * 1000) - ((int) (rtmp * 1000));
						if (wert1 < 0.5)
							rtmp = rtmp * 1000;
						else
							rtmp = (rtmp * 1000) + 1;
						int werttmp = (int) rtmp;
						rtmp = (double) werttmp / 1000;

						wert1 = (xtmp * 1000) - ((int) (xtmp * 1000));
						if (wert1 < 0.5)
							xtmp = xtmp * 1000;
						else
							xtmp = (xtmp * 1000) + 1;
						werttmp = (int) xtmp;
						xtmp = (double) werttmp / 1000;

						wert1 = (uewtmp * 1000) - ((int) (uewtmp * 1000));
						if (wert1 < 0.5)
							uewtmp = uewtmp * 1000;
						else
							uewtmp = (uewtmp * 1000) + 1;
						werttmp = (int) uewtmp;
						uewtmp = (double) werttmp / 1000;

						wert1 = (htmp * 1000) - ((int) (htmp * 1000));
						if (wert1 < 0.5)
							htmp = htmp * 1000;
						else
							htmp = (htmp * 1000) + 1;
						werttmp = (int) htmp;
						htmp = (double) werttmp / 1000;

						klassenfile.write("\nAnzahl der bestellten Bücher: "
								+ buch_count + "\n \n");
						double gtmp = htmp + xtmp;
						klassenfile.write("Bestellwert der Klasse (mit SBX): "
								+ gtmp
								+ " €\nBestellwert der Klasse (ohne SBX): "
								+ htmp + " €\nBestellwert SBX: " + xtmp
								+ " €\nBestellwert für Religion: " + rtmp
								+ "  €\nBestellwert für UeW: " + uewtmp
								+ " €\f");
						rs.close();
						stmt.close();
						con.close();
						gtmp = 0;
						htmp = 0;
						xtmp = 0;
						rtmp = 0;
						uewtmp = 0;
						buch_count = 0;
						klcb.getItemAt(-1);
					}// Ende try 1
					catch (java.lang.Exception ex) {
						ex.printStackTrace();
					}
				}// Ende else-Zweig
			}// Ende if-Abfrage 0
			klassenfile.close();
		} // Ende try 0
		catch (Exception exception) {
			exception.printStackTrace();
		}
		JOptionPane
				.showMessageDialog(
						null,
						"Es wurden "
								+ buch_ges
								+ " Bücher eingegeben und "
								+ klcb_row
								+ " Klassenlisten auf "+ pfadundNameohneEndung+".doc erstellt");
	}
}
