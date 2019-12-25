/**
 * test.edgy is a package for JUnit testing.
 */

package edgy;

import edgy.mechanics.Board;
import edgy.mechanics.Piece;
import edgy.mechanics.Player;
import edgy.mechanics.Pieces;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the methods from the Board class.
 */
public class BoardTest extends TestCase  {

    /**
     * new boards and pieces are initiated.
     */
    int[][] testBoard = new int[20][20];
    Board board = new Board();
    Board b = new Board();
    Board c = new Board();
    Pieces p = new Pieces();

    /**
     * Prints the test board.
     */
    public void printBoard() {

        board.printBoard();

        for(int x=0; x<20;x++){
            for(int y=0; y<20;y++) {
                System.out.print(board.board[x][y]);
            }
            System.out.println();
        }
        System.out.println();

        for(int x=0; x<20;x++){
            for(int y=0; y<20;y++) {
                System.out.print(testBoard[x][y]);
            }
            System.out.println();
        }
    }

    /**
     * Tests the correct placement of the current piece on the board.
     */
    @Test
    public void testCorrectPlacmentOfPiece() {
        testBoard[0][2] = 1;
        testBoard[1][2] = 1;
        testBoard[2][2] = 1;
        testBoard[3][2] = 1;
        testBoard[4][2] = 1;

        int[][] p10 = p.getPiece(10).getPiece();

        board.setPiece(1, p10, 2, 2);

        Assert.assertArrayEquals(board.board, testBoard);
     //   Assert.assertEquals('C', stString.floor('C'));
    }

    /**
     * Tests if the piece is really set on the board.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetPieceException() {
        int[][] p2 = p.getPiece(2).getPiece();

        board.setPiece(1,p2,0,1);

    }


    /**
     *  Tests if move is refused, when the piece is not entirely on the board.
     */
    @Test
    public void testCheckPieceOnBoard() {

       int[][] p2 = p.getPiece(2).getPiece();

       boolean notOnBoard = board.checkPieceOnBoard(p2, 0, 0);
       boolean notOnBoard1 = board.checkPieceOnBoard(p2, 45, 40);
       boolean isOnBoard = board.checkPieceOnBoard(p2, 1, 0);

        Assert.assertFalse(notOnBoard);
        Assert.assertFalse(notOnBoard1);
        Assert.assertTrue(isOnBoard);
    }

    /**
     * Tests if the method for checking
     * overlapping pieces, is working.
     */
    @Test
    public void testOverlappingOfPieces() {
          int[][] p6 = p.getPiece(6).getPiece();
          int[][] p12 = p.getPiece(12).getPiece();


          board.setPiece(1, p6, 1, 0);
          boolean areOverlapping = board.checkNoOverlapping(p12, 2, 1);
          Assert.assertFalse(areOverlapping);

          board.setPiece(1, p6, 1, 0);
          areOverlapping = board.checkNoOverlapping(p12, 4, 1);
          Assert.assertTrue(areOverlapping);

      }

    /**
     * Tests if the method for checking overlapping
     * pieces is throwing the exception
     * if the piece isn't fully set on the board.
     */
    // ToDO Teste bei Row: -5 10
    @Test (expected = ArrayIndexOutOfBoundsException.class)
      public void testExceptionOverlappingMethod() {
        int[][] p6 = p.getPiece(6).getPiece();
        int[][] p12 = p.getPiece(12).getPiece();

        board.setPiece(1, p6, 18, 0);
        board.setPiece(1,p12,-5,10);
      }

    /**
     * Tests, if sides of same colour aren't side by side.
     */
    @Test
    public void testPiecesNoSide() {
        int[][] p1 = p.getPiece(1).getPiece();
        int[][] p2 = p.getPiece(2).getPiece();

        board.setPiece(1, p1, 1, 18);
        board.setPiece(1, p2, 1, 19);

        boolean isSidebySide = board.checkPieceNoSides(1, p2, 1, 19);

        Assert.assertFalse(isSidebySide);
        //ToDO. check out of bounds
    }

