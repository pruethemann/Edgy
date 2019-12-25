/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import edgy.client.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Implementation of the Graphic interface for the Login window.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class LoginGui extends Client {

  /** the panes used to display the chat. */
  public JFrame mainFrame;

  private JLabel headerLabel;
  private JPanel controlPanel;
  private JPanel buttonPanel;
  private TextArea textArea;
  private JTextField userText;
  JButton loginButton;
  JButton registerButton;
  JPasswordField passwordText;

  /** Set up main components of the window. */
  public LoginGui() {
    mainFrame = new JFrame("Login");
    mainFrame.setSize(400, 400);
    mainFrame.setLayout(new GridLayout(4, 1));
    mainFrame.setIconImage(getLogo());

    mainFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            mainFrame.setVisible(false);
            sendToServer("quit_");
          }
        });
    headerLabel = new JLabel("", JLabel.CENTER);

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
  }

  /** sets up the texts and the Buttons with its actionlisteners and sends it to the server. */
  public void show() {
    headerLabel.setText("Login user or create a new user");

    userText = new JTextField(9);
    userText.setText("");
    passwordText = new JPasswordField(9);
    passwordText.setEchoChar('*');
    userText.addKeyListener(new LoginKeyListener());
    passwordText.addKeyListener(new LoginKeyListener());

    JLabel namelabel = new JLabel("Username: ", JLabel.RIGHT);
    loginButton = new JButton("Login");
    registerButton = new JButton("Register");
    controlPanel.add(namelabel);
    controlPanel.add(userText);

    JLabel passwordLabel = new JLabel("Password: ", JLabel.CENTER);
    controlPanel.add(passwordLabel);
    controlPanel.add(passwordText);
    buttonPanel.add(loginButton);
    buttonPanel.add(registerButton);
    mainFrame.setVisible(true);
    buttonListener();
  }

  /**
   * function listens to buttonclicks.
   *
   * @see ActionListener
   */
  public void buttonListener() {
    loginButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            send("login");
          }
        });

    registerButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            send("nuser");
          }
        });
  }

  /**
   * Sends the input for the username and password to the server letting check it.
   *
   * @param command String to define whether user wants to login or register
   */
  private void send(String command) {
    if (userText.getText().equals("") || new String(passwordText.getPassword()).equals("")) {
      textArea.setText("Please enter a username and Password!");
    } else {
      String output =
          command + "&" + userText.getText() + "&" + new String(passwordText.getPassword());
      sendToServer(output);
    }
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
        send("login");
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
