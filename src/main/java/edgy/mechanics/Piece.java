/** edgy.mechanics is a package for the game logic. */

package edgy.mechanics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation and initiation of a single piece of the game.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class Piece {

  /** Instantiation of a two dimensional array, containing the position information of a piece. */
  private int[][] piece;

  // Iniate rolling Server Logger
  private static Logger log = LogManager.getLogger(Piece.class.getName());

  /**
   * a piece is a 7x7 int array. 0 = empty, no entry 1 = entry, part of piece 2 = edge of piece 3 =
   * side of piece
   */
  public Piece() {
    this.piece = new int[7][7];
  }

  /**
   * method to change one particular entry of the generated piece instance.
   *
   * @see Pieces changeEntry. Changes a entry of a piece.
   * @param entry the information of the piece.
   * @param row horizontal of a piece entry position on the board.
   * @param column vertical of a piece entry position on the board.
   * @throws ArrayIndexOutOfBoundsException if position can't be found.
   */
  public void changeEntry(int entry, int row, int column) throws ArrayIndexOutOfBoundsException {
    try {
      if ((row >= 0) && (row < 7) && (column >= 0) && (column < 7) && (entry >= 0) && (entry < 4)) {
        piece[row][column] = entry;
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error("The position doesn't exist. Error: " + e);
    }
  }

  /**
   * Getter method for a piece.
   *
   * @return a two dimensional array, filled with the entry information about a piece.
   */
  public int[][] getPiece() {
    return piece;
  }

  /**
   * Setter method for a piece.
   *
   * @param piece two dimensional array.
   */
  public void setPiece(int[][] piece) {
    this.piece = piece;
  }
}
