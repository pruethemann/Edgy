/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import edgy.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Window for the Winner in gold, for the second player in silver and for the third one in bronze.
 */
public class WinGui extends Client {
  public JFrame mainFrame;
  private JLabel rank;

  /**
   * sets up the mainframe with a to the rank corresponding colored JLabel.
   *
   * @param r the Players rank
   */
  public WinGui(int r) {

    mainFrame = new JFrame("Result");
    mainFrame.setSize(400, 400);

    mainFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            mainFrame.setVisible(false);
          }
        });

    mainFrame.doLayout();
    mainFrame.setLayout(new GridLayout(1, 1));
    mainFrame.setIconImage(getLogo());
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    mainFrame.setLocation(
        dim.width / 2 - mainFrame.getSize().width / 2,
        dim.height / 2 - mainFrame.getSize().height / 2);

    rank = new JLabel();
    rank.setHorizontalAlignment(0);
    rank.setFont(new Font("Arial", Font.PLAIN, 25));
    if (r == 1) {
      rank.setText("Congrats you're the winner");
      rank.setBackground(new Color(255, 215, 0));
    } else if (r == 2) {
      rank.setText("Almost: second place");
      rank.setBackground(new Color(192, 192, 192));
    } else if (r == 3) {
      rank.setText("Exercise more: third place");
      rank.setBackground(new Color(205, 127, 50));
    } else if (r == 4) {
      rank.setText("Why are you playing this game?");
      rank.setBackground(Color.gray);
    }
    rank.setOpaque(true);

    mainFrame.add(rank);
    mainFrame.setVisible(true);
  }
}
