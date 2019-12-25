/**
 * edgy.gui is a package for the graphic implementation.
 */

package edgy.gui;

import edgy.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Implementation of the mainframe with the lobbies which can be joined.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class CreditsGui extends Client {

  private JFrame mainFrame;
  private JPanel mainPanel;


  /**
   * sets up the mainframe with the lobbies to join on the left side and the functions on the right
   * side in a grid layout.
   */
  public CreditsGui() {
    mainFrame = new JFrame("Credits");
    mainFrame.setSize(500, 300);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        setVisibility(false);
        mainFrame.dispose();
      }
    });
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2,
            dim.height / 2 - mainFrame.getSize().height / 2);
    mainFrame.setIconImage(getLogo());
    mainFrame.doLayout();
    mainFrame.setBackground(Color.BLACK);
    mainPanel = new JPanel();

    mainPanel.setLayout(new GridLayout(4, 2, 0, 0));
    mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 80));
    mainFrame.setBackground(Color.BLACK);
    mainPanel.setOpaque(true);
    setMainPanel();
    mainFrame.add(mainPanel);
    mainFrame.setVisible(true);
  }

  /**
   * Sets up the mainPanel with eight JLabels.
   */
  private void setMainPanel() {
    JLabel projectManager = new JLabel();
    projectManager.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    projectManager.setHorizontalAlignment(SwingConstants.RIGHT);
    projectManager.setVerticalAlignment(0);
    projectManager.setText("Projectmanager");
    projectManager.setForeground(Color.white);
    projectManager.setBackground(Color.BLACK);
    projectManager.setOpaque(true);
    mainPanel.add(projectManager);

    JLabel peter = new JLabel();
    peter.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    peter.setVerticalAlignment(0);
    peter.setHorizontalAlignment(SwingConstants.LEFT);
    peter.setText("Peter Rüthemann");
    peter.setForeground(Color.white);
    peter.setBackground(Color.BLACK);
    peter.setOpaque(true);
    mainPanel.add(peter);

    JLabel mechanics = new JLabel();
    mechanics.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    mechanics.setHorizontalAlignment(SwingConstants.RIGHT);
    mechanics.setVerticalAlignment(0);
    mechanics.setText("Mechanics creator");
    mechanics.setForeground(Color.white);
    mechanics.setBackground(Color.BLACK);
    mechanics.setOpaque(true);
    mainPanel.add(mechanics);

    JLabel stephiM = new JLabel();
    stephiM.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    stephiM.setVerticalAlignment(0);
    stephiM.setHorizontalAlignment(SwingConstants.LEFT);
    stephiM.setText("Stephanie Maier");
    stephiM.setForeground(Color.white);
    stephiM.setBackground(Color.BLACK);
    stephiM.setOpaque(true);
    mainPanel.add(stephiM);

    JLabel database = new JLabel();
    database.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    database.setVerticalAlignment(0);
    database.setHorizontalAlignment(SwingConstants.RIGHT);
    database.setText("Database responsible");
    database.setForeground(Color.white);
    database.setBackground(Color.BLACK);
    database.setOpaque(true);
    mainPanel.add(database);

    JLabel stephiB = new JLabel();
    stephiB.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    stephiB.setVerticalAlignment(0);
    stephiB.setHorizontalAlignment(SwingConstants.LEFT);
    stephiB.setText("Stephanie Brogli");
    stephiB.setForeground(Color.white);
    stephiB.setBackground(Color.BLACK);
    stephiB.setOpaque(true);
    mainPanel.add(stephiB);

    JLabel qm = new JLabel();
    qm.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    qm.setVerticalAlignment(0);
    qm.setHorizontalAlignment(SwingConstants.RIGHT);
    qm.setText("Quality Manager");
    qm.setForeground(Color.white);
    qm.setBackground(Color.BLACK);
    qm.setOpaque(true);
    mainPanel.add(qm);

    JLabel tobias = new JLabel();
    tobias.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    tobias.setVerticalAlignment(0);
    tobias.setHorizontalAlignment(SwingConstants.LEFT);
    tobias.setText("Tobias Christ");
    tobias.setForeground(Color.white);
    tobias.setBackground(Color.BLACK);
    tobias.setOpaque(true);
    mainPanel.add(tobias);


  }


  /**
   * sets the mainframes visibility.
   *
   * @param visibility boolean
   */
  public void setVisibility(boolean visibility) {
    mainFrame.setVisible(visibility);
  }
  
}
