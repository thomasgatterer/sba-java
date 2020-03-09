package sba;

//import java.awt.*;
//import java.awt.event.*;
//import java.awt.print.*;
import javax.swing.*;
//import java.net.URL;
import java.sql.*;
import java.io.*;
//import java.util.*;

/**
 * <p>Überschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm für die Eingabe und Bearbeitung für Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class druck_kla_buch_matrix extends Frame {

  public druck_kla_buch_matrix(String sjstr) {

  int klcb_row = klcb.getItemCount();
  String[] klasse = new String[40];
  int kl=0;
  int j, i, anzahl=0, buch_ges=0, klassen_ges=0;
  int buch_anzahl=0;
  String sql="";
  boolean letztes_buch=true;

  JOptionPane.showMessageDialog(null,"Hinweis: Kla_Buch_Matrix ist von Druck/Buchlisten abhängig!");

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
    FileWriter klassenfile = new FileWriter("C:/db_sba/Kla_Buch_Matrix.xls");

    try { // try 1
      String url = "jdbc:odbc:sba"+sjstr;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();

      ResultSet count_klassen = stmt.executeQuery("SELECT * FROM klassen");
      while(count_klassen.next()) klassen_ges++;
      count_klassen.close();

      ResultSet count_buecher = stmt.executeQuery("SELECT * FROM Buecher_gesamt");
      while(count_buecher.next()) buch_ges++;
      count_buecher.close();

      ResultSet rs = stmt.executeQuery("SELECT * FROM Buecher_gesamt ORDER BY Nr, Klasse");

      klassenfile.write("Klassen-Buch-Matrix ZUR AUSGABE DER BESTELLTEN BÜCHER "+date+"\n");
      klassenfile.write(text);
      klassenfile.write("Bücherliste zur Schulbuchaktion ("+(char)169+" A.Knapp)\n");
      klassenfile.write("Das Ergebnis der Abfrage:\nNr.\tKurztitel\tPreis\tFach\tAnz.\tWert");

      for(j=0;j<klcb_row;j++) {
        klcb.setSelectedIndex(j);
        klassecb = (String)klcb.getSelectedItem();
        klassestr = klassecb;
        klasse[kl] = klassestr;
        klassenfile.write("\t"+klassestr);
        kl++;
        }
      klassenfile.write("\n");
      double[] b = new double[1000]; int z=0; //Zwischenwert zur Nr. Abfrage
      String[] b_tmp = new String[1000];
      String[] c = new String[1000];
      double[] d = new double[1000];
      String[] dd = new String[1000];
      String[] e = new String[1000];
      int[] f = new int[1000];
      double[] g = new double[1000];
      String[] h = new String[1000];
      int tmp_anz=0; double tmp_wert=0;
      int[] store_Zahl = new int[1000];
      String[] store_Klasse = new String[1000];
      int st = 0, zaehler = 0, store_Zahl_tmp=0;

          while (rs.next()) {//Beginn WHILE
            b[z] = rs.getDouble("Nr");
            b_tmp[z] = ""+b[z];
            c[z] = rs.getString("Titel");
            d[z] = rs.getDouble("Preis");
            dd[z] = ""+d[z]; dd[z] = dd[z].replace('.',',');
            e[z] = rs.getString("Fach");
            f[z] = rs.getInt("Anzahl");
            g[z] = rs.getDouble("Wert");
            h[z] = rs.getString("Klasse");

            boolean schreibe=true;

            if (z==0) {
              tmp_anz=f[0];
              tmp_wert=g[0];
              store_Zahl_tmp=f[z];
              store_Zahl[st] = f[z];
              store_Klasse[st] = h[z];
              st++;
              zaehler++;
              schreibe=false;
              }
            if(z>0) {
            int nr_pruef = b_tmp[z].compareTo(""+b_tmp[z-1]);

            int kl_pruef_glKl = h[z].compareTo(""+h[z-1]);
            if (kl_pruef_glKl == 0 && nr_pruef == 0) {
              tmp_anz = tmp_anz + f[z];
              tmp_wert = tmp_wert + g[z];
              store_Zahl[st-1] = store_Zahl[st-1] + f[z];
              store_Klasse[st-1] = h[z];
              schreibe = false;
              }
            else{

            if (nr_pruef == 0) {
              tmp_anz = tmp_anz + f[z];
              tmp_wert = tmp_wert + g[z];
              store_Zahl[st] = f[z];
              store_Klasse[st] = h[z];
              st++;
              zaehler++;
              schreibe=false;
              if (z==buch_ges-1){schreibe=true;letztes_buch=false;}
              }//ENDE if(pruefe ...)

            }

            if (schreibe==true) {
              double wert1 = (tmp_wert*1000)-((int)(tmp_wert*1000));
                if (wert1 < 0.5) tmp_wert = tmp_wert*1000;
                else tmp_wert = (tmp_wert*1000)+1;
                int werttmp = (int)tmp_wert;
                tmp_wert = (double)werttmp/1000;

                int index = 0;
                if (b_tmp[z-1].length()<10) {index = b_tmp[z-1].indexOf(".0"); b_tmp[z-1]=b_tmp[z-1].substring(0,index);}
                if (b_tmp[z-1].length()>=10) {index = b_tmp[z-1].indexOf("E"); b_tmp[z-1]=b_tmp[z-1].substring(0,index);b_tmp[z-1]=b_tmp[z-1].substring(0,1)+b_tmp[z-1].substring(2,index);}
                klassenfile.write("\n"+b_tmp[z-1] + "\t" + c[z-1] + "\t" + dd[z-1] + "\t" + e[z-1] + "\t" + tmp_anz +"\t"+ tmp_wert);

                //letztes Buch schreiben ?
                if (z==buch_ges-1 && letztes_buch == true) {
                  wert1 = (g[z]*1000)-((int)(g[z]*1000));
                  if (wert1 < 0.5) g[z] = g[z]*1000;
                  else g[z] = (g[z]*1000)+1;
                  werttmp = (int)g[z];
                  g[z] = (double)werttmp/1000;

                  index = 0;
                  if (b_tmp[z].length()<10) {index = b_tmp[z].indexOf(".0"); b_tmp[z]=b_tmp[z].substring(0,index);}
                  if (b_tmp[z].length()>=10) {index = b_tmp[z].indexOf("E"); b_tmp[z]=b_tmp[z].substring(0,index);b_tmp[z]=b_tmp[z].substring(0,1)+b_tmp[z].substring(2,index);}
                  klassenfile.write("\n"+b_tmp[z] + "\t" + c[z] + "\t" + dd[z] + "\t" + e[z] + "\t" + f[z] +"\t"+ g[z]);
                }//Ende letztes Buch schreiben ?

                st=0;
                for (kl=0;kl<klassen_ges;kl++) {
                  if (st<zaehler) {
                    int klasse_pruef = klasse[kl].compareTo(store_Klasse[st]);
                    if (klasse_pruef == 0) {
                      klassenfile.write("\t"+store_Zahl[st]);
                      st++;
                      }
                    else {
                      klassenfile.write("\t");
                      }
                    }//Ende IF (st<zaehler)
                  }//Ende FOR

                st=0;
                zaehler=0;

                store_Zahl[st]=f[z];
                store_Klasse[st]=h[z];
                st++;
                zaehler++;
                tmp_anz=f[z];
                tmp_wert=g[z];
                buch_anzahl++;
                }
              }//Ende if(schreibe==true)
              z++;//Ende if (z>0)
            }//Ende WHILE

        klassenfile.write("\nAnzahl der bestellten Bücher: "+buch_anzahl+"\n \n");

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
  JOptionPane.showMessageDialog(null,"Es wurden "+buch_ges+" Bücher eingegeben auf C:/db_sba/Kla_Buch_Matrix.xls geschrieben");
  }
}