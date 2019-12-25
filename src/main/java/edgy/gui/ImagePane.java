/** edgy.gui is a package for the graphic implementation. */

package edgy.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * class that contains an image and a image location in form of a point. class extends JPanel and is
 * used to illustrate images on a specific location on a JPanel.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class ImagePane extends JPanel {

  /** private fields image in form of a BufferedImage and its location in form of a point. */
  private BufferedImage image;

  private Point imageLocation;

  /** empty constructor that initializes a new ImagePane. */
  public ImagePane() {}

  /** Setter method for the border in grey. */
  public void setBorder() {
    setBorder(new LineBorder(Color.LIGHT_GRAY));
  }

  /** method to surround an image with a customized color. */
  public void setColorBorder() {
    setBorder(new LineBorder(new Color(148, 0, 200, 255)));
  }

  /** Resets the border, creates a new one which is empty. */
  public void resetBorder() {
    setBorder(new EmptyBorder(1, 1, 1, 1));
  }

  /**
   * method that replaces the current BufferedImage of the ImagePane with the empty image.
   */
  public void putEmptyPiece() {
    try {
      BufferedImage empty = ImageIO.read(
              new File("src\\main\\resources\\PieceImages\\BoardGrey.png"));
      BufferedImage resized = resize(empty, image.getWidth(), image.getHeight());
      this.image = resized;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * method that creates a new dimension containing the width and height of the image from the
   * ImagePane.
   *
   * @see JComponent the width and height of the image from the ImagePane.
   * @return Dimension composed of width and height.
   */
  @Override
  public Dimension getPreferredSize() {
    return image == null
        ? super.getPreferredSize()
        : new Dimension(image.getWidth(this), image.getHeight(this));
  }

  /**
   * Setter method for the Image.
   *
   * @param image BufferedImage that should be shown by the ImagePane.
   */
  public void setImage(BufferedImage image) {
    this.image = image;
    repaint();
  }

  /**
   * Setter method for the Image Location.
   *
   * @param imageLocation in form of a Point where the ImagePane should be newly located.
   */
  public void setImageLocation(Point imageLocation) {
    this.imageLocation = imageLocation;
    repaint();
  }

  /**
   * method that overrides the paintComponent method from JPanel. Here, the image is drawn if
   * imageLocation and image are determined.
   *
   * @see JComponent
   * @param g Graphics that helps the image so it can be painted
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null && imageLocation != null) {
      Point p = SwingUtilities.convertPoint(getParent(), imageLocation, this);
      g.drawImage(image, p.x, p.y, this);
    }
  }

  /**
   * method that resizes an image to a desired with and height.
   *
   * @param img Buffered image that should be resized.
   * @param newWidth desired width.
   * @param newHeight desired height.
   * @return resized BufferedImage
   */
  public BufferedImage resize(BufferedImage img, int newWidth, int newHeight) {
    Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
  }

  /**
   * Getter method for the image.
   *
   * @return the image.
   */
  public BufferedImage getImage() {
    return image;
  }

  /**
   * Getter method for the image location.
   *
   * @return current image location in form of a Point with x and y component
   */
  public Point getImageLocation() {
    return imageLocation;
  }
}
