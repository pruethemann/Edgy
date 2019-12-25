/** edgy.mechanics is a package for the game logic. */

package edgy.mechanics;

import java.util.HashMap;
// REVIEW: functions need longer comments

/**
 * Initiation of the piece set with all pieces stored in a Hashmap.
 *
 * @author while(true){do nothing}.
 * @version 1.0
 */
public class Pieces {

  /**
   * HashMap that contains the number of the piece as key * and the piece in form of a 5x5 array as
   * value.
   */
  public HashMap<Integer, Piece> pieceSet;

  /** All 21 pieces as 5X5 boolean arrays. */
  private Piece piece01;

  private Piece piece02;
  private Piece piece03;
  private Piece piece04;
  private Piece piece05;
  private Piece piece06;
  private Piece piece07;
  private Piece piece08;
  private Piece piece09;
  private Piece piece10;
  private Piece piece11;
  private Piece piece12;
  private Piece piece13;
  private Piece piece14;
  private Piece piece15;
  private Piece piece16;
  private Piece piece17;
  private Piece piece18;
  private Piece piece19;
  private Piece piece20;
  private Piece piece21;

  /**
   * constructor that generates a new set of 21 pieces and 1 hashmap. all 21 pieces are instanced as
   * 5X5 boolean arrays (every entry false); afterwards, the entries according to the shape of the
   * piece are set true
   */
  public Pieces() {
    this.pieceSet = new HashMap<Integer, Piece>(21);

    this.piece01 = new Piece();
    this.piece02 = new Piece();
    this.piece03 = new Piece();
    this.piece04 = new Piece();
    this.piece05 = new Piece();
    this.piece06 = new Piece();
    this.piece07 = new Piece();
    this.piece08 = new Piece();
    this.piece09 = new Piece();
    this.piece10 = new Piece();
    this.piece11 = new Piece();
    this.piece12 = new Piece();
    this.piece13 = new Piece();
    this.piece14 = new Piece();
    this.piece15 = new Piece();
    this.piece16 = new Piece();
    this.piece17 = new Piece();
    this.piece18 = new Piece();
    this.piece19 = new Piece();
    this.piece20 = new Piece();
    this.piece21 = new Piece();

    changeEntries();
    changeEdges();
    changeSides();
    fillPieceSet();
  }

