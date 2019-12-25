/**
 * Test.edgy is a package for JUnit tests.
 */

package edgy;

import edgy.mechanics.Board;
import edgy.mechanics.Player;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests an entire game.
 */
public class GameplayTest extends TestCase {

    /**
     * Initiation of 4 players and a new test board.
     */
    Board b = null;
    Player p1;
    Player p2;
    Player p3;
    Player p4;

    /**
     * Inner class for the information
     * of a piece.
     */
    class coordinatesPiece {
        int color;
        int[][] piece;
        int x;
        int y;
        boolean start;

        /**
         * Constructor for the piece's information.
         * @param p is a player
         * @param color is the color of the player
         * @param pieceID is the unique piece' id
         * @param x is the row coordinate
         * @param y is the column coordinate
         * @param start true if it is first move, false if not
         */
        public coordinatesPiece(Player p, int color, int pieceID, int x, int y, boolean start) {
            this.color = color;
            this.piece = p.pieceHM.get(pieceID).getPiece();
            this.x = x;
            this.y = y;
            this.start = start;
        }
    }

    /**
     * Creates a new test board.
     */
    public void createNewBoard() {
        if(b==null) {
            b = new Board();
        }
    }

    /**
     * Tests all moves of the yellow player.
     */
    @Test
    public void testYellowMatch() {
        p1 = new Player("Gelb 1",0);

        /* Rotate or flip all pieces */
        p1.turnClockwise(11);
        p1.flipPiece(18);
        p1.turnClockwise(18);

        p1.turnClockwise(16);
        p1.turnClockwise(16);
        p1.flipPiece(16);

        p1.turnClockwise(13);
        p1.turnClockwise(13);

        p1.turnCounterClockwise(14);
        p1.turnCounterClockwise(14);

        p1.turnCounterClockwise(8);

        p1.turnCounterClockwise(20);

        p1.flipPiece(21);

        p1.turnClockwise(12);
        p1.flipPiece(12);

        /* Create Board */
        b = new Board();

        int[] order = {11,15,16,18,13,19,1,14,8,3,20,12,9,2,21,17, 5,  4};
        int[] x =     {18,16,13,14,9, 7, 7, 6,8,5,4, 2 ,4,1, 1,14,12, 16};
        int[] y =     {1, 4, 5, 8, 7, 8, 10,4,1,0,3, 7 ,9,4, 2, 1, 3 , 0};


        for(int i=0; i<order.length; i++) {
            boolean start = false;
            if(i==0) { start = true; }
            coordinatesPiece c = new coordinatesPiece(p1,1,order[i],x[i],y[i],start);
            Assert.assertTrue(b.checkPieceOnBoard(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkNoOverlapping(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkPieceNoSides(c.color, c.piece, c.x, c.y));

            if (i == 0) {
                Assert.assertFalse(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
                b.setPiece(1, c.piece, c.x, c.y);
            } else {
                Assert.assertTrue(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
                b.setPiece(1, c.piece, c.x, c.y);

            }
        }
    }

    /**
     * Tests all moves of the blue player.
     */
    @Test
    public void testBlueMatch() {

         p3 = new Player("Blau 3",0);

        /*Rotate and flip*/

        p3.turnClockwise(12);
        p3.turnClockwise(12);
        p3.flipPiece(12);
        p3.turnCounterClockwise(17);

        p3.turnCounterClockwise(8);
        p3.flipPiece(8);

        p3.turnClockwise(5);
        p3.turnClockwise(19);
        p3.turnCounterClockwise(14);
        p3.turnCounterClockwise(14);
        p3.flipPiece(11);
        p3.turnCounterClockwise(11);
        p3.turnCounterClockwise(13);
        p3.turnClockwise(18);
        p3.turnClockwise(18);
        p3.turnClockwise(20);
        p3.turnClockwise(20);
        p3.turnClockwise(21);

        createNewBoard();

        int[] order = {12,7,13,6 ,2 ,10,1 ,18,3 ,20,21,17,16,11,14,8,4,5 ,19};
        int[] x =     {1 ,5,9 ,11,13,12,15,15,18,18,12,5 ,7 ,5 ,2 ,1,1,0 ,1};
        int[] y =     {0 ,1,3 ,0 ,2 ,4 ,3 ,6 ,5 ,9 ,8 ,5 ,6 ,9 ,11,8,5,11,15};

        for(int i=0; i<order.length; i++) {
            boolean start = false;
            if(i==0) { start = true; }
            coordinatesPiece c = new coordinatesPiece(p3,3,order[i],x[i],y[i],start);
            Assert.assertTrue(b.checkPieceOnBoard(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkNoOverlapping(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkPieceNoSides(c.color, c.piece, c.x, c.y));

              if (i == 0) {
                Assert.assertFalse(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
                  b.setPiece(3, c.piece, c.x, c.y);
              } else {
                  b.setPiece(3, c.piece, c.x, c.y);
                  b.printBoard();
                Assert.assertTrue(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
              }

//            Assert.assertTrue(b.checkMove(1, c.piece, c.x, c.y, start));
        }
    }

    /**
     * Tests all moves of the green player.
     */
    @Test
    public void testGreenMatch() {
         p4 = new Player("Grün4",0);

        /*Rotate and Flip*/
        p4.turnCounterClockwise(11);
        p4.turnCounterClockwise(11);
        p4.turnCounterClockwise(17);
        p4.turnCounterClockwise(17);
        p4.flipPiece(17);
        p4.turnClockwise(20);
        p4.turnClockwise(20);
        p4.turnCounterClockwise(12);
        p4.flipPiece(12);
        p4.flipPiece(7);
        p4.turnCounterClockwise(2);
        p4.flipPiece(16);
        p4.turnCounterClockwise(16);
        p4.turnCounterClockwise(16);
        p4.turnCounterClockwise(4);
        p4.turnCounterClockwise(4);
        p4.turnCounterClockwise(13);
        p4.turnCounterClockwise(13);
        p4.turnClockwise(6);
        p4.flipPiece(21);

        createNewBoard();

        int[] order = {11,17,16, 5,10,15,20,12, 8, 7, 2, 4,13, 1, 6,21, 3};
        int[] x =     { 1, 5, 9,13,10,13,15,17,11,14, 9, 9, 6, 5, 4, 2, 2};
        int[] y =     {19,17,18,19,16,14,17,14,11,11,10,13,14,11,15,13,17};


        for(int i=0; i<order.length; i++) {
            boolean start = false;
            if(i==0) { start = true; }
            coordinatesPiece c = new coordinatesPiece(p4,4,order[i],x[i],y[i],start);
            Assert.assertTrue(b.checkPieceOnBoard(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkNoOverlapping(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkPieceNoSides(c.color, c.piece, c.x, c.y));

            if (i == 0) {
                Assert.assertFalse(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
                b.setPiece(4, c.piece, c.x, c.y);
            } else {
                b.setPiece(4, c.piece, c.x, c.y);
                Assert.assertTrue(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
            }
        }
    }

    /**
     * Tests all moves of the red player.
     */
    @Test
    public void testRedMatch() {
         p2 = new Player("Rot2",0);

        /*Rotate and Flip*/
        p2.turnCounterClockwise(2);
        p2.turnCounterClockwise(5);
        p2.turnCounterClockwise(16);
        p2.flipPiece(8);
        p2.turnCounterClockwise(18);
        p2.turnCounterClockwise(18);
        p2.turnCounterClockwise(13);
        p2.turnClockwise(21);
        p2.turnClockwise(12);
        p2.turnClockwise(12);
        p2.flipPiece(12);
        p2.turnCounterClockwise(7);
        p2.flipPiece(17);

        createNewBoard();

        int[] order = {20,18, 2, 5,16, 1, 8, 13, 21, 3,12,17, 4, 7, 6,19,15};
        int[] x =     {18,15,19,18,17,16,18,13,  11, 9, 7, 6, 5, 3,14,11, 8};
        int[] y =     {18,15,17,14,10, 8, 7,18,  14,17,15,19,13,15,12,10,11};

        for(int i=0; i<order.length; i++) {
            boolean start = false;
            if (i == 0) {
                start = true;
            }
            coordinatesPiece c = new coordinatesPiece(p2, 2, order[i], x[i], y[i], start);
            Assert.assertTrue(b.checkPieceOnBoard(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkNoOverlapping(c.piece, c.x, c.y));
            Assert.assertTrue(b.checkPieceNoSides(c.color, c.piece, c.x, c.y));

            if (i == 0) {
                Assert.assertFalse(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
                b.setPiece(2, c.piece, c.x, c.y);
            } else {
                b.setPiece(2, c.piece, c.x, c.y);
                Assert.assertTrue(b.checkPieceTouchesCorner(c.color, c.piece, c.x, c.y));
            }
        }
    }

    /**
     * Calls all four players together.
     */
    @Test
    public void testAllPlayers() {
        testYellowMatch();
        testRedMatch();
        testBlueMatch();
        testGreenMatch();
    }

    /**
     * Tests all four players together
     * and their points.
     */
    @Test
    public void testPoints() {
        testAllPlayers();

        int totalPoints = 89;
        int red = 19;
        int yellow = 13;
        int blue = 9;
        int green = 19;

        Assert.assertEquals(totalPoints - yellow, b.countPoints(1));
        Assert.assertEquals(totalPoints - red, b.countPoints(2));
        Assert.assertEquals(totalPoints - blue, b.countPoints(3));
        Assert.assertEquals(totalPoints - green, b.countPoints(4));
     }

}
