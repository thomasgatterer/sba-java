package sba;

import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * <p>Überschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm für die Eingabe und Bearbeitung für Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class anmeldung extends Frame implements ActionListener {

  String lehrer = "", pwstr = ""; char[] pw;
  //JPanel contentPane;
  Frame anmeldefrm = new Frame();
  JPanel anmeldungpnl = new JPanel();
  JLabel anmeldunglbl = new JLabel("Benutzer: ");
  JTextField anmeldungtxt = new JTextField (5);
  JButton anmeldung_OKbtn = new JButton("OK");
  JButton anmeldung_Cancelbtn = new JButton("Cancel");
  //JTextField lehrertxt = new JTextField();
  JLabel pwlbl = new JLabel("Password");
  JTextField pwhiddentxt = new JTextField(6);
  JPasswordField pwtxt = new JPasswordField(6);

  //Den Frame konstruieren
 public anmeldung() throws Exception {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit1();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    /*try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }*/
  }

  //Die Anwendung konstruieren
  public void jbInit1() throws Exception {

      //contentPane = (JPanel) anmeldefrm.getContentPane();

      anmeldung_OKbtn.addActionListener(this);
      anmeldung_Cancelbtn.addActionListener(this);
      anmeldungpnl.add(anmeldunglbl);
      anmeldungpnl.add(anmeldungtxt);
      anmeldungpnl.add(pwlbl);
      anmeldungpnl.add(pwtxt);
      anmeldungpnl.add(anmeldung_OKbtn);
      anmeldungpnl.add(anmeldung_Cancelbtn);
      anmeldefrm.setResizable(false);
      anmeldefrm.setTitle("Anmeldung");
      anmeldefrm.getContentPane().add(anmeldungpnl);
      anmeldefrm.pack();

    //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = anmeldefrm.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    anmeldefrm.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    anmeldefrm.setVisible(true);
  }

  public void actionPerformed(ActionEvent e){
      if (e.getSource() == anmeldung_OKbtn) {
          anmeldung_return(lehrer);
      }
      if (e.getSource() == anmeldung_Cancelbtn) {
      System.exit(0);
      }
  }

  public String anmeldung_return (String lehrer) {
    String sql = "";
    boolean gefunden = false;

    lehrer = anmeldungtxt.getText();
    int lehrer_length = lehrer.length();
    pw = pwtxt.getPassword();
    pwstr = String.valueOf(pw);
    pwhiddentxt.setText(pwstr);
    //pwhiddentxt.setText("'"+ pw +"'");
    pwstr = pwhiddentxt.getText();
    int pwstr_length = pwstr.length();
    //if (pwstr_length == 7) pwstr = pwstr.substring(1,6);
    if (lehrer_length < 2 || lehrer_length > 4) {
      JOptionPane.showMessageDialog(null,"Das offizielle Namenskürzel hat min. 2, max. 4 Zeichen ODER Passwort falsch !");
    }
    else {
     try {
      String url = "jdbc:odbc:sba"+sjstr;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();

      //1. Versuch PasswordTest
      ResultSet test = stmt.executeQuery("SELECT * FROM Lehrer");
      while (test.next()) {
      String lehrerDB = test.getString(2);
      int lehrertest_zahl = lehrer.compareToIgnoreCase(lehrerDB);
      String pwDB = test.getString(3);
      int pwtest_zahl = pwDB.compareTo(pwstr);
      if (lehrertest_zahl == 0 && pwtest_zahl == 0) gefunden = true;
      }
      if (gefunden == true) {
        lehrer = lehrer.toUpperCase();
        sql = "INSERT INTO tmp"+" (Lehrer)" + " values('"+lehrer+"')";
        stmt.executeUpdate(sql);
        stmt.close();
        con.close();
        anmeldefrm.setVisible(false);
        new sba();
        }
      else JOptionPane.showMessageDialog(null,"Benutzername und Password stimmen nicht überein ODER Benutzer nicht gefunden !");
      }
     catch (java.lang.Exception ex){
      ex.printStackTrace();
      }
    }
    return(lehrer);
  }
  /*public void jbInit() throws Exception {
  }*/
}