    /**
     * Tests if the method for checking
     * if no sides of pieces from same color
     * are touching, is working.
     */
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testPiecesNoSideException() {
        int[][] p2 = p.getPiece(2).getPiece();

        board.checkPieceNoSides(1,p2,0,1);

    }
    //Todo exception test for setPieceMethod.... ArrayIndexOutOfBounds
    /**
     * Tests if the first move is done in a corner.
     */
    @Test
    public void testCheckCorner() {
        int[][] p1 = p.getPiece(1).getPiece();
        int[][] p2 = p.getPiece(2).getPiece();
        int[][] pb1 = p.getPiece(1).getPiece();

        board.setPiece(1, p1, 0,0);
      //  board.setPiece(2, p2, 17, 5);
        boolean isInFreeCorner = board.checkFirstMove1(2, p2, 19, 19);

        Assert.assertTrue(isInFreeCorner);

        boolean isInNoFreeCorner = board.checkFirstMove(2,pb1,0,0);
      //  board.setPiece(2,pb1,0,0);
        printBoard();
        Assert.assertTrue(isInNoFreeCorner);
    }

    /**
     * Tests if the method for checking if
     * the first move has been done in a corner
     * throws an exception piece isn't fully on the board.
     */
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testFirstMoveException() {
        int[][] p1 = p.getPiece(1).getPiece();

        board.checkFirstMove1(1,p1,-4,-6);
    }

    /**
     * Tests if at least one corner touches a piece of the same colour.
     */
    @Test
    //Error, out of Bound?
    public void testCheckCornerAllMoves() {
        int[][] p1 = p.getPiece(1).getPiece();
        int[][] p2 = p.getPiece(2).getPiece();
        int[][] p17 = p.getPiece(17).getPiece();
        int[][] p12 = p.getPiece(12).getPiece();

        board.setPiece(1, p1, 0, 0);
        /*No more needed, because of exception testing*/
     //  boolean isNotTouching = board.checkPieceTouchesCorner(1, p2, 1, 2);
     //  Assert.assertFalse(isNotTouching);

      //  board.setPiece(1, p2, 2, 1);
      //  boolean isTouching = board.checkPieceTouchesCorner(1, p2, 2, 1);
      //  Assert.assertTrue(isTouching);

        board.setPiece(3,p12,18,0);
        printBoard();
        boolean isTouching2 = board.checkPieceTouchesCorner(3,p17,14,0);
        board.setPiece(3,p17,14,0);
        printBoard();
        Assert.assertTrue(isTouching2);
    }


    /**
     * Tests if at least one corner touches a piece of the same colour.
     */
    @Test
    //Error, out of Bound?
    public void testCheckCornerAtBoardEdge() {
        Player p = new Player("Gelb 1",0);

        /* Rotate or flip all pieces */
        p.turnCounterClockwise(13);

        /* Create Board */
        Board b = new Board();

        b.setPiece(1, p.pieceHM.get(13).getPiece(), 12, 18);
        b.setPiece(2, p.pieceHM.get(11).getPiece(), 10,0);


        Assert.assertTrue(b.checkPieceOnBoard(p.pieceHM.get(10).getPiece(),7, 19));
        Assert.assertTrue(b.checkNoOverlapping(p.pieceHM.get(10).getPiece(), 7, 19));
        Assert.assertTrue(b.checkPieceNoSides(1, p.pieceHM.get(10).getPiece(), 7, 19));
        Assert.assertTrue(b.checkPieceTouchesCorner(1,p.pieceHM.get(10).getPiece(), 7, 19));


        b.setPiece(1, p.pieceHM.get(10).getPiece(), 7, 19);
        b.printBoard();
    }

    /**
     * Tests if the method for checking
     * that every piece of the same color are touching
     * at least one corner, throws an exception if
     * the piece isn't fully on the board.
     */
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testExceptionforCornerCheck() {
        int[][] p12 = p.getPiece(2).getPiece();
        int[][] p17 = p.getPiece(17).getPiece();

        board.setPiece(1,p12,2,0);
        board.checkPieceTouchesCorner(1,p17,1,2);
    }

