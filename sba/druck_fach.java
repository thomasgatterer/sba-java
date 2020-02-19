package sba;

//import java.awt.*;
//import java.awt.event.*;
//import java.awt.print.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;
//import java.net.URL;
//import java.util.*;

/**
 * <p>
 * Üerschrift: Schulbuchaktion
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

public class druck_fach extends Frame {
	
	ConnectDB conobj = new ConnectDB();
	Connection con = conobj.getDBconnection();

	public druck_fach(String sjstr) {

		double wertges = 0;

		int counting = 0;
		// Datumseingabe
		String date = JOptionPane.showInputDialog(null, "Datumseingabe:",
				"Datum", JOptionPane.QUESTION_MESSAGE);
		try {
			int dateint = date.compareTo("");
			if (dateint < 0 || dateint > 0)
				date = "vom " + date;
		} catch (java.lang.Exception ex) {
			date = "";
		}
		// Infotexteingabe
		String text = JOptionPane.showInputDialog(null, "Texteingabe",
				"Info Text", JOptionPane.QUESTION_MESSAGE);
		try {
			int textint = text.compareTo("");
			if (textint < 0 || textint > 0)
				text = text;
		} catch (java.lang.Exception ex) {
			text = "";
		}

		try {// Anfang try 0
			FileWriter files = new FileWriter(
					"/home/andreas/db_sba/Fachlisten.odt");
			try { // try 1
				
				
				Statement stmt = con.createStatement();
				ResultSet fach = stmt
						.executeQuery("SELECT * FROM faecher ORDER BY Fach");

				while (fach.next()) {// Beginn while 0
					String fachtmp = fach.getString("Fach");

					int klcb_row = klcb.getItemCount();
					int j, a = 0, b = 0, c = 0, d = 0, l = 0, v = 0, x = 0, count_buch = 0;
					String klassecbtmp[] = new String[100];
					String nrtmp[] = new String[100];
					String titeltmp[] = new String[100];
					double preistmp[] = new double[100];
					String lehrer[] = new String[100];
					int wiedertmp[] = new int[100];
					double werttmp[] = new double[100];
					String Tab1 = "", TabNr = "", lz = " ";

					try { // Anfang try 2
						
						Statement stmts = con.createStatement();

						for (j = 0; j < klcb_row; j++) {
							klcb.setSelectedIndex(j);
							klassecb = (String) klcb.getSelectedItem();
							ResultSet rs = stmts.executeQuery("SELECT * FROM "
									+ klassecb + " WHERE Fach like '%"
									+ fachtmp + "' ORDER BY Fach");
							while (rs.next()) {
								klassecbtmp[x] = klassecb;
								nrtmp[a] = rs.getString(2);
								titeltmp[b] = rs.getString(3);
								preistmp[c] = rs.getDouble(4);
								lehrer[l] = rs.getString(7);
								werttmp[d] = rs.getDouble(8);

								double wert1 = (werttmp[d] * 10)
										- ((int) (werttmp[d] * 10));
								if (wert1 < 0.5)
									werttmp[d] = werttmp[d] * 10;
								else
									werttmp[d] = (werttmp[d] * 10) + 1;
								int werttmps = (int) werttmp[d];
								werttmp[d] = (double) werttmps / 10;

								wiedertmp[v] = rs.getInt(9);
								wertges = werttmp[d] + wertges;
								a++;
								b++;
								c++;
								d++;
								l++;
								v++;
								x++;
								count_buch++;
							}
						}// Ende for Schleife

						stmts.close();
						con.close();
					}// Ende von try 2
					catch (Exception exeption) {
						exeption.printStackTrace();
					}

					double wert1 = (wertges * 10) - ((int) (wertges * 10));
					if (wert1 < 0.5)
						wertges = wertges * 10;
					else
						wertges = (wertges * 10) + 1;
					int werttmps = (int) wertges;
					wertges = (double) werttmps / 10;

					a = 0;
					b = 0;
					c = 0;
					d = 0;
					l = 0;
					v = 0;
					x = 0;

					files.write("LISTE ZUR KONTROLLE der eingegebenen Bücher vom Fach "
							+ fachtmp
							+ " zur Schulbuchaktion "
							+ date
							+ " ("
							+ (char) 169 + " A.Knapp)\n");
					files.write(text + "\n");
					files.write("Nr.\t\tKurztitel d. Buches\t\t\t\tPreis\tWert\tW\tLeh.\tKlasse\n"
							+ "--------------------------------------------------------------------------------\n");
					for (a = 0; a < count_buch; a++) {
						int nrtmp_lang = nrtmp[a].length();
						if (nrtmp_lang < 6)
							TabNr = "\t\t";
						else
							TabNr = "\t";
						int titeltmp_lang = titeltmp[b].length();
						if (titeltmp_lang > 39)
							titeltmp[b].substring(0, 39);
						titeltmp_lang = titeltmp[b].length();
						titeltmp_lang = 39 - titeltmp_lang;
						if (titeltmp_lang > 33)
							Tab1 = "\t\t\t\t\t\t\t";
						else if (titeltmp_lang > 27 && titeltmp_lang <= 33)
							Tab1 = "\t\t\t\t\t\t";
						else if (titeltmp_lang > 21 && titeltmp_lang <= 27)
							Tab1 = "\t\t\t\t\t";
						else if (titeltmp_lang > 15 && titeltmp_lang <= 21)
							Tab1 = "\t\t\t\t";
						else if (titeltmp_lang > 9 && titeltmp_lang <= 15)
							Tab1 = "\t\t\t";
						else if (titeltmp_lang > 3 && titeltmp_lang <= 9)
							Tab1 = "\t\t";
						else if (titeltmp_lang <= 3)
							Tab1 = "\t";
						files.write(nrtmp[a] + TabNr + titeltmp[b] + Tab1
								+ preistmp[c] + "\t" + werttmp[d] + "\t"
								+ wiedertmp[v] + "\t" + lehrer[l] + "\t"
								+ klassecbtmp[x] + "\n");
						b++;
						c++;
						d++;
						l++;
						v++;
						x++;
					}
					files.write("\nAnzahl der eingegebenen Bücher: "
							+ count_buch + "\n");
					files.write("Gesamtwert der Bücher: " + wertges + "\f");
					wertges = 0;
					counting++;
				}// Ende while 0
				stmt.close();
				con.close();
			}// Ende von try 1
			catch (IOException exeption) {
				System.out.println("Error -- " + exeption.toString());
			}
			files.close();
		} // Ende while von try 0
		catch (Exception exeption) {
			exeption.printStackTrace();
		}
		JOptionPane
				.showMessageDialog(
						null,
						"Bücher für "
								+ counting
								+ " Fächer auf /home/andreas/db_sba/Fachlisten.odt geschrieben !");
	}
}
