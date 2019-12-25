/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * sets up the pieces below the board.
 *
 * @author while(true){do nothing}.
 * @version 1.0
 */
public class PiecesGui extends JPanel {

  private int pieceGap;
  private int pieceSize;
  private int pieceBorder;
  private static HashMap<Integer, ImagePane> images = new HashMap<>();
  private BufferedImage empty;
  private static String color;

  private int rowCoordinatesPiece;
  private int columnCoordinatesPiece;

  private BufferedImage piece;

  /**
   * constructor sets up the pieces below the board at initialization.
   * @param color the users color as String
   * @param dim individual Dimension of the screen
   */
  public PiecesGui(String color, int dim) {
    pieceGap = (int) Math.floor((double)dim / 9);
    pieceSize = (int) Math.floor(dim * 5);
    pieceBorder = (int) Math.floor(dim * (double)6 / 9);
    setColor(color);
    setBorder(new EmptyBorder(pieceBorder, pieceBorder, pieceBorder, pieceBorder));
    setLayout(new GridLayout(3, 7, pieceGap, pieceGap));
    try {
      String home = new java.io.File(".").getCanonicalPath();
      String emptyPath = Paths.get(home, "src", "main", "resources", "PieceImages",
              "BoardGrey.png").toString();
      empty = ImageIO.read(new File(emptyPath));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    // adding all pieces PNG's to a Hashmap
    int n = 1;
    for (int i = 1; i < 4; i++) {
      for (int j = 1; j < 8; j++) {
        try {
          String home = new java.io.File(".").getCanonicalPath();
          String piecePath = Paths.get(home, "src", "main", "resources",
                  "PieceImages", color + "P" + n + ".png").toString();
          piece = ImageIO.read(new File(piecePath));
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        ImagePane imagePane = new ImagePane();
        piece = imagePane.resize(piece, pieceSize, pieceSize);
        imagePane.setImageLocation(
            new Point(
                pieceBorder + (j - 1) * pieceGap + (j - 1) * piece.getWidth(),
                pieceBorder + (i - 1) * pieceGap + (i - 1) * piece.getWidth()));
        imagePane.setImage(piece);
        images.put(n, imagePane);
        add(imagePane);
        n++;
      }
    }
  }

  /**
   * setting the color of the user.
   *
   * @param color new color int between 1 and 4
   */
  public static void setColor(String color) {
    PiecesGui.color = color;
  }

  /**
   * methods that leads to the image of a deleted piece being set to the empty image.
   *
   * @param pieceNr number of the piece that should be deleted.
   */
  public void deletePiece(String pieceNr) {

    // images.get(String.valueOf(pieceNr)).setImage(empty);
    ImagePane ip = new ImagePane();
    empty = ip.resize(empty, pieceSize, pieceSize);

    images.get(Integer.parseInt(pieceNr)).setImage(ip.getImage());
  }

  /**
   * method that rearranges the pieceHM according to the incoming String from the server. Pieces
   * that do not exist anymore are replaced by an empty image.
   *
   * @param pieces int array containing the remaining pieces
   */
  public void rearrangeHm(int[] pieces) {
    int l = 0;
    for (int i = 0; i < pieces.length; i++) {
      if (pieces[l] == i + 1) {
        System.out.println(pieces[l] + "i=" + i + 1);
        l++;
      } else {
        ImagePane ip = new ImagePane();
        empty = ip.resize(empty, pieceSize, pieceSize);
        ip.setImage(empty);
        images.put(i + 1, ip);
      }
    }
    repaintPieces();
  }

  /** repaint the piecesPanel with the empty spaces. */
  private void repaintPieces() {
    removeAll();
    int n = 1;
    for (int i = 1; i < 4; i++) {
      for (int j = 1; j < 8; j++) {
        ImagePane imagePane = new ImagePane();
        piece = images.get(n).getImage();
        imagePane.setImageLocation(
            new Point(
                pieceBorder + (j - 1) * pieceGap + (j - 1) * piece.getWidth(),
                pieceBorder + (i - 1) * pieceGap + (i - 1) * piece.getWidth()));
        imagePane.setImage(piece);
        add(imagePane);
        n++;
      }
    }
    revalidate();
    repaint();
  }

  /**
   * Rotates a piece with a specific ID counterclockwise.
   *
   * @param pieceID piece to turn
   */
  public void rotateNrLeft(int pieceID) {
    BufferedImage newImage = rotateImage(images.get(pieceID).getImage(), 90);
    images.get(pieceID).setImage(newImage);
  }

  /**
   * Rotates a piece with a specific ID clockwise.
   *
   * @param pieceID piece to turn
   */
  public void rotateNrRight(int pieceID) {
    BufferedImage newImage = rotateImage(images.get(pieceID).getImage(), -90);
    images.get(pieceID).setImage(newImage);
  }

  /**
   * Method that allows the piece to be rotated 90 degrees (mathematical sense of rotation).
   *
   * @param bufferImg BufferedImage that has to be turned
   * @param degrees angel the image has to be rotated
   * @return rotated BufferedImage
   */
  private BufferedImage rotateImage(BufferedImage bufferImg, double degrees) {
    // use heigtht and width the other way around for rotated image
    BufferedImage rotBImage =
        new BufferedImage(bufferImg.getHeight(), bufferImg.getWidth(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = rotBImage.createGraphics();

    AffineTransform at = new AffineTransform();

    createTransformed(bufferImg, at);
    at.rotate(Math.toRadians(-degrees), (double)bufferImg.getWidth() / 2,
            (double)bufferImg.getHeight() / 2);
    graphics.setTransform(at);
    graphics.drawImage(bufferImg, 0, 0, this);
    return rotBImage;
  }

  /**
   * method that inserts a particular piece in its flipped state in the images HashMap.
   *
   * @param pieceID number of the piece that should be flipped
   */
  public void flipNr(int pieceID) {
    BufferedImage newImage = createFlipped(images.get(pieceID).getImage());
    images.get(pieceID).setImage(newImage);
  }

  /**
   * method that flips a BufferedImage vertically.
   *
   * @param image that should be flipped
   * @return flipped BufferedImage
   */
  private BufferedImage createFlipped(BufferedImage image) {
    AffineTransform at = new AffineTransform();
    at.concatenate(AffineTransform.getScaleInstance(1, -1));
    at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
    BufferedImage newImage =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = newImage.createGraphics();
    g.transform(at);
    g.drawImage(image, 0, 0, null);
    g.dispose();
    // newImage has to be flipped because it was mirrored horizontally and not vertically
    newImage = rotateImage(newImage, 180);
    return newImage;
  }

  private BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
    BufferedImage newImage =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = newImage.createGraphics();
    g.transform(at);
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return newImage;
  }

  /**
   * method that calculates which of the 21 pieces was selected with a mouseClick on the piecesGUI.
   *
   * @param mousePoint Point where the mouseEvent happened
   * @return PieceID in form of an int; returns 0 if no pieceID found
   */
  public int calculateRightPiece(Point mousePoint) {
    Point calculatePoint = new Point(mousePoint);

    // translate the point so its origin is at (0|0), Border thickness 10 also considered
    calculatePoint.translate(
        -(rowCoordinatesPiece + pieceBorder), -(columnCoordinatesPiece + pieceBorder));
    double rowPoint = calculatePoint.getX();
    double columnPoint = calculatePoint.getY();

    // Test single quadrants if point on border
    double rowTestGap = rowPoint % (pieceSize + pieceGap);
    double columnTestGap = columnPoint % (pieceSize + pieceGap);

    if (rowTestGap >= pieceSize || columnTestGap >= pieceSize) {
      return 0;
    }

    // divide position by (pieceSize + pieceGap), round up with Math.ceil
    int rowDirection = (int) Math.ceil((double) rowPoint / (pieceSize + pieceGap));
    int columnDirection = (int) Math.ceil((double) columnPoint / (pieceSize + pieceGap));

    // calculate final pieceID
    int number = rowDirection + 7 * (columnDirection - 1);

    // return 0 if number out of bounds
    if (number <= 0 || number > 21) {
      return 0;
    }

    return number;
  }

  /**
   * method that returns the relative position of the click on one piece.
   * @param click Point where the click event happened relative to the PiecesGui
   * @return Point relative to the selected ImagePane
   */
  public Point calculateHold(Point click) {
    // translate the point so its origin is at (0|0), Border thickness 10 also considered
    click.translate(-(rowCoordinatesPiece + pieceBorder), -(columnCoordinatesPiece + pieceBorder));
    double rowComponent = click.getX();
    double colComponent = click.getY();

    // Test single quadrants if point on border
    int x = (int) Math.ceil(rowComponent % (pieceSize + pieceGap));
    int y = (int) Math.ceil(colComponent % (pieceSize + pieceGap));

    return new Point(x, y);
  }

  /**
   * getter method for HashMap with the images.
   *
   * @return HashMap images
   */
  public static HashMap<Integer, ImagePane> getImages() {
    return images;
  }
  // TODO Stephi Javadoc

  /**
   * Getter method for the piece's row coordinates.
   *
   * @return an int with the row position.
   */
  public int getRowCoordinatesPiece() {
    return rowCoordinatesPiece;
  }

  /**
   * Setter method for the piece's row coordinates.
   *
   * @param rowCoordinatesPiece with the row coordinates.
   */
  public void setRowCoordinatesPiece(int rowCoordinatesPiece) {
    this.rowCoordinatesPiece = rowCoordinatesPiece;
  }

  /**
   * Getter method for the piece's column position.
   *
   * @return an int with the piece's column position.
   */
  public int getColumnCoordinatesPiece() {
    return columnCoordinatesPiece;
  }

  /**
   * Setter method for the piece's column coordinates.
   *
   * @param columnCoordinatesPiece the piece's column coordinates.
   */
  public void setColumnCoordinatesPiece(int columnCoordinatesPiece) {
    this.columnCoordinatesPiece = columnCoordinatesPiece;
  }

  public int getPieceGap() {
    return pieceGap;
  }

  public void setPieceGap(int pieceGap) {
    this.pieceGap = pieceGap;
  }

  public int getPieceSize() {
    return pieceSize;
  }

  public void setPieceSize(int pieceSize) {
    this.pieceSize = pieceSize;
  }

  public int getPieceBorder() {
    return pieceBorder;
  }

  public void setPieceBorder(int pieceBorder) {
    this.pieceBorder = pieceBorder;
  }
}
