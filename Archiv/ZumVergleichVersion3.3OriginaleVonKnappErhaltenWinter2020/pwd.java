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
 * <p>Überschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm für die Eingabe und Bearbeitung für Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class pwd extends Frame {

int i=0, anz=0, x=48, y=65, z=97, a=57, b=90, tmp=6;
int id[] = new int[100];
String sql;
String leh[] = new String[100];
String lehpwd[] = new String[100];

  public pwd(String sjstr) {

  try  {
      String url = "jdbc:odbc:sba"+sjstr;
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      Connection con = DriverManager.getConnection(url,"lehrer","jon96as12");
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Lehrer ORDER BY Lehrer");
      while (rs.next()) {
      id[i] = rs.getInt(1);
      leh[i] = rs.getString(2);
      lehpwd[i] = rs.getString(3);
      i++;
      }
      //Code für Password
      anz=i;
      int j=1;
      for(i=0;i<anz;i++) {
      if(tmp<0) tmp=6;
      id[i]=j;
      leh[i]=leh[i];
      if(x>56) x=48; x++;
      if(y>89) y=65; else y++;
      if(z>121) z=97; else z++;
      if(a<49) a=57; else a--;
      if(b<66) b=90; else b--;
      if(tmp==0) lehpwd[i] =""+(char)x+(char)y+(char)z+(char)a+(char)b;
      if(tmp==1)lehpwd[i] =""+(char)y+(char)a+(char)x+(char)z+(char)b;
      if(tmp==2)lehpwd[i] =""+(char)z+(char)b+(char)a+(char)x+(char)y;
      if(tmp==3)lehpwd[i] =""+(char)x+(char)a+(char)b+(char)z+(char)y;
      if(tmp==4)lehpwd[i] =""+(char)b+(char)a+(char)z+(char)y+(char)x;
      if(tmp==5)lehpwd[i] =""+(char)a+(char)x+(char)y+(char)b+(char)z;
      if(tmp==6)lehpwd[i] =""+(char)b+(char)x+(char)y+(char)z+(char)b;
      j++;tmp--;
      }
      for(i=0;i<anz;i++) {
      sql = "UPDATE Lehrer SET ID = "+id[i]+",Password = '"+lehpwd[i]+"' WHERE Lehrer ='"+leh[i]+"'";
      stmt.executeUpdate(sql);
      }
      con.close();
    }
    catch (java.lang.Exception ex){
      ex.printStackTrace();
      }
    JOptionPane.showMessageDialog(null,"Aktion: Password Neuvergabe beendet");
  }
}