package sba;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import java.net.URL;
//import java.sql.DatabaseMetaData;
//import java.sql.Driver;
//import java.sql.Driver;

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

public class deflehrer extends Frame {

	JFrame lehrerdeffrm = new JFrame();
	JPanel lehrerdefpnl = new JPanel();
	JLabel lehrerdeflbl = new JLabel("Name (Namenskürzel): ");
	JLabel lehrerpwdlbl = new JLabel("Kennwort: ");
	JTextField lehrerdeftxt = new JTextField(5);
	JTextField lehrerpwdtxt = new JTextField(5);
	JButton okbtn = new JButton("Einfügen");
	JButton loeschenbtn = new JButton("Löschen");
	JButton uebernahmebtn = new JButton("GPU004 übernehmen");
	String sjstr_neu;
	String lehrerdefstr = "";
	String lehrerpwdstr = "";

	public deflehrer(String sjstr) {
		sjstr_neu = sjstr;
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit4();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jbInit4() {
		lehrerdefpnl.add(lehrerdeflbl);
		lehrerdefpnl.add(lehrerdeftxt);
		lehrerdefpnl.add(lehrerpwdlbl);
		lehrerdefpnl.add(lehrerpwdtxt);
		lehrerdefpnl.add(okbtn);
		lehrerdefpnl.add(uebernahmebtn);
		lehrerdefpnl.add(loeschenbtn);
		okbtn.addActionListener(this);
		loeschenbtn.addActionListener(this);
		uebernahmebtn.addActionListener(this);
		GridLayout gr = new GridLayout(4, 2, 10, 10);
		lehrerdefpnl.setLayout(gr);
		lehrerdeffrm.setResizable(false);
		lehrerdeffrm.setSize(new Dimension(350, 150));
		lehrerdeffrm.setTitle("Lehrer anlegen/löschen");
		lehrerdeffrm.getContentPane().add(lehrerdefpnl);

		// Das Fenster zentrieren
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = lehrerdeffrm.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		lehrerdeffrm.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		lehrerdeffrm.setVisible(true);
	}

	public void uebernahme() {

		String[] name_lehrer = new String[200];
		String[] name_tmp1 = new String[200];
		String name_tmp = "", sql = "";
		int i = 0, j = 0, lehrer_ges = 0, lehrer_ges1 = 0;
		int numbertmp = 0;
		boolean abbruch = false;
		StringTokenizer strichpunkt;

		try {
			FileReader zaehlen = new FileReader(
					"C:/Programme/gp-Untis/GPU004.txt");
			BufferedReader b = new BufferedReader(zaehlen);
			boolean eofb = false;

			while (!eofb) {
				String line = b.readLine();

				if (line == null)
					eofb = true;
				else {
					strichpunkt = new StringTokenizer(line, ";");
					name_tmp = strichpunkt.nextToken(";");
					int endzeichen = name_tmp.length();
					name_tmp = name_tmp.substring(1, endzeichen - 1);
					name_lehrer[i] = name_tmp;
					i++;
				}
			}
			b.close();
			lehrer_ges = i;
		} catch (IOException ex) {
			System.out.println("Error -- " + ex.toString());
		}

		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM lehrer ORDER BY Lehrer");

			while (rs.next()) {
				name_tmp1[j] = rs.getString("Lehrer");
				j++;
			}
			lehrer_ges1 = j;

			for (i = 0; i < lehrer_ges; i++) {// for-Schleife 1
				for (j = 0; j < lehrer_ges1; j++) {
					int tmpzahl = name_lehrer[i].compareTo(name_tmp1[j]);
					if (tmpzahl == 0)
						abbruch = true;
				}
				if (abbruch == true) {
					sql = "UPDATE lehrer SET Aktive = 'a' WHERE Lehrer LIKE '"
							+ name_lehrer[i] + "'";
					stmt.execute(sql);
				} else {
					int response = JOptionPane
							.showConfirmDialog(null, "Lehrer " + name_lehrer[i]
									+ " anlegen ?", "Lehrer neu anlegen?",
									JOptionPane.OK_CANCEL_OPTION);
					if (response == 0) {
						String name_pwd = name_lehrer[i];
						int name_laenge = name_lehrer[i].length();
						while (name_laenge < 5) {
							if (name_laenge < 4)
								name_pwd = name_pwd + i % 9;
							if (name_laenge < 5)
								name_pwd = name_pwd + i % 8;
							name_laenge = name_pwd.length();
						}
						sql = "INSERT INTO lehrer (Lehrer,Password,Aktive) VALUES ('"
								+ name_lehrer[i] + "','" + name_pwd + "','a')";
						stmt.executeUpdate(sql);
					}
				}// ende else-Schleife 1
				abbruch = false;
			}
			sql = "DELETE FROM lehrer WHERE Aktive = 'n'";
			stmt.executeUpdate(sql);
			sql = "UPDATE lehrer SET Aktive = 'n'";
			stmt.executeUpdate(sql);

			ResultSet rs1 = stmt
					.executeQuery("SELECT * FROM lehrer ORDER BY Lehrer");

			while (rs1.next()) {
				name_lehrer[numbertmp] = rs1.getString("Lehrer");
				numbertmp++;
			}
			for (i = 0; i <= numbertmp; i++) {
				sql = "UPDATE lehrer SET ID = " + i + " WHERE Lehrer = '"
						+ name_lehrer[i] + "'";
				stmt.executeUpdate(sql);
			}
			con.close();
			lehrerdeftxt.setText("");
			lehrerpwdtxt.setText("");
		}

		catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}

