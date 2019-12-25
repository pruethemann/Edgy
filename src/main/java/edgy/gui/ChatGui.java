/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import edgy.client.Client;
import edgy.client.Maths;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * displays the chat in GUI.
 *
 * @author while(true) do nothing;
 * @version 1.0
 */
public class ChatGui extends Client {

  private JPanel mainPanel;

  private JLabel statusLabel;
  private JPanel controlPanel;
  private JTextArea textArea;
  private JScrollPane scroll;
  private JTextField message;
  private Dimension dim;

  /**
   * Constructor sets the mainPanel and adds the matchNumber and username.
   *
   * @param dim Dimension of the users screen.
   */
  public ChatGui(Dimension dim) {
    this.dim = dim;
    prepareGui();
  }

  /** Function sets up the mainPanel the Labels and some of the contents. */
  private void prepareGui() {
    mainPanel = new JPanel();
    // mainPanel.setSize(500, 500);
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBorder(
        new EmptyBorder(
            0,
            dim.width / 2 / 28 + dim.width / 2 / 14 * 11 / 6,
            dim.height * 8 / 100,
            dim.width / 2 / 7));

    // initiating labels
    statusLabel = new JLabel("", JLabel.CENTER);
    statusLabel.setSize(350, 100);

    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setColumns(40);
    textArea.setRows(15);
    scroll = new JScrollPane(textArea);
    scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());

    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    // add everything to the mainframe
    mainPanel.add(scroll, BorderLayout.CENTER);
    mainPanel.add(controlPanel, BorderLayout.SOUTH);
    mainPanel.setVisible(true);
  }

  /**
   * displays the message in the text field.
   *
   * @param msg the string to be displayed
   */
  public void displayMessage(String msg) {
    String text = textArea.getText();
    if (text.equals("")) {
      textArea.setText(msg);
    } else {
      String print = text + "\n" + msg;
      textArea.setText(print);
    }
  }

  /**
   * gets the user input from the text field in case user clicks sendButton and sends it to the
   * server after parsing.
   *
   * @return the main panel.
   */
  public JPanel showChat() {

    // creating Textfield and sendButton
    message = new JTextField(33);
    message.addKeyListener(new CustomKeyListener());
    JButton sendButton = new JButton("Send");
    // Actionlistener to send a Message
    sendButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String msg = message.getText();
            send(msg);
          }
        });
    controlPanel.add(message);
    controlPanel.add(sendButton);
    mainPanel.setVisible(true);
    return mainPanel;
  }

  /**
   * parses a string (public-, private message) sends it to the server.
   *
   * @param msg message to send
   */
  private void send(String msg) {
    if (msg.equals("")) {
      return;
    }
    /* sends message to everyone */
    if (msg.startsWith("/public")) {
      sendToServer("nonmg&" + msg.substring(8));

      /* sends a whisper message */
    } else if (msg.startsWith("@")) {
      displayMessage(msg);
      /* determine the recipient */
      String recipient = msg.split(" ")[0].substring(1);
      String[] output = msg.split(" ", 2);
      sendToServer("prmsg&" + recipient + "&" + output[1]);

      /* returns a hint */
    } else if (msg.startsWith("/hint")) {
      sendToServer("hint_");

      /* Does what it says */
    } else if (msg.startsWith("/trololo")) {
      sendToServer("trolo");

      /* Predicts a prime number */
    } else if (isNumber(msg)) {
      displayMessage(Maths.isPrime(Integer.parseInt(msg)));

      /* Predicts a prime number */
    } else if (msg.startsWith("/skip")) {
      /* determine player for forced skip */
      String recipient = msg.split(" ")[1];
      sendToServer("force&" + recipient);

      /* Kill a client */
    } else if (msg.startsWith("/kill")) {
      /* determine player for forced skip */
      String recipient = msg.split(" ")[1];
      //String[] output = msg.split(" ", 2);
      sendToServer("force&" + recipient);
      sendToServer("kill_&" + recipient);

    } else {
      sendToServer("lomsg&" + msg);
    }
    message.setText("");
  }

  private boolean isNumber(String number) {
    try {
      Integer.parseInt(number);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Inner class implementing the class KeyListener and customizing it.
   *
   * @see KeyListener Keylistener to send messages
   */
  class CustomKeyListener implements KeyListener {

    /**
     * method inherited from the class KeyListener. Called when a key is typed (pressed and
     * immediately released).
     *
     * @param e actual position of the key listener.
     */
    public void keyTyped(KeyEvent e) {}

    /**
     * sends a written message.
     *
     * @param e KeyEvent
     */
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        String msg = message.getText();
        send(msg);
      }
    }

    /**
     * method inherited from the class KeyListener. Called when a key is released.
     *
     * @param e KeyEvent
     */
    public void keyReleased(KeyEvent e) {}
  }
}
