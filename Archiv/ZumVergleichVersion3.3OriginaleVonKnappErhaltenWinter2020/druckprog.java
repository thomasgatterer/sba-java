package sba;

import java.awt.*;
//import java.awt.event.*;
import java.awt.print.*;
import java.awt.print.PageFormat;
//import javax.swing.*;
//import java.net.URL;
//import java.sql.*;
import java.io.*;
import java.util.*;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.Driver;
//import java.sql.Statement;
//import java.sql.Driver;
//import java.sql.DriverManager;
//import java.sql.ResultSet;


/**
 * <p>Überschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm für die Eingabe und Bearbeitung für Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class druckprog extends Frame implements Printable {

private static final int SKALIERFAKTOR = 1;
private PrinterJob pjob;
private PageFormat seitenFormat;
private String druckText;

  //Konstruktor
  public druckprog(String druckText) {
  this.pjob = PrinterJob.getPrinterJob();
  this.druckText = druckText;
  }
  //öffentliche Methoden
  public boolean setupPageFormat() {
  PageFormat defaultPF = pjob.defaultPage();
  this.seitenFormat = pjob.pageDialog(defaultPF);
  pjob.setPrintable(this, this.seitenFormat);
  return (this.seitenFormat != defaultPF);
  }
  public boolean setupJobOptions() {
  return pjob.printDialog();
  }
  public void drucke() throws PrinterException, IOException {
  pjob.print();
  }

  //Implementierung von Printable
  //Methode print() überschreiben
  public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
  int druckErgeb = PAGE_EXISTS;
  // Abbruch des Druckjobs, wenn Seitenzahl >1
  if(page > 0) return NO_SUCH_PAGE;
  String line = null;
  Graphics2D g2 = (Graphics2D)g;
  g2.scale(1.0 / SKALIERFAKTOR, 1.0 / SKALIERFAKTOR);
  int ypos = (int)pf.getImageableY() * SKALIERFAKTOR;
  int xpos = ((int)pf.getImageableX() + 1) * SKALIERFAKTOR;
  int yd = 12 * SKALIERFAKTOR;
  int ymax = ypos + (int)pf.getImageableHeight() * SKALIERFAKTOR - yd;
  //Seitentitel ausgeben
  ypos += yd;
  Date timestamp = new Date();
  String day = new Date(timestamp.getTime()).toString();
  g2.setColor(Color.black);
  g2.setFont(new Font("Times", Font.TRUETYPE_FONT, 10 * SKALIERFAKTOR));
  g.drawString("Seite " + (page + 1), xpos+420, ypos);
  g.drawString("Datum: " + day,xpos,ypos);
  ypos = ypos+20;

  StringTokenizer newline;
  StringTokenizer tab;
  String druckTexttmp="";
  String druckZeile;

  newline = new StringTokenizer(druckText,"\n");
  boolean seitendruck = druckText.startsWith("Berechnung");
  int tokens = newline.countTokens();
  int xtmp=xpos;
  int ytmp=ypos;
  int langZeile = 0, langZeiletmp = 0;

  while (tokens>0) {
    int laengetmp = 0;
    druckTexttmp = newline.nextToken();
    tab = new StringTokenizer(druckTexttmp,"\t");
    int tabs = tab.countTokens();
    int tabs_anzahl = tabs;

    for (tabs=0;tabs<tabs_anzahl;tabs++) {
      druckZeile = tab.nextToken();
      langZeiletmp = druckZeile.length();System.out.println(langZeile);
      langZeile = langZeile + langZeiletmp;
      int laenge = druckZeile.length();
        if (seitendruck == true) {
          if (tabs == 0) g.drawString(druckZeile,xpos,ypos);
          if (tabs == 1) g.drawString(druckZeile,xpos+80,ypos);
          if (tabs == 2) g.drawString(druckZeile,xpos+160,ypos);
          if (tabs == 3) g.drawString(druckZeile,xpos+240,ypos);
          if (tabs == 4) g.drawString(druckZeile,xpos+320,ypos);
        }
        else if (tabs == 0) g.drawString(druckZeile,xpos,ypos);
        else if (tabs == 1) g.drawString(druckZeile,xpos+60,ypos);
        else if (tabs == 2) g.drawString(druckZeile,xpos+260,ypos);
        else if (tabs == 3) g.drawString(druckZeile,xpos+300,ypos);
        else if (tabs == 4) g.drawString(druckZeile,xpos+330,ypos);
        else if (tabs == 5) g.drawString(druckZeile,xpos+360,ypos);
        else if (tabs == 6) g.drawString(druckZeile,xpos+400,ypos);
        else if (tabs == 7) g.drawString(druckZeile,xpos+440,ypos);
      }
    ypos=ypos+10;xpos=xtmp;
    tokens--;
  }
  g.drawString("Ausdruck zur Schulbuchaktion "+(char)169+" by A.Knapp",xpos,ymax);
  //Erneuter Aufruf von print()
  return druckErgeb;
  }
}