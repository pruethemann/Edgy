/** edgy.gui is a package for the whole graphic implementation. */

package edgy.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/** sets up a panel with the board in a Gridlayout. */
public class BoardGui extends JLayeredPane {

  /** Initialisation of the graphic of the board. */
  private int boardSize;

  private int boardBorder;

  private int rowCoordinatesBoard;
  private int columnCoordinatesBoard;

  private BufferedImage piece;
  private BufferedImage pieceYellow;
  private BufferedImage pieceGreen;
  private BufferedImage pieceBlue;
  private BufferedImage pieceRed;
  private BufferedImage pieceGrey;
  private BufferedImage pieceViolet;

  /**
   * constructor sets up an grey empty board.
   * @param dim Dimension of the screen
   */
  public BoardGui(int dim) {
    boardSize = dim;
    boardBorder = (int) Math.floor(dim * (double)5 / 9);
    Color grey = new Color(149, 149, 149);
    setBorder(BorderFactory.createLineBorder(grey, boardBorder));
    setLayout(new GridLayout(20, 20));
    try {
      String home = new java.io.File(".").getCanonicalPath();
      String piecePath = Paths.get(home, "src", "main", "resources",
              "PieceImages", "BoardGrey.png").toString();
      piece = ImageIO.read(new File(piecePath));
      setPieces();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    for (int i = 1; i < 21; i++) {
      for (int j = 1; j < 21; j++) {
        ImagePane imagePane = new ImagePane();
        piece = imagePane.resize(piece, boardSize, boardSize);
        imagePane.setImageLocation(
            new Point(
                boardBorder + (j - 1) * piece.getWidth(),
                boardBorder + (i - 1) * piece.getWidth()));
        imagePane.setImage(piece);
        add(imagePane);
      }
    }
  }

  /**
   * gets the PNG pieces from resources folder.
   *
   * @throws IOException if files couldn't be found.
   */
  private void setPieces() throws IOException {
    String home = new java.io.File(".").getCanonicalPath();
    String yellow = Paths.get(home, "src", "main", "resources",
            "PieceImages", "BoardYellow.png").toString();
    pieceYellow = ImageIO.read(new File(yellow));
    String green = Paths.get(home, "src", "main", "resources",
            "PieceImages", "BoardGreen.png").toString();
    pieceGreen = ImageIO.read(new File(green));
    String blue = Paths.get(home, "src", "main", "resources",
            "PieceImages", "BoardBlue.png").toString();
    pieceBlue = ImageIO.read(new File(blue));
    String red = Paths.get(home, "src", "main", "resources",
            "PieceImages", "BoardRed.png").toString();
    pieceRed = ImageIO.read(new File(red));
    String grey = Paths.get(home, "src", "main", "resources",
            "PieceImages", "BoardGrey.png").toString();
    pieceGrey = ImageIO.read(new File(grey));
    String violet = Paths.get(home, "src", "main", "resources",
            "PieceImages", "BoardViolet.png").toString();
    pieceViolet = ImageIO.read(new File(violet));
  }

  public void updateBoard(String board) {
    updateBoard(board, 21, 21);
  }

  /**
   * method that redraws the boardPanel with the incoming information from the server.
   *
   * @param board string from server with color information of single quadrants.
   * @param row row position of the violet hint piece if there is one
   * @param column column position of the violet hint piece if there is one
   */
  public void updateBoard(String board, int row, int column) {
    removeAll();
    char color;
    int n = 0;
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        if (row == i && column == j) {
          color = 'v';
        } else {
          color = board.charAt(n);
        }
        ImagePane imagePane = new ImagePane();
        Point imagePoint = new Point(boardBorder + j * boardSize, boardBorder + i * boardSize);
        switch (color) {
          case '*':
            pieceGrey = imagePane.resize(pieceGrey, boardSize, boardSize);
            imagePane.setImageLocation(imagePoint);
            imagePane.setImage(pieceGrey);
            break;
          case 'y':
            pieceYellow = imagePane.resize(pieceYellow, boardSize, boardSize);
            imagePane.setImageLocation(imagePoint);
            imagePane.setImage(pieceYellow);
            break;
          case 'b':
            pieceBlue = imagePane.resize(pieceBlue, boardSize, boardSize);
            imagePane.setImageLocation(imagePoint);
            imagePane.setImage(pieceBlue);
            break;
          case 'g':
            pieceGreen = imagePane.resize(pieceGreen, boardSize, boardSize);
            imagePane.setImageLocation(imagePoint);
            imagePane.setImage(pieceGreen);
            break;
          case 'r':
            pieceRed = imagePane.resize(pieceRed, boardSize, boardSize);
            imagePane.setImageLocation(imagePoint);
            imagePane.setImage(pieceRed);
            break;
          default: // equals 'v'
            pieceViolet = imagePane.resize(pieceViolet, boardSize, boardSize);
            imagePane.setImageLocation(imagePoint);
            imagePane.setImage(pieceViolet);
            break;
        }
        add(imagePane);
        n++;
      }
    }
    revalidate();
    repaint();
  }

  /**
   * method that determines the x and y components of where a mouseclick hit the board.
   *
   * @param mousePoint where the mouseEvent happened.
   * @return int array with first entry x and second entry y component of board, returns null if
   *     click not on board.
   */
  public int[] calculateRightQuadrant(Point mousePoint) {
    Point calculatePoint = new Point(mousePoint);
    int[] position = new int[2];

    // translate the point so its origin is at (0|0), Border thickness 10 also considered
    calculatePoint.translate(
        -(rowCoordinatesBoard + boardBorder), -(columnCoordinatesBoard + boardBorder));

    double rowPoint = calculatePoint.getX();

    double columnPoint = calculatePoint.getY();

    // divide position by (pieceSize + pieceGap), round up with Math.ceil
    position[0] = (int) Math.floor((double) rowPoint / (boardSize));
    position[1] = (int) Math.floor((double) columnPoint / (boardSize));

    // return 0 if number out of bounds
    if (position[0] < 0 || position[0] > 19 || position[1] < 0 || position[1] > 19) {
      return null;
    }

    return position;
  }

  /**
   * Getter method for the row coordinates of the board.
   *
   * @return an int with the row position.
   * @deprecated not needed at the moment but added for completeness if needed later on.
   */
  public int getRowCoordinatesBoard() {
    return rowCoordinatesBoard;
  }

  /**
   * Setter method for the row coordinates of the board.
   *
   * @param rowCoordinatesBoard the row coordinates of the board.
   */
  public void setRowCoordinatesBoard(int rowCoordinatesBoard) {
    this.rowCoordinatesBoard = rowCoordinatesBoard;
  }

  /**
   * Getter method for the column coordinates of the board.
   *
   * @return an int with the column position.
   * @deprecated not needed at the moment but added for completeness if needed later on.
   */
  public int getColumnCoordinatesBoard() {
    return columnCoordinatesBoard;
  }

  /**
   * Setter method for the column coordinates of the board.
   *
   * @param columnCoordinatesBoard the column coordinates of the board.
   */
  public void setColumnCoordinatesBoard(int columnCoordinatesBoard) {
    this.columnCoordinatesBoard = columnCoordinatesBoard;
  }

  public int getBoardSize() {
    return boardSize;
  }

  public void setBoardSize(int boardSize) {
    this.boardSize = boardSize;
  }

  public int getBoardBorder() {
    return boardBorder;
  }

  public void setBoardBorder(int boardBorder) {
    this.boardBorder = boardBorder;
  }
}