  /** Implementation of the visible part of the pieces. */
  public void changeEntries() {
    piece01.changeEntry(1, 3, 3);

    piece02.changeEntry(1, 3, 3);
    piece02.changeEntry(1, 2, 3);

    piece03.changeEntry(1, 2, 3);
    piece03.changeEntry(1, 3, 3);
    piece03.changeEntry(1, 4, 3);

    piece04.changeEntry(1, 2, 3);
    piece04.changeEntry(1, 3, 3);
    piece04.changeEntry(1, 3, 4);

    piece05.changeEntry(1, 1, 3);
    piece05.changeEntry(1, 2, 3);
    piece05.changeEntry(1, 3, 3);
    piece05.changeEntry(1, 4, 3);

    piece06.changeEntry(1, 2, 3);
    piece06.changeEntry(1, 3, 3);
    piece06.changeEntry(1, 3, 4);
    piece06.changeEntry(1, 4, 3);

    piece07.changeEntry(1, 2, 3);
    piece07.changeEntry(1, 3, 3);
    piece07.changeEntry(1, 4, 3);
    piece07.changeEntry(1, 4, 4);

    piece08.changeEntry(1, 2, 3);
    piece08.changeEntry(1, 3, 3);
    piece08.changeEntry(1, 3, 4);
    piece08.changeEntry(1, 4, 4);

    piece09.changeEntry(1, 2, 3);
    piece09.changeEntry(1, 2, 4);
    piece09.changeEntry(1, 3, 3);
    piece09.changeEntry(1, 3, 4);

    piece10.changeEntry(1, 1, 3);
    piece10.changeEntry(1, 2, 3);
    piece10.changeEntry(1, 3, 3);
    piece10.changeEntry(1, 4, 3);
    piece10.changeEntry(1, 5, 3);

    piece11.changeEntry(1, 1, 3);
    piece11.changeEntry(1, 2, 3);
    piece11.changeEntry(1, 3, 3);
    piece11.changeEntry(1, 4, 3);
    piece11.changeEntry(1, 4, 4);

    piece12.changeEntry(1, 1, 3);
    piece12.changeEntry(1, 2, 3);
    piece12.changeEntry(1, 3, 3);
    piece12.changeEntry(1, 4, 3);
    piece12.changeEntry(1, 3, 4);

    piece13.changeEntry(1, 1, 3);
    piece13.changeEntry(1, 2, 3);
    piece13.changeEntry(1, 3, 3);
    piece13.changeEntry(1, 3, 4);
    piece13.changeEntry(1, 3, 5);

    piece14.changeEntry(1, 1, 3);
    piece14.changeEntry(1, 2, 3);
    piece14.changeEntry(1, 3, 2);
    piece14.changeEntry(1, 3, 3);
    piece14.changeEntry(1, 3, 4);

    piece15.changeEntry(1, 2, 3);
    piece15.changeEntry(1, 3, 2);
    piece15.changeEntry(1, 3, 3);
    piece15.changeEntry(1, 3, 4);
    piece15.changeEntry(1, 4, 3);

    piece16.changeEntry(1, 2, 3);
    piece16.changeEntry(1, 3, 3);
    piece16.changeEntry(1, 3, 4);
    piece16.changeEntry(1, 4, 3);
    piece16.changeEntry(1, 4, 4);

    piece17.changeEntry(1, 1, 3);
    piece17.changeEntry(1, 2, 3);
    piece17.changeEntry(1, 3, 3);
    piece17.changeEntry(1, 3, 4);
    piece17.changeEntry(1, 4, 4);

    piece18.changeEntry(1, 2, 2);
    piece18.changeEntry(1, 2, 3);
    piece18.changeEntry(1, 3, 3);
    piece18.changeEntry(1, 3, 4);
    piece18.changeEntry(1, 4, 3);

    piece19.changeEntry(1, 2, 2);
    piece19.changeEntry(1, 2, 3);
    piece19.changeEntry(1, 3, 3);
    piece19.changeEntry(1, 4, 3);
    piece19.changeEntry(1, 4, 4);

    piece20.changeEntry(1, 2, 2);
    piece20.changeEntry(1, 2, 3);
    piece20.changeEntry(1, 3, 3);
    piece20.changeEntry(1, 3, 4);
    piece20.changeEntry(1, 4, 4);

    piece21.changeEntry(1, 2, 3);
    piece21.changeEntry(1, 2, 4);
    piece21.changeEntry(1, 3, 3);
    piece21.changeEntry(1, 4, 3);
    piece21.changeEntry(1, 4, 4);
  }