    /**
     * Tests flip funcion.
     */
    @Test
    public void testFlipPiece() {

        Player player = new Player("Harry", 0);

        Piece pTest = new Piece();
        pTest.changeEntry(1,1,3);
        pTest.changeEntry(1,2,3);
        pTest.changeEntry(1,3,3);
        pTest.changeEntry(1,3,2);
        pTest.changeEntry(1,3,1);

        pTest.changeEntry(2,0,2);
        pTest.changeEntry(2,0,4);
        pTest.changeEntry(2,2,0);
        pTest.changeEntry(2,4,0);
        pTest.changeEntry(2,4,4);

        pTest.changeEntry(3,0,3);
        pTest.changeEntry(3,1,2);
        pTest.changeEntry(3,1,4);
        pTest.changeEntry(3,2,1);
        pTest.changeEntry(3,2,2);
        pTest.changeEntry(3,2,4);
        pTest.changeEntry(3,3,0);
        pTest.changeEntry(3,3,4);
        pTest.changeEntry(3,4,1);
        pTest.changeEntry(3,4,2);
        pTest.changeEntry(3,4,3);


        p.pieceSet.put(22, pTest);

       int[][] pTestFlip = p.getPiece(22).getPiece();

       b.setPiece(1,pTestFlip,4,3);
       player.flipPiece(13);
       int[][] p13 = player.pieceHM.get(13).getPiece();
       c.setPiece(2,p13,16,3);

      Assert.assertArrayEquals(p13, pTestFlip );
    }

    /**
     * Tests if the method for
     * turning a piece clockwise is working.
     */
    @Test
    public void testTurnClockwise() {
        Piece testTurnR = new Piece();
        Player player = new Player("Harry", 20000000);


        testTurnR.changeEntry(1,3,2);
        testTurnR.changeEntry(1,3,3);
        testTurnR.changeEntry(1,3,4);
        testTurnR.changeEntry(1,4,2);
        testTurnR.changeEntry(1,4,3);

        testTurnR.changeEntry(2,2,1);
        testTurnR.changeEntry(2,2,5);
        testTurnR.changeEntry(2,4,5);
        testTurnR.changeEntry(2,5,1);
        testTurnR.changeEntry(2,5,4);

        testTurnR.changeEntry(3,2,2);
        testTurnR.changeEntry(3,2,3);
        testTurnR.changeEntry(3,2,4);
        testTurnR.changeEntry(3,3,1);
        testTurnR.changeEntry(3,3,5);
        testTurnR.changeEntry(3,4,1);
        testTurnR.changeEntry(3,4,4);
        testTurnR.changeEntry(3,5,2);
        testTurnR.changeEntry(3,5,3);

        p.pieceSet.put(23,testTurnR);
        int[][] pTestTurnR = p.getPiece(23).getPiece();
        b.setPiece(1,pTestTurnR,4,5);
        player.turnClockwise(16);
        int[][] p16 = player.pieceHM.get(16).getPiece();
        c.setPiece(2,p16,17,15);

        Assert.assertArrayEquals(p16,pTestTurnR);

    }

    /**
     * Tests if the method for turning
     * a piece counterclockwise, is working.
     */
    @Test
    public void testTurnCounterClockwise() {
        Piece testTurnL = new Piece();
        Player player = new Player("Harry", 20000000);

        testTurnL.changeEntry(1,2,3);
        testTurnL.changeEntry(1,2,4);
        testTurnL.changeEntry(1,3,2);
        testTurnL.changeEntry(1,3,3);
        testTurnL.changeEntry(1,3,4);

        testTurnL.changeEntry(2,1,2);
        testTurnL.changeEntry(2,1,5);
        testTurnL.changeEntry(2,2,1);
        testTurnL.changeEntry(2,4,1);
        testTurnL.changeEntry(2,4,5);

        testTurnL.changeEntry(3,1,3);
        testTurnL.changeEntry(3,1,4);
        testTurnL.changeEntry(3,2,2);
        testTurnL.changeEntry(3,2,5);
        testTurnL.changeEntry(3,3,1);
        testTurnL.changeEntry(3,3,5);
        testTurnL.changeEntry(3,4,2);
        testTurnL.changeEntry(3,4,3);
        testTurnL.changeEntry(3,4,4);

        p.pieceSet.put(24,testTurnL);
        int[][] pTestTurnL = p.getPiece(24).getPiece();
        b.setPiece(1,pTestTurnL,4,5);
        player.turnCounterClockwise(16);
        int[][] p16 = player.pieceHM.get(16).getPiece();
        c.setPiece(2,p16,17,15);

        Assert.assertArrayEquals(p16,pTestTurnL);
    }
}
