/**
 * edgy.gui is a package for the graphic implementation.
 */

package edgy.gui;

import edgy.client.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Implements a graphic window for a tutorial of the game.
 */
public class TutorialGui extends Client {

  public JFrame mainFrame;
  private JPanel mainPanel;
  private JButton next;
  private BufferedImage screen;
  private BufferedImage screen2;


  /**
   * sets up the mainframe with the lobbies to join on the left side and the functions on the right
   * side in a grid layout.
   */

  public TutorialGui() {
    mainFrame = new JFrame("Tutorial-Edgy");

    mainFrame.setSize(800, 500);

    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        mainFrame.setVisible(false);
        mainFrame.dispose();
      }
    });
    mainFrame.doLayout();
    mainFrame.setIconImage(getLogo());


    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    setLocation();
    mainFrame.add(mainPanel);
    setFirst();

    mainFrame.setVisible(true);
  }

  private void setLocation() {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2,
            dim.height / 2 - mainFrame.getSize().height / 2);
  }


  private void setFirst() {
    StyleContext.NamedStyle centerStyle = StyleContext.getDefaultStyleContext().new NamedStyle();
    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);

    JTextPane text = new JTextPane();

    Font font = new Font("Aharoni", Font.BOLD, 18);
    //text.setFont(font);
    setJTextPaneFont(text, font, new Color(0,0,0));
    //mainFrame.getContentPane().add(text, BorderLayout.CENTER);
    text.setBackground(new Color(162,205,90));
    text.setLogicalStyle(centerStyle);

    text.setEditable(false);
    text.setText("Welcome to Edgy!\n\nObviously you’re new to this game "
            + "and you don’t know what to do… \n"
            + "That’s a horrible situation which \nwe will change as quickly as possible! 😊\n\n"
            + "This Tutorial will guide you through the most important steps \n"
            + "and features of this game. \n"
            + "If we weren’t precise enough, \nyou can take a glimpse in our manual \n"
            + "which is waiting for you "
            + "on our website. \n"
            + "(Only if you didn’t have enough patience \nfor already downloading it "
            + "because the game itself was too tempting….) 😉\n"
            + "Now let’s start!");
    mainPanel.add(text, BorderLayout.CENTER);
    addNextButton(1);
    // mainFrame.getContentPane().add(text, BorderLayout.CENTER);

  }

  private void setSecond() {
    mainPanel.removeAll();
    mainFrame.setSize(1000, 400);
    setLocation();
    try {
      screen = ImageIO.read(new File("src\\main\\resources\\Tutorial\\Lobby.jpg"));
    } catch (IOException e) {
      System.out.println(e);
    }
    ImagePane ip = new ImagePane();
    screen = ip.resize(screen, 492, 340);
    ip.setImage(screen);
    ip.setImageLocation(new Point(0, 0));
    JPanel center = new JPanel(new GridLayout(1, 2));
    center.add(ip);


    StyleContext.NamedStyle centerStyle = StyleContext.getDefaultStyleContext().new NamedStyle();
    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);

    JTextPane text = new JTextPane();
    Font font = new Font("Aharoni", Font.BOLD, 14);
    setJTextPaneFont(text, font, new Color(0,0,0));
    text.setBackground(new Color(151,221,255));
    text.setLogicalStyle(centerStyle);
    text.setEditable(false);
    text.setText(" First things first: You can create a new match in the lobby. \n "
            + "But you need to be four players to play a game. \n\n "
            + "If a match is already created, you can join \n "
            + "by simply pressing the following button : \n "
            + "If there are already 4 players in a match, \n you can’t join it any more. \n "
            + "But don’t worry, Edgy will tell you that :\n "
            + "If you're wondering wheter you're doing well \n you can click on "
            + "Highscores Button \n and find a list of the 32 Best Players\n "
            + "\n"
            + " Game Buttons:\n"
            + " 1) Create New Match\n"
            + " 2) Join\n"
            + " 3) Highscores");
    center.add(text);

    mainPanel.add(center, BorderLayout.CENTER);
    addNextButton(2);
    mainPanel.revalidate();
    mainPanel.repaint();

  }

  private void setThird() {
    mainPanel.removeAll();
    mainFrame.setSize(1000, 400);
    setLocation();
    try {
      screen = ImageIO.read(new File("src\\main\\resources\\Tutorial\\FirstMove.jpg"));
    } catch (IOException e) {
      System.out.println(e);
    }
    ImagePane ip = new ImagePane();
    screen = ip.resize(screen, 500, 280);
    ip.setImage(screen);
    ip.setImageLocation(new Point(0, 0));
    JPanel center = new JPanel(new GridLayout(1, 2));
    center.add(ip);

    StyleContext.NamedStyle centerStyle = StyleContext.getDefaultStyleContext().new NamedStyle();
    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);

    JTextPane text = new JTextPane();
    Font font = new Font("Aharoni", Font.BOLD, 14);
    setJTextPaneFont(text, font, new Color(0,0,0));
    text.setBackground(new Color(255,128,128));
    text.setEditable(false);
    text.setLogicalStyle(centerStyle);

    text.setText("Here is a little overview of the main game window. \nBeautiful, isn’t it ??\n"
            + "\n"
            + "The following Buttons are available:\n"
            + "1) turn left: turns the piece counterclockwise.\n"
            + "2) turn right: turns the piece clockwise.\n"
            + "3) flip: I think it's self explanatory... :)\n"
            + "4) surrender: Give up (Don't dare!).\n"
            + "5) tutorial: My humble self...\n"
            + "6) hint: shows you a option of a possible move."
            + "");
    center.add(text);

    mainPanel.add(center, BorderLayout.CENTER);
    addNextButton(3);
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  private void setFourth() {
    mainPanel.removeAll();

    mainFrame.setSize(1000, 400);
    setLocation();
    try {
      screen = ImageIO.read(new File("src\\main\\resources\\Tutorial\\Board.png"));
      screen2 = ImageIO.read(new File("src\\main\\resources\\Tutorial\\Pieces.png"));
    } catch (IOException e) {
      System.out.println(e);
    }
    ImagePane ip = new ImagePane();
    ImagePane ip2 = new ImagePane();
    screen = ip.resize(screen, 160, 160);
    screen2 = ip2.resize(screen2, 400, 160);

    JPanel centerLeft = new JPanel(new GridLayout(2, 1));
    ip.setImage(screen);
    ip.setImageLocation(new Point(centerLeft.getWidth() / 2 - ip.getWidth() / 2,
            centerLeft.getHeight() / 2 - ip.getHeight() / 2));
    ip2.setImageLocation(new Point(centerLeft.getWidth() / 2 - ip2.getWidth() / 2,
            centerLeft.getHeight() / 2 - ip2.getHeight() / 2));
    centerLeft.add(ip);
    ip2.setImage(screen2);

    JPanel centerRight = new JPanel(new GridLayout(2, 1));
    centerRight.add(ip2);

    StyleContext.NamedStyle centerStyle = StyleContext.getDefaultStyleContext().new NamedStyle();
    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);

    JTextPane text = new JTextPane();
    Font font = new Font("Aharoni", Font.BOLD, 14);
    setJTextPaneFont(text, font, new Color(0,0,0));
    text.setBackground(new Color(238,232,170));
    text.setLogicalStyle(centerStyle);
    text.setEditable(false);
    JTextPane text2 = new JTextPane();
    setJTextPaneFont(text2, font, new Color(0,0,0));
    text2.setBackground(new Color(245,222,179));
    text2.setEditable(false);
    text2.setLogicalStyle(centerStyle);

    text.setText("Here you can set your pieces. \nBy either dragging it on the board \n"
            + "or simply by first clicking on a piece and then clicking \n"
            + "on the right place on the board.");
    text2.setText("All your pieces you didn’t \nalready use are listed here. \n"
            + "You can chose one of them and put it on the board.");
    centerLeft.add(text);
    centerRight.add(text2);

    JPanel center = new JPanel(new GridLayout(1, 2));

    center.add(centerLeft);
    center.add(centerRight);

    mainPanel.add(center, BorderLayout.CENTER);
    addNextButton(4);
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  private void setFifth() {
    mainPanel.removeAll();

    mainFrame.setSize(1000, 400);
    setLocation();
    try {
      screen = ImageIO.read(new File("src\\main\\resources\\Tutorial\\Tabelle.png"));
      screen2 = ImageIO.read(new File("src\\main\\resources\\Tutorial\\Chat.png"));
    } catch (IOException e) {
      System.out.println(e);
    }
    ImagePane ip = new ImagePane();
    ImagePane ip2 = new ImagePane();
    screen = ip.resize(screen, 400, 160);
    screen2 = ip2.resize(screen2, 240, 160);

    JPanel centerLeft = new JPanel(new GridLayout(2, 1));
    ip.setImage(screen);
    ip.setImageLocation(new Point(centerLeft.getWidth() / 2 - ip.getWidth() / 2,
            centerLeft.getHeight() / 2 - ip.getHeight() / 2));
    ip2.setImageLocation(new Point(centerLeft.getWidth() / 2 - ip2.getWidth() / 2,
            centerLeft.getHeight() / 2 - ip2.getHeight() / 2));
    centerLeft.add(ip);
    ip2.setImage(screen2);

    JPanel centerRight = new JPanel(new GridLayout(2, 1));
    centerRight.add(ip2);

    StyleContext.NamedStyle centerStyle = StyleContext.getDefaultStyleContext().new NamedStyle();
    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);

    JTextPane text = new JTextPane();
    Font font = new Font("Aharoni", Font.BOLD, 14);
    setJTextPaneFont(text, font, new Color(0,0,0));
    text.setBackground(new Color(205,181,205));
    text.setEditable(false);
    text.setLogicalStyle(centerStyle);
    JTextPane text2 = new JTextPane();
    setJTextPaneFont(text2, font, new Color(0,0,0));
    text2.setBackground(new Color(205,181,205));
    text2.setLogicalStyle(centerStyle);

    text.setText("The red arrow is showing \n"
            + "you who’s turn is right now. \n"
            + "A black cross tells you when someone has surrendered. \n"
            + "In the first column you can see your rank \n"
            + "on the highscore-list over all matches \n"
            + "that were played since the beginning of Edgy !\n"
            + "In the last column \nyou can see your current score.");
    text2.setText("Tell your friends (or enemies in the game) \n"
            + "what you think about their moves. \n"
            + "(Or simply ask if it will be raining today)…");
    centerLeft.add(text);
    centerRight.add(text2);

    JPanel center = new JPanel(new GridLayout(1, 2));
    center.add(centerLeft);
    center.add(centerRight);

    mainPanel.add(center, BorderLayout.CENTER);

    addNextButton(5);
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  private void setSixth() {
    mainPanel.removeAll();

    mainFrame.setSize(1000, 400);
    setLocation();
    try {
      screen = ImageIO.read(new File("src\\main\\resources\\Tutorial\\GameRules.jpg"));
    } catch (IOException e) {
      System.out.println(e);
    }
    ImagePane ip = new ImagePane();
    screen = ip.resize(screen, 370, 330);

    ip.setImage(screen);
    ip.setImageLocation(new Point(0, 0));

    StyleContext.NamedStyle centerStyle = StyleContext.getDefaultStyleContext().new NamedStyle();
    StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);

    JTextPane text = new JTextPane();
    Font font = new Font("Aharoni", Font.BOLD, 14);
    setJTextPaneFont(text, font, new Color(0,0,0));
    text.setBackground(new Color(238,203,173));
    text.setLogicalStyle(centerStyle);

    text.setEditable(false);
    text.setText("The Rules :\n"
            + "1.Your first move must be in one of the free corners.\n"
            + "2.Each of your pieces must be touching a corner of another of your pieces. "
            + "(Doesn’t matter which corner.)\n"
            + "3.Your pieces aren’t allowed to touch a side of another of your pieces.\n"
            + "4.Pieces aren’t allowed to overlap.\n\n"
            + ""
            + "Congratulations ! Now you know everything you need to play Edgy like a pro !\n"
            + "Let’s play now !!!!");

    JPanel center = new JPanel(new GridLayout(1, 2));
    center.add(ip);
    center.add(text);

    mainPanel.add(center, BorderLayout.CENTER);

    addNextButton(6);
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  private void addNextButton(int i) {
    next = new JButton("Next>>");
    next.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        switch (i) {
          case 1:
            setSecond();
            break;
          case 2:
            setThird();
            break;
          case 3:
            setFourth();
            break;
          case 4:
            setFifth();
            break;
          case 5:
            setSixth();
            break;
          case 6:
            mainFrame.dispose();
            break;
          default:
            break;
        }
      }
    });
    mainPanel.add(next, BorderLayout.SOUTH);
  }

  /**
   * Utility method for setting the font and color of a JTextPane. The * result is roughly
   * equivalent to calling setFont(...) and * setForeground(...) on an AWT TextArea.
   * @param jtp JTextPane
   * @param font font how the text should be written
   * @param c color the JTextPane should get
   */
  public static void setJTextPaneFont(JTextPane jtp, Font font, Color c) {
    // Start with the current input attributes for the JTextPane. This
    // should ensure that we do not wipe out any existing attributes
    // (such as alignment or other paragraph attributes) currently
    // set on the text area.
    MutableAttributeSet attrs = jtp.getInputAttributes();

    // Set the font family, size, and style, based on properties of
    // the Font object. Note that JTextPane supports a number of
    // character attributes beyond those supported by the Font class.
    // For example, underline, strike-through, super- and sub-script.
    StyleConstants.setFontFamily(attrs, font.getFamily());
    StyleConstants.setFontSize(attrs, font.getSize());
    StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
    StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);

    // Set the font color
    StyleConstants.setForeground(attrs, c);

    // Retrieve the pane's document object
    StyledDocument doc = jtp.getStyledDocument();

    // Replace the style for the entire document. We exceed the length
    // of the document by 1 so that text entered at the end of the
    // document uses the attributes.
    doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
  }



}

