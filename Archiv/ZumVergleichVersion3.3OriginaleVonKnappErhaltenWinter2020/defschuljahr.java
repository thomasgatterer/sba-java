package sba;

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

public class defschuljahr extends Frame{

int sjstr_neu_zahl=0;
String sjstr_neu="";
int sjint_neu = 0;
String sj_insert="";
String sj_delete="";
String test_jahr[] = new String[30];
int jj=0;

   JFrame schuljahrfrm = new JFrame();
   JPanel schuljahrpn0 = new JPanel();
   JPanel schuljahrpnl = new JPanel();
   JPanel schuljahrpn2 = new JPanel();
   JLabel schuljahrlbl = new JLabel("NEUES SCHULJAHR: ");
   JTextField schuljahrtxt = new JTextField(5);
   JButton okbtn = new JButton("Einfügen");
   JButton loeschenbtn = new JButton("Löschen");
   JButton abbruchbtn = new JButton("Abbruch");
   info_dialog insertDB;

  public defschuljahr(String sjstr) {
  enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit5();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit5() {

   schuljahrpnl.add(schuljahrlbl);
   schuljahrpnl.add(schuljahrtxt);
   schuljahrpnl.add(okbtn);
   schuljahrpnl.add(loeschenbtn);
   schuljahrpn2.add(abbruchbtn);
   okbtn.addActionListener(this);
   loeschenbtn.addActionListener(this);
   abbruchbtn.addActionListener(this);
   GridLayout grground = new GridLayout(2,1,2,2);
   GridLayout grschuljahr = new GridLayout(2,2,2,2);
   FlowLayout flschuljahr = new FlowLayout();
   schuljahrpn0.setLayout(grground);
   schuljahrpnl.setLayout(grschuljahr);
   schuljahrpn2.setLayout(flschuljahr);
   schuljahrpn0.add(schuljahrpnl);
   schuljahrpn0.add(schuljahrpn2);
   schuljahrfrm.setResizable(false);
   schuljahrfrm.setSize(new Dimension(350, 140));
   schuljahrfrm.setTitle("Schuljahr anlegen/löschen");
   schuljahrfrm.getContentPane().add(schuljahrpn0);

   //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = schuljahrfrm.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    schuljahrfrm.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    schuljahrfrm.setVisible(true);
  }

  public void einfuegen () {

  int insert = 0;
  insert = JOptionPane.showConfirmDialog(null,"Anlegen des NEUEN SCHULJAHRES bestätigen!","Schuljahr ANLEGEN",JOptionPane.OK_CANCEL_OPTION);

  if (insert != 0) {
      JOptionPane.showMessageDialog(null,"Abbruch des Vorgangs wird bestätigt!");
      schuljahrfrm.dispose();
      }
  else {
      sjstr_neu = schuljahrtxt.getText();
      sjstr_neu_zahl = sjstr_neu.compareTo("");
      String klassenNr[] = new String [50];
      int kk=0;

      if (sjstr_neu_zahl==0 || sjstr_neu.length()<4 || sjstr_neu.length()>4) {
        JOptionPane.showMessageDialog(null,"Fehlerhafte Eingabe - Abbruch!");
        schuljahrfrm.dispose();
        }
      else {
        try {
          String url = "jdbc:odbc:sba";
          Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
          Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
          Statement stmt = con.createStatement();
          ResultSet test_jahr_rs = stmt.executeQuery("SELECT * FROM schuljahr ORDER BY SJ");
          while (test_jahr_rs.next()) {
            test_jahr[jj]=test_jahr_rs.getString("SJ");
            jj++;
            }
          sj_insert = "INSERT INTO schuljahr (SJ) VALUES ("+sjstr_neu+")";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Schuljahr "+sjstr_neu+" wird in sba übernommen!");
          String sj_database = "CREATE DATABASE IF NOT EXISTS sba"+sjstr_neu;
          stmt.execute(sj_database);
          JOptionPane.showMessageDialog(null,"Datenbank sba"+sjstr_neu+" wurde erstellt!");
          stmt.close();
          con.close();

          JOptionPane.showMessageDialog(null,"Bitte ODBC-Verbindung mit sba"+sjstr_neu+" herstellen und dann fortfahren mit OK!");

          url = "jdbc:odbc:sba"+sjstr;
          Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
          con = DriverManager.getConnection(url,"lehrer","jon96as12");
          stmt = con.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT * FROM klassen");
          while (rs.next()) {
          klassenNr[kk] = rs.getString(1);
          kk++;
          }
          stmt.close();
          con.close();

          url = "jdbc:odbc:sba"+sjstr_neu;
          Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
          con = DriverManager.getConnection(url,"lehrer","jon96as12");
          stmt = con.createStatement();
          int klassen_zahl = kk;
          for(kk=0;kk<klassen_zahl;kk++) {
            String sj_table = "CREATE TABLE "+klassenNr[kk]+" (ID INT(11), Nr VARCHAR(50), Titel VARCHAR(50), Preis double, Fach VARCHAR(50), Anzahl smallint(5), Lehrer VARCHAR(50), Wert double, W smallint(6))";
            stmt.executeUpdate(sj_table);
          }
          stmt.close();
          con.close();
          }
        catch (java.lang.Exception ex){
          ex.printStackTrace();
          }

        int eintraege=jj;
        for (jj=0;jj<eintraege;jj++){
          try {
            String url = "jdbc:odbc:sba"+test_jahr[jj];
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
            Statement stmt = con.createStatement();
            sj_insert = "INSERT INTO schuljahr (SJ) VALUES ("+sjstr_neu+")";
            stmt.executeUpdate(sj_insert);
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null,"Schuljahr "+sjstr_neu+" wird in sba"+test_jahr[jj]+" übernommen!");
            }
        catch (java.lang.Exception ex){
          JOptionPane.showMessageDialog(null,"Fehlerhafte Verbindung zur Datenbank !");
          //ex.printStackTrace();
          }
        }// Ende FOR-Schleife

      //Erstellen aller notwendigen Tabellen
      try {
          String url = "jdbc:odbc:sba"+sjstr_neu;
          Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
          Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
          Statement stmt = con.createStatement();
          sjint_neu = Integer.parseInt(sjstr_neu);
          String sjstr_alt = Integer.toString(sjint_neu-1);
          //Table schuljahr
          sj_insert = "CREATE TABLE schuljahr SELECT * FROM sba"+ sjstr_alt +".schuljahr";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table schuljahr wurde in sba"+sjstr_neu+" übernommen!");
          //Table klassen
          sj_insert = "CREATE TABLE klassen SELECT * FROM sba"+ sjstr_alt +".klassen";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table klassen wurde in sba"+sjstr_neu+" übernommen!");
          //Table buecher_gesamt
          sj_insert = "CREATE TABLE buecher_gesamt SELECT * FROM sba"+ sjstr_alt +".buecher_gesamt WHERE 1=0;";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table buecher_gesamt wurde in sba"+sjstr_neu+" übernommen!");
          //Table faecher
          sj_insert = "CREATE TABLE faecher SELECT * FROM sba"+ sjstr_alt +".faecher";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table faecher wurde in sba"+sjstr_neu+" übernommen!");
          //Table kl_summe
          sj_insert = "CREATE TABLE kl_summe SELECT * FROM sba"+ sjstr_alt +".kl_summe WHERE 1=0";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table kl_summe wurde in sba'"+sjstr_neu+"' übernommen!");
          //Table lehrer
          sj_insert = "CREATE TABLE lehrer SELECT * FROM sba"+ sjstr_alt +".lehrer";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table lehrer wurde in sba"+sjstr_neu+" übernommen!");
          //Table lehrerhand
          sj_insert = "CREATE TABLE lehrerhand SELECT * FROM sba"+ sjstr_alt +".lehrerhand";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table lehrerhand wurde in sba"+sjstr_neu+" übernommen!");
          //Table limits
          sj_insert = "CREATE TABLE limits SELECT * FROM sba"+ sjstr_alt +".limits";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table limits wurde in sba"+sjstr_neu+" übernommen!");
          //Table limts_gesamt
          sj_insert = "CREATE TABLE limits_gesamt SELECT * FROM sba"+ sjstr_alt +".limits_gesamt";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table limits_gesamt wurde in sba"+sjstr_neu+" übernommen!");
          //Table liste
          sj_insert = "CREATE TABLE liste SELECT * FROM sba"+ sjstr_alt +".liste WHERE 1=0";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table liste wurde in sba"+sjstr_neu+" übernommen!");
          //Table schueler
          sj_insert = "CREATE TABLE schueler SELECT * FROM sba"+ sjstr_alt +".schueler";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table schueler wurde in sba"+sjstr_neu+" übernommen!");
          //Table schulstufe
          sj_insert = "CREATE TABLE schulstufe SELECT * FROM sba"+ sjstr_alt +".schulstufe";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table schulstufe wurde in sba"+sjstr_neu+" übernommen!");
          //Table tmp
          sj_insert = "CREATE TABLE tmp SELECT * FROM sba"+ sjstr_alt +".tmp WHERE 1=0";
          stmt.executeUpdate(sj_insert);
          JOptionPane.showMessageDialog(null,"Table tmp wurde in sba"+sjstr_neu+" übernommen!");
          stmt.close();
          con.close();
          }
      catch (java.lang.Exception ex){
        JOptionPane.showMessageDialog(null,"Fehlerhafte Verbindung zur Datenbank !");
        //ex.printStackTrace();
        }
      }// Ende IF-Schleife für MessageDialog
    }
  }

