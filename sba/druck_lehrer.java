package sba;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;

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

public class druck_lehrer extends Frame {

	ConnectDB conobj = new ConnectDB();
	Connection con = conobj.getDBconnection();

	public druck_lehrer(String sjstr) {

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
			if (textint == 0)
				text = "";
			else
				text = text + "\f";
		} catch (java.lang.Exception ex) {
			text = "\f";
		}

		try {// Anfang try 0
			FileWriter files = new FileWriter(
					"/home/andreas/db_sba/Lehrer_LH.odt");
			try { // try 1

				Statement stmt = con.createStatement();
				ResultSet kol = stmt
						.executeQuery("SELECT * FROM lehrer ORDER BY ID");

				while (kol.next()) {// Beginn while 0
					String lehrer = kol.getString(2);

					int klcb_row = klcb.getItemCount();
					int j, a = 0, b = 0, c = 0, d = 0, v = 0, x = 0, count_buch = 0;
					String klassecbtmp[] = new String[100];
					String nrtmp[] = new String[5000];
					String titeltmp[] = new String[5000];
					String fachtmp[] = new String[5000];
					double preistmp[] = new double[5000];
					int wiedertmp[] = new int[5000];
					int anz_LH = 0, aa = 0, ab = 0, ac = 0, ad = 0;// Variablen
																	// für
																	// Lehrer_Hand
					String nrLHtmp[] = new String[5000];
					String titelLHtmp[] = new String[5000];
					String fachLHtmp[] = new String[5000];
					String Tab1 = "", TabNr = "", lz = " ";

					try { // Anfang try 2
						
						
						Statement stmts = con.createStatement();

						for (j = 0; j < klcb_row; j++) {
							klcb.setSelectedIndex(j);
							klassecb = (String) klcb.getSelectedItem();
							ResultSet rs = stmts.executeQuery("SELECT * FROM "
									+ klassecb + " WHERE Lehrer = '" + lehrer
									+ "' ORDER BY Fach");
							while (rs.next()) {
								klassecbtmp[x] = klassecb;
								nrtmp[a] = rs.getString(2);
								titeltmp[b] = rs.getString(3);
								preistmp[c] = rs.getDouble(4);
								fachtmp[d] = rs.getString(5);
								wiedertmp[v] = rs.getInt(9);
								a++;
								b++;
								c++;
								d++;
								v++;
								x++;
								// System.out.println("Lehrer="+lehrer+"; klasse="+klassecb);
								// System.out.println("a="+a+ "; "+"b="+b+
								// "; "+"c="+c+ "; "+"d="+d+ "; "+"v="+v+
								// "; "+"x="+x);
								count_buch++;
							}
						}// Ende for Schleife

						// Anzahl der gesamten LehrerHand-Exemplare bestimmen
						// und speichern
						ResultSet k = stmts
								.executeQuery("SELECT * FROM lehrerhand WHERE Lehrer = '"
										+ lehrer
										+ "' AND Jahr = "
										+ (Integer.parseInt(sjstr) - 2000)
										+ " ORDER BY Fach");
						while (k.next()) {
							nrLHtmp[aa] = k.getString(2);
							titelLHtmp[ab] = k.getString(3);
							fachLHtmp[ac] = k.getString(4);
							aa++;
							ab++;
							ac++;
							anz_LH++;
						}
						stmts.close();
						con.close();
					}// Ende von try 2
					catch (Exception exeption) {
						exeption.printStackTrace();
					}

					a = 0;
					b = 0;
					c = 0;
					d = 0;
					v = 0;
					x = 0;

					files.write("LISTE ZUR KONTROLLE DER EINGEGEBENEN BÜCHER "
							+ date + " !\n\n");
					files.write("Eingegebene Bücher von " + lehrer
							+ " zur Schulbuchaktion (" + (char) 169
							+ " A.Knapp)\n\n");
					files.write("Nr.\t\tKurztitel d. Buches\t\t\t\tPreis\tFach\tW\tKlasse\n"
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
								+ preistmp[c] + "\t" + fachtmp[d] + "\t"
								+ wiedertmp[v] + "\t" + klassecbtmp[x] + "\n");
						b++;
						c++;
						d++;
						v++;
						x++;
					}
					files.write("\n \nAnzahl der eingegebenen Bücher: "
							+ count_buch + "\n \n");

					files.write("Eingegebene Lehrer-Handexemplare von "
							+ lehrer + "\n \n");
					files.write("Nr.\t\tKurztitel d. Buches\t\t\t\tFach\n"
							+ "--------------------------------------------------------------------------------\n");
					aa = 0;
					ab = 0;
					ac = 0;
					for (ad = 0; ad < anz_LH; ad++) {
						int nrtmpLH_lang = nrLHtmp[aa].length();
						if (nrtmpLH_lang < 6)
							TabNr = "\t\t";
						else
							TabNr = "\t";
						int titelLH_lang = titelLHtmp[ac].length();
						if (titelLH_lang > 39)
							titelLHtmp[ac].substring(0, 39);
						titelLH_lang = titelLHtmp[ac].length();
						titelLH_lang = 39 - titelLH_lang;
						if (titelLH_lang > 33)
							Tab1 = "\t\t\t\t\t\t\t";
						else if (titelLH_lang > 27 && titelLH_lang <= 33)
							Tab1 = "\t\t\t\t\t\t";
						else if (titelLH_lang > 21 && titelLH_lang <= 27)
							Tab1 = "\t\t\t\t\t";
						else if (titelLH_lang > 15 && titelLH_lang <= 21)
							Tab1 = "\t\t\t\t";
						else if (titelLH_lang > 9 && titelLH_lang <= 15)
							Tab1 = "\t\t\t";
						else if (titelLH_lang > 3 && titelLH_lang <= 9)
							Tab1 = "\t\t";
						else if (titelLH_lang <= 3)
							Tab1 = "\t";
						files.write(nrLHtmp[aa] + TabNr + titelLHtmp[ab] + Tab1
								+ fachLHtmp[ac] + "\t" + "\n");
						aa++;
						ab++;
						ac++;
					}
					files.write("\n \nAnzahl der Lehrer-Hand-Exemplare: "
							+ anz_LH + "\n\n\f");
					files.write(text);
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
								+ " Lehrer und Handexemplare auf /home/andreas/db_sba/Lehrer_LH.odt geschrieben !");
	}// Ende public
}// Ende class
