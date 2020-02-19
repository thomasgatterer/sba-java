package sba;


import java.sql.*;
import java.util.Scanner;
import java.io.*;

   public class ConnectDB
   {
	Connection connec = null;
  //private String URL = "jdbc:mysql://10.0.0.17/sba2019test";
  //private String USER = "lehrer";
  //private String PASSWORD = "jon96as12";
  //private String JDBCDRIVER = "com.mysql.cj.jdbc.Driver";

  public ConnectDB()
  {
    Scanner in;
    String[] conArray = new String[10];
    try {
      in = new Scanner(new FileReader(new File("").getAbsolutePath() + "/sba/data.txt"));
      int i = 0;
      while(in.hasNext()) {
        conArray[i] = in.next();
        i++;
      }
      in.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try
    {
		  try {
        Class.forName(conArray[3]);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
		  connec = DriverManager.getConnection(conArray[0], conArray[1], conArray[2]);
        System.out.println ("Database connection established");
    }
    catch (Exception e)
    {
      System.err.println ("Cannot connect to database server");
    }
  }

	public Connection getDBconnection(){
		return connec;
	}
}
