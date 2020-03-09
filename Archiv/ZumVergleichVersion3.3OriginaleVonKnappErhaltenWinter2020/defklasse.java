package sba;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import java.io.*;
//import java.util.*;
//import java.net.URL;
import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.Driver;
import java.sql.Statement;
//import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * <p>Überschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm für die Eingabe und Bearbeitung für Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class defklasse extends Frame {

   JFrame klassefrm = new JFrame();
   JPanel klassepnl = new JPanel();
   JLabel klasselbl = new JLabel("Bezeichnung: ");
   JTextField klassetxt = new JTextField(5);
   JButton okbtn = new JButton("Einfügen");
   JButton loeschenbtn = new JButton("Löschen");
   String sjstr_neu;
   String klassestr = "";

  public defklasse(String sjstr) {
  sjstr_neu=sjstr;
  enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit2();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit2() {
   klassepnl.add(klasselbl);
   klassepnl.add(klassetxt);
   klassepnl.add(okbtn);
   klassepnl.add(loeschenbtn);
   okbtn.addActionListener(this);
   loeschenbtn.addActionListener(this);
   GridLayout grklasse = new GridLayout(2,2,10,10);
   klassepnl.setLayout(grklasse);
   klassefrm.setResizable(false);
   klassefrm.setSize(new Dimension(250, 100));
   klassefrm.setTitle("Klassen anlegen/löschen");
   klassefrm.getContentPane().add(klassepnl);

   //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = klassefrm.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    klassefrm.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    klassefrm.setVisible(true);
  }

  public void einfuegen() {

  klassestr = klassetxt.getText();
  klassestr = klassestr.toUpperCase();
  boolean abbruch = false;

  int testzahl = klassestr.length();
  if (testzahl == 0) JOptionPane.showMessageDialog(null,"Keine Eingabe !");

  else {

  try  {
      String url = "jdbc:odbc:sba"+sjstr_neu;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM klassen");
      while (rs.next()) {
      String tmp = rs.getString(1);
      int tmpzahl = tmp.compareTo(klassestr);
      if (tmpzahl == 0) abbruch = true;
      }
      if (abbruch == true) JOptionPane.showMessageDialog(null,"Bereits in Liste enthalten !");
      else {
        String sql = "INSERT INTO klassen (Klasse) VALUES ('"+klassestr+"')";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE "+klassestr+" (ID INTEGER, Nr TEXT, Titel TEXT, Preis DOUBLE, Fach TEXT, Anzahl INTEGER, Lehrer TEXT, Wert DOUBLE, W INTEGER, PRIMARY KEY (ID))";
        stmt.executeUpdate(sql);
        JOptionPane.showMessageDialog(null,"Klasse "+klassestr+" wurde angelegt !");
        }
      con.close();
      klassetxt.setText("");
    }
    catch (java.lang.Exception ex){
      ex.printStackTrace();
    }
    } // Ende des Else-Zweiges
  }
  // Eintrag löschen
  public void loeschen() {

  klassestr = klassetxt.getText();
  klassestr = klassestr.toUpperCase();
  klassetxt.setText(klassestr);
  boolean abbruch = true;

  int testzahl = klassestr.length();
  if (testzahl == 0) JOptionPane.showMessageDialog(null,"Keine Eingabe !");

  else {

  try  {
      String url = "jdbc:odbc:sba"+sjstr_neu;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM klassen");
      while (rs.next()) {
      String tmp = rs.getString(1);
      int tmpzahl = tmp.compareTo(klassestr);
      if (tmpzahl == 0) abbruch = false;
      }
      if (abbruch == true) JOptionPane.showMessageDialog(null,"Kein Eintrag in der Liste !");
      else {
        String sql = "DELETE FROM klassen WHERE Klasse = '"+klassestr+"'";
        stmt.executeUpdate(sql);
        sql = "DROP TABLE "+klassestr;
        stmt.executeUpdate(sql);
        JOptionPane.showMessageDialog(null,"Klasse "+klassestr+" wurde gelöscht !");
        }
      con.close();
      klassetxt.setText("");
    }

    catch (java.lang.Exception ex){
      ex.printStackTrace();
    }
    } // Ende des Else-Zweiges
  }

  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();

    if (source == okbtn) einfuegen();
    if (source == loeschenbtn) loeschen();
    }
}