package sba;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
//import java.awt.image.*;
//import java.awt.print.*;
//import java.net.URL;
//import java.sql.Statement.*;

/**
 * <p>
 * Überschrift: Schulbuchaktion
 * </p>
 * <p>
 * Beschreibung: Programm für die Eingabe und Bearbeitung für
 * Schulbuchreferenten
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003/04 A.pp
 * </p>
 * <p>
 * Organisation: AGI
 * </p>
 *
 * @author Andreas Knapp
 * @version 1.2
 */

public class Frame extends JFrame implements ActionListener, ItemListener {

	// public Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

	String klassestr = "", klassecb = "", fachtxt = "", klassetxt = "",
			lehrer = "";
	int us, usr, os, osr; // Schüler
	String sjstr = ""; // Schuljahr
	int lf = 10; // line feed
	int ht = 9; // horizontal tabulator
	int wvint = 0; // Variable für die Eingabe der zu wiederverwendeten Bücher
	FileDialog open = new FileDialog(this, "Datei öffnen", FileDialog.LOAD);
	int zeilen;
	String anmeldesjstr = "";
	char pwd[] = new char[6]; // Variablen für class pwdlang - pwd in Worten
								// ausdrücken
	String pwdwort[] = new String[6];
	int pwd_counter = 0;
	boolean anzeigen_klasse = true; // notwendig um itemStateChange für
									// anzeigen(klassecb) aus- und
									// einzuschalten; Schuljahrwechsel!!

	JPanel contentPane;
	JPanel pane = new JPanel();
	JPanel subpane = new JPanel();
	JMenuBar jMenuBar1 = new JMenuBar();
	JMenu jMenuFile = new JMenu();
	JMenuItem jMenuFileExit = new JMenuItem();
	JMenuItem jMenuFileOpen = new JMenuItem();
	JMenuItem jMenuFileRe = new JMenuItem();
	JMenuItem jMenuPwd = new JMenuItem();
	JMenuItem jMenuDelete = new JMenuItem();
	JMenuItem jMenuDeleteLH = new JMenuItem();
	JMenuItem jMenuanmeldenals = new JMenuItem();
	JMenu jMenuHelp = new JMenu();
	JMenuItem jMenuHelphelp = new JMenuItem();
	JMenu jMenuAnlegen = new JMenu();
	JMenuItem jMenuAnlegenFach = new JMenuItem();
	JMenuItem jMenuAnlegenKlasse = new JMenuItem();
	JMenuItem jMenuAnlegenLehrer = new JMenuItem();
	JMenuItem jMenuAnlegenSchueler = new JMenuItem();
	JMenuItem jMenuAnlegenSchuljahr = new JMenuItem();
	JMenuItem jMenuNeuSchuljahr = new JMenuItem();
	JMenuItem jMenuAnlegenLimits = new JMenuItem();
	JMenu jMenuDruck = new JMenu();
	JMenuItem jMenuDruckLehrer = new JMenuItem();
	JMenuItem jMenuDruckKlasse = new JMenuItem();
	JMenuItem jMenuDruckFach = new JMenuItem();
	JMenuItem jMenuDruckBuch = new JMenuItem();
	JMenuItem jMenuDruckKlaBuch = new JMenuItem();
	JMenuItem jMenuDruckPwd = new JMenuItem();
	JLabel lehrerlbl = new JLabel();
	JTextField lehrertxt = new JTextField();
	JLabel USlbl = new JLabel();
	JTextField UStxt = new JTextField();
	JLabel USrlbl = new JLabel();
	JTextField USrtxt = new JTextField();
	JLabel OSlbl = new JLabel();
	JTextField OStxt = new JTextField();
	JLabel OSrlbl = new JLabel();
	JTextField OSrtxt = new JTextField();
	JMenuItem jMenuHelpAbout = new JMenuItem();
	JToolBar jToolBar = new JToolBar();
	JButton jButton1 = new JButton();
	JButton jButton2 = new JButton();
	JButton jButton3 = new JButton();
	JButton jButton4 = new JButton();
	ImageIcon image1;
	ImageIcon image2;
	ImageIcon image3;
	ImageIcon image4;
	JLabel statusBar = new JLabel();
	JLabel nrlbl = new JLabel("Nr.");
	JTextField nrtxt = new JTextField(10);
	JLabel titellbl = new JLabel("Titel");
	JTextField titeltxt = new JTextField(20);
	JLabel preislbl = new JLabel("Preis");
	JTextField preistxt = new JTextField(4);
	JLabel fachlbl = new JLabel("Fach");
	JLabel klasselbl = new JLabel("Klasse");
	JButton insertbtn = new JButton("Einfügen");
	JButton deletebtn = new JButton("Löschen");
	JButton searchbtn = new JButton("Suchen");
	JButton klassebtn = new JButton("Klasse anzeigen");
	JButton pwdaendernbtn = new JButton("Password ändern");
	JButton druckbtn = new JButton("Druck");
	JButton resetbtn = new JButton("reset");
	JComboBox<String> klcb = new JComboBox<String>();
	JComboBox<String> fachcb = new JComboBox<String>();
	JComboBox<String> klassecb1 = new JComboBox<String>();
	JTextField schuljahrtxt = new JTextField();
	Checkbox UeW = new Checkbox("Unterrichtsm.e.W.", null, false);
	JTextArea output = new JTextArea(30, 120);
	JScrollPane scroller = new JScrollPane(output);
	BorderLayout borderLayout1 = new BorderLayout(10, 10);


	// Den Frame konstruieren
	public Frame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Initialisierung der Komponenten
	public void jbInit() throws Exception {

		// Das Fenster zentrieren
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// JOptionPane.showMessageDialog(null,"width:"+screenSize.width+"; height:"+screenSize.height);
		// Ende Fenster zentrieren

		image1 = new ImageIcon(sba.class.getResource("openFile.gif"));
		image2 = new ImageIcon(sba.class.getResource("closeFile.gif"));
		image3 = new ImageIcon(sba.class.getResource("help.gif"));
		image4 = new ImageIcon(sba.class.getResource("schreiben.gif"));

		// ImageIcon image1 = new
		// ImageIcon(getClass().getResource("/images/openFile.gif"));
		// ImageIcon image2 = new
		// ImageIcon(getClass().getResource("/images/closeFile.gif"));
		// ImageIcon image4 = new
		// ImageIcon(getClass().getResource("/images/schreiben.gif"));
		// System.out.println(image1);

		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout1);
		output.setFont(new Font("FREEMONO", Font.PLAIN, 16));
		klcb.setFont(new Font("FREEMONO", Font.PLAIN, 16));
		fachcb.setFont(new Font("FREEMONO", Font.PLAIN, 16));
		klassecb1.setFont(new Font("FREEMONO", Font.PLAIN, 16));
		this.setResizable(false);

		// neu (27.2.2006) Fenstergröße anpassen siehe auch "scroller"
		// NT Fensterbreite -100 wegen Seitenleiste auf Linux-Clients
		int newscreenSizewidth = screenSize.width - 100;
		int newscreenSizeheight = screenSize.height - 10;
		this.setSize(new Dimension(newscreenSizewidth, newscreenSizeheight));
		this.setLocation((screenSize.width) / 2, (screenSize.height) / 2);

		this.setTitle("Schulbuchaktion");

