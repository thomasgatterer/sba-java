package sba;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * <p>�berschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm f�r die Eingabe und Bearbeitung f�r Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class druck_schulstufe extends Frame {

  public druck_schulstufe(String sjstr) {

  String Tab = "\t", Tab1 = "\t", Tab2="\t", Tab3="\t", TabNr = "", Tabh = "";

  //Datumseingabe
  String date = JOptionPane.showInputDialog(null,"Datum:","Datumseingabe",JOptionPane.QUESTION_MESSAGE);
  try {
  int dateint = date.compareTo("");
  if (dateint < 0 || dateint > 0) date = "vom "+date;
  }
  catch (java.lang.Exception ex) {
  date="";
  }
  //Infotexteingabe
  String text = JOptionPane.showInputDialog(null,"Text:","Info Text",JOptionPane.QUESTION_MESSAGE);
  try {
  int textint = text.compareTo("");
  if (textint < 0 || textint > 0) text = text+"\n \n";
  }
  catch (java.lang.Exception ex) {
  text="";
  }

  try {//try 0
    FileWriter klassenfile = new FileWriter("C:/db_sba/Schulstufeliste.doc");

    try { // try 1
      String url = "jdbc:odbc:sba"+sjstr;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Schulstufe ORDER BY Klasse,Nr");

          klassenfile.write("LISTE F�R DEN Buchh�ndler ZUR AUSGABE DER BESTELLTEN B�CHER "+date+"\n\n");
          klassenfile.write(text);
          klassenfile.write("B�cherliste zur Schulbuchaktion ("+(char)169+" A.Knapp)\n\n");
          klassenfile.write("Das Ergebnis der Abfrage:\nNr.\t\tKurztitel d. Buches\t\t\tPreis\tFach\tAnz.\tStufe\n\n"+

          "--------------------------------------------------------------------------------\n\n");
          double b;
          String c;
          double d;
          String e;
          int f;
          String h;

          while (rs.next()) {
            b = rs.getDouble("Nr");
            String b_tmp = ""+b;
            c = rs.getString("Titel");
            d = rs.getDouble("Preis");
            e = rs.getString("Fach");
            f = rs.getInt("Anzahl");
            h = rs.getString("Klasse");

                int Nr_laenge = b_tmp.length();
                if (Nr_laenge < 8) TabNr = "\t\t";
                else TabNr = "\t";
                int cl = c.length();
                if (cl > 35) c = c.substring(0,35);
                cl = c.length();
                cl = 35-cl; if (cl >= 30) Tab1="\t\t\t\t\t\t"; else if (cl >= 24 && cl < 30) Tab1="\t\t\t\t\t"; else if (cl >= 18 && cl < 24) Tab1="\t\t\t\t"; else if (cl >= 12 && cl < 18) Tab1="\t\t\t"; else if (cl >= 6 && cl < 12) Tab1 = "\t\t"; else if (cl < 6) Tab1 = "\t";
                int index = 0;
                if (b_tmp.length()<10) {index = b_tmp.indexOf(".0"); b_tmp=b_tmp.substring(0,index);}
                if (b_tmp.length()>=10) {index = b_tmp.indexOf("E"); b_tmp=b_tmp.substring(0,index);b_tmp=b_tmp.substring(0,1)+b_tmp.substring(2,index);}
                klassenfile.write(b_tmp + TabNr + c + Tab1 + d + Tab2 + e + Tab3 + f +"\t"+ h + "\n");
            }
        rs.close();
        stmt.close();
        con.close();
        }//Ende try 1
        catch (java.lang.Exception ex) {
          ex.printStackTrace();
          }
   klassenfile.close();
   } //Ende try 0
   catch (Exception exception) {
    exception.printStackTrace();
    }
  JOptionPane.showMessageDialog(null,"Es wurde die Schulstufeliste geschrieben auf C:/db_sba/Schulstufeliste.doc");
  }
}