  /** Implementation of the edges touching the visible piece. */
  public void changeEdges() {
    piece01.changeEntry(2, 2, 2);
    piece01.changeEntry(2, 2, 4);
    piece01.changeEntry(2, 4, 2);
    piece01.changeEntry(2, 4, 4);

    piece02.changeEntry(2, 1, 2);
    piece02.changeEntry(2, 1, 4);
    piece02.changeEntry(2, 4, 2);
    piece02.changeEntry(2, 4, 4);

    piece03.changeEntry(2, 1, 2);
    piece03.changeEntry(2, 1, 4);
    piece03.changeEntry(2, 5, 2);
    piece03.changeEntry(2, 5, 4);

    piece04.changeEntry(2, 1, 2);
    piece04.changeEntry(2, 1, 4);
    piece04.changeEntry(2, 2, 5);
    piece04.changeEntry(2, 4, 2);
    piece04.changeEntry(2, 4, 5);

    piece05.changeEntry(2, 0, 2);
    piece05.changeEntry(2, 0, 4);
    piece05.changeEntry(2, 5, 2);
    piece05.changeEntry(2, 5, 4);

    piece06.changeEntry(2, 1, 2);
    piece06.changeEntry(2, 1, 4);
    piece06.changeEntry(2, 2, 5);
    piece06.changeEntry(2, 4, 5);
    piece06.changeEntry(2, 5, 2);
    piece06.changeEntry(2, 5, 4);

    piece07.changeEntry(2, 1, 2);
    piece07.changeEntry(2, 1, 4);
    piece07.changeEntry(2, 3, 5);
    piece07.changeEntry(2, 5, 2);
    piece07.changeEntry(2, 5, 5);

    piece08.changeEntry(2, 1, 2);
    piece08.changeEntry(2, 1, 4);
    piece08.changeEntry(2, 2, 5);
    piece08.changeEntry(2, 4, 2);
    piece08.changeEntry(2, 5, 3);
    piece08.changeEntry(2, 5, 5);

    piece09.changeEntry(2, 1, 2);
    piece09.changeEntry(2, 1, 5);
    piece09.changeEntry(2, 4, 2);
    piece09.changeEntry(2, 4, 5);

    piece10.changeEntry(2, 0, 2);
    piece10.changeEntry(2, 0, 4);
    piece10.changeEntry(2, 6, 2);
    piece10.changeEntry(2, 6, 4);

    piece11.changeEntry(2, 0, 2);
    piece11.changeEntry(2, 0, 4);
    piece11.changeEntry(2, 3, 5);
    piece11.changeEntry(2, 5, 2);
    piece11.changeEntry(2, 5, 5);

    piece12.changeEntry(2, 0, 2);
    piece12.changeEntry(2, 0, 4);
    piece12.changeEntry(2, 2, 5);
    piece12.changeEntry(2, 4, 5);
    piece12.changeEntry(2, 5, 2);
    piece12.changeEntry(2, 5, 4);

    piece13.changeEntry(2, 0, 2);
    piece13.changeEntry(2, 0, 4);
    piece13.changeEntry(2, 2, 6);
    piece13.changeEntry(2, 4, 2);
    piece13.changeEntry(2, 4, 6);

    piece14.changeEntry(2, 0, 2);
    piece14.changeEntry(2, 0, 4);
    piece14.changeEntry(2, 2, 1);
    piece14.changeEntry(2, 2, 5);
    piece14.changeEntry(2, 4, 1);
    piece14.changeEntry(2, 4, 5);

    piece15.changeEntry(2, 1, 2);
    piece15.changeEntry(2, 1, 4);
    piece15.changeEntry(2, 2, 1);
    piece15.changeEntry(2, 2, 5);
    piece15.changeEntry(2, 4, 1);
    piece15.changeEntry(2, 4, 5);
    piece15.changeEntry(2, 5, 2);
    piece15.changeEntry(2, 5, 4);

    piece16.changeEntry(2, 1, 2);
    piece16.changeEntry(2, 1, 4);
    piece16.changeEntry(2, 2, 5);
    piece16.changeEntry(2, 5, 2);
    piece16.changeEntry(2, 5, 5);

    piece17.changeEntry(2, 0, 2);
    piece17.changeEntry(2, 0, 4);
    piece17.changeEntry(2, 2, 5);
    piece17.changeEntry(2, 4, 2);
    piece17.changeEntry(2, 5, 3);
    piece17.changeEntry(2, 5, 5);

    piece18.changeEntry(2, 1, 1);
    piece18.changeEntry(2, 1, 4);
    piece18.changeEntry(2, 2, 5);
    piece18.changeEntry(2, 3, 1);
    piece18.changeEntry(2, 4, 5);
    piece18.changeEntry(2, 5, 2);
    piece18.changeEntry(2, 5, 4);

    piece19.changeEntry(2, 1, 1);
    piece19.changeEntry(2, 1, 4);
    piece19.changeEntry(2, 3, 1);
    piece19.changeEntry(2, 3, 5);
    piece19.changeEntry(2, 5, 2);
    piece19.changeEntry(2, 5, 5);

    piece20.changeEntry(2, 1, 1);
    piece20.changeEntry(2, 1, 4);
    piece20.changeEntry(2, 2, 5);
    piece20.changeEntry(2, 3, 1);
    piece20.changeEntry(2, 4, 2);
    piece20.changeEntry(2, 5, 3);
    piece20.changeEntry(2, 5, 5);

    piece21.changeEntry(2, 1, 2);
    piece21.changeEntry(2, 1, 5);
    piece21.changeEntry(2, 3, 5);
    piece21.changeEntry(2, 5, 2);
    piece21.changeEntry(2, 5, 5);
  }

