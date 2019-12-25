/** edgy.mechanics is a package for the game logic. */

package edgy.mechanics;

import edgy.usermanagment.Metrics;
import java.util.*;
import java.util.regex.PatternSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implements a match with 1 board and 4 game members.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class Match {

  /* match specific match identifier */
  private int matchID;

  /* Board defines current state of the board */
  public Board board;

  /**
   * Creates HashMap for the colors of the pieces of every player. Key is the color and values are
   * the credentials of a Player coming from a constructor.
   */
  public HashMap<Integer, Player> players;

  /* gives information whether game is ongoing or finished */
  private boolean gameOngoing = true;

  // Initiate rolling Server Logger
  private static Logger log = LogManager.getLogger(Match.class.getName());

  /**
   * Constructor match handles gameplay.
   *
   * @param matchNumber number of matches running.
   * @param firstPlayer the player who is the first starting a new match.
   */
  public Match(int matchNumber, Player firstPlayer) {
    this.matchID = matchNumber;
    board = new Board();

    players = new HashMap<Integer, Player>();
    players.put(firstPlayer.getColor(), firstPlayer);
  }

  /** Prints the scores for every player after during the game. Is updated for every move. */
  private void printScores() {
    String scores = "";
    for (Integer playerIndex : players.keySet()) {
      scores += "Player " + playerIndex + ": " + board.countPoints(playerIndex) + "\n";
    }
    for (Integer playerIndex : players.keySet()) {
      players.get(playerIndex).ct.printToClient(scores);
    }
  }

  private HashMap<String, Integer> calculateScores() {

    HashMap<String, Integer> scores = new HashMap<>();
    for (Integer p : players.keySet()) {
      scores.put(players.get(p).getUsername(), board.countPoints(p));
    }
    return scores;
  }

  /**
   * Calculates the points for every player in a game and prints them out.
   *
   * @return an array for the ranking from the highest points to the lowest.
   */
  public int[] calculateRanking() {
    /* color / points */
    HashMap<Integer, Integer> ranking = new HashMap<Integer, Integer>();
    for (Integer playerIndex : players.keySet()) {
      ranking.put(playerIndex, board.countPoints(playerIndex));
    }
    Object[] a = ranking.entrySet().toArray();

    Arrays.sort(
        a,
        new Comparator() {
          public int compare(Object o1, Object o2) {
            return ((Map.Entry<String, Integer>) o2)
                .getValue()
                .compareTo(((Map.Entry<String, Integer>) o1).getValue());
          }
        });

    int countRank = 0;
    // int count = 0;
    int[] rank = new int[4];
    int previous = -1;
    for (Object e : a) {
      System.out.println(
          ((Map.Entry<Integer, Integer>) e).getKey()
              + " : "
              + ((Map.Entry<Integer, Integer>) e).getValue());

      if (((Map.Entry<Integer, Integer>) e).getValue() == previous) {
        rank[((Map.Entry<Integer, Integer>) e).getKey() - 1] = countRank;
      } else {
        previous = ((Map.Entry<Integer, Integer>) e).getValue();
        countRank++;
        rank[((Map.Entry<Integer, Integer>) e).getKey() - 1] = countRank;
      }
    }
    return rank;
  }

  /**
   * Checks if game has ended. But is inactive.
   *
   * @return boolean: true if game ends, false if not.
   */
  private boolean calculateGameEnd() {
    for (Integer playerIndex : players.keySet()) {
      if (players.get(playerIndex).isNotSkipped()) {
        return true;
      }
    }
    return false;
  }

  /** Prints out the welcome message for a player joining a match. Not active. */
  private void welcomeMessage() {
    for (Integer key : players.keySet()) {
      players.get(key).ct.printToClient("Match is complete- get ready!");
    }
  }

  /**
   * method that leads to the move of one piece to the board and removes the piece from the
   * according HashMap by calling removePiece().
   *
   * @param color of the pieces.
   * @param pieceID number of the piece. (Between 1-21).
   * @param positionColumn vertical position/index of the piece.
   * @param positionRow horizontal position/index of the piece.
   * @return true if move is allowed, false if not.
   */
  public boolean move(int color, int pieceID, int positionRow, int positionColumn) {
    /* checks whether user owns the required piece */
    if (!players.get(color).getPieceHM().containsKey(pieceID)) {
      return false;
    }

    /* get specific piece */
    int[][] piece = players.get(color).getPieceHM().get(pieceID).getPiece();

    /* In case of first move, a piece can only be placed in a corner */
    boolean firstMove = (players.get(color).getPieceHM().size() == 21);

    /* checks if move is allowed */
    if (board.checkMove(color, piece, positionRow, positionColumn, firstMove)) {
      board.setPiece(color, piece, positionRow, positionColumn);
      // sounds.playPlace();
      return true;
    }
    return false;
  }

  /**
   * method that removes the piece from the hashmap of the according color * note: color already.
   * tested for value between 1 and 4
   *
   * @param playerColor of a piece.
   * @param key number of the piece (between 1-21).
   */
  private void removePiece(int playerColor, int key) {
    players.get(playerColor).getPieceHM().remove(key);
  }

  /**
   * method that checks user input and (if successful) moves piece on board.
   *
   * @param player current player with color
   * @param pieceID the unique Id of the current piece that will be moved.
   * @param positionRow the row position on the board where piece will be set.
   * @param positionColumn the column position on the board where piece will be set.
   * @throws PatternSyntaxException if input pattern of argument cannot be split.
   * @throws NumberFormatException if invalid input was done.
   */
  public void movePiece(Player player, int pieceID, int positionRow, int positionColumn) {
    /* check whether player surrendered */
    if (!player.isNotSkipped()) {
      player.ct.sendToClient("moved&FAIL&Sorry, you are skipped.");

      /* check it's players turn */
    } else if (!player.isYourTurn()) {
      player.ct.sendToClient("moved&FAIL&Sorry, not your turn.");

      /* check move for correctness */
    } else if (move(player.getColor(), pieceID, positionRow, positionColumn)) {

      /* updates all Match relevant data */
      updateMatch(player, pieceID);

      /* Move is not allowed */
    } else {
      player.ct.sendToClient("moved&FAIL&Move failed.");
    }
  }

  /**
   * Updates Board, rank, Points, turn.
   *
   * @param player information.
   */
  private void updateMatch(Player player, int pieceID) {
    /* update the board for everyone in Lobby */
    player.ct.packetCommunication.sendBoard();

    /* confirm move */
    player.ct.sendToClient("moved&OK&Move is ok!");

    /* decide next turn and deplet piece */
    removePiece(player.getColor(), pieceID);
    player.ct.sendToClient("delpi&" + pieceID);

    /* in case player layed all pieces and finishes the game. Player gets skipped */
    if (player.getPieceAmount() == 0) {
      player.ct.packetCommunication.skip();
    }

    /* Set next turn */
    player.setYourTurn(false);
    setNextYourTurn(player);

    /* update the current ranks and points in match */
    player.ct.packetCommunication.sendRanks();


  }

  /**
   * method to set the boolean yourTurn of the next valid player true.
   *
   * @param player player who is made last move is send along to determine next player
   */
  public void setNextYourTurn(Player player) {
    // iterate over players to get the right key of the player
    for (Integer playerID : players.keySet()) {
      if (players.get(playerID) == player) {
        int nextKey1 = playerID + 1;
        if (nextKey1 >= 5) {
          nextKey1 -= 4;
        }
        int nextKey2 = playerID + 2;
        if (nextKey2 >= 5) {
          nextKey2 -= 4;
        }
        int nextKey3 = playerID + 3;
        if (nextKey3 >= 5) {
          nextKey3 -= 4;
        }

        // set next your turn if no skipping
        if (players.get(nextKey1).isNotSkipped()) {
          players.get(nextKey1).setYourTurn(true);
          players
              .get(nextKey1)
              .ct
              .broadcast
              .privateBroadcast(whosTurn(), "turn_", "It's your turn");
          break;

        } else if (players.get(nextKey2).isNotSkipped()) {
          players.get(nextKey2).setYourTurn(true);
          players
              .get(nextKey2)
              .ct
              .broadcast
              .privateBroadcast(whosTurn(), "turn_", "It's your turn");
          break;

        } else if (players.get(nextKey3).isNotSkipped()) {
          players.get(nextKey3).setYourTurn(true);
          players
              .get(nextKey3)
              .ct
              .broadcast
              .privateBroadcast(whosTurn(), "turn_", "It's your turn");
          break;

        } else if (player.isNotSkipped()) {
          player.setYourTurn(true);
          player.ct.broadcast.privateBroadcast(whosTurn(), "turn_", "It's your turn");
          break;

        } else {
          for (Integer key : players.keySet()) {
            players.get(key).ct.printToClient("info_&Match finished.");
          }
          //printScores();
        }
      }
    }

    /*check whether end of game is reached */
    if (checkEndGame()) {
      /* If end of game is reached inform winner and losers and close match */
      endGame(player);
    }
  }

  private void endGame(Player player) {
    /* Calculate final highscore for every player save points of every user in database */
    player.ct.packetCommunication.updateScore(calculateScores());

    /* check whether requested username exists in database */
    player.ct.packetCommunication.userManagement.getHighscore();
    player.ct.packetCommunication.userManagement.sql.getHighscoreSql();

    /* Determine winner */
    int[] ranking = calculateRanking();

    /* inform losers */
    for (int i = 0; i < 4; i++) {
      if (ranking[i] == 1) {
        String winner = players.get(i + 1).getUsername();
        player.ct.broadcast.privateBroadcast(winner, "win__", "You won!!");

        /* Calculate total highscore */
        int userID = player.ct.packetCommunication.userManagement.getUserID(winner);
        Metrics m = player.ct.packetCommunication.userManagement.sql.highscore.get(userID);

        /* send new current highscore */
        player.ct.broadcast.privateBroadcast(winner, "score", Integer.toString(m.score));

      } else {
        String loser = players.get(i + 1).getUsername();
        player.ct.broadcast.privateBroadcast(loser, "lost_", "You lost!! Rank:" + (ranking[i]));

        /* Calculate total highscore */
        int userID = player.ct.packetCommunication.userManagement.getUserID(loser);
        Metrics m = player.ct.packetCommunication.userManagement.sql.highscore.get(userID);

        /* send new current highscore */
        player.ct.broadcast.privateBroadcast(loser, "score", Integer.toString(m.score));
      }
    }

    System.out.println("END game erreicht.");
    resetStatus();

    /* update Lobby status for everyone */
    player.ct.broadcast.publicBroadcast("lslob", player.ct.lobby.getLobbies());
  }

  /** Resets all player specific information to be able to restart a new game. */
  private void resetStatus() {
    for (Integer key : players.keySet()) {
      players.get(key).setYourTurn(false);
      players.get(key).setColor(1);
      players.get(key).setNotSkipped(true);
      players.get(key).setMatchID(-1);
      Pieces pieces = new Pieces();
      players.get(key).setPieceHM(pieces.getPieceSet());
      /* mark match as finished */
      gameOngoing = false;
    }
  }

  /**
   * Getter method for the Player amount.
   *
   * @return number of players.
   */
  public int getPlayerAmount() {
    return players.size();
  }

  /**
   * Returns all players of match in a Arraylist. In order: yellow, blue, green, blue
   *
   * @return String list holding all usernames and player numbers of the players who have joined.
   */
  public ArrayList<String> getMatchPlayers() {
    ArrayList<String> playerUsernames = new ArrayList<>();
    for (Integer key : players.keySet()) {
      playerUsernames.add(players.get(key).getUsername());
    }
    return playerUsernames;
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
        int currentEntry = board.board[i][j];
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
      // s += "\n";
    }
    return s;
  }

  /** Prints board for the player. Used on the console. */
  public void printBoard() {
    String board = getBoard();
    System.out.println(board);
  }

  /**
   * method that returns a String containing all pieces that are left. Note: color is not checked
   * for range 1-4.
   *
   * @param color of the pieces.
   * @param hmL Hashmap for the pieces of a match.
   * @return printed pieces.
   */
  public String printPieces(int color, HashMap<Integer, Match> hmL) {
    HashMap<Integer, Piece> hmColor = players.get(color).getPieceHM();
    String s = "";

    for (Integer pieceNumber : hmL.keySet()) {
      s += printOnePiece(hmColor.get(pieceNumber).getPiece(), color);
      s += "\n";
    }
    return s;
  }

  /**
   * Prints only one piece.
   *
   * @param piece two dimensional array.
   * @param color of the piece.
   * @return piece printed.
   */
  public String printOnePiece(int[][] piece, int color) {
    String pieceString = "";
    for (int r = 1; r < 6; r++) {
      for (int c = 1; c < 6; c++) {
        if (piece[r][c] == 1) {
          pieceString += color;
        } else {
          pieceString += "*";
        }
      }
      pieceString += "\n";
    }
    return pieceString;
  }

  /**
   * method that lists all pieces that are left.
   *
   * @param color of the pieces.
   * @return printed list.
   */
  public String listPieces(int color) {
    HashMap<Integer, Piece> hmColor = players.get(color).getPieceHM();
    String remainingPieces = "";
    for (Integer key : hmColor.keySet()) {
      remainingPieces += (key + ":");
    }
    return remainingPieces;
  }

  /**
   * method that adds further player to the match.
   *
   * @param color of the player that acts as key.
   * @param player constructor instance with all credentials of a player.
   */
  public void addPlayer(int color, Player player) {
    player.setColor(color);
    // images are set according to color
    players.put(color, player);
  }

  /**
   * Getter method for Match number.
   *
   * @return number of matches.
   */
  public int getMatchNumber() {
    return matchID;
  }

  /**
   * Setter method for Match number.
   *
   * @param matchNumber number of actual matches.
   */
  public void setMatchNumber(int matchNumber) {
    this.matchID = matchNumber;
  }

  /**
   * Getter method for HashMap for the Players in a match.
   *
   * @return HashMap.
   */
  public HashMap<Integer, Player> getPlayers() {
    return players;
  }

  /**
   * Determines whether the match with 4 players is full.
   *
   * @return HashMap.
   */
  public boolean isFull() {
    if (getPlayers().size() >= 4) {
      return true;
    }
    return false;
  }

  /**
   * Setter method for HashMap of the Players in a match.
   *
   * @param hmPlayers with the credentials of the players.
   */
  public void setHmPlayers(HashMap<Integer, Player> hmPlayers) {
    this.players = hmPlayers;
  }

  /**
   * Returns username of player which turn it is.
   *
   * @return username of player who can play the next round.
   */
  public String whosTurn() {
    for (Player p : players.values()) {

      if (p.isYourTurn()) {
        return p.getUsername();
      }
    }
    return "We have a problem";
  }

  /**
   * Checks if the game will be ended. Which will be done, if all players have skipped.
   *
   * @return boolean: true if all players skipped, false if not.
   */
  public boolean checkEndGame() {
    /* in case a player is still active the game will continue */
    for (Player p : players.values()) {
      if (p.isNotSkipped()) {
        return false;
      }
    }
    /* a Winner needs to be determined */
    return true;
  }

  /**
   * method that calculates a piece that the player could play or tells if no move possible anymore.
   *
   * @param color color of the player
   * @return String containing the hint information (piece number, board row and board color)
   *     separated by ampersands
   */
  public String predictHint(int color) {
    /* retrieve player information */
    Player p = players.get(color);

    /* avoid first move */
    if (p.getPieceAmount() == 21) {
      return "hint_&start&0&0";
    }

    Integer[] orderedPieces = getOrderedPieces(color);

    /* iterate over all pieces. Larger ones first */
    for (Integer pieceID : orderedPieces) {
      for (int flip = 0; flip < 2; flip++) {
        for (int rot = 0; rot < 3; rot++) {
          /* iterate over whole board from 0 to 19 */
          for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
              /* in case move is possible */
              if (checkMove(p, pieceID, x, y)) {
                return "hint_&" + pieceID + "&" + x + "&" + y;
              }
            }
          }
          p.turnClockwise(pieceID);
          p.ct.sendToClient("piece&turnright&" + pieceID);
        }
        p.flipPiece(pieceID);
        p.ct.sendToClient("piece&flip&" + pieceID);
      }
    }
    /* it's not possible to continue */
    return "hint_&0&0&0";
  }

  private Integer[] getOrderedPieces(int color) {
    /* Get pieces Set of specific player */
    HashMap<Integer, Piece> pieces = players.get(color).getPieceHM();

    Integer[] order = new Integer[pieces.size()];
    int counter = 0;
    for (int pieceID : pieces.keySet()) {
      order[counter] = pieceID;
      counter++;
    }
    Arrays.sort(order, Collections.reverseOrder());
    return order;
  }

  private boolean checkMove(Player p, int pieceID, int x, int y) {
    /* check first whether there is already a piece on x, y */
    if (board.board[x][y] != 0) {
      return false;
    }

    int color = p.getColor();
    int[][] piece = p.pieceHM.get(pieceID).getPiece();
    if (!board.checkPieceTouchesCorner(color, piece, x, y)) {
      return false;
    }
    ;
    if (!board.checkPieceOnBoard(piece, x, y)) {
      return false;
    }
    if (!board.checkNoOverlapping(piece, x, y)) {
      return false;
    }
    ;
    if (!board.checkPieceNoSides(color, piece, x, y)) {
      return false;
    }
    ;
    return true;
  }

  /**
   * method that returns whether the match is not full yet, ongoing or already finished.
   *
   * @return match status in form of a String: 1: not full yet 2: ongoing 3: finished
   */
  public String getMatchStatus() {
    /* Match is not full yet. Game hasn't started */
    if (getPlayerAmount() <= 3) {
      return "1";
    }

    /* Match is ongoing */
    if (gameOngoing) {
      return "2";
    }

    /* Match has finished */
    return "3";
  }
}
