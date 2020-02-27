package sba;

//import java.awt.*;
//import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;
//import java.net.URL;
//import java.io.*;
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

public class berechnen extends Frame {
	String sjstr = "";
	String sql = "";
	String fach_re = "";
	String titel_uew = "";
	double[] tmp2 = new double[1000];
	double tmp_summe = 0, tmp_gesamt = 0, sbx_gesamt = 0, rel_gesamt = 0,
			uew_gesamt = 0, gesamt = 0, gesamt_ohne = 0;
	double sbx, rel, uew;
	int i = 0, count = 0;
	String[] nrre = new String[1000];
	String lehrer;
	String[] lehrer_tmp = new String[1000];

	public berechnen(String klassecb, String sjstr, String lehrer) {
		// lehrer=lehrer;

		int allow = lehrer.compareTo("ADM");

		if (allow == 0) {

			int klcb_row = 0/* = klcb.getItemCount() */;
			int j = 0; // k
			String[] klassenanzahl = new String[40];

			// Einschub-NEU (29.4.05) für das korrekte Einlesen der Klassen
			// Beachte: klassecb wurde in dieser Klasse mehrmals ersetzt!
			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				ResultSet rs1 = stmt
						.executeQuery("SELECT * FROM klassen ORDER BY Klasse");
				while (rs1.next()) {
					klassenanzahl[j] = rs1.getString("Klasse").toLowerCase();
					j++;
				}
				klcb_row = j;
				stmt.close();
				con.close();
			} catch (Exception klassex) {
				klassex.printStackTrace();
			}
			// Ende des Einschub-NEU

			for (j = 0; j < klcb_row; j++) {
				// klcb.setSelectedIndex(j);
				// klassecb = (String) klcb.getSelectedItem();
				sbx = 0;
				rel = 0;
				uew = 0;
				tmp_summe = 0;
				tmp_gesamt = 0;

				try {

					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM "
							+ klassenanzahl[j]);
					while (rs.next()) {
						nrre[i] = rs.getString("Nr");
						double tmp = rs.getDouble("Preis");
						fach_re = rs.getString("Fach");
						double tmp1 = rs.getDouble("Anzahl");
						titel_uew = rs.getString("Titel");
						lehrer_tmp[i] = rs.getString("Lehrer");
						tmp2[i] = rs.getDouble("Wert");

						double wert1 = tmp2[i];
						int decimalPlace = 2;
						BigDecimal bd = new BigDecimal(wert1);
						bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
						tmp2[i] = bd.doubleValue();

						// ABFRAGE und BERECHNUNG für SBX oder Religion oder uew
						// sonst allgemein
						boolean fach_sbx = fach_re.startsWith("X");
						boolean fach_rel = fach_re.startsWith("R");
						boolean fach_relX = fach_re.startsWith("X-R");
						if (fach_relX == true) {
							fach_sbx = false;
							fach_rel = true;
						}
						boolean fach_relXK = fach_re.startsWith("XK-R");
						if (fach_relXK == true) {
							fach_sbx = false;
							fach_rel = true;
						}
						boolean fach_uew = titel_uew.startsWith("UeW");
						if (fach_sbx == true)
							sbx = sbx + tmp2[i];
						else if (fach_rel == true || fach_relX == true
								|| fach_relXK == true)
							rel = rel + tmp2[i];
						else if (fach_uew == true)
							uew = uew + tmp2[i];
						else
							tmp_summe = tmp_summe + tmp2[i];

						/*
						 * boolean fach_sbx = false; boolean fach_rel = false;
						 * boolean teste_sbx = fach_re.startsWith("X"); if
						 * (teste_sbx == true) { boolean teste_rel =
						 * fach_re.startsWith("X-R"); boolean teste_rel_XKR =
						 * fach_re.startsWith("XK-R"); if (teste_rel == true ||
						 * teste_rel_XKR == true) { fach_rel = true; } else {
						 * fach_sbx = true; } } boolean fach_uew =
						 * titel_uew.startsWith("UeW"); if (fach_sbx == true)
						 * sbx = sbx+tmp2[i]; else if (fach_rel == true) rel =
						 * rel+tmp2[i]; else if (fach_uew == true) uew =
						 * uew+tmp2[i]; else tmp_summe = tmp_summe+tmp2[i];
						 */

						// Umwandlung SBX
						wert1 = sbx;
						decimalPlace = 2;
						bd = new BigDecimal(wert1);
						bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
						sbx = bd.doubleValue();

						// Umwandlung Religion
						wert1 = rel;
						decimalPlace = 2;
						bd = new BigDecimal(wert1);
						bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
						rel = bd.doubleValue();

						// Umwandlung UeW
						wert1 = uew;
						decimalPlace = 2;
						bd = new BigDecimal(wert1);
						bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
						uew = bd.doubleValue();

						// Umwandlung Allgemein
						wert1 = tmp_summe;
						decimalPlace = 2;
						bd = new BigDecimal(wert1);
						bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
						tmp_summe = bd.doubleValue();
						i++;
					}
					count = i;

					/*
					 * for (i=0;i<count;i++) { sql =
					 * "UPDATE "+klassecb+" SET Wert = "
					 * +tmp2[i]+" WHERE Nr = '"+
					 * nrre[i]+"' AND Lehrer = '"+lehrer_tmp[i]+"'";
					 * stmt.executeUpdate(sql); }
					 */
					sbx_gesamt = sbx_gesamt + sbx; // Gesamtsumme SBX
					rel_gesamt = rel_gesamt + rel; // Gesamtsumme Religion
					uew_gesamt = uew_gesamt + uew; // Gesamtsumme UeW
					tmp_gesamt = tmp_gesamt + tmp_summe + sbx;
					gesamt_ohne = gesamt_ohne + tmp_summe; // Gesamtsumme
															// Allgemein ohne
															// SBX
					gesamt = gesamt + tmp_gesamt; // Gesamtsumme Allgemein und
													// SBX

					double wert1 = tmp_gesamt;
					int decimalPlace = 2;
					BigDecimal bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					tmp_gesamt = bd.doubleValue();

					sql = "INSERT INTO kl_summe (Klasse, Summe_SBX, Summe_ohneSBX, SBX, Religion, UeW, Gesamt, Gesamt_ohneSBX, Gesamt_SBX, Gesamt_Religion, Gesamt_UeW) values ('"
							+ klassenanzahl[j]
							+ "', "
							+ tmp_gesamt
							+ ", "
							+ tmp_summe
							+ ", "
							+ sbx
							+ ","
							+ rel
							+ ","
							+ uew
							+ ","
							+ gesamt
							+ ","
							+ gesamt_ohne
							+ ","
							+ sbx_gesamt
							+ ","
							+ rel_gesamt
							+ ","
							+ uew_gesamt
							+ ")";
					stmt.executeUpdate(sql);
					stmt.close();
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} // Ende for Schleife
			JOptionPane.showMessageDialog(null,
					"Berechnung für Klassen erfolgreich durchgeführt !");
		} // Ende if Schleife
		else {
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
			anzeigen(klassecb);
		}
	} // Ende public berechne
} // Ende class berechne
