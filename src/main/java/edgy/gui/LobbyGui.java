/**
 * edgy.gui is a package for the graphic implementation.
 */

package edgy.gui;

import edgy.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Implementation of the mainframe with the lobbies which can be joined.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class LobbyGui extends Client {

  public JFrame mainFrame;
  private JPanel matchesPanel;
  private JPanel buttonPanel;
  private JPanel matchesPanelRight;
  private JPanel lobbiesPanel;
  private JTextArea messages;
  private static boolean inMatch = false;
  private JLabel usernameLabel;
  private JLabel myScore;

  /**
   * sets up the mainframe with the lobbies to join on the left side and the functions on the right
   * side in a grid layout.
   */
  public LobbyGui() {
    mainFrame = new JFrame("Lobby");
    mainFrame.setSize(600, 400);
    mainFrame.setLayout(new GridLayout(1, 2));

    mainFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            mainFrame.setVisible(false);
            sendToServer("quit_");
          }
        });
    mainFrame.doLayout();
    mainFrame.setIconImage(getLogo());


    matchesPanel = new JPanel();
    GridLayout grid = new GridLayout(1, 2);
    matchesPanel.setLayout(grid);
    setMatchesPanel();

    buttonPanel = new JPanel();
    GridLayout gridLayout = new GridLayout(9, 1);
    gridLayout.setVgap(7);
    buttonPanel.setLayout(gridLayout);
    buttonPanel.setBackground(Color.lightGray);
    setButtonPanel();

    mainFrame.add(matchesPanel);
    mainFrame.add(buttonPanel);
    mainFrame.setVisible(true);
  }

  /**
   * Splits the matchespanel in two columns on the left are the lobbied on the rigth the number of
   * players in the lobby and a join Button.
   */
  private void setMatchesPanel() {
    // left Part showing the existing lobbies
    lobbiesPanel = new JPanel();
    matchesPanelRight = new JPanel();

  }


  /**
   * Sets up two labels on the top with username and score with four Buttons with the following
   * purposes: logout, new match, change username, change password in the bottom there's a textarea
   * to display messages from the server.
   */
  private void setButtonPanel() {
    usernameLabel = new JLabel();
    usernameLabel.setHorizontalAlignment(0);
    usernameLabel.setText("User: " + player.getUsername());

    myScore = new JLabel();
    myScore.setHorizontalAlignment(0);
    myScore.setText("Myscore: " + player.getHighscore());

    JButton logoutButton = new JButton("Logout User");
    logoutButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            sendToServer("lgout");
            loginGUI.mainFrame.setVisible(true);
            mainFrame.setVisible(false);
            mainFrame.dispose();
          }
        });
    JButton nwmatButton = new JButton("Create New Match");
    nwmatButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (inMatch) {
              messages.setText("You already joined a match!");
            } else {
              sendToServer("nwmat");
              sendToServer("lobby");
              inMatch = true;
            }
          }
        });

    JButton highscoreButton = new JButton("Highscores");
    highscoreButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            sendToServer("highs");
          }
        });

    JButton creditsButton = new JButton("Credits");
    creditsButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            System.out.println("Credits");
            new CreditsGui();
          }
        });
    JButton changePwButton = new JButton("Change Password");
    changePwButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new ChangeGui("password");
          }
        });
    JButton changeUsernameButton = new JButton("Change Username");
    changeUsernameButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new ChangeGui("username");
          }
        });

    messages = new JTextArea();
    messages.setEditable(false);

    buttonPanel.add(usernameLabel);
    buttonPanel.add(myScore);
    buttonPanel.add(logoutButton);
    buttonPanel.add(nwmatButton);
    buttonPanel.add(highscoreButton);
    buttonPanel.add(creditsButton);
    buttonPanel.add(changeUsernameButton);
    buttonPanel.add(changePwButton);
    buttonPanel.add(messages);
  }

  /**
   * Prints the message to the textarea(from server).
   *
   * @param text text to display
   */
  public void setText(String text) {
    messages.setText(text);
  }

  public void changeUsername(String username) {
    usernameLabel.setText(username);
  }

  /**
   * sets the mainframes visibility.
   * @param visibility boolean
   */
  public void setVisibility(boolean visibility) {
    mainFrame.setVisible(visibility);
  }

  /**
   * sets boolean inMatch.
   * @param inMatch boolean to set
   */

  public static void setInMatch(boolean inMatch) {
    LobbyGui.inMatch = inMatch;
  }

  /**
   * updates the score after a finished match.
   */
  public void updateScore() {
    myScore.setText("Myscore: " + player.getHighscore());
  }

  /**
   * If this method is called, the GUI will be updated. So it is called:
   * 1. if Lobby GUI is started for the first time (Constructor)
   * 2. Every time a user joins or exits a lobby. (Done by the server)
   * @param lobbies contains the number of players in lobbies (ordered) if the only entry is 0: no
   *     lobbies have been created.
   * @param matchStatus String array containing the information whether the match is
   *                    not full yet, ongoing or finished
   */
  public void updateLobbys(int[] lobbies, String[] matchStatus) {
    /* No matches created yet */
    if (lobbies[0] == 0) {
      JLabel empty = new JLabel();
      matchesPanel.removeAll();
      empty.setHorizontalAlignment(0);
      empty.setText("no matches were created yet");
      matchesPanel.add(empty);
      matchesPanel.revalidate();
      matchesPanel.repaint();

      /* repaint all Lobbies */
    } else {
      matchesPanel.removeAll();
      lobbiesPanel.removeAll();
      matchesPanelRight.removeAll();
      lobbiesPanel.setLayout(new GridLayout(lobbies.length, 1));
      matchesPanelRight.setLayout(new GridLayout(lobbies.length, 2));

      for (int i = 0; i < lobbies.length; i++) {
        JTextField lobby = new JTextField();
        lobby.setBorder(BorderFactory.createLineBorder(Color.black));
        lobby.setHorizontalAlignment(0);
        lobby.setEditable(false);
        lobby.setText("Lobby " + (i + 1));
        lobbiesPanel.add(lobby);
      }

      updateMatchesPanelRight(lobbies, matchStatus);

      matchesPanelRight.revalidate();
      matchesPanelRight.repaint();
      lobbiesPanel.revalidate();
      lobbiesPanel.repaint();
      matchesPanel.add(lobbiesPanel);
      matchesPanel.add(matchesPanelRight);
      matchesPanel.revalidate();
      matchesPanel.repaint();

    }
  }

  /**
   * Updates the number ob players in a lobby everytime someone joins a lobby or creates a new one.
   * @param lobbies number ob players
   */
  private void updateMatchesPanelRight(int[] lobbies, String[] matchStatus) {

    for (int i = 0; i < lobbies.length; i++) {
      JTextField nrPlayers = new JTextField();
      nrPlayers.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      nrPlayers.setHorizontalAlignment(0);
      nrPlayers.setBackground(Color.lightGray);
      nrPlayers.setEditable(false);
      nrPlayers.setText(String.valueOf(lobbies[i]));
      matchesPanelRight.add(nrPlayers);

      if (lobbies[i] < 4) {
        JButton joinButton = new JButton("join " + (i + 1));
        joinButton.addActionListener(
            new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                if (inMatch) {
                  messages.setText("You can't play two matches at the same time");
                } else {
                  sendToServer("joinm&" + joinButton.getText().split(" ")[1]);
                  sendToServer("lobby");
                  inMatch = true;
                }
              }
            });
        matchesPanelRight.add(joinButton);
      } else {
        JTextField fullGame = new JTextField();
        fullGame.setEditable(false);
        fullGame.setHorizontalAlignment(0);

        /* define status of a game */
        if (matchStatus[i].equals("2")) {
          fullGame.setText("Ongoing");
        } else if (matchStatus[i].equals("3")) {
          fullGame.setText("Finished");
        }
        matchesPanelRight.add(fullGame);
      }
    }
  }
}
