/**
 * edgy.gui is a package for the graphic implementation.
 */

package edgy.gui;

import edgy.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * class to display the game with Java Swing/Awt.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class MatchGui extends Client {

  /**
   * Initiate rolling Server Logger.
   */
  private static Logger log = LogManager.getLogger(MatchGui.class.getName());

  /**
   * all private fields the MatchGui is composed of like the mainFrame as the essential component
   * and the component it is composed of such as the board and the piecesGui. Additionally, the
   * class contains information received from the server about the game.
   */
  private JFrame mainFrame;

  private JLayeredPane layeredPane;
  private ImagePane dragPane;

  private JPanel boardPanel;
  private BoardGui board;
  private JPanel buttonPanel;
  private JPanel highscore;
  private DataOutputStream dataOutputStream;
  private ChatGui chatGui;
  private PiecesGui piecesGui;
  private String matchNumber;
  private Color color;
  private String playerColor;
  private BufferedImage arrow;
  private BufferedImage skip;
  private String currentBoardString;

  private Dimension dim;
  private int currentImagePaneNumber;
  private int previousImagePaneNumber;
  private int suggestedPane;
  private BufferedImage draggedImage;

  /**
   * Constructor calls prepareGui to set up a primary GUI containing a mouse that listens to user
   * input.
   *
   * @param matchNumber the actual matches number
   * @param col         Color of the player that is needed
   *                    for displaying the pieces in the right color.
   * @see MouseAdapter
   */
  public MatchGui(String matchNumber, String col) {
    this.matchNumber = matchNumber;
    this.playerColor = col;
    this.previousImagePaneNumber = 1;
    this.currentImagePaneNumber = 0;
    this.suggestedPane = 0;
    this.currentBoardString = null;
    this.dragPane = new ImagePane();
    this.draggedImage = null;
    prepareGui();
    try {
      String home = new java.io.File(".").getCanonicalPath();
      String pathArrow = Paths.get(home, "src", "main", "resources", "Arrow.png").toString();
      String pathCross = Paths.get(home, "src", "main", "resources", "Cross.png").toString();

      arrow = ImageIO.read(new File(pathArrow));
      skip = ImageIO.read(new File(pathCross));
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    MouseAdapter mouseHandler =
            new MouseAdapter() {
          private Point delta;
          private Point dragPoint;
          private Point holdPoint;
          //private Point originalPosition;

          /**
           * Implementation to click on a piece with the mouse
           * and perform actions with the selected
           * piece such as moving it to the board..
           *
           * @param e MouseEvent
           */
          @Override
              public void mouseClicked(MouseEvent e) {
            setComponentCoordinates(boardPanel);

            int[] quadrant = board.calculateRightQuadrant(e.getPoint());
          // move if currentImagePane previously selected and click now on board
          if (quadrant != null && currentImagePaneNumber != 0) {
              sendToServer(
                          "moved&"
                                  + Integer.toString(currentImagePaneNumber)
                                  + "&"
                                  + quadrant[1]
                                  + "&"
                                  + quadrant[0]);
              piecesGui.getImages().get(currentImagePaneNumber).resetBorder();

              // quadrant = null;

              previousImagePaneNumber = 0;
              currentImagePaneNumber = 0;
            } else if (quadrant != null && currentImagePaneNumber == 0) {
              // return if click on board but no currentImagePane previously selected
              return;
            } else if (piecesGui.calculateRightPiece(e.getPoint()) == 0) {
              // sounds.playPieceWrong();
              // return if mouseClick was not on a valid piece (from before: click not on board)
              return;
            } else {
              // click must be on a piece
              currentImagePaneNumber = piecesGui.calculateRightPiece(e.getPoint());
              // change border to currently selected image
              if (previousImagePaneNumber != 0) {
                piecesGui.getImages().get(previousImagePaneNumber).resetBorder();
              }
              // reset border of suggested image pane
              if (suggestedPane != 0) {
                piecesGui.getImages().get(suggestedPane).resetBorder();
                // sounds.playPlace();
                suggestedPane = 0;
              }
              piecesGui.getImages().get(currentImagePaneNumber).setBorder();
              previousImagePaneNumber = currentImagePaneNumber;
            }
          }

          @Override
              public void mousePressed(MouseEvent e) {
            setComponentCoordinates(boardPanel);

            // return if mouseClick was not on a valid piece
            if (piecesGui.calculateRightPiece(e.getPoint()) == 0) {
            return;
            }

            // set right border
            currentImagePaneNumber = piecesGui.calculateRightPiece(e.getPoint());
            if (previousImagePaneNumber != 0) {
            piecesGui.getImages().get(previousImagePaneNumber).resetBorder();
          }
            piecesGui.getImages().get(currentImagePaneNumber).setBorder();
            previousImagePaneNumber = currentImagePaneNumber;

            holdPoint = piecesGui.calculateHold(e.getPoint());

            draggedImage = piecesGui.getImages().get(currentImagePaneNumber).getImage();

            Point currentImagePoint =
                        piecesGui.getImages().get(currentImagePaneNumber).getImageLocation();

            dragPoint =
                        new Point(
                                (int) Math.floor(currentImagePoint.getX()
                                        + piecesGui.getRowCoordinatesPiece()),
                                (int)
                                        Math.floor(
                                                currentImagePoint.getY()
                                                        + piecesGui.getColumnCoordinatesPiece()));

            // set ImageLocation of ImagePane on top
            // layer to where the one on the bottom layer is
            dragPane.setVisible(true);
            dragPane.setImageLocation(dragPoint);
            dragPane.setImage(piecesGui.getImages().get(currentImagePaneNumber).getImage());

            // replace image in pieceGui with empty image
            piecesGui.deletePiece(String.valueOf(currentImagePaneNumber));
            //piecesGui.getImages().get(currentImagePaneNumber).putEmptyPiece();

            delta = new Point(e.getPoint().x - dragPoint.x, e.getPoint().y - dragPoint.y);
          }

          @Override
              public void mouseDragged(MouseEvent e) {
          if (delta != null) {
          Point currentImagePoint = e.getPoint();
          currentImagePoint.translate(-delta.x, -delta.y);
          // draggedImage = piecesGui.getImages().get(currentImagePaneNumber).getImage();
          // piecesGui.getImages().get(currentImagePaneNumber).setImage(null);

          dragPane.setImageLocation(currentImagePoint);
            }
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            delta = null;
          if (PiecesGui.getImages().get(currentImagePaneNumber) == null) {
            } else {
              PiecesGui.getImages().get(currentImagePaneNumber).resetBorder();

              Point relativePoint = calculateRightPoint(e.getPoint(), holdPoint);
              int[] quadrant = board.calculateRightQuadrant(relativePoint);
              if (quadrant != null && currentImagePaneNumber != 0) {
                sendToServer(
                            "moved&"
                                    + Integer.toString(currentImagePaneNumber)
                                    + "&"
                                    + quadrant[1]
                                    + "&"
                                    + quadrant[0]);
                dragPane.setVisible(false);
                return;
              }
              resetChoice();
              dragPane.setVisible(false);
            }
          }
        };

    boardPanel.addMouseListener(mouseHandler);
    boardPanel.addMouseMotionListener(mouseHandler);
  }

  /**
   * method that resets the previously selected piece to the original state
   * where nothing was selected.
   */
  public void resetChoice() {
    try {
      piecesGui.getImages().get(currentImagePaneNumber).setImage(draggedImage);
      piecesGui.repaint();
      currentImagePaneNumber = 0;
    } catch (NullPointerException e) {
      log.info("Piece was not dragged but inserted via click.");
    }
  }

  // calculates where the middle of the piece that was dragged actually is relative to the board
  private Point calculateRightPoint(Point release, Point hold) {
    // add half of the size of a piece (because middle of piece is needed)
    int pieceSize = piecesGui.getPieceSize();
    release.translate(pieceSize / 2, pieceSize / 2);
    // subtract where the piece is held
    Point middle = new Point(release.x - hold.x, release.y - hold.y);
    return middle;
  }

  /**
   * Method that sets up primary GUI components in mainFrame.
   *
   * @see WindowAdapter
   */
  private void prepareGui() {
    mainFrame = new JFrame("Match");
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    mainFrame.setLayout(new GridLayout(1, 2));
    mainFrame.addKeyListener(new CustomKeyListener());
    mainFrame.setFocusable(true);
    mainFrame.addWindowListener(
          new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
              mainFrame.setVisible(false);
              /* disconnect from match */
              sendToServer("skipp");
              sendToServer("quit_");
            }
          });
    mainFrame.doLayout();
    mainFrame.setIconImage(getLogo());
    mainFrame.setUndecorated(true);


    this.layeredPane = new JLayeredPane();

    boardPanel = new JPanel();
    boardPanel.setLayout(new FlowLayout());
    dim = Toolkit.getDefaultToolkit().getScreenSize();
    // mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2,
    // dim.height/2-mainFrame.getSize().height/2);
    boardPanel.setBorder(new EmptyBorder(dim.height / 100 * 8, 0, 0, 0));
    setBoardPanel();

    buttonPanel = new JPanel();
    GridLayout gridLayout = new GridLayout(2, 1);
    buttonPanel.setLayout(gridLayout);
    buttonPanel.setBackground(Color.lightGray);
    setButtonPanel();

    boardPanel.setBounds(0, 0, dim.width / 2, dim.height / 2);
    buttonPanel.setBounds(0, dim.width / 2, dim.width / 2, dim.height / 2);

    boardPanel.setOpaque(false);
    buttonPanel.setOpaque(false);
    boardPanel.setVisible(true);
    buttonPanel.setVisible(true);

    boardPanel.setBackground(color);
    boardPanel.setLocation(0, 0);
    boardPanel.setSize(dim.width / 2, dim.height);

    buttonPanel.setBackground(color);
    buttonPanel.setLocation(dim.width / 2, 0);
    buttonPanel.setSize(dim.width / 2, dim.height);

    layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
    layeredPane.add(buttonPanel, JLayeredPane.DEFAULT_LAYER);

    dragPane.setBounds(0, 0, dim.width, dim.height);
    dragPane.setOpaque(false);
    dragPane.setVisible(true);
    dragPane.setBackground(color);
    // change the location where the dragPane can be visible
    dragPane.setLocation(0, 0);
    dragPane.setSize(dim.width, dim.height);
    // layeredPane.add(dragPane, JLayeredPane.DRAG_LAYER);
    layeredPane.add(dragPane, JLayeredPane.DRAG_LAYER);

    mainFrame.add(layeredPane);

    // mainFrame.add(boardPanel);
    // mainFrame.add(buttonPanel);
    mainFrame.setVisible(true);
  }

  /**
   * method that sets the boardpanel (left part of the GUI).
   */
  private void setBoardPanel() {
    board = new BoardGui((int) Math.floor(84 / 38) * dim.height / 100);
    // board.setBoardSize((int)Math.floor(84/38)*dim.height/100);
    boardPanel.add(board, JLayeredPane.PALETTE_LAYER);

    piecesGui = new PiecesGui(playerColor, (int) Math.floor(84 / 38) * dim.height / 100);
    // piecesGui.setPieceGap((int)Math.floor(84/38)*dim.height/100/9);
    // piecesGui.setPieceSize((int)Math.floor(84/38)*dim.height/100*5);
    boardPanel.add(piecesGui);
  }

  /**
   * method that leads to the boardGUI being updated.
   *
   * @param boardString string from server with color information of single quadrants.
   */
  public void updateBoard(String boardString) {
    currentBoardString = boardString;
    ((BoardGui) board).updateBoard(boardString);
  }

  /**
   * creates the ButtonPanel in a Gridlayout, with chat in the Bottom and the highscores and action.
   * buttons on the top.
   *
   * @see ActionListener
   */
  private void setButtonPanel() {
    JPanel upper = new JPanel();
    upper.setBorder(
            new EmptyBorder(dim.height * 8 / 100, dim.width / 2 / 28, 0, dim.width / 2 / 7));
    GridLayout grid = new GridLayout(2, 1, 0, 0);
    upper.setLayout(grid);

    highscore = new JPanel();
    GridLayout gbl = new GridLayout(5, 3);
    highscore.setLayout(gbl);

    sendToServer("rankm");

    JPanel buttons = new JPanel(new GridLayout(2, 2, 15, 15));
    buttons.setBorder(
            new EmptyBorder(
                    dim.height * 11 / 100 / 5, dim.width / 2 / 14 * 11 / 6,
                    dim.height * 7 / 100, 0));
    JButton left = new JButton("turn left");
    left.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if (currentImagePaneNumber != 0) {
                sendToServer("turnl&" + currentImagePaneNumber);
              }
            }
          });
    JButton right = new JButton("turn right");
    right.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if (currentImagePaneNumber != 0) {
                sendToServer("turnr&" + currentImagePaneNumber);
              }
            }
          });
    JButton flip = new JButton("flip");
    flip.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if (currentImagePaneNumber != 0) {
                sendToServer("flipp&" + currentImagePaneNumber);
              }
            }
          });
    JButton skip = new JButton("surrender");
    skip.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              new SkipGui();
            }
          });
    JButton tutorial = new JButton("tutorial");
    tutorial.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              new TutorialGui();
            }
          });
    JButton hint = new JButton("hint");
    hint.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              sendToServer("hint_");
            }
          });
    buttons.add(left);
    buttons.add(right);
    buttons.add(flip);
    buttons.add(skip);
    buttons.add(tutorial);
    buttons.add(hint);

    upper.add(highscore);
    upper.add(buttons);

    buttonPanel.add(upper);
    chatGui = new ChatGui(dim);
    buttonPanel.add(chatGui.showChat());
  }

  /**
   * sets the mainframes visibility to true.
   */
  public void show() {
    mainFrame.setVisible(true);
  }

  /**
   * method that assigns the coordinates of the BoardGui and the PieceGUI on the JPanel. It is
   * assumed that the first two components are a board and a piece.
   *
   * @param jpanel of which the coordinates of the first two components get calculated.
   */
  private void setComponentCoordinates(JPanel jpanel) {
    int componentNr = jpanel.getComponentCount();

    if (componentNr >= 2) {
      board.setRowCoordinatesBoard(jpanel.getComponent(0).getX());
      board.setColumnCoordinatesBoard(jpanel.getComponent(0).getY());

      piecesGui.setRowCoordinatesPiece(jpanel.getComponent(1).getX());
      piecesGui.setColumnCoordinatesPiece(jpanel.getComponent(1).getY());

    } else {
      log.error(
              "Coordinates of BoardGui and PiecesGui could not be calculated "
                      + "because there are less than two components on the JPanel.");
    }
  }

  /**
   * changes the color to set the highscore panel.
   */
  private void setColor() {
    Color lb = new Color(51, 130, 255, 255);
    if (color == Color.yellow) {
      color = Color.red;
    } else if (color == Color.red) {
      color = lb;
    } else if (color == Color.green) {
      color = Color.yellow;
    } else {
      color = Color.green;
    }
  }

  /**
   * Prints messag on the chat.
   *
   * @param msg message
   */
  public void printMsg(String msg) {
    chatGui.displayMessage(msg);
  }


  /**
   * Sets MatchGui to fullscreen.
   */
  private void setMainFrameUndecorated() {
    // mainFrame.setVisible(false);
    mainFrame.dispose();
    mainFrame.setUndecorated(false);
    mainFrame.setVisible(true);
  }

  /**
   * sets MatchGui decorated.
   */
  private void setMainFrameDecorated() {
    // mainFrame.setVisible(false);
    mainFrame.dispose();
    mainFrame.setUndecorated(true);
    mainFrame.setVisible(true);
  }

  /**
   * sets the legend on the highscore panel.
   */
  private void setLegend() {
    JPanel legend12 = new JPanel(new GridLayout(1, 2));

    JPanel legend1 = new JPanel();
    /*legend1.setText("status");
    legend1.setBackground(Color.lightGray);
    legend1.setEditable(false);*/
    legend12.add(legend1);

    JTextField legend2 = new JTextField();
    legend2.setText("rank");
    legend2.setBackground(Color.lightGray);
    legend2.setEditable(false);
    legend12.add(legend2);

    highscore.add(legend12);

    JTextField legend3 = new JTextField();
    legend3.setText("name");
    legend3.setBackground(Color.lightGray);
    legend3.setEditable(false);
    highscore.add(legend3);

    JTextField legend4 = new JTextField();
    legend4.setText("score");
    legend4.setBackground(Color.lightGray);
    legend4.setEditable(false);
    highscore.add(legend4);
  }

  /**
   * Updates the highScore panel.
   *
   * @param ranks     the players ranks
   * @param usernames the players usernames
   * @param points    the players points
   * @param status    String containing the statuses of the four players (turn, skip, wait)
   */
  public void updateRank(String[] ranks, String[] usernames, String[] points, String[] status) {
    highscore.removeAll();
    setLegend();
    color = Color.yellow;
    for (int i = 0; i < 4; i++) {
      // JTextField turn=new JTextField();
      ImagePane turn = new ImagePane();

      int height = (int) Math.floor((double) highscore.getHeight() / 5);
      turn.setImageLocation(new Point(dim.width / 2 / 28 + highscore.getWidth() / 6 / 7, 0));
      if (status[i].equals("turn")) {
        int arrowWidth = (int) Math.floor(height * (double) 80 / 55);
        arrow = turn.resize(arrow, arrowWidth, height);
        turn.setImage(arrow);
      }

      if (status[i].equals("skipped")) {
        skip = turn.resize(skip, height, height);
        turn.setImage(skip);
      }

      JPanel statusRank = new JPanel(new GridLayout(1, 2));

      // turn.setEditable(false);
      statusRank.add(turn);

      JTextField rank = new JTextField();
      rank.setText(ranks[i]);
      rank.setBackground(color);
      rank.setEditable(false);
      statusRank.add(rank);
      highscore.add(statusRank);

      JTextField name = new JTextField();
      name.setText("Player " + (i + 1) + ": " + usernames[i]);
      name.setEditable(false);
      name.setBackground(color);
      highscore.add(name);

      JTextField actualScore = new JTextField();
      actualScore.setText(points[i]);
      actualScore.setBackground(color);
      actualScore.setEditable(false);
      highscore.add(actualScore);

      setColor();
    }
    highscore.revalidate();
    highscore.repaint();
  }

  /**
   * Deletes piece in Gui.
   *
   * @param piece String: of the piece.
   */
  public void deletePiece(String piece) {
    piecesGui.deletePiece(piece);
  }

  /**
   * method that deletes the border of the previously selected imagePane and draws a border around
   * the newly suggested one.
   *
   * @param pieceID key to the currently suggested piece image
   */
  public void drawBorder(int pieceID) {
    // reset border if there is previously suggested image pane
    if (suggestedPane != 0) {
      piecesGui.getImages().get(suggestedPane).resetBorder();
    }
    suggestedPane = pieceID;
    if (currentImagePaneNumber != 0) {
      piecesGui.getImages().get(currentImagePaneNumber).resetBorder();
    }
    piecesGui.getImages().get(pieceID).setColorBorder();
    currentImagePaneNumber = pieceID;
  }

  public void hintQuadrant(int row, int column) {
    board.updateBoard(currentBoardString, row, column);
  }

  /**
   * method that leads to the mainFrame being disposed.
   */
  public void disposeMainframe() {
    mainFrame.dispose();
  }

  /**
   * class to handle keyEvents.
   *
   * @see KeyListener
   */
  class CustomKeyListener implements KeyListener {
    private boolean altPressed = false;

    /**
     * function inhertitet from superclass.
     *
     * @param e KeyEvent
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * sets altPressed true after pressing and sets mainframe decorated when clicking esc sets the
     * mainFrame to Fullscreen if alt and enter is pressed.
     *
     * @param e KeyEvent
     */
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        if (mainFrame.isUndecorated()) {
          setMainFrameUndecorated();
        }
      }
      if (e.getKeyCode() == KeyEvent.VK_ALT) {
        altPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_ENTER && altPressed) {
        if (!mainFrame.isUndecorated()) {
          setMainFrameDecorated();
        }
      }
    }

    /**
     * Sets altPressed false after releasing.
     *
     * @param e KeyEvent
     * @see KeyListener
     */
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ALT) {
        altPressed = false;
      }
    }
  }

  /**
   * Getter method for the Pieces in Gui.
   *
   * @return the GUI of the pieces.
   */
  public PiecesGui getPiecesGui() {
    return piecesGui;
  }

  /**
   * Setter method for the Pieces in Gui.
   *
   * @param piecesGui PiecesGui to set
   */
  public void setPiecesGui(PiecesGui piecesGui) {
    this.piecesGui = piecesGui;
  }
}
