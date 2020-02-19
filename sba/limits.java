package sba;

import java.awt.AWTEvent;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import java.io.*;
//import java.util.*;
//import java.net.URL;
//import java.sql.DatabaseMetaData;
//import java.sql.Driver;
//import java.sql.Driver;
//import java.sql.ResultSet;

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

public class limits extends Frame {

	JFrame limitsfrm = new JFrame();
	JPanel limits = new JPanel();
	JLabel uslbl = new JLabel("Unterstufe: ");
	JTextField ustxt = new JTextField(5);
	JLabel us_sbxlbl = new JLabel("Unterstufe-SBX:");
	JTextField us_sbxtxt = new JTextField(5);
	JLabel us_rellbl = new JLabel("Unterstufe-REL: ");
	JTextField us_reltxt = new JTextField(5);
	JLabel us_rel_sbxlbl = new JLabel("Unterstufe-REL-SBX: ");
	JTextField us_rel_sbxtxt = new JTextField(5);
	JLabel oslbl = new JLabel("Oberstufe:");
	JTextField ostxt = new JTextField(5);
	JLabel os_sbxlbl = new JLabel("Oberstufe-SBX: ");
	JTextField os_sbxtxt = new JTextField(5);
	JLabel os_rellbl = new JLabel("Oberstufe-REL:");
	JTextField os_reltxt = new JTextField(5);
	JLabel os_rel_sbxlbl = new JLabel("Oberstufe-REL-SBX:");
	JTextField os_rel_sbxtxt = new JTextField(5);
	Checkbox limges = new Checkbox("Gesamt Limits", null, false);
	String sjstr_neu;

	JButton okbtn = new JButton("übernehmen");
	JButton abbruchbtn = new JButton("Abbruch");

	public limits(String sjstr) {
		sjstr_neu = sjstr;
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jbInit2() {
		limits.add(uslbl);
		limits.add(ustxt);
		ustxt.setSize(5, 10);
		limits.add(us_sbxlbl);
		limits.add(us_sbxtxt);
		us_sbxtxt.setSize(5, 10);
		limits.add(us_rellbl);
		limits.add(us_reltxt);
		us_reltxt.setSize(5, 10);
		limits.add(us_rel_sbxlbl);
		limits.add(us_rel_sbxtxt);
		us_rel_sbxtxt.setSize(5, 10);
		limits.add(oslbl);
		limits.add(ostxt);
		ostxt.setSize(5, 10);
		limits.add(os_sbxlbl);
		limits.add(os_sbxtxt);
		os_sbxtxt.setSize(5, 10);
		limits.add(os_rellbl);
		limits.add(os_reltxt);
		os_reltxt.setSize(5, 10);
		limits.add(os_rel_sbxlbl);
		limits.add(os_rel_sbxtxt);
		os_rel_sbxtxt.setSize(5, 10);
		limits.add(okbtn);
		limits.add(abbruchbtn);
		limits.add(limges);
		okbtn.addActionListener(this);
		abbruchbtn.addActionListener(this);
		GridLayout gr = new GridLayout(5, 4, 10, 10);
		limits.setLayout(gr);
		limitsfrm.setResizable(false);
		limitsfrm.setSize(new Dimension(510, 200));
		limitsfrm.setTitle("Limits eingeben");
		limitsfrm.getContentPane().add(limits);

		// Das Fenster zentrieren
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = limitsfrm.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		limitsfrm.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		limitsfrm.setVisible(true);
	}

	public void eingabe() {

		String us = ustxt.getText();
		String us_sbx = us_sbxtxt.getText();
		String us_rel = us_reltxt.getText();
		String us_rel_sbx = us_rel_sbxtxt.getText();
		String os = ostxt.getText();
		String os_sbx = os_sbxtxt.getText();
		String os_rel = os_reltxt.getText();
		String os_rel_sbx = os_rel_sbxtxt.getText();

		boolean abbruch = false;
		boolean limgesbol = false;

		int usint = us.length();
		int ussbxint = us_sbx.length();
		int usrelint = us_rel.length();
		int usrelsbxint = us_rel_sbx.length();
		int osint = os.length();
		int ossbxint = os_sbx.length();
		int osrelint = os_rel.length();
		int osrelsbxint = os_rel_sbx.length();
		if (usint == 0 || ussbxint == 0 || usrelint == 0 || usrelsbxint == 0
				|| osint == 0 || ossbxint == 0 || osrelint == 0
				|| osrelsbxint == 0)
			JOptionPane.showMessageDialog(null, "Keine Eingabe !");

		else {
			double usdouble = Double.parseDouble(us);
			double ussbxdouble = Double.parseDouble(us_sbx);
			double usreldouble = Double.parseDouble(us_rel);
			double usrelsbxdouble = Double.parseDouble(us_rel_sbx);
			double osdouble = Double.parseDouble(os);
			double ossbxdouble = Double.parseDouble(os_sbx);
			double osreldouble = Double.parseDouble(os_rel);
			double osrelsbxdouble = Double.parseDouble(os_rel_sbx);

			limgesbol = limges.getState();
			if (limgesbol == true) {
				try {
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					int response = JOptionPane.showConfirmDialog(null,
							"Alte Limits überschreiben ?", "Limits",
							JOptionPane.YES_NO_OPTION);
					if (response == 0) {
						String sql = "DELETE FROM limits_gesamt";
						stmt.execute(sql);
						sql = "INSERT INTO limits_gesamt (Unterstufe,Unterstufe_SBX,Unterstufe_Religion,Unterstufe_Religion_SBX,Oberstufe,Oberstufe_SBX,Oberstufe_Religion,Oberstufe_Religion_SBX) VALUES ("
								+ usdouble
								+ ","
								+ ussbxdouble
								+ ","
								+ usreldouble
								+ ","
								+ usrelsbxdouble
								+ ","
								+ osdouble
								+ ","
								+ ossbxdouble
								+ ","
								+ osreldouble + "," + osrelsbxdouble + ")";
						stmt.executeUpdate(sql);
						JOptionPane.showMessageDialog(null,
								"Die neuen Limits wurden angelegt !");
						limitsfrm.setVisible(false);
						con.close();
					}
				}

				catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}
			}

			else {
				try {
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					int response = JOptionPane.showConfirmDialog(null,
							"Alte Limits überschreiben ?", "Limits",
							JOptionPane.YES_NO_OPTION);
					if (response == 0) {
						String sql = "DELETE FROM limits";
						stmt.execute(sql);
						sql = "INSERT INTO limits (Unterstufe,Unterstufe_SBX,Unterstufe_Religion,Unterstufe_Religion_SBX,Oberstufe,Oberstufe_SBX,Oberstufe_Religion,Oberstufe_Religion_SBX) VALUES ("
								+ usdouble
								+ ","
								+ ussbxdouble
								+ ","
								+ usreldouble
								+ ","
								+ usrelsbxdouble
								+ ","
								+ osdouble
								+ ","
								+ ossbxdouble
								+ ","
								+ osreldouble + "," + osrelsbxdouble + ")";
						stmt.executeUpdate(sql);
						JOptionPane.showMessageDialog(null,
								"Die neuen Limits wurden angelegt !");
						limitsfrm.setVisible(false);
						con.close();
					}
				}

				catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}
			} // Ende des Else-Zweiges
		}
	}

	// Abbruch
	public void abbruch() {
		limitsfrm.setVisible(false);
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == okbtn)
			eingabe();
		if (source == abbruchbtn)
			abbruch();
	}
}
