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

public class pwdneu extends Frame {

 JFrame pwfrm = new JFrame();
 JPanel pw = new JPanel();
 JLabel pwaltlbl = new JLabel("altes Password: ");
 JPasswordField pwalttxt = new JPasswordField(5);
 JLabel pwneulbl = new JLabel("neues Password: ");
 JPasswordField pwneutxt = new JPasswordField(5);
 JLabel pwneubestlbl = new JLabel("neues Password bestätigen: ");
 JPasswordField pwneubesttxt = new JPasswordField(5);
 JTextField pwalthiddentxt = new JTextField(7);
 JTextField pwneuhiddentxt = new JTextField(7);
 JTextField pwneubesthiddentxt = new JTextField(7);
 JButton okbtn = new JButton("OK");
 JButton cancelbtn = new JButton("Cancel");
 String sjstr_neu;
 String lehrer_neu;

  public pwdneu(String lehrer, String sjstr) {
  sjstr_neu=sjstr;
  lehrer_neu=lehrer;
  enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit1();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

   public void jbInit1() {
   pw.add(pwaltlbl);
   pw.add(pwalttxt);
   pw.add(pwneulbl);
   pw.add(pwneutxt);
   pw.add(pwneubestlbl);
   pw.add(pwneubesttxt);
   pw.add(okbtn);
   pw.add(cancelbtn);
   okbtn.addActionListener(this);
   cancelbtn.addActionListener(this);
   GridLayout gr = new GridLayout(4,1,10,10);
   pw.setLayout(gr);
   pwfrm.setResizable(false);
   pwfrm.setSize(new Dimension(350, 200));
   pwfrm.setTitle("Password ändern");
   pwfrm.getContentPane().add(pw);

   //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = pwfrm.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    pwfrm.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    pwfrm.setVisible(true);
  }

  public void aendern() {
    String sql = "", pwALTstr = "", pwNEUstr = "", pwNEUBESTstr = "";
    char[] pwaltstr, pwneustr, pwneubeststr;
    boolean gefunden = false;
    boolean gueltig = false;

    pwaltstr = pwalttxt.getPassword();
    //pwalthiddentxt.setText("'"+pwaltstr+"'");
    pwalthiddentxt.setText(String.valueOf(pwaltstr));
    pwALTstr = pwalthiddentxt.getText();
    int pwALTstr_length = pwALTstr.length();

    if (pwALTstr_length == 5) {
    //pwALTstr = pwALTstr.substring(1,6);

    pwneustr = pwneutxt.getPassword();
    //pwneuhiddentxt.setText("'"+pwneustr+"'");
    pwneuhiddentxt.setText(String.valueOf(pwneustr ));
    pwNEUstr = pwneuhiddentxt.getText();
    int pwNEUstr_length = pwNEUstr.length();

    if (pwNEUstr_length == 5) {
    //pwNEUstr = pwNEUstr.substring(1,6);

    pwneubeststr = pwneubesttxt.getPassword();
    //pwneubesthiddentxt.setText("'"+pwneubeststr+"'");
    pwneubesthiddentxt.setText(String.valueOf(pwneubeststr));
    pwNEUBESTstr = pwneubesthiddentxt.getText();
    int pwNEUBESTstr_length = pwNEUBESTstr.length();

    if (pwNEUBESTstr_length == 5) {
    //pwNEUBESTstr = pwNEUBESTstr.substring(1,6);

    int testpw = pwNEUBESTstr.compareTo(pwNEUstr);

    if (testpw == 0) {

    char a = pwNEUstr.charAt(0); int aint = (byte)a;
    char b = pwNEUstr.charAt(1); int bint = (byte)b;
    char c = pwNEUstr.charAt(2); int cint = (byte)c;
    char d = pwNEUstr.charAt(3); int dint = (byte)d;
    char e = pwNEUstr.charAt(4); int eint = (byte)e;

    if( (aint < 58 && aint > 47) || (aint < 91 && aint > 64) || (aint < 123 && aint > 96)) gueltig = true; else gueltig = false;
    if( ((bint < 58 && bint > 47) || (bint < 91 && bint > 64) || (bint < 123 && bint > 96)) && gueltig == true) gueltig = true; else gueltig = false;
    if( ((cint < 58 && cint > 47) || (cint < 91 && cint > 64) || (cint < 123 && cint > 96)) && gueltig == true) gueltig = true; else gueltig = false;
    if( ((dint < 58 && dint > 47) || (dint < 91 && dint > 64) || (dint < 123 && dint > 96)) && gueltig == true) gueltig = true; else gueltig = false;
    if( ((eint < 58 && eint > 47) || (eint < 91 && eint > 64) || (eint < 123 && eint > 96)) && gueltig == true) gueltig = true; else gueltig = false;

    int pwneuTest = pwNEUstr.compareTo(pwNEUBESTstr);

    if (gueltig == true && pwneuTest == 0) {

     try {
      String url = "jdbc:odbc:sba"+sjstr_neu;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();

      //1. Versuch PasswordTest
      ResultSet test = stmt.executeQuery("SELECT * FROM Lehrer WHERE Lehrer = '"+lehrer_neu+"'");
      while(test.next()) {
      String pwdb = test.getString(3);
      int pwtest_zahl = pwdb.compareTo(pwALTstr);
      if (pwtest_zahl == 0) gefunden = true;
      }
      if (gefunden == true) {
        sql = "UPDATE Lehrer SET Password = '"+pwNEUstr+"' WHERE Lehrer = '"+lehrer_neu+"'";
        stmt.executeUpdate(sql);
        stmt.close();
        con.close();
        JOptionPane.showMessageDialog(null,"Password wurde erfolgreich geändert !");
        pwfrm.setVisible(false);
        }
      else JOptionPane.showMessageDialog(null,"Benutzername und altes Password stimmen nicht überein !");
      }
     catch (java.lang.Exception ex){
JOptionPane.showMessageDialog(null,"Im Schuljahr "+sjstr_neu+" keine Änderung möglich ! \nGrund: "+ex.toString().substring(72,120));
      //ex.printStackTrace();
      }
    }
    else JOptionPane.showMessageDialog(null,"Fehler bei der Eingabe !! \nSie dürfen nur Zahlen von 0 - 9 , Großbuchstaben A - Z und Kleinbuchstaben a - z , aber keine Umlaute verwenden !");

    } //Ende if-Abfrage für Übereinstimmung der neuen Passwörter
    else JOptionPane.showMessageDialog(null,"Das neu eingegebene Password stimmt nicht mit dem bestätigten neuen Password überein !");
    } //Ende if-Abfrage für Eingabe neues Password
    else JOptionPane.showMessageDialog(null,"Sie müssen für das neue Password genau 5 Zeichen eingeben !");
    }// Ende if-Abfrage für Anzahl der Zeichen
    else JOptionPane.showMessageDialog(null,"Für das neue Password bitte genau 5 Zeichen eingeben !");
  } // Ende if-Abfrage für Eingabe altes Password
  else JOptionPane.showMessageDialog(null,"Sie müssen das alte Password eingeben !");
  }

  public void cancel() {
  pwalttxt.setText("");
  pwneutxt.setText("");
  pwneubesttxt.setText("");
  pwfrm.setVisible(false);
  }

  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();

    if (source == okbtn) aendern();
    if (source == cancelbtn) cancel();
    }
}