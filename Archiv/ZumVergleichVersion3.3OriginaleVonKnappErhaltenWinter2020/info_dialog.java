package sba;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.io.*;
import java.util.*;

public class info_dialog extends Frame {

   JFrame info_dialogfrm = new JFrame();
   JPanel info_dialogpn0 = new JPanel();
   JLabel info_dialoglbl = new JLabel("Wollen Sie wirklich ein NEUES SCHULJAHR anlegen?");
   JButton okbtn = new JButton("JA");
   JButton abbruchbtn = new JButton("NEIN");
   int insert=0;

  public info_dialog() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      try {
        jbInit6();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

  public void jbInit6() {

   info_dialogpn0.add(info_dialoglbl);
   info_dialogpn0.add(okbtn);
   info_dialogpn0.add(abbruchbtn);
   okbtn.addActionListener(this);
   abbruchbtn.addActionListener(this);
   FlowLayout flground = new FlowLayout();
   info_dialogpn0.setLayout(flground);
   info_dialogfrm.setResizable(false);
   info_dialogfrm.setSize(new Dimension(450, 100));
   info_dialogfrm.setTitle("Schuljahr anlegen/löschen");
   info_dialogfrm.getContentPane().add(info_dialogpn0);

   //Das Fenster zentrieren
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = info_dialogfrm.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    info_dialogfrm.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    info_dialogfrm.setVisible(true);
  }

  public void einfuegen_ok() {
    insert = 1;
    info_dialogfrm.dispose();
    }

  public void abbruch_ok() {
    insert = 0;
    info_dialogfrm.dispose();
    }

  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();

    if (source == okbtn) einfuegen_ok();
    if (source == abbruchbtn) abbruch_ok();
    }

}