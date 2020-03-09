package sba;

import javax.swing.UIManager;
import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import java.net.URL;
//import java.sql.*;
//import java.io.*;
//import java.util.*;

/**
 * <p>�berschrift: Schulbuchaktion</p>
 * <p>Beschreibung: Programm f�r die Eingabe und Bearbeitung f�r Schulbuchreferenten</p>
 * <p>Copyright: Copyright (c) 2003/04 A.Knapp</p>
 * <p>Organisation: AGI</p>
 * @author Andreas Knapp
 * @version 1.2
 */

public class sba {
  boolean packFrame = false;
  //boolean anmeldung_ok = false;

  //Die Anwendung konstruieren
  public sba() {
    Frame frame = new Frame ();
    //Frames �berpr�fen, die voreingestellte Gr��e haben
    //Frames packen, die nutzbare bevorzugte Gr��eninformationen enthalten, z.B. aus dem Layout
    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }

  //Main-Methode
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      new anmeldung();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    //new sba();
  }
}