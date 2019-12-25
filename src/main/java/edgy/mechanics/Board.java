/** edgy.mechanics is a package for the game logic. */

package edgy.mechanics;

import edgy.sound.Sounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Instantiation of a game board.
 *
 * @author while(true){do nothing}.
 * @version 1.0
 */
public class Board {

  /**
   * creates board as int array, 0= empty 1 = yellow 2 = red 3 = blue 4 = green initialized with 0-
   * entries.
   */
  public int[][] board = new int[20][20];

  // Iniate rolling Server Logger
  private static Logger log = LogManager.getLogger(Board.class.getName());

  Sounds sounds = new Sounds();

  /**
   * Set piece on the right position on the board.
   *
   * @param color the color of the player/piece.
   * @param piece the piece which will be moved and set on the board.
   * @param positionRow the row on the board.
   * @param positionColumn the column on the board.
   * @throws ArrayIndexOutOfBoundsException if the position where the piece will be set, isn't on
   *     the board.
   */
  public void setPiece(int color, int[][] piece, int positionRow, int positionColumn) {
    try {
      for (int row = 0; row < 7; row++) {
        for (int column = 0; column < 7; column++) {
          if (piece[row][column] == 1) {
            board[positionRow - 3 + row][positionColumn - 3 + column] = color;
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error("wrong Index. Error: " + e);
    }
  }

  /*method that checks whether move is allowed:
   * whole piece on board
   * first piece in edge- not possible to check here!
   * at least one edge of piece with same color
   * no sides of piece with same color
   * not on top of other piece (same or other color)
   * note: the position is the index of the board where the middle of the piece comes to rest
   */

  /**
   * Checks if move is allowed.
   *
   * @param color of a piece.
   * @param piece with all the entry information stored in a two dimensional array.
   * @param positionRow horizontal position of a piece entry.
   * @param positionColumn vertical position of a piece entry.
   * @param firstMove checks whether it's the players turn.
   * @return true or false, true if move is allowed, false if not.
   */
  public boolean checkMove(
      int color, int[][] piece, int positionRow, int positionColumn, boolean firstMove) {
    if (!checkPosition(positionRow, positionColumn)) {
      throw new IndexOutOfBoundsException();
    }

    /* checks whether whole Piece is on Board */
    if (!checkPieceOnBoard(piece, positionRow, positionColumn)) {
      return false;
    }

    /* checks whether pieces are overlapping */
    if (!checkNoOverlapping(piece, positionRow, positionColumn)) {
      return false;
    }

    /* checks whether two pieces of same color touches edges */
    if (!checkPieceNoSides(color, piece, positionRow, positionColumn)) {
      return false;
    }
    /* In case of first move check whether it's corner */
    if (firstMove) {
      return checkFirstMove(color, piece, positionRow, positionColumn);
    } else {
      /* check whether pieces touch edges */
      return checkPieceTouchesCorner(color, piece, positionRow, positionColumn);
    }
  }

  /**
   * method that checks whether whole piece lies fully on board.
   *
   * @param piece two dimensional int array for each piece.
   * @param positionRow horizontal position of a piece entry.
   * @param positionColumn vertical position of a piece entry.
   * @return true if all entries of a piece are fully on the bord, false if not.
   */
  public boolean checkPieceOnBoard(int[][] piece, int positionRow, int positionColumn) {
    try {
      for (int r = 1; r < 6; r++) {
        for (int c = 1; c < 6; c++) {
          if (piece[r][c] == 1) {
            if ((positionRow - 3 + r) < 0) {
              return false;
            } else if ((positionRow - 3 + r) >= 20) {
              return false;
            } else if ((positionColumn - 3 + c) < 0) {
              return false;
            } else if ((positionColumn - 3 + c) >= 20) {
              return false;
            }
          }
        }
      }
    } catch (Exception e) {
      log.error("Wrong Arrayindex. Error: " + e);
    }
    return true;
  }

  /**
   * Method that checks whether piece overlaps with other piece.
   *
   * @param piece two dimensional array.
   * @param positionRow horizontal position of a piece entry.
   * @param positionColumn vertical position of a piece entry.
   * @return true if no pieces are overlapping and false if they do.
   * @throws ArrayIndexOutOfBoundsException if a piece can't be found.
   */
  public boolean checkNoOverlapping(int[][] piece, int positionRow, int positionColumn) {
    try {
      for (int r = 1; r < 6; r++) {
        for (int c = 1; c < 6; c++) {
          if (piece[r][c] == 1) {
            int row = positionRow - 3 + r;
            int col = positionColumn - 3 + c;

            /* In case position is outside of board, these position doesn't need to be checked */
            if (!checkPosition(row, col)) {
              continue;
            }

            if (board[row][col] != 0) {
              log.warn("Überlappt: col: " + positionColumn + " row: " + positionRow);
              return false;
            }
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error("No valid entry for the indexes. Outside the board. Error: " + e);
    }
    return true;
  }

  /**
   * method that checks whether pieces of the same color touch at sides.
   *
   * @param color of the pieces.
   * @param piece two dimensional array.
   * @param positionRow horizontal position of a piece entry.
   * @param positionColumn vertical position of a piece entry.
   * @return true if pieces of the same color don't touch a side of each other, false if they do.
   * @throws ArrayIndexOutOfBoundsException if piece isn't fully on the board.
   */
  public boolean checkPieceNoSides(int color, int[][] piece, int positionRow, int positionColumn) {
    try {
      for (int r = 0; r <= 6; r++) {
        for (int c = 0; c <= 6; c++) {
          if (piece[r][c] == 3) {
            int row = positionRow - 3 + r;
            int col = positionColumn - 3 + c;

            /* In case position is outside of board, these position doesn't need to be checked */
            if (!checkPosition(row, col)) {
              continue;
            }

            if (board[row][col] == color) {
              log.warn(
                  "Berührt Kante: col: "
                      + positionColumn
                      + " row: "
                      + positionRow
                      + " Farbe "
                      + color);
              return false;
            }
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error("Wrong indexes. Outside the board. Error: " + e);
    }
    return true;
  }

  private boolean checkPosition(int row, int col) {
    if (row >= 20 || row < 0 || col >= 20 || col < 0) {
      return false;
    }
    return true;
  }

  /**
   * method that checks the first move with the additional condition of the piece touching the edge.
   * It does not matter which edge is touched (convention).
   *
   * @param color of the piece.
   * @param piece two dimensional array.
   * @param positionRow horizontal position of a piece entry.
   * @param positionColumn vertical position of a piece entry.
   * @return true if first move is correct, false if not.
   */
  public boolean checkFirstMove(int color, int[][] piece, int positionRow, int positionColumn) {
    try {
      for (int r = 0; r <= 6; r++) {
        for (int c = 0; c <= 6; c++) {
          if (piece[r][c] == 1) {
            int rowBoard = positionRow - 3 + r;
            int columnBoard = positionColumn - 3 + c;
            if ((rowBoard == 0 || rowBoard == 19) && (columnBoard == 0 || columnBoard == 19)) {
              return true;
            }
          }
        }
      }
    } catch (IndexOutOfBoundsException e) {
      log.error("Error: OutOfBoundException CheckFirstMove: " + e);
    }
    return false;
  }

  /**
   * method that checks the first move with the additional condition of the piece touching the edge.
   * It does not matter which edge is touched (convention). The edges are checked one by one with
   * the corresponding indices.
   *
   * @param color of the piece
   * @param piece two dimensional int array representing the piece
   * @param positionRow horizontal position of a piece entry
   * @param positionColumn vertical position of a piece entry.
   * @return true if first move is correct, false if not
   */
  public boolean checkFirstMove1(int color, int[][] piece, int positionRow, int positionColumn) {
    try {
      for (int r = 0; r <= 6; r++) {
        for (int c = 0; c <= 6; c++) {
          if (piece[r][c] == 1) {
            int rowBoard = positionRow - 3 + r;
            int columnBoard = positionColumn - 3 + c;
            int boardColumnLeftTop = board[0][0];
            int boardColumnRightTop = board[0][19];
            int boardColumnLeftDown = board[19][0];
            int boardColumnRightDown = board[19][19];
            if (board[rowBoard][columnBoard] == boardColumnLeftDown
                || board[rowBoard][columnBoard] == boardColumnLeftTop
                || board[rowBoard][columnBoard] == boardColumnRightTop
                || board[rowBoard][columnBoard] == boardColumnRightDown) {
              return true;
            }
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error("Wrong indexes, out of bound. Error: " + e);
    }
    return false;
  }

  /**
   * method that checks whether Piece touches at least one gPiece of same color.
   *
   * @param color of the pieces.
   * @param piece two dimensional array.
   * @param positionRow horizontal position of a piece entry.
   * @param positionColumn vertical position of a piece entry.
   * @return true if pieces only touch the edges of the same color, false if not.
   * @throws ArrayIndexOutOfBoundsException if piece can't be found on the board.
   */
  public boolean checkPieceTouchesCorner(
      int color, int[][] piece, int positionRow, int positionColumn) {
    try {
      for (int r = 0; r <= 6; r++) {
        for (int c = 0; c <= 6; c++) {
          if (piece[r][c] == 2) {
            int row = positionRow - 3 + r;
            int col = positionColumn - 3 + c;

            /* In case position is outside of board, these position doesn't need to be checked */
            if (!checkPosition(row, col)) {
              continue;
            }

            if (board[row][col] == color) {
              return true;
            }
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error("Piece isn't on the board. Out of bound. Error: " + e);
    }
    log.warn(
        "Berührt KEINE Ecke: col: " + positionColumn + " row: " + positionRow + " Farbe " + color);
    return false;
  }

  /**
   * method that returns number of points (one per square) of particular color.
   *
   * @param color of the pieces.
   * @return number of points for a player.
   */
  public int countPoints(int color) {
    int points = 0;
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        if (board[i][j] == color) {
          points++;
        }
      }
    }
    return points;
  }

  /**
   * method that prints the board in its current state with chars as symbols. y = yellow, r = red, b
   * = blue, g = green, * = empty
   *
   * @return printed board.
   */
  public String getBoard() {
    String s = "";
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        int currentEntry = board[i][j];
        switch (currentEntry) {
          case 1:
            s += "y";
            break;
          case 2:
            s += "r";
            break;
          case 3:
            s += "b";
            break;
          case 4:
            s += "g";
            break;
          default:
            s += "*";
            break;
        }
      }
      s += "\n";
    }
    return s;
  }

  /** Prints the board on the GUI-window. */
  public void printBoard() {
    String board = getBoard();
    System.out.println(board);
  }
}
