/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import edgy.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * SkipWindow to prevent users to skip by just clicking the Button.
 *
 * @author while(true){do nothing}.
 * @version 1.0
 */
public class SkipGui extends Client {
  /** field for JFrame. */
  private JFrame mainFrame;

  /** constructor sets up JFrame with JLabel and two Buttons. */
  public SkipGui() {
    mainFrame = new JFrame("Surrender");
    mainFrame.setSize(300, 200);

    mainFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            disposeFrame();
          }
        });
    mainFrame.doLayout();
    mainFrame.setLayout(new FlowLayout());
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    mainFrame.setLocation(
        dim.width / 2 - mainFrame.getSize().width / 2,
        dim.height / 2 - mainFrame.getSize().height / 2);
    mainFrame.setIconImage(getLogo());

    JLabel title = new JLabel();
    title.setHorizontalAlignment(0);
    title.setText("Are you sure you want to surrender?");
    title.setBorder(BorderFactory.createEmptyBorder(50, 30, 4, 30));

    JButton no = new JButton("no!");

    no.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            disposeFrame();
          }
        });

    JButton yes = new JButton("yes!");
    yes.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            sendToServer("skipp");
            disposeFrame();
          }
        });

    mainFrame.add(title);
    mainFrame.add(no);
    mainFrame.add(yes);
    mainFrame.setVisible(true);
  }

  /** method destroys JFrame. */
  private void disposeFrame() {
    mainFrame.setVisible(false);
    mainFrame.dispose();
  }
}
