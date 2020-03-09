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

public class lh extends Frame {

  public lh(String ID, String nrin, String lehrer, String sjstr) {

  int klcb_row = klcb.getItemCount();
  int j,i;
  String nrtmp = "";
  String lehrertmp = "";
  String sql = "";

  i=0;
  for (j=0;j<klcb_row;j++) {
  klcb.setSelectedIndex(j);
  klassecb = (String) klcb.getSelectedItem();
    try {
        String url = "jdbc:odbc:sba"+sjstr;
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
        Statement stmt = con.createStatement();
        ResultSet lhrs = stmt.executeQuery("SELECT * FROM "+klassecb);
        while (lhrs.next()) {
          nrtmp = lhrs.getString(2);
          lehrertmp = lhrs.getString(7);
          int nrtmp_zahl = nrtmp.compareTo(nrin);
          int lehrertmp_zahl = lehrertmp.compareTo(lehrer);
          if (nrtmp_zahl == 0 && lehrertmp_zahl == 0) i++;
        }
    }
    catch (java.lang.Exception ex){
        ex.printStackTrace();
        }
    }
    try {
        String url = "jdbc:odbc:sba"+sjstr;
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
        Statement stmt = con.createStatement();
        if (i>0) JOptionPane.showMessageDialog(null,"Buch ist weiterhin als Lehrerhandexemplar vorhanden !");
        else {
          sql = "DELETE FROM LehrerHand WHERE ID = "+ID;
          stmt.executeUpdate(sql);
          JOptionPane.showMessageDialog(null,"BuchNr "+nrin+" wurde als Lehrerhandexemplar gelöscht !");
        }
    }
    catch (java.lang.Exception ex){
        ex.printStackTrace();
    }
  }
}