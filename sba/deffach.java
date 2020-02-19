package sba;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

public class deffach extends Frame {

	JFrame fachfrm = new JFrame();
	JPanel fach = new JPanel();
	JLabel fachlbl = new JLabel("Bezeichnung: ");
	JTextField fachtxt = new JTextField(5);
	JButton okbtn = new JButton("Einfügen");
	JButton loeschenbtn = new JButton("Löschen");
	String sjstr_neu;
	String fachstr = "";

	public deffach(String sjstr) {
		sjstr_neu = sjstr;
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit1();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jbInit1() {
		fach.add(fachlbl);
		fach.add(fachtxt);
		fach.add(okbtn);
		fach.add(loeschenbtn);
		okbtn.addActionListener(this);
		loeschenbtn.addActionListener(this);
		GridLayout gr = new GridLayout(2, 2, 10, 10);
		fach.setLayout(gr);
		fachfrm.setResizable(false);
		fachfrm.setSize(new Dimension(250, 100));
		fachfrm.setTitle("Fächer anlegen/löschen");
		fachfrm.getContentPane().add(fach);

		// Das Fenster zentrieren
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = fachfrm.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		fachfrm.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		fachfrm.setVisible(true);
	}

	public void einfuegen() {

		fachstr = fachtxt.getText();
		fachstr = fachstr.toUpperCase();
		boolean abbruch = false;

		int testzahl = fachstr.length();
		if (testzahl == 0)
			JOptionPane.showMessageDialog(null, "Keine Eingabe !");

		else {

			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM faecher");
				while (rs.next()) {
					String tmp = rs.getString(1);
					int tmpzahl = tmp.compareTo(fachstr);
					if (tmpzahl == 0)
						abbruch = true;
				}
				if (abbruch == true)
					JOptionPane.showMessageDialog(null,
							"Bereits in Liste enthalten !");
				else {
					String sql = "INSERT INTO faecher (Fach) VALUES ('"
							+ fachstr + "')";
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Fach " + fachstr
							+ " wurde angelegt !");
				}
				con.close();
				fachtxt.setText("");
			}

			catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
		} // Ende des Else-Zweiges
	}

	// Eintrag löschen
	public void loeschen() {

		fachstr = fachtxt.getText();
		fachstr = fachstr.toUpperCase();
		fachtxt.setText(fachstr);
		boolean abbruch = true;

		int testzahl = fachstr.length();
		if (testzahl == 0)
			JOptionPane.showMessageDialog(null, "Keine Eingabe !");

		else {

			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM faecher");
				while (rs.next()) {
					String tmp = rs.getString(1);
					int tmpzahl = tmp.compareTo(fachstr);
					if (tmpzahl == 0)
						abbruch = false;
				}
				if (abbruch == true)
					JOptionPane.showMessageDialog(null,
							"Kein Eintrag in der Liste !");
				else {
					String sql = "DELETE FROM faecher WHERE Fach = '" + fachstr
							+ "'";
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Fach " + fachstr
							+ " wurde gelöscht !");
				}
				con.close();
				fachtxt.setText("");
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
	}
}