		JOptionPane.showMessageDialog(null, "Übernahme der GPU004.txt beendet");
	}

	public void einfuegen() {

		lehrerdefstr = lehrerdeftxt.getText();
		lehrerdefstr = lehrerdefstr.toUpperCase();
		lehrerpwdstr = lehrerpwdtxt.getText();
		boolean abbruch = false;
		int number = 1, numbertmp = 1, i = 1;
		String tmp[] = new String[150];

		int testzahl = lehrerdefstr.length();
		int testzahl0 = lehrerpwdstr.length();
		if (testzahl < 1 || testzahl0 != 5 || testzahl > 4)
			JOptionPane.showMessageDialog(null,
					"Eingabe unvollständig bzw. Format falsch !");

		else {

			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT * FROM lehrer ORDER BY Lehrer");
				while (rs.next()) {
					tmp[number] = rs.getString(2);
					int tmpzahl = tmp[number].compareTo(lehrerdefstr);
					number++;
					if (tmpzahl == 0)
						abbruch = true;
				}
				if (abbruch == true)
					JOptionPane.showMessageDialog(null,
							"Lehrer bereits in Liste enthalten !");
				else {
					String sql = "INSERT INTO lehrer (Lehrer,Password) VALUES ('"
							+ lehrerdefstr + "','" + lehrerpwdstr + "')";
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Lehrer "
							+ lehrerdefstr + " wurde angelegt !");
				}
				ResultSet rs1 = stmt
						.executeQuery("SELECT * FROM lehrer ORDER BY Lehrer");
				while (rs1.next()) {
					tmp[numbertmp] = rs1.getString(2);
					numbertmp++;
				}
				for (i = 1; i <= numbertmp; i++) {
					String sql = "UPDATE lehrer SET ID = " + i
							+ " WHERE Lehrer = '" + tmp[i] + "'";
					stmt.executeUpdate(sql);
				}
				con.close();
				lehrerdeftxt.setText("");
				lehrerpwdtxt.setText("");
			}

			catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
		} // Ende des Else-Zweiges
	}

	// Eintrag löschen
	public void loeschen() {

		lehrerdefstr = lehrerdeftxt.getText();
		lehrerdefstr = lehrerdefstr.toUpperCase();
		int number = 1, numbertmp = 1, i = 1;
		String tmploeschen[] = new String[150];

		boolean abbruch = true;

		int testzahl = lehrerdefstr.length();
		if (testzahl == 0)
			JOptionPane.showMessageDialog(null, "Keine Eingabe !");

		else {

			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM lehrer");
				while (rs.next()) {
					String tmp = rs.getString(2);
					int tmpzahl = tmp.compareTo(lehrerdefstr);
					if (tmpzahl == 0)
						abbruch = false;
				}
				if (abbruch == true)
					JOptionPane.showMessageDialog(null,
							"Kein Eintrag in der Liste !");
				else {
					String sql = "DELETE FROM lehrer WHERE Lehrer = '"
							+ lehrerdefstr + "'";
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Lehrer "
							+ lehrerdefstr + " wurde gelöscht !");
				}
				ResultSet rs1 = stmt
						.executeQuery("SELECT * FROM lehrer ORDER BY Lehrer");
				while (rs1.next()) {
					tmploeschen[numbertmp] = rs1.getString(2);
					numbertmp++;
				}
				for (i = 1; i <= numbertmp; i++) {
					String sql = "UPDATE lehrer SET ID = " + i
							+ " WHERE Lehrer = '" + tmploeschen[i] + "'";
					stmt.executeUpdate(sql);
				}
				con.close();
				lehrerdeftxt.setText("");
			}

			catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
		} // Ende des Else-Zweiges
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == okbtn)
			einfuegen();
		if (source == loeschenbtn)
			loeschen();
		if (source == uebernahmebtn)
			uebernahme();
	}
}
