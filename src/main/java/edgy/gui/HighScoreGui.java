/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import edgy.client.Client;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Implementation of a window for the Highscore-table with sorted score for the 32 best players.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class HighScoreGui extends Client {

  private JFrame mainFrame;
  private JPanel highScore;

  /**
   * creates a mainFrame containing the username, score and onlineStatus of the players in the user
   * database.
   *
   * @param username String array containing all usernames
   * @param score String array containing all scores
   * @param onlineStatus String array containing the information of the online status
   */
  public HighScoreGui(String[] username, String[] score, String[] onlineStatus) {
    mainFrame = new JFrame("Highscores");
    mainFrame.setSize(500, 650);
    mainFrame.setLocation(700, 0);

    mainFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            mainFrame.setVisible(false);
          }
        });
    mainFrame.doLayout();
    mainFrame.setIconImage(getLogo());
    highScore = new JPanel();
    highScore.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    updateHighscore(username, score, onlineStatus);
    mainFrame.setVisible(true);
  }

  /**
   * method that updates the information in the HighScoreGui if it was already instanced.
   *
   * @param usernames Sting array containing all usernames
   * @param points Sting array containing all points
   * @param online String array containing the information of the online status
   */
  public void updateHighscore(String[] usernames, String[] points, String[] online) {
    highScore.removeAll();

    int n = usernames.length;
    if (n > 33) {
      n = 33;
    }
    int x = 150 / n;

    highScore.setLayout(new GridLayout(n, 4));
    setLegend();

    Color color = new Color(255, 215, 0);
    for (int i = 0; i < n - 1; i++) {
      if (i == 1) {
        color = new Color(192, 192, 192);
      }
      if (i == 2) {
        color = new Color(205, 127, 50);
      } else if (i > 2) {
        color = new Color(255 - i * x, 255 - i * x, 255);
      }
      JTextField rank = new JTextField();
      rank.setBackground(color);
      rank.setText(Integer.toString(i + 1));
      rank.setEditable(false);
      highScore.add(rank);

      JTextField name = new JTextField();
      name.setBackground(color);
      name.setText(usernames[i]);
      name.setEditable(false);
      highScore.add(name);

      JTextField actualScore = new JTextField();
      actualScore.setBackground(color);
      actualScore.setText(points[i]);
      actualScore.setEditable(false);
      highScore.add(actualScore);

      JTextField onlineStatus = new JTextField();
      onlineStatus.setBackground(color);
      if (online[i].equals("1")) {
        onlineStatus.setText("online");
      } else {
        onlineStatus.setText("");
      }
      onlineStatus.setEditable(false);
      highScore.add(onlineStatus);
    }
    highScore.revalidate();
    highScore.repaint();
    mainFrame.add(highScore);
    mainFrame.revalidate();
    mainFrame.repaint();
    mainFrame.setVisible(true);
  }

  /**
   * four JLabels with either ranks, usernames, scorer or online statuses. Used for displaying the
   * table for the HightscoreGUI.
   */
  private void setLegend() {
    JLabel legend1 = new JLabel();
    legend1.setHorizontalAlignment(0);
    legend1.setFont(new Font("Arial", Font.PLAIN, 25));
    legend1.setText("rank");
    legend1.setBackground(Color.lightGray);
    highScore.add(legend1);

    JLabel legend2 = new JLabel();
    legend2.setHorizontalAlignment(0);
    legend2.setFont(new Font("Arial", Font.PLAIN, 25));
    legend2.setText("name");
    legend2.setBackground(Color.lightGray);
    highScore.add(legend2);

    JLabel legend3 = new JLabel();
    legend3.setHorizontalAlignment(0);
    legend3.setFont(new Font("Arial", Font.PLAIN, 25));
    legend3.setText("score");
    legend3.setBackground(Color.lightGray);
    highScore.add(legend3);

    JLabel legend4 = new JLabel();
    legend4.setHorizontalAlignment(0);
    legend4.setFont(new Font("Arial", Font.PLAIN, 25));
    legend4.setText("online");
    legend4.setBackground(Color.lightGray);
    highScore.add(legend4);
  }

}