  public void loeschen() {

  jj=0;
  sjstr_neu = schuljahrtxt.getText();
  boolean vorhanden = false;
  boolean exit = false;

  int delete = 0;
  delete = JOptionPane.showConfirmDialog(null,"Löschen des SCHULJAHRES bestätigen!","Schuljahr LÖSCHEN",JOptionPane.OK_CANCEL_OPTION);

  if (delete != 0) {
      JOptionPane.showMessageDialog(null,"Abbruch des Löschvorgangs wird bestätigt!");
      schuljahrfrm.dispose();
      }
  else{
    try {
        String url = "jdbc:odbc:sba";
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
        Statement stmt = con.createStatement();
        ResultSet test_jahr_rs = stmt.executeQuery("SELECT * FROM schuljahr ORDER BY SJ");
        while (test_jahr_rs.next()) {
          test_jahr[jj]=test_jahr_rs.getString("SJ");
          int test_zahl = test_jahr[jj].compareTo(sjstr_neu);
          if (test_zahl == 0) vorhanden = true;
          jj++;
          }
        if (vorhanden == true) {
          sj_delete = "DELETE FROM schuljahr WHERE (SJ) LIKE ("+sjstr_neu+")";
          stmt.executeUpdate(sj_delete);
          JOptionPane.showMessageDialog(null,"Schuljahr "+sjstr_neu+" wurde in sba gelöscht!");
          stmt.close();
          con.close();
          }
        else {
          JOptionPane.showMessageDialog(null,"Das eingegebene Schuljahr ist nicht vorhanden! - Abbruch!");
          exit=true;
          schuljahrfrm.dispose();
          }
        }
    catch (java.lang.Exception ex){
        ex.printStackTrace();
        }

      if (vorhanden == true) {
        int eintraege=jj;
        for (jj=0;jj<eintraege;jj++){
          try {
            String url = "jdbc:odbc:sba"+test_jahr[jj];
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
            Statement stmt = con.createStatement();
            sj_delete = "DELETE FROM schuljahr WHERE (SJ) LIKE ('"+sjstr_neu+"')";
            stmt.executeUpdate(sj_delete);
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null,"Schuljahr "+sjstr_neu+" wurde in sba"+test_jahr[jj]+" gelöscht!");
            }
        catch (java.lang.Exception ex){
          JOptionPane.showMessageDialog(null,"Fehlerhafte Verbindung zur Datenbank !");
          //ex.printStackTrace();
          }
        }// Ende FOR-Schleife
        //entsprechende DATABASE LÖSCHEN
        try {
            String url = "jdbc:odbc:sba"+sjstr_neu;
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
            Statement stmt = con.createStatement();
            sj_delete = "DROP DATABASE sba"+sjstr_neu;
            stmt.executeUpdate(sj_delete);
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null,"DATABASE sba"+sjstr_neu+" wurde gelöscht!");
            }
        catch (java.lang.Exception ex){
          JOptionPane.showMessageDialog(null,"Fehlerhafte Verbindung zur Datenbank !");
          //ex.printStackTrace();
          }
      }//Ende IF-Schleife
      else if (exit == false) JOptionPane.showMessageDialog(null,"Das eingegebene Schuljahr ist nicht vorhanden!");
    }
  }

  public void abbruch() {
    schuljahrfrm.dispose();
  }

   public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();

    if (source == okbtn) einfuegen();
    if (source == loeschenbtn) loeschen();
    if (source == abbruchbtn) abbruch();
    }
}