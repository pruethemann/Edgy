/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import edgy.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Implementation of the Graphic interface for the Login window.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class ChangeGui extends Client {

  /** the panes used to display the chat. */
  public JFrame mainFrame;

  private JLabel headerLabel;
  private JPanel controlPanel;
  private JPanel buttonPanel;
  private TextArea textArea;
  private JTextField userText;
  private boolean change = false;
  JButton changeButton;
  JPasswordField passwordText;

  /** Set up main components of the window.
   * @param gui String containing the username
   */
  public ChangeGui(String gui) {

    if (gui.equals("username")) {
      change = true;
    }

    mainFrame = new JFrame("Change " + gui);
    mainFrame.setSize(400, 400);
    mainFrame.setLayout(new GridLayout(4, 1));
    mainFrame.setIconImage(getLogo());

    mainFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            mainFrame.setVisible(false);
          }
        });
    headerLabel = new JLabel("Choose your new " + gui, JLabel.CENTER);

    // prints messages from server
    textArea = new TextArea();
    textArea.setEditable(false);

    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    mainFrame.add(headerLabel);
    mainFrame.add(controlPanel);
    mainFrame.add(buttonPanel);
    mainFrame.add(textArea);
    mainFrame.setVisible(true);
    show();
  }

  /** sets up the texts and the Buttons with its actionlisteners and sends it to the server. */
  public void show() {

    if (change) {
      userText = new JTextField(9);
      userText.setText("");
      userText.addKeyListener(new LoginKeyListener());
      controlPanel.add(userText);

    } else {
      passwordText = new JPasswordField(9);
      passwordText.setEchoChar('*');
      passwordText.addKeyListener(new LoginKeyListener());
      controlPanel.add(passwordText);
    }

    changeButton = new JButton("Change");

    buttonPanel.add(changeButton);
    mainFrame.setVisible(true);
    buttonListener();
  }

  /**
   * function listens to buttonclicks.
   *
   * @see ActionListener
   */
  public void buttonListener() {
    changeButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (change) {
              send("cuser");
            } else {
              send("chpwd");
            }
          }
        });
  }

  /**
   * Sends the input for the username and password to the server letting check it.
   *
   * @param command String to define whether user wants to login or register
   */
  private void send(String command) {
    if (change) {
      if (userText.getText().equals("")) {
        textArea.setText("Please enter a username!");
      } else {
        String output = command + "&" + userText.getText();
        sendToServer(output);
      }
    } else {
      if (new String(passwordText.getPassword()).equals("")) {
        textArea.setText("Please enter a Password!");
      } else {
        String output = command + "&" + new String(passwordText.getPassword());
        sendToServer(output);
      }
    }
    mainFrame.setVisible(false);
    mainFrame.dispose();
  }

  /**
   * Sets up the message from the server in the textarea.
   *
   * @param text string to display
   */
  public void setText(String text) {
    textArea.setText(text);
  }

  /**
   * Todo: explanation. Keylistener class so the Player can login through pressing enter.
   *
   * @see KeyListener
   */
  class LoginKeyListener implements KeyListener {
    /**
     * Function inherited from superclass doing nothing.
     *
     * @param e KeyEvent
     * @see KeyListener
     */
    public void keyTyped(KeyEvent e) {}

    /**
     * Sends login to the server if Enter is pressed.
     *
     * @param e the occured KeyEvent
     * @see KeyListener
     */
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        if (change) {
          send("cuser");
        } else {
          send("chpwd");
        }
      }
    }

    /**
     * Function inherited from superclass doing nothing.
     *
     * @param e KeyEvent
     * @see KeyListener
     */
    public void keyReleased(KeyEvent e) {}
  }
}
