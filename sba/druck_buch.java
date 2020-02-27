package sba;

import java.io.FileWriter;
import javax.swing.JFileChooser;
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

public class druck_buch extends Frame {

	ConnectDB conobj = new ConnectDB();
	Connection con = conobj.getDBconnection();

	public druck_buch(String sjstr) {

		int klcb_row = klcb.getItemCount();
		int j, i, anzahl = 0, buch_ges = 0;
		int[] anzahlin = new int[100];
		int buch_anzahl = 0;
		String[] titelin = new String[500];
		String[] fachin = new String[500];
		String[] nrin = new String[500];
		double[] preisin = new double[500];
		double[] wertin = new double[500];
		String sql = "";
		boolean letztes_buch = true;

		try {
			
			
			Statement stmt = con.createStatement();

			String LHloeschen = "";
			LHloeschen = "DELETE FROM buecher_gesamt";
			stmt.executeUpdate(LHloeschen);
			LHloeschen = "DELETE FROM schulstufe";
			stmt.executeUpdate(LHloeschen);

			for (j = 0; j < klcb_row; j++) {
				i = 0;
				klcb.setSelectedIndex(j);
				klassecb = (String) klcb.getSelectedItem();

				klassestr = klassecb;
				ResultSet rs = stmt.executeQuery("SELECT * FROM " + klassestr);

				while (rs.next()) {
					nrin[i] = rs.getString("Nr");
					titelin[i] = rs.getString("Titel");
					preisin[i] = rs.getDouble("Preis");
					fachin[i] = rs.getString("Fach");
					anzahlin[i] = rs.getInt("Anzahl");
					wertin[i] = rs.getDouble("Wert");
					i++;
					buch_ges++;
				}
				anzahl = i;
				for (i = 0; i < anzahl; i++) {
					sql = "INSERT INTO buecher_gesamt (Nr,Titel,Preis,Fach,Anzahl,Wert,Klasse,Stufe)"
							+ " values('"
							+ nrin[i]
							+ "','"
							+ titelin[i]
							+ "',"
							+ preisin[i]
							+ ",'"
							+ fachin[i]
							+ "','"
							+ anzahlin[i]
							+ "',"
							+ wertin[i]
							+ ",'"
							+ klassestr
							+ "','" + klassestr.charAt(0) + "')";
					stmt.executeUpdate(sql);
				}
			}
		} catch (Exception trystart) {
			trystart.printStackTrace();
		}

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
		try {// try 0
			FileWriter klassenfile = new FileWriter(pfadundNameohneEndung+ ".doc");

			try { // try 1
				
				
				Statement stmt = con.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT * FROM buecher_gesamt ORDER BY Nr");

				klassenfile
						.write("LISTE FÜR DEN Buchhändler ZUR AUSGABE DER BESTELLTEN BÜCHER "
								+ date + "\n\n");
				klassenfile.write(text);
				klassenfile.write("Bücherliste zur Schulbuchaktion ("
						+ (char) 169 + " A.Knapp)\n\n");
				klassenfile
						.write("Das Ergebnis der Abfrage:\nNr.\t\tKurztitel d. Buches\t\t\tPreis\tFach\tAnz.\tWert\n\n"
								+

								"--------------------------------------------------------------------------------\n\n");
				double[] b = new double[1000];
				int z = 0; // Zwischenwert zur Nr. Abfrage
				String[] b_tmp = new String[1000];
				String[] c = new String[1000];
				double[] d = new double[1000];
				String[] e = new String[1000];
				int[] f = new int[1000];
				double[] g = new double[1000];
				String[] h = new String[1000];
				int tmp_anz = 0;
				double tmp_wert = 0;

				while (rs.next()) {
					b[z] = rs.getDouble("Nr");
					b_tmp[z] = "" + b[z];
					c[z] = rs.getString("Titel");
					d[z] = rs.getDouble("Preis");
					e[z] = rs.getString("Fach");
					f[z] = rs.getInt("Anzahl");
					g[z] = rs.getDouble("Wert");
					h[z] = rs.getString("Klasse");

					boolean schreibe = true;

					if (z == 0) {
						tmp_anz = f[0];
						tmp_wert = g[0];
					}
					if (z > 0) {
						int nr_pruef = b_tmp[z].compareTo("" + b_tmp[z - 1]);
						if (nr_pruef == 0) {
							tmp_anz = tmp_anz + f[z];
							tmp_wert = tmp_wert + g[z];
							schreibe = false;
							if (z == buch_ges - 1) {
								schreibe = true;
								letztes_buch = false;
							}
						}

						if (schreibe == true) {
							double wert1 = (tmp_wert * 1000)
									- ((int) (tmp_wert * 1000));
							if (wert1 < 0.5)
								tmp_wert = tmp_wert * 1000;
							else
								tmp_wert = (tmp_wert * 1000) + 1;
							int werttmp = (int) tmp_wert;
							tmp_wert = (double) werttmp / 1000;

							int Nr_laenge = b_tmp[z - 1].length();
							if (Nr_laenge < 8)
								TabNr = "\t\t";
							else
								TabNr = "\t";
							int cl = c[z - 1].length();
							if (cl > 35)
								c[z - 1] = c[z - 1].substring(0, 35);
							cl = c[z - 1].length();
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
							int index = 0;
							if (b_tmp[z - 1].length() < 10) {
								index = b_tmp[z - 1].indexOf(".0");
								b_tmp[z - 1] = b_tmp[z - 1].substring(0, index);
							}
							if (b_tmp[z - 1].length() >= 10) {
								index = b_tmp[z - 1].indexOf("E");
								b_tmp[z - 1] = b_tmp[z - 1].substring(0, index);
								b_tmp[z - 1] = b_tmp[z - 1].substring(0, 1)
										+ b_tmp[z - 1].substring(2, index);
							}
							klassenfile.write(b_tmp[z - 1] + TabNr + c[z - 1]
									+ Tab1 + d[z - 1] + Tab2 + e[z - 1] + Tab3
									+ tmp_anz + "\t" + tmp_wert + "\n");

							// letztes Buch schreiben?
							if (z == buch_ges - 1 && letztes_buch == true) {
								wert1 = (g[z] * 1000) - ((int) (g[z] * 1000));
								if (wert1 < 0.5)
									g[z] = g[z] * 1000;
								else
									g[z] = (g[z] * 1000) + 1;
								werttmp = (int) g[z];
								g[z] = (double) werttmp / 1000;

								Nr_laenge = b_tmp[z].length();
								if (Nr_laenge < 8)
									TabNr = "\t\t";
								else
									TabNr = "\t";
								cl = c[z].length();
								if (cl > 35)
									c[z] = c[z].substring(0, 35);
								cl = c[z].length();
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
								index = 0;
								if (b_tmp[z].length() < 10) {
									index = b_tmp[z].indexOf(".0");
									b_tmp[z] = b_tmp[z].substring(0, index);
								}
								if (b_tmp[z].length() >= 10) {
									index = b_tmp[z].indexOf("E");
									b_tmp[z] = b_tmp[z].substring(0, index);
									b_tmp[z] = b_tmp[z].substring(0, 1)
											+ b_tmp[z].substring(2, index);
								}
								if (letztes_buch == true)
									klassenfile.write(b_tmp[z] + TabNr + c[z]
											+ Tab1 + d[z] + Tab2 + e[z] + Tab3
											+ f[z] + "\t" + g[z] + "\n");
								buch_anzahl++;
							}// Ende letztes Buch schreiben

							try {
								
								stmt = con.createStatement();
								sql = "INSERT INTO schulstufe (Nr,Titel,Preis,Fach,Anzahl,Wert,Klasse) values('"
										+ b_tmp[z - 1]
										+ "','"
										+ c[z - 1]
										+ "',"
										+ d[z - 1]
										+ ",'"
										+ e[z - 1]
										+ "',"
										+ tmp_anz
										+ ","
										+ tmp_wert
										+ ",'"
										+ (char) h[z - 1].charAt(0)
										+ "')";
								stmt.executeUpdate(sql);
								if (z == buch_ges - 1 && letztes_buch == true) {
									sql = "INSERT INTO schulstufe (Nr,Titel,Preis,Fach,Anzahl,Wert,Klasse) values('"
											+ b_tmp[z]
											+ "','"
											+ c[z]
											+ "',"
											+ d[z]
											+ ",'"
											+ e[z]
											+ "',"
											+ f[z]
											+ ","
											+ g[z]
											+ ",'"
											+ (char) h[z].charAt(0) + "')";
									stmt.executeUpdate(sql);
								}
							} catch (java.lang.Exception exept) {
								exept.printStackTrace();
							}
							tmp_anz = f[z];
							tmp_wert = g[z];
							buch_anzahl++;
						}
					}
					z++;
				}

				klassenfile.write("\nAnzahl der bestellten Bücher: "
						+ (buch_anzahl) + "\n \n");

				rs.close();
				stmt.close();
				con.close();
			}// Ende try 1
			catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
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
								+ " Bücher eingegeben auf "+ pfadundNameohneEndung+".doc geschrieben");

		new druck_schulstufe(sjstr);
	}
}
