package sba;

//import java.awt.*;
//import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
//import java.net.URL;
//import java.io.*;

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

public class anzahl extends Frame {

	public anzahl(String klassecb, String sjstr, String text, int textstart,
			int textend) {

		StringTokenizer input;
		String[] nrstr = new String[50];
		String[] anzahlstr = new String[50];
		String[] lehrerstr = new String[50];
		String[] lehrerstrID = new String[50];
		String[] kennzeichenstr = new String[50];
		String[] fachstr = new String[50];
		int[] idint = new int[50];
		int i = 0, count = 0; // j=0
		boolean[] lehrerSet = new boolean[50];
		String sql = "";

		String[] preisstr = new String[50];

		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + klassecb
					+ " ORDER BY Fach");
			while (rs.next()) {
				idint[count] = rs.getInt("ID");
				lehrerstrID[count] = rs.getString("Lehrer");
				// System.out.println(lehrerstrID[count]);
				// preis[i]=rs.getDouble("Preis");
				// i++;
				count++;
			}

			// i=0;

			text = text.substring(textstart, textend);
			// System.out.println(text);
			input = new StringTokenizer(text, "\t");

			while (i < count) {
				nrstr[i] = input.nextToken("\t");
				if (i > 0)
					nrstr[i] = nrstr[i].substring(1);
				input.nextToken("\t");
				preisstr[i] = input.nextToken("\t");
				fachstr[i] = input.nextToken("\t");
				anzahlstr[i] = input.nextToken("\t");
				lehrerstr[i] = input.nextToken("\t");
				int lehrerComp = lehrerstr[i].compareTo(lehrerstrID[i]);
				// System.out.println(lehrerComp + ";" + lehrerstr[i] + ";" +
				// lehrerstrID[i] + ";" + idint[i]);
				if (lehrerComp == 0)
					lehrerSet[i] = false;
				else
					lehrerSet[i] = true;
				input.nextToken("\t");
				kennzeichenstr[i] = input.nextToken("\n");
				kennzeichenstr[i] = kennzeichenstr[i].substring(1);
				i++;
			}

			for (i = 0; i < count; i++) {
				sql = "UPDATE " + klassecb + " SET Anzahl = " + anzahlstr[i]
						+ ", W = " + kennzeichenstr[i] + " WHERE Nr = '"
						+ nrstr[i] + "' AND Lehrer = '" + lehrerstr[i] + "'";
				stmt.executeUpdate(sql);
			}

			for (i = 0; i < count; i++) {
				sql = "UPDATE " + klassecb + " SET Fach = '" + fachstr[i]
						+ "' WHERE Nr = '" + nrstr[i] + "' AND Lehrer = '"
						+ lehrerstr[i] + "'";
				stmt.executeUpdate(sql);
			}

			for (i = 0; i < count; i++) {
				double wertdouble = Integer.parseInt(anzahlstr[i])
						* Double.parseDouble(preisstr[i]);

				double wert1 = wertdouble;
				int decimalPlace = 2;
				BigDecimal bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				wertdouble = bd.doubleValue();

				sql = "UPDATE " + klassecb + " SET Wert = " + wertdouble
						+ " WHERE Nr = '" + nrstr[i] + "' AND Lehrer = '"
						+ lehrerstr[i] + "'";
				stmt.executeUpdate(sql);
			}

			for (i = 0; i < count; i++) {
				if (lehrerSet[i] == true)
					sql = "UPDATE " + klassecb + " SET Lehrer = '"
							+ lehrerstr[i] + "' WHERE ID=" + idint[i]
							+ " AND Lehrer = '" + lehrerstrID[i] + "'";
				stmt.executeUpdate(sql);
			}

			stmt.close();
			con.close();
			JOptionPane
					.showMessageDialog(null,
							"Werte für ANZAHL, LEHRER, WERT(Kosten) und KENNZEICHEN neu gesetzt !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
