/**
 * edgy.gui is a package for the graphic implementation.
 */

package edgy.gui;

import edgy.client.Client;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * Implementation for Errors.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class ErrorGui extends Client {
  private JFrame mainFrame;
  private JLabel title;
  private JTextArea messages;

  /** sets up the mainframe with a JLabel on top and a text area in the bottom. */
  public ErrorGui() {

    mainFrame = new JFrame("Errors");
    mainFrame.setSize(600, 400);

    mainFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            mainFrame.setVisible(false);
            mainFrame.dispose();
          }
        });
    mainFrame.doLayout();
    mainFrame.setLayout(new GridLayout(2, 1));

    title = new JLabel();
    title.setHorizontalAlignment(0);
    title.setText("You got an error!");

    messages = new JTextArea();
    messages.setEditable(false);
    mainFrame.setIconImage(getLogo());

    mainFrame.add(title);
    mainFrame.add(messages);
    mainFrame.setVisible(true);
  }

  /**
   * prints the message to the text area(from server).
   * @param text text to display
   */
  public void setText(String text) {
    messages.setText(text);
  }
}