  /** Implementation of the sides of the visible piece. */
  private void changeSides() {
    piece01.changeEntry(3, 2, 3);
    piece01.changeEntry(3, 3, 2);
    piece01.changeEntry(3, 3, 4);
    piece01.changeEntry(3, 4, 3);

    piece02.changeEntry(3, 1, 3);
    piece02.changeEntry(3, 2, 2);
    piece02.changeEntry(3, 3, 2);
    piece02.changeEntry(3, 2, 4);
    piece02.changeEntry(3, 3, 4);
    piece02.changeEntry(3, 4, 3);

    piece03.changeEntry(3, 1, 3);
    piece03.changeEntry(3, 2, 2);
    piece03.changeEntry(3, 3, 2);
    piece03.changeEntry(3, 4, 2);
    piece03.changeEntry(3, 2, 4);
    piece03.changeEntry(3, 3, 4);
    piece03.changeEntry(3, 4, 4);
    piece03.changeEntry(3, 5, 3);

    piece04.changeEntry(3, 1, 3);
    piece04.changeEntry(3, 2, 2);
    piece04.changeEntry(3, 2, 4);
    piece04.changeEntry(3, 3, 2);
    piece04.changeEntry(3, 3, 5);
    piece04.changeEntry(3, 4, 3);
    piece04.changeEntry(3, 4, 4);

    piece05.changeEntry(3, 0, 3);
    piece05.changeEntry(3, 1, 2);
    piece05.changeEntry(3, 1, 4);
    piece05.changeEntry(3, 2, 2);
    piece05.changeEntry(3, 2, 4);
    piece05.changeEntry(3, 3, 2);
    piece05.changeEntry(3, 3, 4);
    piece05.changeEntry(3, 4, 2);
    piece05.changeEntry(3, 4, 4);
    piece05.changeEntry(3, 5, 3);

    piece06.changeEntry(3, 1, 3);
    piece06.changeEntry(3, 2, 2);
    piece06.changeEntry(3, 2, 4);
    piece06.changeEntry(3, 3, 2);
    piece06.changeEntry(3, 3, 5);
    piece06.changeEntry(3, 4, 2);
    piece06.changeEntry(3, 4, 4);
    piece06.changeEntry(3, 5, 3);

    piece07.changeEntry(3, 1, 3);
    piece07.changeEntry(3, 2, 2);
    piece07.changeEntry(3, 2, 4);
    piece07.changeEntry(3, 3, 2);
    piece07.changeEntry(3, 3, 4);
    piece07.changeEntry(3, 4, 2);
    piece07.changeEntry(3, 4, 5);
    piece07.changeEntry(3, 5, 3);
    piece07.changeEntry(3, 5, 4);

    piece08.changeEntry(3, 1, 3);
    piece08.changeEntry(3, 2, 2);
    piece08.changeEntry(3, 2, 4);
    piece08.changeEntry(3, 3, 2);
    piece08.changeEntry(3, 3, 5);
    piece08.changeEntry(3, 4, 3);
    piece08.changeEntry(3, 4, 5);
    piece08.changeEntry(3, 5, 4);

    piece09.changeEntry(3, 1, 3);
    piece09.changeEntry(3, 1, 4);
    piece09.changeEntry(3, 2, 2);
    piece09.changeEntry(3, 2, 5);
    piece09.changeEntry(3, 3, 2);
    piece09.changeEntry(3, 3, 5);
    piece09.changeEntry(3, 4, 3);
    piece09.changeEntry(3, 4, 4);

    piece10.changeEntry(3, 0, 3);
    piece10.changeEntry(3, 1, 2);
    piece10.changeEntry(3, 1, 4);
    piece10.changeEntry(3, 2, 2);
    piece10.changeEntry(3, 2, 4);
    piece10.changeEntry(3, 3, 2);
    piece10.changeEntry(3, 3, 4);
    piece10.changeEntry(3, 4, 2);
    piece10.changeEntry(3, 4, 4);
    piece10.changeEntry(3, 5, 2);
    piece10.changeEntry(3, 5, 4);
    piece10.changeEntry(3, 6, 3);

    piece11.changeEntry(3, 0, 3);
    piece11.changeEntry(3, 1, 2);
    piece11.changeEntry(3, 1, 4);
    piece11.changeEntry(3, 2, 2);
    piece11.changeEntry(3, 2, 4);
    piece11.changeEntry(3, 3, 2);
    piece11.changeEntry(3, 3, 4);
    piece11.changeEntry(3, 4, 2);
    piece11.changeEntry(3, 4, 5);
    piece11.changeEntry(3, 5, 3);
    piece11.changeEntry(3, 5, 4);

    piece12.changeEntry(3, 0, 3);
    piece12.changeEntry(3, 1, 2);
    piece12.changeEntry(3, 1, 4);
    piece12.changeEntry(3, 2, 2);
    piece12.changeEntry(3, 2, 4);
    piece12.changeEntry(3, 3, 2);
    piece12.changeEntry(3, 3, 5);
    piece12.changeEntry(3, 4, 2);
    piece12.changeEntry(3, 4, 4);
    piece12.changeEntry(3, 5, 3);

    piece13.changeEntry(3, 0, 3);
    piece13.changeEntry(3, 1, 2);
    piece13.changeEntry(3, 1, 4);
    piece13.changeEntry(3, 2, 2);
    piece13.changeEntry(3, 2, 4);
    piece13.changeEntry(3, 2, 5);
    piece13.changeEntry(3, 3, 2);
    piece13.changeEntry(3, 3, 6);
    piece13.changeEntry(3, 4, 3);
    piece13.changeEntry(3, 4, 4);
    piece13.changeEntry(3, 4, 5);

    piece14.changeEntry(3, 0, 3);
    piece14.changeEntry(3, 1, 2);
    piece14.changeEntry(3, 1, 4);
    piece14.changeEntry(3, 2, 2);
    piece14.changeEntry(3, 2, 4);
    piece14.changeEntry(3, 3, 1);
    piece14.changeEntry(3, 3, 5);
    piece14.changeEntry(3, 4, 2);
    piece14.changeEntry(3, 4, 3);
    piece14.changeEntry(3, 4, 4);

    piece15.changeEntry(3, 1, 3);
    piece15.changeEntry(3, 2, 2);
    piece15.changeEntry(3, 2, 4);
    piece15.changeEntry(3, 3, 1);
    piece15.changeEntry(3, 3, 5);
    piece15.changeEntry(3, 4, 2);
    piece15.changeEntry(3, 4, 4);
    piece15.changeEntry(3, 5, 3);

    piece16.changeEntry(3, 1, 3);
    piece16.changeEntry(3, 2, 2);
    piece16.changeEntry(3, 2, 4);
    piece16.changeEntry(3, 3, 2);
    piece16.changeEntry(3, 3, 5);
    piece16.changeEntry(3, 4, 2);
    piece16.changeEntry(3, 4, 5);
    piece16.changeEntry(3, 5, 3);
    piece16.changeEntry(3, 5, 4);

    piece17.changeEntry(3, 0, 3);
    piece17.changeEntry(3, 1, 2);
    piece17.changeEntry(3, 1, 4);
    piece17.changeEntry(3, 2, 2);
    piece17.changeEntry(3, 2, 4);
    piece17.changeEntry(3, 3, 2);
    piece17.changeEntry(3, 3, 5);
    piece17.changeEntry(3, 4, 3);
    piece17.changeEntry(3, 4, 5);
    piece17.changeEntry(3, 5, 4);

    piece18.changeEntry(3, 1, 2);
    piece18.changeEntry(3, 1, 3);
    piece18.changeEntry(3, 2, 1);
    piece18.changeEntry(3, 2, 4);
    piece18.changeEntry(3, 3, 2);
    piece18.changeEntry(3, 3, 5);
    piece18.changeEntry(3, 4, 2);
    piece18.changeEntry(3, 4, 4);
    piece18.changeEntry(3, 5, 3);

    piece19.changeEntry(3, 1, 2);
    piece19.changeEntry(3, 1, 3);
    piece19.changeEntry(3, 2, 1);
    piece19.changeEntry(3, 2, 4);
    piece19.changeEntry(3, 3, 2);
    piece19.changeEntry(3, 3, 4);
    piece19.changeEntry(3, 4, 2);
    piece19.changeEntry(3, 4, 5);
    piece19.changeEntry(3, 5, 3);
    piece19.changeEntry(3, 5, 4);

    piece20.changeEntry(3, 1, 2);
    piece20.changeEntry(3, 1, 3);
    piece20.changeEntry(3, 2, 1);
    piece20.changeEntry(3, 2, 4);
    piece20.changeEntry(3, 3, 2);
    piece20.changeEntry(3, 3, 5);
    piece20.changeEntry(3, 4, 3);
    piece20.changeEntry(3, 4, 5);
    piece20.changeEntry(3, 5, 4);

    piece21.changeEntry(3, 1, 3);
    piece21.changeEntry(3, 1, 4);
    piece21.changeEntry(3, 2, 2);
    piece21.changeEntry(3, 2, 5);
    piece21.changeEntry(3, 3, 2);
    piece21.changeEntry(3, 3, 4);
    piece21.changeEntry(3, 4, 2);
    piece21.changeEntry(3, 4, 5);
    piece21.changeEntry(3, 5, 3);
    piece21.changeEntry(3, 5, 4);
  }

