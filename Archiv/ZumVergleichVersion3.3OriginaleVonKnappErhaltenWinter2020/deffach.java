package sba;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * <p>�berschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm f�r die Eingabe und Bearbeitung f�r Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class deffach extends Frame {

   JFrame fachfrm = new JFrame();
   JPanel fach = new JPanel();
   JLabel fachlbl = new JLabel("Bezeichnung: ");
   JTextField fachtxt = new JTextField(5);
   JButton okbtn = new JButton("Einf�gen");
   JButton loeschenbtn = new JButton("L�schen");
   String sjstr_neu;
   String fachstr = "";

  public deffach(String sjstr) {
  sjstr_neu=sjstr;
  enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit1();
    }
    catch(Exception e) {
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
   GridLayout gr = new GridLayout(2,2,10,10);
   fach.setLayout(gr);
   fachfrm.setResizable(false);
   fachfrm.setSize(new Dimension(250, 100));
   fachfrm.setTitle("F�cher anlegen/l�schen");
   fachfrm.getContentPane().add(fach);

   //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = fachfrm.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    fachfrm.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    fachfrm.setVisible(true);
  }

  public void einfuegen() {

  fachstr = fachtxt.getText();
  fachstr = fachstr.toUpperCase();
  boolean abbruch = false;

  int testzahl = fachstr.length();
  if (testzahl == 0) JOptionPane.showMessageDialog(null,"Keine Eingabe !");

  else {

  try  {
      String url = "jdbc:odbc:sba"+sjstr_neu;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM faecher");
      while (rs.next()) {
      String tmp = rs.getString(1);
      int tmpzahl = tmp.compareTo(fachstr);
      if (tmpzahl == 0) abbruch = true;
      }
      if (abbruch == true) JOptionPane.showMessageDialog(null,"Bereits in Liste enthalten !");
      else {
        String sql = "INSERT INTO faecher (Fach) VALUES ('"+fachstr+"')";
        stmt.executeUpdate(sql);
        JOptionPane.showMessageDialog(null,"Fach "+fachstr+" wurde angelegt !");
        }
      con.close();
      fachtxt.setText("");
    }

    catch (java.lang.Exception ex){
      ex.printStackTrace();
    }
    } // Ende des Else-Zweiges
  }

  // Eintrag l�schen
  public void loeschen() {

  fachstr = fachtxt.getText();
  fachstr = fachstr.toUpperCase();
  fachtxt.setText(fachstr);
  boolean abbruch = true;

  int testzahl = fachstr.length();
  if (testzahl == 0) JOptionPane.showMessageDialog(null,"Keine Eingabe !");

  else {

  try  {
      String url = "jdbc:odbc:sba"+sjstr_neu;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM faecher");
      while (rs.next()) {
      String tmp = rs.getString(1);
      int tmpzahl = tmp.compareTo(fachstr);
      if (tmpzahl == 0) abbruch = false;
      }
      if (abbruch == true) JOptionPane.showMessageDialog(null,"Kein Eintrag in der Liste !");
      else {
        String sql = "DELETE FROM faecher WHERE Fach = '"+fachstr+"'";
        stmt.executeUpdate(sql);
        JOptionPane.showMessageDialog(null,"Fach "+fachstr+" wurde gel�scht !");
        }
      con.close();
      fachtxt.setText("");
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