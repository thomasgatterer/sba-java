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
 * <p>Überschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm für die Eingabe und Bearbeitung für Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class anmeldungneu extends Frame {

   JFrame anmeldungneufrm = new JFrame();
   JPanel anmeldungneu = new JPanel();
   JLabel passwordlbl = new JLabel("Eingabe Password: "); 
   JPasswordField passwordtxt = new JPasswordField(6);
   JButton okbtn = new JButton("OK");
   JButton abbruchbtn = new JButton("Abbruch");
   boolean pruefung=false;
   String sjstr_neu;
   String lehrerneu;

  public anmeldungneu(String sjstr) {
  sjstr_neu=sjstr;
  enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit3();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

   public void jbInit3() {
   anmeldungneu.add(passwordlbl);
   anmeldungneu.add(passwordtxt);
   anmeldungneu.add(okbtn);
   anmeldungneu.add(abbruchbtn);
   okbtn.addActionListener(this);
   abbruchbtn.addActionListener(this);
   GridLayout gr = new GridLayout(2,2,10,10);
   anmeldungneu.setLayout(gr);
   anmeldungneufrm.setResizable(false);
   anmeldungneufrm.setSize(new Dimension(250, 100));
   anmeldungneufrm.setTitle("Password Abfrage");
   anmeldungneufrm.getContentPane().add(anmeldungneu);

   //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = anmeldungneufrm.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    anmeldungneufrm.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    anmeldungneufrm.setVisible(true);
  }

  public void pruefe() {

  String pwdlehrertmp="";
  char[] passwordtmp = passwordtxt.getPassword();
  int l = passwordtmp.length;
  if (l != 5) {
   JOptionPane.showMessageDialog(null,"Password inkorrekt eingeben ! \nAktion wird abgebrochen !");
   anmeldungneufrm.setVisible(false);
   }
  else {//else-Zweig 0
  //String passwordstr = "'"+passwordtmp+"'";
  String passwordstr = "";
  passwordstr = String.valueOf(passwordtmp);
  //System.out.println(passwordstr);
  passwordstr = passwordstr.substring(0,5);
  //System.out.println(passwordstr);
  try  {
      String url = "jdbc:odbc:sba"+sjstr;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Lehrer WHERE Lehrer = 'ADM'");
      while (rs.next()) {
        pwdlehrertmp = rs.getString(3);
      }
  }
  catch (java.lang.Exception ex){
      ex.printStackTrace();
    }
      int pruefepwd = passwordstr.compareTo(pwdlehrertmp);

      if (pruefepwd < 0 || pruefepwd > 0) {
       JOptionPane.showMessageDialog(null,"Password falsch !");
       anmeldungneufrm.setVisible(false);
       JOptionPane.showMessageDialog(null,"Aktion wurde abgebrochen !");
       }
      else { //else-Zweig 1
          lehrerneu = JOptionPane.showInputDialog(null,"Anmeldung als:","Anmeldung",JOptionPane.PLAIN_MESSAGE);
          int eingabe = lehrerneu.compareTo("");
          if (eingabe == 0) JOptionPane.showMessageDialog(null,"Lehrer eingeben");
          else { //else-Zweig 2
            lehrerneu = lehrerneu.toUpperCase();
            try  {
              String url = "jdbc:odbc:sba"+sjstr;
              Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
              Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
              Statement stmt = con.createStatement();
              /*String lehreralt = lehrertxt.getText();//Neueingabe
              String tmploeschen = "DELETE FROM tmp WHERE Lehrer = '"+lehreralt+"'";
              stmt.executeUpdate(tmploeschen);*/
              String tmpeingabe = "INSERT INTO tmp (Lehrer) VALUES ('"+lehrerneu+"')";
              stmt.executeUpdate(tmpeingabe) ;
            JOptionPane.showMessageDialog(null,"Neue Anmeldung erfolgreich !");
            anmeldungneufrm.setVisible(false);
            einfuegen(lehrerneu);

            con.close();
            stmt.close();
            }
            catch (java.lang.Exception ex){
              ex.printStackTrace();
            }
        }//Ende else-Zweig 2
      }//Ende else-Zweig 1
    }//Ende else-Zweig 0
  }

  public void abbruch() {
  anmeldungneufrm.setVisible(false);
  }

  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();

    if (source == okbtn) pruefe();
    if (source == abbruchbtn) abbruch();
    }
}