  /** Fills the Hashmap with all the pieces of a set. */
  public void fillPieceSet() {
    pieceSet.put(1, piece01);
    pieceSet.put(2, piece02);
    pieceSet.put(3, piece03);
    pieceSet.put(4, piece04);
    pieceSet.put(5, piece05);
    pieceSet.put(6, piece06);
    pieceSet.put(7, piece07);
    pieceSet.put(8, piece08);
    pieceSet.put(9, piece09);
    pieceSet.put(10, piece10);
    pieceSet.put(11, piece11);
    pieceSet.put(12, piece12);
    pieceSet.put(13, piece13);
    pieceSet.put(14, piece14);
    pieceSet.put(15, piece15);
    pieceSet.put(16, piece16);
    pieceSet.put(17, piece17);
    pieceSet.put(18, piece18);
    pieceSet.put(19, piece19);
    pieceSet.put(20, piece20);
    pieceSet.put(21, piece21);
  }

  /**
   * Getter method for the Hashmap with 21 pieces.
   *
   * @return HashMap pieceSet, filled with a complete set.
   */
  public HashMap<Integer, Piece> getPieceSet() {
    return pieceSet;
  }

  /**
   * Setter method for the HashMap with pieces.
   *
   * @param pieceSet with all necessary pieces of a game stored in it.
   */
  public void setPieceSet(HashMap<Integer, Piece> pieceSet) {
    this.pieceSet = pieceSet;
  }

  /**
   * Getter method for the pieces.
   *
   * @param pieceID unique piece id, which allows to call the right piece.
   * @return a piece from the Piece class.
   * @see Piece
   */
  public Piece getPiece(int pieceID) {
    return pieceSet.get(pieceID);
  }
}