		statusBar.setText(" ");
		// Datei öffnen
		jMenuFile.setText("Datei");
		jMenuFileOpen.setText("Datei öffnen");
		jMenuFileOpen.setMnemonic('a');
		jMenuFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuFileOpen_actionPerformed(e);
			}
		});
		// Rechnung
		jMenuFileRe.setText("Rechnung");
		jMenuFileExit.setMnemonic('R');
		jMenuFileRe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuFileRe_actionPerformed(e);
			}
		});
		// Password
		jMenuPwd.setText("Password");
		jMenuPwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuPwd_actionPerformed(e);
			}
		});
		// Delete - Löschen ALLER Datensätze in den Klassen
		jMenuDelete.setText("Datensätze löschen");
		jMenuDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDelete_actionPerformed(e);
			}
		});
		// Delete - Löschen ALLER Datensätze in den Lehrerhandexemplar
		jMenuDeleteLH.setText("LH-Exemplar löschen");
		jMenuDeleteLH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDeleteLH_actionPerformed(e);
			}
		});
		// Anmelden als (anderer Lehrer) nur für Admin
		jMenuanmeldenals.setText("anmelden als ...");
		jMenuanmeldenals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuanmeldenals_actionPerformed(e);
			}
		});
		// Beenden
		jMenuFileExit.setText("Beenden");
		jMenuFileExit.setMnemonic('e');
		jMenuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuFileExit_actionPerformed(e);
			}
		});
		// Anlegen Fach
		jMenuAnlegen.setText("Anlegen");
		jMenuAnlegenFach.setText("Fächer");
		jMenuAnlegenFach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuAnlegenFach_actionPerformed(e);
			}
		});
		// Anlegen Klasse
		jMenuAnlegenKlasse.setText("Klasse");
		jMenuAnlegenKlasse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuAnlegenKlasse_actionPerformed(e);
			}
		});
		// Anlegen Lehrer
		jMenuAnlegenLehrer.setText("Lehrer");
		jMenuAnlegenLehrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuAnlegenLehrer_actionPerformed(e);
			}
		});
		// Anlegen Schüler-Anzahl
		jMenuAnlegenSchueler.setText("Schüler übernehmen");
		jMenuAnlegenSchueler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuAnlegenSchueler_actionPerformed(e);
			}
		});
		// Anlegen Schuljahr
		jMenuAnlegenSchuljahr.setText("Schuljahr übernehmen");
		jMenuAnlegenSchuljahr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuAnlegenSchuljahr_actionPerformed(e);
			}
		});
		// Neu Schuljahr
		jMenuNeuSchuljahr.setText("Schuljahr NEU anlegen/löschen");
		jMenuNeuSchuljahr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuNeuSchuljahr_actionPerformed(e);
			}
		});
		// Anlegen Limits
		jMenuAnlegenLimits.setText("Limits eingeben");
		jMenuAnlegenLimits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuAnlegenLimits_actionPerformed(e);
			}
		});
		// Druck Lehrerlisten + LehrerHandexemplar
		jMenuDruck.setText("Druck");
		jMenuDruckLehrer.setText("Lehrerlisten");
		jMenuDruckLehrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDruckLehrer_actionPerformed(e);
			}
		});
		// Druck Klasse
		jMenuDruckKlasse.setText("Klassenlisten");
		jMenuDruckKlasse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDruckKlasse_actionPerformed(e);
			}
		});
		// Druck Fach Berechnung
		jMenuDruckFach.setText("Fachlisten");
		jMenuDruckFach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDruckFach_actionPerformed(e);
			}
		});
		// Druck Buchlisten sortiert und Schulstufe
		jMenuDruckBuch.setText("Buchlisten");
		jMenuDruckBuch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDruckBuch_actionPerformed(e);
			}
		});
		// Druck Klasssen und Buch Matrix
		jMenuDruckKlaBuch.setText("Kla-Buch-Matrix");
		jMenuDruckKlaBuch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDruckKlaBuch_actionPerformed(e);
			}
		});
		// Druck Passwörter
		jMenuDruckPwd.setText("Leh_Pwd");
		jMenuDruckPwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuDruckPwd_actionPerformed(e);
			}
		});
		// Hilfe
		jMenuHelp.setText("?");
		jMenuHelpAbout.setText("Info");
		jMenuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuHelpAbout_actionPerformed(e);
			}
		});
		// Hilfe
		jMenuHelphelp.setText("Hilfe");
		jMenuHelphelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuHelphelp_actionPerformed(e);
			}
		});
		jButton1.setIcon(image1);
		jButton1.setMnemonic('a');
		jButton1.setToolTipText("Datei öffnen 'alt + a' ");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton1_actionPerformed(e);
			}
		});
		jButton2.setIcon(image2);
		jButton2.setToolTipText("markierten Text einfügen");
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});
		jButton3.setIcon(image3);
		jButton3.setToolTipText("Eingebene Bücher anzeigen");
		jButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton3_actionPerformed(e);
			}
		});
		jButton4.setIcon(image4);
		jButton4.setToolTipText("Eingabe SB_Anzahl");
		jButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton4_actionPerformed(e);
			}
		});
		klassebtn.addActionListener(this);
		insertbtn.addActionListener(this);
		deletebtn.addActionListener(this);
		searchbtn.addActionListener(this);
		pwdaendernbtn.addActionListener(this);
		druckbtn.addActionListener(this);
		resetbtn.addActionListener(this);
		resetbtn.setToolTipText("Löschen der Eingabefelder: Nr.; Titel; Preis; Fach; Klasse");

		// Textbox Schuljahr
		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM schuljahr ORDER BY SJ");

			while (rs.next()) {
				sjstr = rs.getString(1);
				// System.out.print(sjstr);
				// int sjint = new Integer(sjstr).intValue();
			}
			schuljahrtxt.setText(sjstr);
			anmeldesjstr = sjstr;
		} catch (java.lang.Exception fehler) {
			fehler.printStackTrace();
		}

		schuljahrtxt.setMaximumSize(new Dimension(60, 25));
		schuljahrtxt.setEditable(false);
		schuljahrtxt.setBackground(Color.red);

		// ComboBox Klasse anzeigen
		klcb.addItemListener(this);
		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM klassen ORDER BY Klasse");
			while (rs.next()) {
				String klassestr = rs.getString(1);
				klcb.addItem(klassestr.toLowerCase());
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		klcb.setMaximumSize(new Dimension(60, 25));
		klcb.setMinimumSize(new Dimension(60, 25));
		klcb.setPreferredSize(new Dimension(60, 25));
		klcb.setToolTipText("Klasse anzeigen");
		klcb.setMaximumRowCount(20);

		preistxt.setMaximumSize(new Dimension(50, 25));
		preistxt.setMinimumSize(new Dimension(50, 25));
		preistxt.setPreferredSize(new Dimension(50, 25));
		// 27.2.2006 Anpassen von "scroller" an die Fenstergröße (screenSize)
		// siehe auch oben !
		scroller.setAutoscrolls(true);
		scroller.setPreferredSize(new Dimension(newscreenSizewidth - 124,
				newscreenSizeheight - 268));
		lehrertxt.setBackground(Color.yellow);
		lehrertxt.setFont(new java.awt.Font("SansSerif", 1, 12));
		lehrertxt.setMaximumSize(new Dimension(50, 25));
		lehrertxt.setMinimumSize(new Dimension(50, 25));
		lehrertxt.setPreferredSize(new Dimension(50, 25));
		klassecb1.setMaximumRowCount(20);
		fachcb.setMaximumRowCount(20);
		UStxt.setBackground(Color.lightGray);
		UStxt.setMaximumSize(new Dimension(40, 25));
		UStxt.setMinimumSize(new Dimension(40, 25));
		UStxt.setPreferredSize(new Dimension(40, 25));
		USrtxt.setBackground(Color.lightGray);
		USrtxt.setMaximumSize(new Dimension(40, 25));
		USrtxt.setMinimumSize(new Dimension(40, 25));
		USrtxt.setPreferredSize(new Dimension(40, 25));
		OSrtxt.setBackground(Color.lightGray);
		OSrtxt.setMaximumSize(new Dimension(40, 25));
		OSrtxt.setMinimumSize(new Dimension(40, 25));
		OSrtxt.setPreferredSize(new Dimension(40, 25));
		OStxt.setBackground(Color.lightGray);
		OStxt.setMaximumSize(new Dimension(40, 25));
		OStxt.setMinimumSize(new Dimension(40, 25));
		OStxt.setPreferredSize(new Dimension(40, 25));
		jToolBar.add(klcb);
		jToolBar.add(jButton1);
		jToolBar.add(jButton2);
		jToolBar.add(jButton3);
		jToolBar.add(jButton4);
		jToolBar.add(lehrerlbl);
		lehrerlbl.setText("angemeldet als:");
		lehrertxt.setEditable(false);
		lehrertxt.setText(lehrer);
		jToolBar.add(lehrertxt);

		// Textbox Lehrer
		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM tmp");
			while (rs.next()) {
				lehrer = rs.getString(2);
				lehrertxt.setText(lehrer);
			}
			String sqltmpdelete = "DELETE FROM tmp";
			stmt.execute(sqltmpdelete);
			con.close();
			stmt.close();
		} catch (java.lang.Exception fehler) {
			fehler.printStackTrace();
		}

		// Anlegen der Schüler-Anzahl Felder
		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM schueler");
			while (rs.next()) {
				us = rs.getInt(1);
				usr = rs.getInt(2);
				os = rs.getInt(3);
				osr = rs.getInt(4);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		jToolBar.add(USlbl);
		USlbl.setText("Schüler: US ");
		jToolBar.add(UStxt);
		UStxt.setText("" + us);
		int allow = lehrer.compareTo("ADM");
		if (allow == 0)
			UStxt.setEditable(true);
		else
			UStxt.setEditable(false);
		jToolBar.add(USrlbl);
		USrlbl.setText(" US-Rel: ");
		jToolBar.add(USrtxt);
		USrtxt.setText("" + usr);
		if (allow == 0)
			USrtxt.setEditable(true);
		else
			USrtxt.setEditable(false);
		jToolBar.add(OSlbl);
		OSlbl.setText(" OS: ");
		jToolBar.add(OStxt);
		OStxt.setText("" + os);
		if (allow == 0)
			OStxt.setEditable(true);
		else
			OStxt.setEditable(false);
		jToolBar.add(OSrlbl);
		OSrlbl.setText(" OS-Rel: ");
		OSrtxt.setEditable(false);
		jToolBar.add(OSrtxt);
		OSrtxt.setText("" + osr);
		if (allow == 0)
			OSrtxt.setEditable(true);
		else
			OSrtxt.setEditable(false);
		// Ende Anlegen der Schüler-Anzahl Felder
		contentPane.add(pane, BorderLayout.CENTER);
		// pane.add(scroller);
		jMenuFile.add(jMenuFileOpen);
		jMenuFile.add(jMenuFileRe);
		jMenuFile.add(jMenuPwd);
		jMenuFile.add(jMenuDelete);
		jMenuFile.add(jMenuDeleteLH);
		jMenuFile.add(jMenuanmeldenals);
		jMenuFile.add(jMenuFileExit);
		jMenuAnlegen.add(jMenuAnlegenFach);
		jMenuAnlegen.add(jMenuAnlegenKlasse);
		jMenuAnlegen.add(jMenuAnlegenLehrer);
		jMenuAnlegen.add(jMenuAnlegenSchueler);
		jMenuAnlegen.add(jMenuAnlegenSchuljahr);
		jMenuAnlegen.add(jMenuNeuSchuljahr);
		jMenuAnlegen.add(jMenuAnlegenLimits);
		jMenuDruck.add(jMenuDruckLehrer);
		jMenuDruck.add(jMenuDruckKlasse);
		jMenuDruck.add(jMenuDruckFach);
		jMenuDruck.add(jMenuDruckBuch);
		jMenuDruck.add(jMenuDruckKlaBuch);
		jMenuDruck.add(jMenuDruckPwd);
		jMenuHelp.add(jMenuHelphelp);
		jMenuHelp.add(jMenuHelpAbout);
		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(jMenuAnlegen);
		jMenuBar1.add(jMenuDruck);
		jMenuBar1.add(jMenuHelp);
		jMenuBar1.add(schuljahrtxt);
		this.setJMenuBar(jMenuBar1);
		pane.add(klassebtn);
		pane.add(nrlbl);
		pane.add(nrtxt);
		pane.add(titellbl);
		pane.add(titeltxt);
		pane.add(preislbl);
		pane.add(preistxt);
		pane.add(fachlbl);
		pane.add(fachcb);
		pane.add(klasselbl);
		pane.add(klassecb1);
		pane.add(insertbtn);
		pane.add(deletebtn);
		pane.add(searchbtn);
		pane.add(subpane, BorderLayout.EAST);
		subpane.add(scroller);
		// subpane.add(UeW);
		pane.add(UeW);
		pane.add(pwdaendernbtn);
		pane.add(druckbtn);
		pane.add(resetbtn);

		// ComboBox Fach
		fachcb.addItemListener(this);

		// Einfügen der Fächer in die ComboBox fachcb
		fachcb.addItem("");
		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM faecher ORDER BY Fach");
			while (rs.next()) {
				String fachstr = rs.getString(1);
				fachcb.addItem(fachstr);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		fachcb.setMaximumSize(new Dimension(70, 25));
		fachcb.setMinimumSize(new Dimension(70, 25));
		fachcb.setPreferredSize(new Dimension(70, 25));
		fachcb.setToolTipText("Fach auswählen");
		fachcb.setAutoscrolls(true);
		// ComboBox Klasse
		klassecb1.addItemListener(this);
		klassecb1.addItem("");

		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM klassen ORDER BY Klasse");
			while (rs.next()) {
				String klassestr = rs.getString(1);
				klassecb1.addItem(klassestr);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}

		klassecb1.setMaximumSize(new Dimension(60, 25));
		klassecb1.setMinimumSize(new Dimension(60, 25));
		klassecb1.setPreferredSize(new Dimension(60, 25));
		klassecb1.setToolTipText("Klasse auswählen");
		klassecb1.setAutoscrolls(true);
		contentPane.add(jToolBar, BorderLayout.NORTH);
	}

	// Eingabe SB_Anzahl
	public void jButton4_actionPerformed(ActionEvent e) {

		int allow = lehrer.compareTo("ADM");
		String text = "";

		if (allow == 0) {
			klassecb = (String) klcb.getSelectedItem();
			text = output.getText();
			int textlength = text.length();
			int textend = textlength;
			int textstart = 220; // alter WERT 218
			sjstr = schuljahrtxt.getText();
			new anzahl(klassecb, sjstr, text, textstart, textend);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
	}

	// Aktion Datei | Beenden durchgeführt
	public void jMenuFileExit_actionPerformed(ActionEvent e) {
		String sqldelete = "";
		try {
			String abmeldesjstr = anmeldesjstr;
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			sqldelete = "DELETE FROM kl_summe";
			stmt.executeUpdate(sqldelete);
			/*
			 * sqldelete =
			 * "DELETE FROM tmp WHERE lehrer = '"+lehrertxt.getText()+"'";
			 * stmt.executeUpdate(sqldelete);
			 */
			con.close();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}

	// Überschrieben, so dass eine Beendigung beim Schließen des Fensters
	// möglich ist.
	protected void processWindowEvent(WindowEvent e) {
		String sqldelete = "";

		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				String abmeldesjstr = anmeldesjstr;
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				sqldelete = "DELETE FROM kl_summe";
				stmt.executeUpdate(sqldelete);
				/*
				 * sqldelete =
				 * "DELETE FROM tmp WHERE lehrer = '"+lehrertxt.getText()+"'";
				 * stmt.executeUpdate(sqldelete);
				 */
				con.close();
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
			System.exit(0);
		}
	}

	// Aktion Hilfe | Hilfe durchgeführt
	public void jMenuHelphelp_actionPerformed(ActionEvent e) {
		JOptionPane
				.showMessageDialog(
						null,
						"1. SUCHE entweder unter Nr. (NUR ZIFFERNFOLGE) oder TITEL oder FACH\n"
								+ "Empfehlung: Suche mit Nr. (ohne Sonderzeichen wie *,., R, etc.)\n"
								+ "2. EINFÜGEN: markieren Sie die gewünschte Zeile des Buches\n\t und klicken sie auf das Symbol mit dem Hilfe Text 'markierten Text einfügen'\n\t "
								+ "dann das entsprechende Fach und die Klasse auswählen\n"
								+ "klicken Sie dann den Button EINFÜGEN\n\t "
								+ "bei der Abfrage Anzahl können Sie die Anzahl der wiederverwendeten Bücher zum eingefügten Buch angeben\n\t "
								+ "bei Bedarf können Sie das Lehrerhandexemplar einfügen\n\t "
								+ "Für Unterrichtsmittel eigener Wahl müssen Sie UeW anwählen\n"
								+ "3. LÖSCHEN: analoger Vorgang wie unter Punkt 2 - erste Zeile\n\n"
								+ "Sie können mit dem Button 'eingefügte Bücher anzeigen' Ihre Bücher anzeigen lassen\n\t "
								+ "und mit DRUCK auch ausdrucken.");
	}

	// Aktion Hilfe | Info durchgeführt
	public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
		sba_Infodialog dlg = new sba_Infodialog(this);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
				(frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		//\/\/\/\/\/\/ tg start
		//dlg.show(); changed with
		dlg.setVisible(true);
		//-------tg dlg.show(); end
	}


	// Aktion Datei öffnen | öffnen durchgeführt
	public void jMenuFileOpen_actionPerformed(ActionEvent e) {
		int allow = lehrer.compareTo("ADM");
		if (allow == 0) {
			open.setDirectory("C:/db_sba");
			open.getDirectory();
			//\/\/\/\/\/\/ tg start
			//open.show(); changed with
			open.setVisible(true);
			//-------tg dlg.show(); end

		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
	}


	// Rechnung
	public void jMenuFileRe_actionPerformed(ActionEvent e) {
		int allow = lehrer.compareTo("ADM");

		if (allow == 0) {
			String Tabb[] = new String[8];
			String length_tmp[] = new String[8];
			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				String sqldelete = "DELETE FROM kl_summe";
				stmt.executeUpdate(sqldelete);
				con.close();
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
			klassecb = (String) klcb.getSelectedItem();
			sjstr = schuljahrtxt.getText();
			new berechnen(klassecb, sjstr, lehrer);

			// anzeigen der berechneten Daten
			output.setText("");
			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM kl_summe");
				output.setText("Berechnung der eingegebenen Bücher\n \n");
				output.append("Klasse\tBW+SBX\t\t\tBW-SBX\t\t\tSBX\t\t\tRel.\t\t\tUeW\n \n");

				int num = 0;
				double ges = 0, ges_o_sbx = 0, ges_sbx = 0, ges_rel = 0, ges_uew = 0, conv = 0;
				double US = 0, US_o_sbx = 0, US_sbx = 0, US_rel = 0, US_uew = 0;
				double OS = 0, OS_o_sbx = 0, OS_sbx = 0, OS_rel = 0, OS_uew = 0;
				double limitges = 0, limitges_o_sbx = 0, limitges_sbx = 0, limitges_rel = 0;
				boolean us_kl = true;
				while (rs.next()) {
					String kltmp = rs.getString(1);

					boolean kltmp_int = kltmp.startsWith("5");
					if (kltmp_int == true && us_kl == true) {
						US = ges;
						US_o_sbx = ges_o_sbx;
						US_sbx = ges_sbx;
						US_rel = ges_rel;
						US_uew = ges_uew;
						us_kl = false;
					}

					double mitsbx = rs.getDouble(2);
					double ohnesbx = rs.getDouble(3);
					double sbx = rs.getDouble(4);
					double rel = rs.getDouble(5);
					double uew = rs.getDouble(6);
					ges = rs.getDouble(7);
					ges_o_sbx = rs.getDouble(8);
					ges_sbx = rs.getDouble(9);
					ges_rel = rs.getDouble(10);
					ges_uew = rs.getDouble(11);

					// Rundung
					double wert1 = mitsbx;
					int decimalPlace = 2;
					BigDecimal bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					mitsbx = bd.doubleValue();

					/*
					 * double wert1 = (mitsbx*1000)-((int)(mitsbx*1000)); if
					 * (wert1 < 0.5) mitsbx = mitsbx*1000; else mitsbx =
					 * (mitsbx*1000)+1; int werttmp = (int)mitsbx; mitsbx =
					 * (double)werttmp/1000;
					 */

					wert1 = ohnesbx;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					ohnesbx = bd.doubleValue();

					/*
					 * wert1 = (ohnesbx*1000)-((int)(ohnesbx*1000)); if (wert1 <
					 * 0.5) ohnesbx = ohnesbx*1000; else ohnesbx =
					 * (ohnesbx*1000)+1; werttmp = (int)ohnesbx; ohnesbx =
					 * (double)werttmp/1000;
					 */

					wert1 = sbx;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					sbx = bd.doubleValue();

					/*
					 * wert1 = (sbx*1000)-((int)(sbx*1000)); if (wert1 < 0.5)
					 * sbx = sbx*1000; else sbx = (sbx*1000)+1; werttmp =
					 * (int)sbx; sbx = (double)werttmp/1000;
					 */

					wert1 = rel;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					rel = bd.doubleValue();

					/*
					 * wert1 = (rel*1000)-((int)(rel*1000)); if (wert1 < 0.5)
					 * rel = rel*1000; else rel = (rel*1000)+1; werttmp =
					 * (int)rel; rel = (double)werttmp/1000;
					 */

					wert1 = uew;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					uew = bd.doubleValue();

					/*
					 * wert1 = (uew*1000)-((int)(uew*1000)); if (wert1 < 0.5)
					 * uew = uew*1000; else uew = (uew*1000)+1; werttmp =
					 * (int)uew; uew = (double)werttmp/1000;
					 */

					wert1 = ges;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					ges = bd.doubleValue();

					/*
					 * wert1 = (ges*1000)-((int)(ges*1000)); if (wert1 < 0.5)
					 * ges = ges*1000; else ges = (ges*1000)+1; werttmp =
					 * (int)ges; ges = (double)werttmp/1000;
					 */

					wert1 = ges_o_sbx;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					ges_o_sbx = bd.doubleValue();

					/*
					 * wert1 = (ges_o_sbx*1000)-((int)(ges_o_sbx*1000)); if
					 * (wert1 < 0.5) ges_o_sbx = ges_o_sbx*1000; else ges_o_sbx
					 * = (ges_o_sbx*1000)+1; werttmp = (int)ges_o_sbx; ges_o_sbx
					 * = (double)werttmp/1000;
					 */

					wert1 = ges_sbx;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					ges_sbx = bd.doubleValue();

					/*
					 * wert1 = (ges_sbx*1000)-((int)(ges_sbx*1000)); if (wert1 <
					 * 0.5) ges_sbx = ges_sbx*1000; else ges_sbx =
					 * (ges_sbx*1000)+1; werttmp = (int)ges_sbx; ges_sbx =
					 * (double)werttmp/1000;
					 */

					wert1 = ges_rel;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					ges_rel = bd.doubleValue();

					/*
					 * wert1 = (ges_rel*1000)-((int)(ges_rel*1000)); if (wert1 <
					 * 0.5) ges_rel = ges_rel*1000; else ges_rel =
					 * (ges_rel*1000)+1; werttmp = (int)ges_rel; ges_rel =
					 * (double)werttmp/1000;
					 */

					wert1 = US_uew;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					US_uew = bd.doubleValue();

					/*
					 * wert1 = (US_uew*1000)-((int)(US_uew*1000)); if (wert1 <
					 * 0.5) US_uew = US_uew*1000; else US_uew = (US_uew*1000)+1;
					 * werttmp = (int)US_uew; US_uew = (double)werttmp/1000;
					 */

					wert1 = ges_uew;
					decimalPlace = 2;
					bd = new BigDecimal(wert1);
					bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
					ges_uew = bd.doubleValue();

					/*
					 * wert1 = (ges_uew*1000)-((int)(ges_uew*1000)); if (wert1 <
					 * 0.5) ges_uew = ges_uew*1000; else ges_uew =
					 * (ges_uew*1000)+1; werttmp = (int)ges_uew; ges_uew =
					 * (double)werttmp/1000;
					 */

					output.append(kltmp + "\t" + mitsbx + "\t\t\t" + ohnesbx
							+ "\t\t\t" + sbx + "\t\t\t" + rel + "\t\t\t" + uew
							+ "\n");
				}
				for (int ii = 0; ii < 4; ii++) {
					length_tmp[0] = Double.toString(US);
					length_tmp[1] = Double.toString(US_o_sbx);
					length_tmp[2] = Double.toString(US_sbx);
					length_tmp[3] = Double.toString(US_rel);
					int length_tmp_int = length_tmp[ii].length();
					if (length_tmp_int > 7)
						Tabb[ii] = "\t\t";
					else
						Tabb[ii] = "\t\t\t";
				}
				output.append("\n \nUS:" + "\t" + US + Tabb[0] + US_o_sbx
						+ Tabb[1] + US_sbx + Tabb[2] + US_rel + Tabb[3]
						+ US_uew + "\n");
				OS = ges - US;
				OS_o_sbx = ges_o_sbx - US_o_sbx;
				OS_sbx = ges_sbx - US_sbx;
				OS_rel = ges_rel - US_rel;
				OS_uew = ges_uew - US_uew;

				double wert1 = OS;
				int decimalPlace = 2;
				BigDecimal bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				OS = bd.doubleValue();

				/*
				 * double wert1 = (OS*1000)-((int)(OS*1000)); if (wert1 < 0.5)
				 * OS = OS*1000; else OS = (OS*1000)+1; int werttmp = (int)OS;
				 * OS = (double)werttmp/1000;
				 */

				wert1 = OS_o_sbx;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				OS_o_sbx = bd.doubleValue();

				/*
				 * wert1 = (OS_o_sbx*1000)-((int)(OS_o_sbx*1000)); if (wert1 <
				 * 0.5) OS_o_sbx = OS_o_sbx*1000; else OS_o_sbx =
				 * (OS_o_sbx*1000)+1; werttmp = (int)OS_o_sbx; OS_o_sbx =
				 * (double)werttmp/1000;
				 */

				wert1 = OS_sbx;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				OS_sbx = bd.doubleValue();

				/*
				 * wert1 = (OS_sbx*1000)-((int)(OS_sbx*1000)); if (wert1 < 0.5)
				 * OS_sbx = OS_sbx*1000; else OS_sbx = (OS_sbx*1000)+1; werttmp
				 * = (int)OS_sbx; OS_sbx = (double)werttmp/1000;
				 */

				wert1 = OS_rel;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				OS_rel = bd.doubleValue();

				/*
				 * wert1 = (OS_rel*1000)-((int)(OS_rel*1000)); if (wert1 < 0.5)
				 * OS_rel = OS_rel*1000; else OS_rel = (OS_rel*1000)+1; werttmp
				 * = (int)OS_rel; OS_rel = (double)werttmp/1000;
				 */

				wert1 = OS_uew;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				OS_uew = bd.doubleValue();

				/*
				 * wert1 = (OS_uew*1000)-((int)(OS_uew*1000)); if (wert1 < 0.5)
				 * OS_uew = OS_uew*1000; else OS_uew = (OS_uew*1000)+1; werttmp
				 * = (int)OS_uew; OS_uew = (double)werttmp/1000;
				 */

				// Limits berechnen
				int us, usr, os, osr;
				double limitus = 0, limitus_sbx = 0, limitus_rel = 0, limitos = 0, limitos_sbx = 0, limitos_rel = 0, limit_sbx = 0, limit_uew = 0;
				String tmp = "";

				tmp = UStxt.getText();
				us = Integer.parseInt(tmp);
				tmp = USrtxt.getText();
				usr = Integer.parseInt(tmp);
				tmp = OStxt.getText();
				os = Integer.parseInt(tmp);
				tmp = OSrtxt.getText();
				osr = Integer.parseInt(tmp);

				try {
					ConnectDB conobj2 = new ConnectDB();
					Connection con2 = conobj2.getDBconnection();
					stmt = con2.createStatement();
					rs = stmt.executeQuery("SELECT * FROM limits_gesamt");
					while (rs.next()) {
						limitus = rs.getDouble(1);
						limitus_sbx = rs.getDouble(2);
						limitus_rel = rs.getDouble(3);
						limitos = rs.getDouble(5);
						limitos_sbx = rs.getDouble(6);
						limitos_rel = rs.getDouble(7);
					}
				} catch (Exception event) {
					event.printStackTrace();
				}
				limitges_sbx = limitus_sbx * us + limitos_sbx * os;
				limitges = limitus * us + limitos * os;
				limit_sbx = limitges_sbx * 0.045;
				limitges_rel = (limitus_rel/* +limitus)*0.115 */* usr)
						+ (limitos_rel/* +limitos)*0.075 */* osr);
				limit_uew = limitges * 0.15; // Ende Limits Berechnung

				wert1 = limitges_sbx;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				limitges_sbx = bd.doubleValue();

				/*
				 * wert1 = (limitges_sbx*1000)-((int)(limitges_sbx*1000)); if
				 * (wert1 < 0.5) limitges_sbx = limitges_sbx*1000; else
				 * limitges_sbx = (limitges_sbx*1000)+1; werttmp =
				 * (int)limitges_sbx; limitges_sbx = (double)werttmp/1000;
				 */

				wert1 = limitges;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				limitges = bd.doubleValue();

				/*
				 * wert1 = (limitges*1000)-((int)(limitges*1000)); if (wert1 <
				 * 0.5) limitges = limitges*1000; else limitges =
				 * (limitges*1000)+1; werttmp = (int)limitges; limitges =
				 * (double)werttmp/1000;
				 */

				wert1 = limit_sbx;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				limit_sbx = bd.doubleValue();

				/*
				 * wert1 = (limit_sbx*1000)-((int)(limit_sbx*1000)); if (wert1 <
				 * 0.5) limit_sbx = limit_sbx*1000; else limit_sbx =
				 * (limit_sbx*1000)+1; werttmp = (int)limit_sbx; limit_sbx =
				 * (double)werttmp/1000;
				 */

				wert1 = limitges_rel;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				limitges_rel = bd.doubleValue();

				/*
				 * wert1 = (limitges_rel*1000)-((int)(limitges_rel*1000)); if
				 * (wert1 < 0.5) limitges_rel = limitges_rel*1000; else
				 * limitges_rel = (limitges_rel*1000)+1; werttmp =
				 * (int)limitges_rel; limitges_rel = (double)werttmp/1000;
				 */

				wert1 = limit_uew;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				limit_uew = bd.doubleValue();

				/*
				 * wert1 = (limit_uew*1000)-((int)(limit_uew*1000)); if (wert1 <
				 * 0.5) limit_uew = limit_uew*1000; else limit_uew =
				 * (limit_uew*1000)+1; werttmp = (int)limit_uew; limit_uew =
				 * (double)werttmp/1000;
				 */

				for (int ii = 0; ii < 8; ii++) {
					length_tmp[0] = Double.toString(OS);
					length_tmp[1] = Double.toString(OS_o_sbx);
					length_tmp[2] = Double.toString(OS_sbx);
					length_tmp[3] = Double.toString(OS_rel);
					length_tmp[4] = Double.toString(ges);
					length_tmp[5] = Double.toString(ges_o_sbx);
					length_tmp[6] = Double.toString(ges_sbx);
					length_tmp[7] = Double.toString(ges_rel);
					int length_tmp_int = length_tmp[ii].length();
					if (length_tmp_int > 7)
						Tabb[ii] = "\t\t";
					else
						Tabb[ii] = "\t\t\t";
				}

				output.append("OS:" + "\t" + OS + Tabb[0] + OS_o_sbx + Tabb[1]
						+ OS_sbx + Tabb[2] + OS_rel + Tabb[3] + OS_uew + "\n");
				output.append("GES.:" + "\t" + ges + Tabb[4] + ges_o_sbx
						+ Tabb[5] + ges_sbx + Tabb[6] + ges_rel + Tabb[7]
						+ ges_uew + "\n");
				output.append("Limit:" + "\t" + (int) limitges_sbx + "\t\t\t"
						+ (int) limitges + "\t\t\t" + (int) limit_sbx
						+ "\t\t\t" + (int) limitges_rel + "\t\t\t"
						+ (int) limit_uew + "\n");
				output.append("Diff.:" + "\t" + (int) (limitges_sbx - ges)
						+ "\t\t\t" + (int) (limitges - ges_o_sbx) + "\t\t\t"
						+ (int) (limit_sbx - ges_sbx) + "\t\t\t"
						+ (int) (limitges_rel - ges_rel) + "\t\t\t"
						+ (int) (limit_uew - ges_uew) + "\n");
				output.append("\nGesamt-Differenz: "
						+ (int) (limitges - ges - ges_uew));
				con.close();
			} catch (Exception exep) {
				exep.printStackTrace();
			}
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
	}

	// Aktion Password
	public void jMenuPwd_actionPerformed(ActionEvent e) {

		int i = 0;
		int id[] = new int[100];
		String leh[] = new String[100];
		String lehpwd[] = new String[100];

		int allow = lehrer.compareTo("ADM");
		if (allow != 0)
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
		else {
			int response0 = JOptionPane.showConfirmDialog(null,
					"Password anzeigen ?", "Anzeigen der Passwörter !",
					JOptionPane.YES_NO_OPTION);
			if (response0 == 0) {
				output.setText("");
				try {
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt
							.executeQuery("SELECT * FROM lehrer ORDER BY Lehrer");
					while (rs.next()) {
						id[i] = rs.getInt(1);
						leh[i] = rs.getString(2);
						lehpwd[i] = rs.getString(3);
						output.append(id[i] + "\t" + leh[i] + "\t" + lehpwd[i]
								+ "\n");
						i++;
					}
				} catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}
			}
			int response = JOptionPane.showConfirmDialog(null,
					"Password neu vergeben ?", "Neuvergabe Password",
					JOptionPane.YES_NO_OPTION);
			if (response == 0) {
				sjstr = schuljahrtxt.getText();
				new pwd(sjstr);
			}
		}
	}

	// Aktion Delete - Löschen aller Datensätze der Tabellen
	public void jMenuDelete_actionPerformed(ActionEvent e) {

		boolean loeschen = false;

		int allow = lehrer.compareTo("ADM");
		if (allow != 0)
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
		else {
			int response = JOptionPane.showConfirmDialog(null,
					"Sollen wirklich alle Datensätze gelöscht werden ?",
					"Daten Löschen", JOptionPane.YES_NO_CANCEL_OPTION);
			if (response == 1) {
				loeschen = false;
				JOptionPane.showMessageDialog(null,
						"Aktion nicht durchgeführt !");
			} else if (response == 0) {
				int klcb_row = klcb.getItemCount();
				int j;
				try {
					sjstr = schuljahrtxt.getText();
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					String Datenloeschen = "";
					for (j = 0; j < klcb_row; j++) {
						klcb.setSelectedIndex(j);
						klassecb = (String) klcb.getSelectedItem();
						Datenloeschen = "DELETE FROM " + klassecb;
						stmt.executeUpdate(Datenloeschen);
					}
					JOptionPane.showMessageDialog(null,
							"Alle Datensätze wurden gelöscht !");
				} catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}
			} // Ende else if (response == 0)
		} // Ende else
	}

	// Aktion DeleteLH - Löschen gewisser Datensätze in Lehrerhandexemplar
	public void jMenuDeleteLH_actionPerformed(ActionEvent e) {

		boolean loeschen = false;

		int allow = lehrer.compareTo("ADM");
		if (allow != 0)
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
		else {
			int response = JOptionPane
					.showConfirmDialog(
							null,
							"Sollen Datensätze in LH gelöscht werden, die älter als 4 Jahre sind ?",
							"Daten Löschen", JOptionPane.YES_NO_CANCEL_OPTION);
			if (response == 1) {
				loeschen = false;
				JOptionPane.showMessageDialog(null,
						"Aktion nicht durchgeführt !");
			} else if (response == 0) {
				try {
					sjstr = schuljahrtxt.getText();
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					String LHcount = "";
					String LHloeschen = "";
					int sjint = Integer.parseInt(sjstr);
					sjint = sjint - 4;
					int sjint_tmp = sjint - 2000;
					LHloeschen = "DELETE FROM lehrerhand where Jahr < "
							+ sjint_tmp;
					stmt.executeUpdate(LHloeschen);
					umbenennen("LehrerHand");
					JOptionPane.showMessageDialog(null, "Datensätze < " + sjint
							+ " wurden gelöscht !");
				} catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}
			} // Ende else if (response == 0)
		} // Ende else
	}

	// Aktion Anmelden als ... (anderer Lehrer)
	public void jMenuanmeldenals_actionPerformed(ActionEvent e) {
		sjstr = schuljahrtxt.getText();
		new anmeldungneu(sjstr);
	}

	// Fortsetzung von Aktion Anmelden als ... - fügt lehrerneu ein!
	public void einfuegen(String lehrerneu) {
		lehrertxt.setEditable(true);
		lehrertxt.setText(lehrerneu);
		lehrertxt.setEditable(false);
		new sba();
	}

	// Aktion Datei öffnen2 | öffnen2 durchgeführt
	public void jButton1_actionPerformed(ActionEvent e) {
		int allow = lehrer.compareTo("ADM");
		if (allow == 0) {
			open.setDirectory("C:/db_sba");
			open.getDirectory();
			//\/\/\/\/\/\/ tg start
			//open.show(); changed with
			open.setVisible(true);
			//-------tg dlg.show(); end

		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung !");
	}


	// Aktion markierten Text einfügen
	public void jButton2_actionPerformed(ActionEvent e) {
		StringTokenizer auswahl;

		int tmp = output.getSelectionStart();
		int tmp1 = output.getSelectionEnd();
		if (tmp == tmp1)
			JOptionPane
					.showMessageDialog(null,
							"Sie müssen zuerst den Text \"Nr, Kurztitel, Preis\" markieren !");
		else {
			String texttmp = output.getSelectedText();
			auswahl = new StringTokenizer(texttmp, "\t");
			int anzahl_Tokens = auswahl.countTokens();
			if (anzahl_Tokens < 3)
				JOptionPane.showMessageDialog(null,
						"Bitte Nr., Kurztitel, Preis markieren !");
			else {
				String x = auswahl.nextToken();
				nrtxt.setText(x);
				String y = auswahl.nextToken();
				titeltxt.setText(y);
				String z = auswahl.nextToken();
				preistxt.setText(z);
			}
		}
	}

	// Aktion Eingebene Bücher anzeigen
	public void jButton3_actionPerformed(ActionEvent e) {

		String lehrer_anm = lehrertxt.getText();
		int anmeldung = lehrer_anm.compareTo("");
		if (anmeldung == 0)
			JOptionPane.showMessageDialog(null,
					"Sie müssen sich zuerst anmelden !");
		else {

			int klcb_row = klcb.getItemCount();
			int j, a = 0, b = 0, c = 0, d = 0, v = 0, x = 0, count_buch = 0;
			String klassecbtmp[] = new String[50];
			String nrtmp[] = new String[50];
			String titeltmp[] = new String[50];
			String fachtmp[] = new String[50];
			double preistmp[] = new double[50];
			int wiedertmp[] = new int[50];
			int anz_LH = 0, aa = 0, ab = 0, ac = 0, ad = 0;// Variablen für
															// Lehrer_Hand
			String nrLHtmp[] = new String[20];
			String titelLHtmp[] = new String[20];
			String fachLHtmp[] = new String[20];
			// String klLHtmp[] = new String[20];
			String Tab1 = "", TabNr = "", lz = " ";

			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();

				for (j = 0; j < klcb_row; j++) {
					klcb.setSelectedIndex(j);
					klassecb = (String) klcb.getSelectedItem();
					ResultSet rs = stmt.executeQuery("SELECT * FROM "
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
						count_buch++;
					}
				}// Ende for Schleife

				// Anzahl der gesamten LehrerHand-Exemplare bestimmen und
				// speichern
				ResultSet k = stmt
						.executeQuery("SELECT * FROM lehrerhand WHERE Lehrer = '"
								+ lehrer
								+ "' AND Jahr = "
								+ (Integer.parseInt(sjstr) - 2000)
								+ " ORDER BY Fach");
				while (k.next()) {
					nrLHtmp[aa] = k.getString(2);
					titelLHtmp[ab] = k.getString(3);
					fachLHtmp[ac] = k.getString(4);
					// klLHtmp[ad] = k.getString(5);
					aa++;
					ab++;
					ac++;// ad++;
					anz_LH++;
				}
				stmt.close();
				con.close();
			} catch (Exception exeption) {
				exeption.printStackTrace();
			}
			JOptionPane.showMessageDialog(null,
					"Suche für eingegebene Bücher für " + lehrer
							+ " durchgeführt !");

			a = 0;
			b = 0;
			c = 0;
			d = 0;
			v = 0;
			x = 0;
			output.setText("Eingegebene Bücher von " + lehrer + (char) lf + lz
					+ (char) lf);
			output.append("Nr.\t\tKurztitel d. Buches\t\t\tPreis\tFach\tW\tKlasse\n"
					+ "--------------------------------------------------------------------------------------------------------------------\n");
			for (a = 0; a < count_buch; a++) {
				int nrtmp_lang = nrtmp[a].length();
				if (nrtmp_lang < 8)
					TabNr = "\t\t";
				else
					TabNr = "\t";
				int titeltmp_lang = titeltmp[b].length();
				titeltmp_lang = 39 - titeltmp_lang;
				if (titeltmp_lang > 31)
					Tab1 = "\t\t\t\t\t";
				else if (titeltmp_lang > 23 && titeltmp_lang <= 31)
					Tab1 = "\t\t\t\t";
				else if (titeltmp_lang > 15 && titeltmp_lang <= 23)
					Tab1 = "\t\t\t";
				else if (titeltmp_lang > 7 && titeltmp_lang <= 15)
					Tab1 = "\t\t";
				else if (titeltmp_lang <= 7)
					Tab1 = "\t";
				output.append(nrtmp[a] + TabNr + titeltmp[b] + Tab1
						+ preistmp[c] + "\t" + fachtmp[d] + "\t" + wiedertmp[v]
						+ "\t" + klassecbtmp[x] + "\n");
				b++;
				c++;
				d++;
				v++;
				x++;
			}
			output.append("\n \nAnzahl der eingegebenen Bücher: " + count_buch
					+ "\n \n");

			output.append("Eingegebene Lehrer-Handexemplare von " + lehrer
					+ "\n \n");
			output.append("Nr.\t\tKurztitel d. Buches\t\t\tFach\n"
					+ "--------------------------------------------------------------------------------------------------------------------\n");
			aa = 0;
			ab = 0;
			ac = 0;
			for (ad = 0; ad < anz_LH; ad++) {
				int nrtmpLH_lang = nrLHtmp[aa].length();
				if (nrtmpLH_lang < 8)
					TabNr = "\t\t";
				else
					TabNr = "\t";
				int titelLH_lang = titelLHtmp[ac].length();
				titelLH_lang = 39 - titelLH_lang;
				if (titelLH_lang > 31)
					Tab1 = "\t\t\t\t\t";
				else if (titelLH_lang > 23 && titelLH_lang <= 31)
					Tab1 = "\t\t\t\t";
				else if (titelLH_lang > 15 && titelLH_lang <= 23)
					Tab1 = "\t\t\t";
				else if (titelLH_lang > 7 && titelLH_lang <= 15)
					Tab1 = "\t\t";
				else if (titelLH_lang <= 7)
					Tab1 = "\t";
				output.append(nrLHtmp[aa] + TabNr + titelLHtmp[ab] + Tab1
						+ fachLHtmp[ac] + "\t" + "\n");
				aa++;
				ab++;
				ac++;
			}
			output.append("\n \nAnzahl der Lehrer-Hand-Exemplare: " + anz_LH);
		}
	}

	/*
	 * //angemeldeten Lehrer aus der tmp-Datei lesen public void lehrer_a() {
	 * try { sjstr=schuljahrtxt.getText(); String url =
	 * "jdbc:mysql://server:3306/sba"+sjstr;
	 * Class.forName("com.mysql.jdbc.Driver"); Connection con =
	 * DriverManager.getConnection(url,"lehrer","xxxxx"); Statement stmt =
	 * con.createStatement(); ResultSet rs =
	 * stmt.executeQuery("SELECT * FROM tmp"); while (rs.next()) { lehrer =
	 * rs.getString(2); lehrer = lehrer.toUpperCase();
	 * lehrertxt.setText(lehrer); } rs.close(); stmt.close(); con.close(); }
	 * catch (Exception e) { e.printStackTrace(); } }
	 */
	// Klassen anzeigen
	public void anzeigen(String klassecb) {
		int lang0 = 0;
		int buch_count = 0; // Anzahl der bestellen Bücher bzgl. Klasse
		double htmp = 0;// Variable um die Klassen-Bestellsumme zu berechnen u.
						// ausgeben
		double rtmp = 0;// Variable um die R-Bestellsumme zu berechnen u.
						// ausgegeben
		double xtmp = 0;// Variable um den SBX-Bestellwert zu berechnen
		double uewtmp = 0;// Variable um den UeW-Bestellwert zu berechnen
		String Tab = "\t", Tab1 = "\t", Tab2 = "\t", Tab3 = "\t", TabNr = "";

		klassecb = (String) klcb.getSelectedItem();

		lang0 = klassecb.length();
		if (lang0 < 1) {
			output.setText("Bitte eine Klasse eingeben");
		} else {
			// lehrer_a();

			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				klassestr = klassecb;
				ResultSet rs = stmt.executeQuery("SELECT * FROM " + klassestr
						+ " ORDER BY Fach");
				output.setText("Das Ergebnis der Abfrage für die "
						+ klassestr
						+ " Klasse:\nNr.\t\tKurztitel d. Buches\t\t\tPreis €\tFach\tAnz.\tLehrer\tWert\tW\n"
						+ "--------------------------------------------------------------------------------------------------------------------\n");
				while (rs.next()) {
					String b = rs.getString(2);
					int Nr_laenge = b.length();
					if (Nr_laenge >= 8)
						TabNr = "\t";
					else
						TabNr = "\t\t";
					int bl = b.length();
					bl = 7 - bl;
					if (bl > 0)
						Tab = "\t";
					String c = rs.getString(3);
					int cl = c.length();
					if (cl > 39)
						c = c.substring(0, 39);
					cl = 39 - cl;

					if (cl > 31)
						Tab1 = "\t\t\t\t\t";
					else if (cl > 23 && cl <= 31)
						Tab1 = "\t\t\t\t";
					else if (cl > 15 && cl <= 23)
						Tab1 = "\t\t\t";
					else if (cl > 7 && cl <= 15)
						Tab1 = "\t\t";
					else if (cl <= 7)
						Tab1 = "\t";

					String d = rs.getString(4);
					String e = rs.getString(5);
					String f = rs.getString(6);
					String g = rs.getString(7);
					double h = rs.getDouble(8);
					String i = rs.getString(9);
					output.append(b + TabNr + c + Tab1 + d + Tab2 + e + Tab3
							+ f + "\t" + g + "\t" + h + "\t" + i + "\n");
					int fach_rk = -1;
					int fach_re = -1;
					boolean fach_x = false;
					boolean fach_uew = c.startsWith("UeW");
					if (fach_uew == false) {
						fach_rk = e.compareTo("RK");
						fach_re = e.compareTo("RE");
						if (fach_rk != 0) {
							boolean fach_rk_tmp = e.startsWith("X-R");
							boolean fach_rk_tmp1 = e.startsWith("XK-R");
							if (fach_rk_tmp == true || fach_rk_tmp1 == true)
								fach_rk = 0;
						}
						fach_x = false;
						if (fach_rk != 0) {
							fach_x = e.startsWith("X");
						}
					}

					if (fach_rk == 0 || fach_re == 0)
						rtmp = rtmp + h;
					else if (fach_x == true)
						xtmp = xtmp + h;
					else if (fach_uew == true)
						uewtmp = uewtmp + h;
					else
						htmp = htmp + h;
					buch_count++;
				}

				double wert1 = rtmp;
				int decimalPlace = 2;
				BigDecimal bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				rtmp = bd.doubleValue();

				/*
				 * double wert1 = (rtmp*1000)-((int)(rtmp*1000)); if (wert1 <
				 * 0.5) rtmp = rtmp*1000; else rtmp = (rtmp*1000)+1; int werttmp
				 * = (int)rtmp; rtmp = (double)werttmp/1000;
				 */

				wert1 = xtmp;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				xtmp = bd.doubleValue();

				/*
				 * wert1 = (xtmp*1000)-((int)(xtmp*1000)); if (wert1 < 0.5) xtmp
				 * = xtmp*1000; else xtmp = (xtmp*1000)+1; werttmp = (int)xtmp;
				 * xtmp = (double)werttmp/1000;
				 */

				wert1 = uewtmp;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				uewtmp = bd.doubleValue();

				/*
				 * wert1 = (uewtmp*1000)-((int)(uewtmp*1000)); if (wert1 < 0.5)
				 * uewtmp = uewtmp*1000; else uewtmp = (uewtmp*1000)+1; werttmp
				 * = (int)uewtmp; uewtmp = (double)werttmp/1000;
				 */

				wert1 = htmp;
				decimalPlace = 2;
				bd = new BigDecimal(wert1);
				bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
				htmp = bd.doubleValue();

				/*
				 * wert1 = (htmp*1000)-((int)(htmp*1000)); if (wert1 < 0.5) htmp
				 * = htmp*1000; else htmp = (htmp*1000)+1; werttmp = (int)htmp;
				 * htmp = (double)werttmp/1000;
				 */

				output.append("\n \n");
				output.append("Anzahl der bestellten Bücher: " + buch_count
						+ "\n \n");
				double gtmp = htmp + xtmp;
				output.append("Bestellwert der Klasse (mit SBX): "
						+ gtmp
						+ " €\nBestellwert der Klasse (ohne SBX): "
						+ htmp
						+ " €\nBestellwert SBX: "
						+ xtmp
						+ " €\nBestellwert für Religion (+SBX bzw. SBX-Kombi): "
						+ rtmp + " €\nBestellwert UeW: " + uewtmp + " €\n");
				rs.close();
				stmt.close();
				con.close();
				klcb.getItemAt(-1);
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/*
	 * -------------------------------------------------------------------
	 * Einfügen eines Buches über "Eingabe" Uew; SBX; SBXK = SBX-Kombi; Anhang
	 * -> LH nein Einfügen von SBX ermöglichen
	 * ---------------------------------------------------------------------
	 */
	public void eingabe(String lehrer) {
		int lang0, lang1, lang2, lang3, lang4, lang5, count = 1;
		String sql = "";
		String anhang_liste = "";
		int anhang_liste_ja = 0;
		int anhang_liste_sbx = -1;
		int anhang_liste_sbxk = -1; // statt titelsbx = -1
		int titelsbx = -1;

		boolean gefunden = false;
		boolean Buch_gefunden = false;
		boolean vorhanden = false; // LehrHand Abfrage
		boolean uew = false;
		boolean sbx = false;
		boolean sucheListe = false;

		String leer1 = nrtxt.getText();
		lang1 = leer1.length();
		String leer2 = titeltxt.getText();
		lang2 = leer2.length();
		String leer3 = preistxt.getText();
		lang3 = leer3.length();
		String leer4 = fachtxt;
		lang4 = leer4.length();
		String leer5 = klassetxt;
		lang5 = leer5.length();
		String titelinuew = "";

		uew = UeW.getState();
		boolean preisgueltig = false;
		boolean nrgueltig = false;
		if (lang1 < 1 || lang2 < 1 || lang3 < 1 || lang4 < 1 || lang5 < 2) {
			JOptionPane
					.showMessageDialog(
							null,
							"Bitte Nummer (nur ZIFFERNFOLGE), Titel, Preis, Fach und Klasse (z.B. 1a) eingeben");
			UeW.setState(false);
		} else { // else-Zweig 1
			if (uew == true) {
				String nrinuew = nrtxt.getText();
				char[] nrinpruefen = nrinuew.toCharArray();
				int nrinuew_lang = nrinuew.length();
				if (nrinuew_lang > 13) {
					JOptionPane
							.showMessageDialog(null,
									"Es können max. 13 Ziffern eingegeben werden (978...können eingeben werden) !");
					nrtxt.setText("");
					nrgueltig = false;
				} else {
					while (nrinuew_lang > 0) {
						for (int j = 0; j < nrinuew_lang; j++) {
							if (nrinpruefen[j] < 59 && nrinpruefen[j] > 47)
								nrgueltig = true;
							else {
								JOptionPane
										.showMessageDialog(
												null,
												"Die eingegebene Nummer (ISBN - nur ZIFFERNFOLGE) darf maximal aus 13 Ziffern zwischen 0 und 9 bestehen! (978...kann eingeben werden)\n");
								nrinuew_lang = 0;
								nrtxt.setText("");
								nrgueltig = false;
							}
						}
						nrinuew_lang--;
					}
				}
				String preisinuew = preistxt.getText();
				char[] preispruefen = preisinuew.toCharArray();
				int preispruefen_lang = preisinuew.length();
				while (preispruefen_lang > 0) {
					for (int i = 0; i < preispruefen_lang; i++) {
						if ((preispruefen[i] < 59 && preispruefen[i] > 47)
								|| preispruefen[i] == 46)
							preisgueltig = true;
						else {
							JOptionPane
									.showMessageDialog(
											null,
											"Eingegebenes Format für den Preis ist falsch!\nVerwenden Sie nur Zahlen bzw. den 'Punkt' als Komma!");
							preispruefen_lang = 0;
							preistxt.setText("");
							preisgueltig = false;
						}
					}
					preispruefen_lang--;
				}
				if (preisgueltig == false || nrgueltig == false) {
					// JOptionPane.showMessageDialog(null,"Die eingegebene Nummer (ISBN) darf maximal aus 10 Zeichen zwischen 0 und 9 bestehen!\nGeben Sie nur die Ziffern ein.\nODER\nEingegebenes Format für den Preis ist falsch!\nVerwenden Sie nur Zahlen bzw. den 'Punkt' als Komma!"
					// );
					uew = false;
				} else {
					titelinuew = titeltxt.getText();
					boolean uewalt = titelinuew.startsWith("UeW");
					if (uewalt == true)
						titelinuew = titelinuew.substring(4);
					int lastIndexuew = titelinuew.length();
					if (lastIndexuew > 35)
						lastIndexuew = 35;
					titelinuew = titelinuew.substring(0, lastIndexuew);
					sucheListe = true;
				}
			} else {
				try {
					sjstr = schuljahrtxt.getText();
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					String nrinliste = nrtxt.getText();
					String titelinliste = titeltxt.getText();
					String preisinliste = preistxt.getText();

					// Hier muss wegen der mysql Datenbank der Preis in der
					// Anzeige von z.B. 3.0 auf 3 verändert werden bzw. 4.09
					// erhalten bleiben!!
					int kommanull = preisinliste.indexOf(".0");
					if (kommanull > -1) {
						int kommaint = preisinliste.indexOf(".");
						int last = preisinliste.length();
						String kommatest = preisinliste.substring(kommaint,
								last);
						int kommatestint = kommatest.compareTo(".0");
						if (kommatestint == 0)
							preisinliste = preisinliste.substring(0, kommaint);
					}
					// Ende der Preiskonvertierung für die mysql Datenbank -
					// unsympathisch !!

					ResultSet listetmp = stmt
							.executeQuery("SELECT * FROM liste");
					while (listetmp.next()) {
						String nrinlistetest = listetmp.getString(2);
						int nrinlistetest_zahl = nrinliste
								.compareTo(nrinlistetest);
						String titelinlistetest = listetmp.getString(3);
						int lastIndex = titelinlistetest.length();
						if (lastIndex > 39)
							lastIndex = 39;
						titelinlistetest = titelinlistetest.substring(0,
								lastIndex);
						int titelinlistetest_zahl = titelinliste
								.compareTo(titelinlistetest);
						String preisinlistetest = listetmp.getString(4);
						int preisinlistetest_zahl = preisinliste
								.compareTo(preisinlistetest);
						if (nrinlistetest_zahl == 0
								&& titelinlistetest_zahl == 0
								&& preisinlistetest_zahl == 0) {
							sucheListe = true;
							anhang_liste = listetmp.getString("Anhang");
							anhang_liste_ja = anhang_liste.compareTo("ja");
							// Abfrage ob SBX
							anhang_liste_sbx = anhang_liste.compareTo("SBX");
							anhang_liste_sbxk = anhang_liste.compareTo("SBXK");
							// hier ist nocht die alte Version (falls ja:
							// titelsbx = -1; sonst entsprechender indexOf)
							String titelinlistetest_lc = titelinlistetest
									.toLowerCase();
							titelsbx = titelinlistetest_lc.indexOf("sbx");
							// Ende Abfrage ob SBX
						}
					}
				} catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}
			}
			if (sucheListe == false) {
				JOptionPane
						.showMessageDialog(
								null,
								"Das angegebene Buch kann nicht eingegeben werden, da es nicht in der Schulbuchliste gefunden wurde !\nODER\nEs wurde bei UeW mindestens ein falsches Format verwendet!");
			} else {
				try {
					sjstr = schuljahrtxt.getText();
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					String nrin = nrtxt.getText();
					String titelin = titeltxt.getText();
					String preisin = preistxt.getText();
					double preisfl = Double.parseDouble(preisin);
					String fachin = fachtxt;
					// neue Abfrag (17.1.08) ob ein SBX Buch vorliegt, um das
					// Fach aufjedenfall mit X zu merken - Rechnung!!
					if (anhang_liste_sbx == 0)
						fachin = "X-" + fachin;
					if (anhang_liste_sbxk == 0)
						fachin = "XK-" + fachin;
					String klassein = klassetxt;
					int lehrertest_zahl_0 = 0;
					ResultSet test = stmt.executeQuery("SELECT * FROM "
							+ klassein + " ORDER BY Fach");
					while (test.next()) {
						String nrtest = test.getString("Nr");
						String lehrertest = test.getString("Lehrer");
						int nrtest_zahl = nrin.compareTo(nrtest);
						lehrertest_zahl_0 = lehrer.compareTo(lehrertest);
						if (nrtest_zahl == 0 && lehrertest_zahl_0 == 0)
							gefunden = true;
						if (nrtest_zahl == 0)
							Buch_gefunden = true;
						count++;
					}
					boolean sbx_pruefen = fachin.startsWith("X");
					if (sbx_pruefen == true || titelsbx == 0) {
						sbx = true;
						JOptionPane.showMessageDialog(null,
								"HINWEIS: Das Werk ist ein SBX-Buch!");
					} else
						sbx = false;
					if (gefunden == true || Buch_gefunden == true) {
						JOptionPane.showMessageDialog(null, "BuchNr. " + nrin
								+ " ist bereits in der Klasse vorhanden !");
						if (gefunden == true && Buch_gefunden == true)
							gefunden = true;
						else {
							int response = JOptionPane
									.showConfirmDialog(
											null,
											"BuchNr. "
													+ nrin
													+ " trotzdem in die Klasse einfügen ?",
											"Buch unter Lehrer " + lehrer
													+ " einfügen",
											JOptionPane.YES_NO_OPTION);
							if (response == 0)
								gefunden = false;
							else
								gefunden = true;
						}
					}
					if (gefunden == true)
						JOptionPane.showMessageDialog(null,
								"Hinweis: Buch wurde nicht eingetragen!");
					/*
					 * ---------------------------------------------------------
					 * ACHTUNG !! in der folgenden Klammer steht ein ODER ||,
					 * wenn es zu einem && kippt - wird SBX wieder von der
					 * Bestellung ausgenommen !! Im ODER - FALL kann SBX
					 * bestellt werden, aber ohne Lehrerhandexemplar!
					 * ---------------------------------------------------------
					 */
					else if (gefunden == false
							|| (titelsbx != 0 && anhang_liste_sbx != 0 && anhang_liste_sbxk != 0)) { // Start
																										// else-Zweig
																										// 2

						wiederverwendung();

						if (wvint < 0 || wvint > 99)
							wvint = 0;
						if (uew == true)
							sql = "INSERT INTO " + klassein
									+ "(ID,Nr,Titel,Preis,Fach,Lehrer,W)"
									+ " values(" + count + ",'" + nrin
									+ "','UeW " + titelinuew + "'," + preisin
									+ ",'" + fachin + "','" + lehrer + "','"
									+ wvint + "')";
						else
							sql = "INSERT INTO " + klassein
									+ "(ID,Nr,Titel,Preis,Fach,Lehrer,W)"
									+ " values(" + count + ",'" + nrin + "','"
									+ titelin + "'," + preisin + ",'" + fachin
									+ "','" + lehrer + "','" + wvint + "')";
						stmt.executeUpdate(sql);
						JOptionPane.showMessageDialog(null, "Eingabe: BuchNr: "
								+ nrin + " Titel: " + titelin + " Preis: €"
								+ preisin + " Fach: " + fachin + " Klasse: "
								+ klassein + " erfolgreich!");
						// als Lehrerhandexemplar einfügen ? Ausnahmen: UeW,
						// Anhangliste und SBX Eingaben !!
						if (uew == false) {
							if (sbx == false
									&& (titelsbx == -1
											|| anhang_liste_sbx == -1 || anhang_liste_sbxk == -1)) {
								if (anhang_liste_ja < 0 || anhang_liste_ja > 0) {
									int response = JOptionPane
											.showConfirmDialog(
													null,
													"Als Lehrerhandexemplar einfügen ?",
													"Lehrerhandexemplar",
													JOptionPane.YES_NO_OPTION);
									if (response == 0) {
										String lehrertmp = lehrertxt.getText();
										ResultSet test1 = stmt
												.executeQuery("SELECT * FROM lehrerhand ORDER BY Jahr");
										while (test1.next()) {
											String nrtest = test1.getString(2);
											int nrtest_zahl = nrin
													.compareTo(nrtest);
											int jahrtest = test1.getInt("Jahr");
											int jahrtest_zahl = Integer
													.parseInt(sjstr)
													- 2000
													- jahrtest;
											String lehrertest = test1
													.getString(6);
											int lehrertest_zahl = lehrertmp
													.compareTo(lehrertest);
											if (nrtest_zahl == 0
													&& lehrertest_zahl == 0
													&& jahrtest_zahl >= 0) {
												JOptionPane
														.showMessageDialog(
																null,
																"BuchNr "
																		+ nrin
																		+ " ist bereits als Lehrerhandexemplar enthalten bzw. wurde bereits erhalten !");
												vorhanden = true;
											}
										}// Ende while-Schleife
										if (vorhanden == false && uew == false) {
											sql = "INSERT INTO lehrerhand (Nr, Titel, Fach, Jahr, Lehrer) VALUES ('"
													+ nrin
													+ "','"
													+ titelin
													+ "','"
													+ fachin
													+ "','"
													+ (Integer.parseInt(sjstr) - 2000)
													+ "','" + lehrertmp + "')";
											stmt.executeUpdate(sql);
											String lehrerhand = "lehrerhand";
											// umbenennen(X): ID fuer lehrerhand
											// wird neu definiert!
											umbenennen(lehrerhand);
											JOptionPane
													.showMessageDialog(
															null,
															"BuchNr '"
																	+ nrin
																	+ "' wurde als Lehrerhandexemplar eingefügt !");
										}
									} // Ende if (response == 0)
								} // Ende if (anhang_liste_ja < 0 ||
									// anhang_liste_ja > 0)
								else
									JOptionPane
											.showMessageDialog(
													null,
													"Hinweis: Für Schulbücher aus der Anhangliste gibt es kein gratis Lehrerhandexemplar!");
							} // Ende if (sbx == false)
							else
								JOptionPane
										.showMessageDialog(null,
												"Hinweis: Für SBX-Bücher gibt es kein Lehrerhandexemplar!");
						} // Ende if (uew == false)
						else
							JOptionPane
									.showMessageDialog(
											null,
											"Hinweis: Für Schulbücher 'Unterrichtsmittel eigener Wahl' gibt es kein Lehrerhandexemplar!");
					} // Ende else-Zweig 2
					else if (titelsbx > -1 || anhang_liste_sbx > -1
							|| anhang_liste_sbxk > -1)
						JOptionPane
								.showMessageDialog(null,
										"Hinweis: SBX-Bücher können in diesem Schuljahr nicht bestellt werden!");

					umbenennen(klassein);

					fachcb.setSelectedIndex(0);
					nrtxt.setText("");
					titeltxt.setText("");
					preistxt.setText("");
					klcb.setSelectedItem(klassetxt);
					klassecb1.setSelectedIndex(0);
					UeW.setState(false);
					anzeigen(klassecb);
					stmt.close();
					con.close();
				} catch (java.lang.Exception ex) {
					JOptionPane.showMessageDialog(null, "Im Schuljahr " + sjstr
							+ " kein Einfügen möglich! \nGrund: "
							+ ex.toString().substring(72, 120));
					// ex.printStackTrace();
				}
			}
		}// Ende else-Zweig 1
	}

	// Umbenennung der ID
	public void umbenennen(String klassein) {
		int z = 0, count = 0;
		int[] itmp = new int[10000];
		String sql = "";
		ResultSet test1;

		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			int klassein_zahl = klassein.compareTo("LehrerHand");
			if (klassein_zahl == 0)
				test1 = stmt.executeQuery("SELECT * FROM " + klassein
						+ " ORDER BY Jahr,Lehrer");
			else
				test1 = stmt.executeQuery("SELECT * FROM " + klassein
						+ " ORDER BY Fach");
			count = 0;
			while (test1.next()) {
				itmp[z] = test1.getInt(1);
				z++;
				count++;
			}
			stmt.close();
			con.close();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		weiter(klassein, count, itmp);
	}

	// Fortsetzung von 'umbenennen der ID' bzgl. Einfügen und Löschen eines
	// Buches
	public void weiter(String klassein, int count, int[] itmp) {
		int i = 0, z = 0;
		String sql = "";

		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			for (z = 0; z < count; z++) {
				i = i + 100;
				sql = "UPDATE " + klassein + " SET ID = " + i + " WHERE ID = "
						+ itmp[z];
				itmp[z] = i;
				stmt.executeUpdate(sql);
			}
			i = 0;
			for (z = 0; z < count; z++) {
				i++;
				sql = "UPDATE " + klassein + " SET ID = " + i + " WHERE ID = "
						+ itmp[z];
				stmt.executeUpdate(sql);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		UeW.setState(false);
	}

	// Löschen eines Buches über "Löschen"
	public void loesche() {
		int lang1, lang5;
		String sql = "", ID = "";
		boolean gefunden = false;
		boolean vorhanden = false; // LehrHand Abfrage
		lehrer = lehrertxt.getText();
		String leer1 = nrtxt.getText();
		lang1 = leer1.length();
		String leer5 = klassetxt;
		lang5 = leer5.length();

		if (lang1 < 1 || lang5 < 2)
			JOptionPane
					.showMessageDialog(null,
							"Bitte mindestens die Nummer und die Klasse (z.B. 1a) eingeben");
		else {
			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				String nrin = nrtxt.getText();
				String klassein = klassetxt;
				ResultSet test = stmt.executeQuery("SELECT * FROM " + klassein);
				while (test.next()) {
					String nrtest = test.getString(2);
					String lehrertest = test.getString(7);
					int zahltestlehrer = lehrertest.compareTo(lehrer);
					int zahltest = nrtest.compareTo(nrin);
					if (zahltest == 0 && zahltestlehrer == 0)
						gefunden = true;
				}
				if (gefunden == true) { // If-Zweig 1
					sql = "DELETE FROM " + klassein + " WHERE (Nr = '" + nrin
							+ "' AND Lehrer = '" + lehrer + "')";
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Buch: " + nrin
							+ " erfolgreich gelöscht!");
					// als Lehrerhandexemplar löschen
					lehrer = lehrertxt.getText();
					test = stmt
							.executeQuery("SELECT * FROM lehrerhand ORDER BY Jahr");
					while (test.next()) {
						String IDtest = test.getString(1);
						String nrtest = test.getString(2);
						int nrtest_zahl = nrin.compareTo(nrtest);
						int jahrtest = test.getInt("Jahr");
						int jahrtest_zahl = Integer.parseInt(sjstr) - 2000
								- jahrtest;
						String lehrertest = test.getString(6);
						int lehrertest_zahl = lehrer.compareTo(lehrertest);
						if (nrtest_zahl == 0 && lehrertest_zahl == 0
								&& jahrtest_zahl == 0) {
							ID = IDtest;
							vorhanden = true;
						}
					}// Ende while-Schleife
					if (vorhanden == true) {
						sjstr = schuljahrtxt.getText();
						new lh(ID, nrin, lehrer, sjstr);
					} else {
						JOptionPane
								.showMessageDialog(
										null,
										"BuchNr '"
												+ nrin
												+ "' war nicht als Lehrerhandexemplar eingetragen bzw. wurde bereits erhalten !");
					}
					umbenennen(klassein);
					nrtxt.setText("");
					titeltxt.setText("");
					preistxt.setText("");
					klcb.setSelectedItem(klassetxt);
					klassecb1.setSelectedIndex(0);
					anzeigen(klassecb);
				} // Ende if-Zweig 1
				else
					JOptionPane
							.showMessageDialog(
									null,
									"ACHTUNG: BuchNr: "
											+ nrin
											+ " nicht gefunden ODER Buch unter einem anderen Lehrer eingegeben !");
				stmt.close();
				con.close();
			}

			catch (java.lang.Exception ex) {
				JOptionPane.showMessageDialog(null, "Im Schuljahr " + sjstr
						+ " kein Löschen möglich! \nGrund: "
						+ ex.toString().substring(72, 120));
				// ex.printStackTrace();
			}
		}
	}

	// Aktion SUCHEN
	public void suchen() {
		int lang0 = 0, lang1 = 0, lang2 = 0, clength = 0, treffer = 0;
		String Tab = "\t", Tab1 = "\t", Tab2 = "\t", Tab3 = "\t";
		ResultSet rs;

		String leer0 = nrtxt.getText();
		lang0 = leer0.length();
		String leer1 = titeltxt.getText();
		lang1 = leer1.length();
		String leer2 = (String) fachcb.getSelectedItem();
		lang2 = leer2.length();
		if (lang0 < 1 && lang1 < 1 && lang2 < 1) {
			JOptionPane.showMessageDialog(null,
					"Sie müssen entweder Nummer, Titel oder Fach eingeben");
			preistxt.setText("");
			klassecb1.setSelectedIndex(0);
		} else {
			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				String nrin = nrtxt.getText();
				String titelin = titeltxt.getText();
				String fachin = (String) fachcb.getSelectedItem();
				if (lang0 > 0) {
					rs = stmt.executeQuery("SELECT * FROM liste"
							+ " WHERE Nr = '" + nrin + "'");
					output.setText("Das Ergebnis der Abfrage für die Schulbuchliste:\n\nNr.\tKurztitel d. Buches\t\t\tPreis\tFach\n"
							+ "---------------------------------------------------------------------------------------------------------\n");
					while (rs.next()) {
						String b = rs.getString(2);
						String c = rs.getString(3);
						clength = c.length();
						if (clength > 39)
							c = c.substring(0, 39);
						clength = c.length();
						clength = 39 - clength;
						if (clength > 31)
							Tab1 = "\t\t\t\t\t";
						else if (clength > 23 && clength <= 31)
							Tab1 = "\t\t\t\t";
						else if (clength > 15 && clength <= 23)
							Tab1 = "\t\t\t";
						else if (clength > 7 && clength <= 15)
							Tab1 = "\t\t";
						else if (clength <= 7)
							Tab1 = "\t";
						String d = rs.getString(4);
						double preis = Double.parseDouble(d);
						String e = rs.getString(5);
						treffer++;
						output.append(b + Tab + c + Tab1 + preis + Tab2 + e
								+ "\n");
					}
					JOptionPane.showMessageDialog(null, treffer + " Treffer");
					nrtxt.setText("");
					rs.close();
				} else if (lang1 > 0) {
					rs = stmt.executeQuery("SELECT * FROM liste"
							+ " WHERE Titel LIKE '%" + titelin + "%'");
					output.setText("Das Ergebnis der Abfrage für die Schulbuchliste:\n\nNr.\tKurztitel d. Buches\t\t\tPreis\tFach\n"
							+ "----------------------------------------------------------------------------------------------------------\n");
					while (rs.next()) {
						String b = rs.getString(2);
						String c = rs.getString(3);
						clength = c.length();
						if (clength > 39)
							c = c.substring(0, 39);
						clength = c.length();
						clength = 39 - clength;
						if (clength > 31)
							Tab1 = "\t\t\t\t\t";
						else if (clength > 23 && clength <= 31)
							Tab1 = "\t\t\t\t";
						else if (clength > 15 && clength <= 23)
							Tab1 = "\t\t\t";
						else if (clength > 7 && clength <= 15)
							Tab1 = "\t\t";
						else if (clength <= 7)
							Tab1 = "\t";
						String d = rs.getString(4);
						double preis = Double.parseDouble(d);
						String e = rs.getString(5);
						output.append(b + Tab + c + Tab1 + preis + Tab2 + e
								+ "\n");
						treffer++;
					}
					JOptionPane.showMessageDialog(null, treffer + " Treffer");
					titeltxt.setText("");
					rs.close();
				} else if (lang2 > 0) {
					int fach_zahl = fachin.compareTo("BE");
					if (fach_zahl == 0) {
						fachin = "Bildnerische Erziehung";
					}
					fach_zahl = fachin.compareTo("BU");
					if (fach_zahl == 0) {
						fachin = "Biologie";
					}
					fach_zahl = fachin.compareTo("CH");
					if (fach_zahl == 0) {
						fachin = "Chemie";
					}
					fach_zahl = fachin.compareTo("D");
					if (fach_zahl == 0) {
						fachin = "Deutsch";
					}
					fach_zahl = fachin.compareTo("E1");
					if (fach_zahl == 0) {
						fachin = "Englisch";
					}
					fach_zahl = fachin.compareTo("E2");
					if (fach_zahl == 0) {
						fachin = "Englisch";
					}
					fach_zahl = fachin.compareTo("F1");
					if (fach_zahl == 0) {
						fachin = "Französisch";
					}
					fach_zahl = fachin.compareTo("F2");
					if (fach_zahl == 0) {
						fachin = "Französisch";
					}
					fach_zahl = fachin.compareTo("G");
					if (fach_zahl == 0) {
						fachin = "Griechisch";
					}
					fach_zahl = fachin.compareTo("GW");
					if (fach_zahl == 0) {
						fachin = "Geographie";
					}
					fach_zahl = fachin.compareTo("HS");
					if (fach_zahl == 0) {
						fachin = "Geschichte";
					}
					fach_zahl = fachin.compareTo("INF");
					if (fach_zahl == 0) {
						fachin = "Informatik";
					}
					fach_zahl = fachin.compareTo("IT");
					if (fach_zahl == 0) {
						fachin = "Italienisch";
					}
					fach_zahl = fachin.compareTo("L1");
					if (fach_zahl == 0) {
						fachin = "Latein";
					}
					fach_zahl = fachin.compareTo("L2");
					if (fach_zahl == 0) {
						fachin = "Latein";
					}
					fach_zahl = fachin.compareTo("M");
					if (fach_zahl == 0) {
						fachin = "Mathematik";
					}
					fach_zahl = fachin.compareTo("ME");
					if (fach_zahl == 0) {
						fachin = "Musik";
					}
					fach_zahl = fachin.compareTo("PE");
					if (fach_zahl == 0) {
						fachin = "Philosophie";
					}
					fach_zahl = fachin.compareTo("PH");
					if (fach_zahl == 0) {
						fachin = "Physik";
					}
					fach_zahl = fachin.compareTo("RE");
					if (fach_zahl == 0) {
						fachin = "Religion (evangelisch)";
					}
					fach_zahl = fachin.compareTo("RK");
					if (fach_zahl == 0) {
						fachin = "Religion (katholisch)";
					}
					fach_zahl = fachin.compareTo("SP");
					if (fach_zahl == 0) {
						fachin = "Spanisch";
					}
					fach_zahl = fachin.compareTo("GWE");
					if (fach_zahl == 0) {
						fachin = "Geographie" + "%" + "Englisch";
					}
					fach_zahl = fachin.compareTo("BUE");
					if (fach_zahl == 0) {
						fachin = "Biologie" + "%" + "Englisch";
					}
					rs = stmt.executeQuery("SELECT * FROM liste"
							+ " WHERE Fach LIKE '%" + fachin + "%'");
					output.setText("Das Ergebnis der Abfrage für die Schulbuchliste:\n\n Nr.\tKurztitel d. Buches\t\t\tPreis\tFach\n"
							+ "----------------------------------------------------------------------------------------------------------\n");
					while (rs.next()) {
						String b = rs.getString(2);
						String c = rs.getString(3);
						clength = c.length();
						if (clength > 39)
							c = c.substring(0, 39);
						clength = c.length();
						clength = 39 - clength;
						if (clength > 31)
							Tab1 = "\t\t\t\t\t";
						else if (clength > 23 && clength <= 31)
							Tab1 = "\t\t\t\t";
						else if (clength > 15 && clength <= 23)
							Tab1 = "\t\t\t";
						else if (clength > 7 && clength <= 15)
							Tab1 = "\t\t";
						else if (clength <= 7)
							Tab1 = "\t";
						String d = rs.getString(4);
						double preis = Double.parseDouble(d);
						String e = rs.getString(5);

						output.append(b + Tab + c + Tab1 + preis + Tab2 + e
								+ "\n");
						treffer++;
					}
					JOptionPane.showMessageDialog(null, treffer + " Treffer");
					fachcb.setSelectedIndex(0);
					rs.close();
				}
				stmt.close();
				con.close();
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
			if (treffer == 0) {
				JOptionPane
						.showMessageDialog(null,
								"Keine Entsprechungen gefunden - versuchen sie eine andere Eingabe");
				nrtxt.setText("");
				titeltxt.setText("");
				fachcb.setSelectedIndex(0);
			}
		}
	}

	public void itemStateChanged(ItemEvent evt) {
		Object source = evt.getSource();
		if (anzeigen_klasse == true) {
			if (source == klcb) {
				Object newkl = evt.getItem();
				klassecb = newkl.toString();
				anzeigen(klassecb);
			}
		}
		if (source == fachcb) {
			Object newfach = evt.getItem();
			fachtxt = newfach.toString();
		}
		if (source == klassecb1) {
			Object newklasse = evt.getItem();
			klassetxt = newklasse.toString();
		}
	}

	// Fächer anlegen/löschen
	public void jMenuAnlegenFach_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new deffach(sjstr);
			load_fach();
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Klassen anlegen/löschen
	public void jMenuAnlegenKlasse_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new defklasse(sjstr);
			load_klassen();
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Lehrer anlegen/löschen
	public void jMenuAnlegenLehrer_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new deflehrer(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Schüler-Anzahl Eingabe
	public void jMenuAnlegenSchueler_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			String sus, susr, sos, sosr;
			sus = UStxt.getText();
			Integer.parseInt(sus);
			susr = USrtxt.getText();
			Integer.parseInt(susr);
			sos = OStxt.getText();
			Integer.parseInt(sos);
			sosr = OSrtxt.getText();
			Integer.parseInt(sosr);
			String sql = "";
			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				int response = JOptionPane.showConfirmDialog(null,
						"Neue Schülerzahlen übernehmen ?",
						"Schüleranzahl übernehmen", JOptionPane.YES_NO_OPTION);
				if (response == 0) {
					sql = "UPDATE schueler SET US = " + sus
							+ ", US_Religion = " + susr + ", OS = " + sos
							+ ", OS_Religion = " + sosr;
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null,
							"Schülerzahlen wurden neu übernommen !");
				} else
					JOptionPane.showMessageDialog(null, "Abbruch !");
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Schuljahr "anlegen"/neues Schuljahr übernehmen
	public void jMenuAnlegenSchuljahr_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		/*
		 * int allow = lehrertest.compareTo(""); FÜR ALLE LEHRER / USER if
		 * (allow == 0){
		 */
		String test_jahr[] = new String[30];
		int jj = 0;
		boolean eintrag_yes = false;
		schuljahrtxt.setEditable(true);
		try {
			String sjstr_neu = JOptionPane.showInputDialog(null,
					"Eingabe neues Schuljahr", "Schuljahr wechseln",
					JOptionPane.QUESTION_MESSAGE);
			try {
				ConnectDB conobj = new ConnectDB();
				Connection con = conobj.getDBconnection();
				Statement stmt = con.createStatement();
				ResultSet test_jahr_rs = stmt
						.executeQuery("SELECT * FROM schuljahr ORDER BY SJ");
				while (test_jahr_rs.next()) {
					test_jahr[jj] = test_jahr_rs.getString("SJ");
					jj++;
				}
			} catch (java.lang.Exception ex) {
				ex.printStackTrace();
			}
			int eintraege = jj;
			for (jj = 0; jj < eintraege; jj++) {
				int test_jahr_int = sjstr_neu.compareTo(test_jahr[jj]);
				if (test_jahr_int == 0) {
					eintrag_yes = true;
					sjstr = sjstr_neu;
				}
			}

			if (eintrag_yes == true)
				JOptionPane.showMessageDialog(null, "Schuljahr " + sjstr
						+ " wird übernommen!");
			else
				JOptionPane.showMessageDialog(null,
						"Schuljahr nicht vorhanden !");

			int sjneu = sjstr.compareTo("");
			if (sjneu == 0 || eintrag_yes == false)
				JOptionPane.showMessageDialog(null, "Gleiches Schuljahr !");
			else if (sjneu > 0 || sjneu < 0) {
				schuljahrtxt.setText(sjstr);
				load_klassen();
				load_fach();
				load_schueler();
			}
		} catch (java.lang.Exception ex) {
			JOptionPane.showMessageDialog(null, "Gleiches Schuljahr !");
			// ex.printStackTrace();
		}
		/*
		 * } else JOptionPane.showMessageDialog(null,"Keine Berechtigung");
		 */
	}

	public void jMenuNeuSchuljahr_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrer.compareTo("ADM");
		if (allow == 0) {
			new defschuljahr(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Limits eingeben
	public void jMenuAnlegenLimits_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new limits(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Password ändern
	public void pwdaendern() {
		sjstr = schuljahrtxt.getText();
		new pwdneu(lehrer, sjstr);
	}

	// Druck Lehrer + LH
	public void jMenuDruckLehrer_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new druck_lehrer(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Druck Buchbestellung Klassen
	public void jMenuDruckKlasse_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new druck_klassen(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Druck Fachberechnung
	public void jMenuDruckFach_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new druck_fach(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Druck Buchlisten und Schulstufe
	public void jMenuDruckBuch_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new druck_buch(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Druck Klassen und Buch Matrix
	public void jMenuDruckKlaBuch_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer;
		int allow = lehrertest.compareTo("ADM");
		if (allow == 0) {
			sjstr = schuljahrtxt.getText();
			new druck_kla_buch_matrix(sjstr);
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Druck Pwd
	public void jMenuDruckPwd_actionPerformed(ActionEvent e) {
		String lehrertest = lehrer, benutzer[] = new String[200], pwdbenutzer[] = new String[200];
		int allow = lehrertest.compareTo("ADM");
		int l = 0, anz_lehrer = 0;
		if (allow == 0) {
			try {
				FileWriter file = new FileWriter("/home/andreas/db_sba/pwd.odt");
				output.setText("");
				try {
					sjstr = schuljahrtxt.getText();
					ConnectDB conobj = new ConnectDB();
					Connection con = conobj.getDBconnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM lehrer");
					while (rs.next()) {
						benutzer[l] = rs.getString(2);
						pwdbenutzer[l] = rs.getString(3);
						l++;
					}
					con.close();
				} catch (java.lang.Exception ex) {
					ex.printStackTrace();
				}
				anz_lehrer = l;

				// Datumseingabe
				String date = JOptionPane.showInputDialog(null, "Datum vom:",
						"Datumseingabe", JOptionPane.QUESTION_MESSAGE);
				try {
					int dateint = date.compareTo("");
					if (dateint < 0 || dateint > 0)
						date = "vom " + date;
				} catch (java.lang.Exception ex) {
					date = "";
				}
				// INFO1 - Eingabe zum Zeitraum
				String info1 = JOptionPane.showInputDialog(null, "Zeitraum:",
						"Zeitraum der Eingabe", JOptionPane.QUESTION_MESSAGE);
				try {
					int info1int = info1.compareTo("");
					if (info1int < 0 || info1int > 0)
						info1 = "Zeitraum der Eingabe: " + info1 + "\n \n";
				} catch (java.lang.Exception ex) {
					info1 = "";
				}
				// INFO2 - Eingabe zum Ort
				String info2 = JOptionPane.showInputDialog(null,
						"Örtlichkeit:", "Ort der Eingabe",
						JOptionPane.QUESTION_MESSAGE);
				try {
					int info2int = info2.compareTo("");
					if (info2int < 0 || info2int > 0)
						info2 = "Ort der Eingabe: " + info2 + "\n \n";
				} catch (java.lang.Exception ex) {
					info2 = "";
				}

				for (l = 0; l < anz_lehrer; l++) {
					file.write("Die persönlichen Daten für die Anmeldung zum Programm für die Schulbuchaktion "
							+ date + "\n \n");
					file.write("Benutzer: " + benutzer[l] + "  Passwort: "
							+ pwdbenutzer[l] + "\n \n");
					for (pwd_counter = 0; pwd_counter < 5; pwd_counter++) {
						pwd[pwd_counter] = pwdbenutzer[l].charAt(pwd_counter);
						// new pwdlang(pwd);
						switch ((char) (pwd[pwd_counter])) {
						case 'a': {
							pwdwort[pwd_counter] = "anton";
							break;
						}
						case 'A': {
							pwdwort[pwd_counter] = "Anton";
							break;
						}
						case 'b': {
							pwdwort[pwd_counter] = "berta";
							break;
						}
						case 'B': {
							pwdwort[pwd_counter] = "Berta";
							break;
						}
						case 'c': {
							pwdwort[pwd_counter] = "caesar";
							break;
						}
						case 'C': {
							pwdwort[pwd_counter] = "Caesar";
							break;
						}
						case 'd': {
							pwdwort[pwd_counter] = "dora";
							break;
						}
						case 'D': {
							pwdwort[pwd_counter] = "Dora";
							break;
						}
						case 'e': {
							pwdwort[pwd_counter] = "emil";
							break;
						}
						case 'E': {
							pwdwort[pwd_counter] = "Emil";
							break;
						}
						case 'f': {
							pwdwort[pwd_counter] = "friedrich";
							break;
						}
						case 'F': {
							pwdwort[pwd_counter] = "Friedrich";
							break;
						}
						case 'g': {
							pwdwort[pwd_counter] = "gustav";
							break;
						}
						case 'G': {
							pwdwort[pwd_counter] = "Gustav";
							break;
						}
						case 'h': {
							pwdwort[pwd_counter] = "heinrich";
							break;
						}
						case 'H': {
							pwdwort[pwd_counter] = "Heinrich";
							break;
						}
						case 'i': {
							pwdwort[pwd_counter] = "ida";
							break;
						}
						case 'I': {
							pwdwort[pwd_counter] = "Ida";
							break;
						}
						case 'j': {
							pwdwort[pwd_counter] = "julius";
							break;
						}
						case 'J': {
							pwdwort[pwd_counter] = "Julius";
							break;
						}
						case 'k': {
							pwdwort[pwd_counter] = "konrad";
							break;
						}
						case 'K': {
							pwdwort[pwd_counter] = "Konrad";
							break;
						}
						case 'l': {
							pwdwort[pwd_counter] = "ludwig";
							break;
						}
						case 'L': {
							pwdwort[pwd_counter] = "Ludwig";
							break;
						}
						case 'm': {
							pwdwort[pwd_counter] = "martha";
							break;
						}
						case 'M': {
							pwdwort[pwd_counter] = "Martha";
							break;
						}
						case 'n': {
							pwdwort[pwd_counter] = "nordpol";
							break;
						}
						case 'N': {
							pwdwort[pwd_counter] = "Nordpol";
							break;
						}
						case 'o': {
							pwdwort[pwd_counter] = "otto";
							break;
						}
						case 'O': {
							pwdwort[pwd_counter] = "Otto";
							break;
						}
						case 'p': {
							pwdwort[pwd_counter] = "paula";
							break;
						}
						case 'P': {
							pwdwort[pwd_counter] = "Paula";
							break;
						}
						case 'q': {
							pwdwort[pwd_counter] = "ku";
							break;
						}
						case 'Q': {
							pwdwort[pwd_counter] = "Ku";
							break;
						}
						case 'r': {
							pwdwort[pwd_counter] = "richard";
							break;
						}
						case 'R': {
							pwdwort[pwd_counter] = "Richard";
							break;
						}
						case 's': {
							pwdwort[pwd_counter] = "siegfried";
							break;
						}
						case 'S': {
							pwdwort[pwd_counter] = "Siegfried";
							break;
						}
						case 't': {
							pwdwort[pwd_counter] = "theodor";
							break;
						}
						case 'T': {
							pwdwort[pwd_counter] = "Theodor";
							break;
						}
						case 'u': {
							pwdwort[pwd_counter] = "ulrich";
							break;
						}
						case 'U': {
							pwdwort[pwd_counter] = "Ulrich";
							break;
						}
						case 'v': {
							pwdwort[pwd_counter] = "viktor";
							break;
						}
						case 'V': {
							pwdwort[pwd_counter] = "Viktor";
							break;
						}
						case 'w': {
							pwdwort[pwd_counter] = "wilhelm";
							break;
						}
						case 'W': {
							pwdwort[pwd_counter] = "Wilhelm";
							break;
						}
						case 'x': {
							pwdwort[pwd_counter] = "xaver";
							break;
						}
						case 'X': {
							pwdwort[pwd_counter] = "Xaver";
							break;
						}
						case 'y': {
							pwdwort[pwd_counter] = "ypsilon";
							break;
						}
						case 'Y': {
							pwdwort[pwd_counter] = "Ypsilon";
							break;
						}
						case 'z': {
							pwdwort[pwd_counter] = "zuerich";
							break;
						}
						case 'Z': {
							pwdwort[pwd_counter] = "Zuerich";
							break;
						}
						case '0': {
							pwdwort[pwd_counter] = "null";
							break;
						}
						case '1': {
							pwdwort[pwd_counter] = "eins";
							break;
						}
						case '2': {
							pwdwort[pwd_counter] = "zwei";
							break;
						}
						case '3': {
							pwdwort[pwd_counter] = "drei";
							break;
						}
						case '4': {
							pwdwort[pwd_counter] = "vier";
							break;
						}
						case '5': {
							pwdwort[pwd_counter] = "fuenf";
							break;
						}
						case '6': {
							pwdwort[pwd_counter] = "sechs";
							break;
						}
						case '7': {
							pwdwort[pwd_counter] = "sieben";
							break;
						}
						case '8': {
							pwdwort[pwd_counter] = "acht";
							break;
						}
						case '9': {
							pwdwort[pwd_counter] = "neun";
							break;
						}
						default: {
							pwdwort[pwd_counter] = "" + pwd[pwd_counter];
						}
						}
					}
					file.write("In Worten: " + pwdwort[0] + "-" + pwdwort[1]
							+ "-" + pwdwort[2] + "-" + pwdwort[3] + "-"
							+ pwdwort[4] + "\n\n");
					file.write(info1);
					file.write(info2);
					file.write("Es besteht die Möglichkeit bei allen Geräten im Konferenzzimmer über\n START-LEHRER-SCHULBUCHAKTION\n das Programm für die Eingabe zu starten.\f");
				}
				file.close();
			} catch (IOException exeption) {
				System.out.println("Error -- " + exeption.toString());
			}
			JOptionPane
					.showMessageDialog(
							null,
							l
									+ " Benutzer und Passwörter auf /home/andreas/db_sba/pwd.odt geschrieben !");
		} else
			JOptionPane.showMessageDialog(null, "Keine Berechtigung");
	}

	// Drucken
	public void drucken() {
		int zeilen = 0;
		String druckText;
		String zeiletmp;

		output.selectAll();
		druckText = output.getSelectedText();
		druckprog druckSeite = new druckprog(druckText);
		if (druckSeite.setupPageFormat()) {
			if (druckSeite.setupJobOptions()) {
				try {
					druckSeite.drucke();
				} catch (Exception e) {
					System.err.println(e.toString());
				}
			}
		}
	}

	// Kontrolle der Eingabe zur Wiederverwendung
	public int wiederverwendung() {
		int wvtmp = 0;

		String wv = JOptionPane.showInputDialog(null,
				"Anzahl der Bücher zur Wiederverwendung ?",
				"Wiederverwendete Bücher", JOptionPane.QUESTION_MESSAGE);

		try {
			wvtmp = wv.compareTo("");

			if (wvtmp == 0) {
				JOptionPane.showMessageDialog(null,
						"Sie müssen eine Zahl eingeben !");
				return (wiederverwendung());
			} else {
				wvint = Integer.parseInt(wv);
				if (wvint < 0 || wvint > 99) {
					JOptionPane
							.showMessageDialog(null,
									"Sie können nur eine Zahl zwischen 0 und 99 eingeben");
					return (wiederverwendung());
				} else {
					return (wvint);
				}
			}
		}// try Ende
		catch (java.lang.Exception ex) {
			wvint = 0;
			return (0);
		}
	}

	public void clear() {
		nrtxt.setText("");
		titeltxt.setText("");
		preistxt.setText("");
		fachcb.setSelectedIndex(0);
		klassecb1.setSelectedIndex(0);
		UeW.setState(false);
	}

	public void load_klassen() {
		try {
			anzeigen_klasse = false;
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM klassen ORDER BY Klasse");
			klassecb1.removeAllItems();
			klcb.removeAllItems();
			klassecb1.addItem("");
			while (rs.next()) {
				String klassestr = rs.getString(1);
				klassecb1.addItem(klassestr);
				klcb.addItem(klassestr);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		anzeigen_klasse = true;
	}

	// load für neues Schuljahr - Anlegen der Schüler-Anzahl Felder
	public void load_schueler() {
		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM schueler");
			while (rs.next()) {
				us = rs.getInt(1);
				UStxt.setText("" + us);
				usr = rs.getInt(2);
				USrtxt.setText("" + usr);
				os = rs.getInt(3);
				OStxt.setText("" + os);
				osr = rs.getInt(4);
				OSrtxt.setText("" + osr);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
	}

	// Einfügen der Fächer in die ComboBox fachcb
	public void load_fach() {
		fachcb.removeAllItems();
		fachcb.addItem("");
		try {
			ConnectDB conobj = new ConnectDB();
			Connection con = conobj.getDBconnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM faecher ORDER BY Fach");
			while (rs.next()) {
				String fachstr = rs.getString(1);
				fachcb.addItem(fachstr);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
	}

	public InputStream getResourceStream(String pkgname, String fname) {
		String resname = "/" + pkgname.replace('.', '/') + "/" + fname;
		// System.out.println(resname);
		Class<? extends Frame> clazz = getClass();
		// System.out.println(clazz);
		InputStream is = clazz.getResourceAsStream(resname);
		// System.out.println(is);
		return is;
	}

	// Image loader (dies soll für die -jar Ausfuehrung die Bilder einbinden)
	public Image loadImageResource(String pkgname, String fname)
			throws IOException {
		Image ret = null;
		InputStream is = getResourceStream(pkgname, fname);
		if (is != null) {
			byte[] buffer = new byte[0];
			byte[] tmpbuf = new byte[1024];
			while (true) {
				int len = is.read(tmpbuf);
				if (len <= 0) {
					break;
				}
				byte[] newbuf = new byte[buffer.length + len];
				System.arraycopy(buffer, 0, newbuf, 0, buffer.length);
				System.arraycopy(tmpbuf, 0, newbuf, buffer.length, len);
				buffer = newbuf;
			}
			// create image
			ret = Toolkit.getDefaultToolkit().createImage(buffer);
			is.close();
		}
		return ret;
	}

	// Ende Image loader

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == klassebtn)
			anzeigen(klassecb);
		if (source == insertbtn) {
			sjstr = schuljahrtxt.getText();
			lehrer = lehrertxt.getText();
			int sjstrtest_sj = sjstr.compareTo(anmeldesjstr);
			int allow_eingabe = lehrer.compareTo("ADM");
			if (sjstrtest_sj == 0 || allow_eingabe == 0)
				eingabe(lehrer);
			else
				JOptionPane.showMessageDialog(null, "Im Schuljahr " + sjstr
						+ " ist keine Eingabe möglich !");
		}
		if (source == deletebtn) {
			sjstr = schuljahrtxt.getText();
			lehrer = lehrertxt.getText();
			int sjstrtest_sj = sjstr.compareTo(anmeldesjstr);
			int allow_eingabe = lehrer.compareTo("ADM");
			if (sjstrtest_sj == 0 || allow_eingabe == 0)
				loesche();
			else
				JOptionPane.showMessageDialog(null, "Im Schuljahr " + sjstr
						+ " ist kein Löschen möglich !");
		}
		if (source == searchbtn)
			suchen();
		if (source == pwdaendernbtn)
			pwdaendern();
		if (source == druckbtn)
			drucken();
		if (source == resetbtn)
			clear();
	}

}
