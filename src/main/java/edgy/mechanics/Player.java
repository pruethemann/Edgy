/** edgy.mechanics is a package for the game logic. */

package edgy.mechanics;

import edgy.server.ClientThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Initialisation for players and game tools. Constructors and all getter and setter methods.
 *
 * @author while(true){do nothing}.
 * @version 1.0
 */
public class Player {

  /** Initiates the rolling server logger. */
  private static Logger log = LogManager.getLogger(Player.class.getName());

  /** field declarations with user and game tool attributes. */
  private String username;

  private int id;
  private int highscore;
  private String password;
  private int score;
  private String computerLoginName;
  private String computername;
  private String IP = "";
  private int color;
  private Pieces pieces = new Pieces();
  public HashMap<Integer, Piece> pieceHM = pieces.getPieceSet();
  private int matchID = -1; // not assigned to a match yet
  private boolean yourTurn = false;
  public ClientThread ct;
  private boolean notSkipped;
  private boolean isLoggedIn = false;

  /**
   * Constructor initializes computername, IP and Login Name of System.Constructor used for Server.
   * Additionally, each player has a color and a pieceSet in form of a HashMap.
   *
   * @param computername used for username and login.
   * @param IP from serveraddress.
   * @param computerLoginName for username and login.
   * @param ct a ClientThread.
   */
  public Player(String computername, String IP, String computerLoginName, ClientThread ct) {
    this.computername = computername;
    this.IP = IP;
    this.computerLoginName = computerLoginName;
    username = "Client: " + computername;
    color = 1;
    pieces = new Pieces();
    pieceHM = pieces.getPieceSet();
    yourTurn = false;
    notSkipped = true;
    this.ct = ct;
  }

  /**
   * Constructor for creating a new user with nuser function.
   * @param username for new user
   * @param score total points of the user.
   */
  public Player(String username, int score) {
    this.username = username;
    this.score = score;
  }

  /**
   * Constructor with all the credentials of an user stored in the database.
   * @param id automatically created for every user.
   * @param username is unique.
   * @param score the total points of an user.
   */
  public Player(int id, String username, int score) {
    this.id = id;
    this.username = username;
    this.score = score;
  }

  /** default constructor. */
  public Player() {}

  /**
   * method for creating a new user with nuser function. Constructor used for Client.
   *
   * @param username for new user
   * @param score total points of the user.
   */
  public void setPlayer(String username, int score) {
    this.username = username;
    this.highscore = score;
    pieces = new Pieces();
    pieceHM = pieces.getPieceSet();
  }

  /**
   * method that returns the current amount of pieces of this player color.
   *
   * @return an integer for the number of pieces of a player.
   */
  public int getPieceAmount() {
    return pieceHM.size();
  }

  /**
   * method to print all remaining pieces inclusive their configuration.
   *
   * @param color color of the player whose pieces should be printed
   * @return pieceString holding all pieces 5x5 to print on the console
   */
  public String printPiecesOfPlayer(int color) {
    String pieceString = "";
    char colorChar = getColorChar(color);
    for (Integer key : pieceHM.keySet()) {
      pieceString += printOnePiece(key, colorChar);
      pieceString += "\n";
    }
    return pieceString;
  }

  /**
   * Get the color of the piece. And prints it out as a char. y = yellow. r= red. b = blue. g =
   * green.
   *
   * @param color is the color of the piece.
   * @return char y, r, b or g.
   */
  // REVIEW: Javadoc missing
  private char getColorChar(int color) {
    char colorChar;
    switch (color) {
      case 1:
        colorChar = 'y';
        break;
      case 2:
        colorChar = 'r';
        break;
      case 3:
        colorChar = 'b';
        break;
      case 4:
        colorChar = 'g';
        break;
      default:
        colorChar = '?';
        break;
    }
    return colorChar;
  }

  /**
   * Prints out only one of the pieces. So the user can see the piece array.
   *
   * @param key the piece unique id.
   * @param color the color of the piece.
   * @return a String of the piece array.
   */
  // REVIEW: Javadoc missing
  private String printOnePiece(int key, char color) {
    String pieceFragment = "Piece " + key + ": \n";
    int[][] piece = pieceHM.get(key).getPiece();
    for (int row = 1; row <= 6; row++) {
      for (int column = 1; column <= 6; column++) {
        if (piece[row][column] == 1) {
          pieceFragment += color;
        } else {
          pieceFragment += "*";
        }
      }
      pieceFragment += "\n";
    }
    return pieceFragment;
  }

  /**
   * Method that deletes the piece from pieceHM. The acceptance of the move is not checked here.
   *
   * @param key position of piece to remove.
   */
  public void deletePiece(int key) {
    pieceHM.remove(key);
  }

  /**
   * method to flip the piece counterclockwise.
   *
   * @param key number of the piece.
   */
  public void turnCounterClockwise(int key) {
    Piece p = pieceHM.get(key);
    int[][] value = p.getPiece();
    int[][] tc = new int[7][7];
    for (int row = 0; row < 7; row++) {
      for (int column = 0; column < 7; column++) {
        tc[row][column] = value[column][6 - row];
      }
      p.setPiece(tc);
      pieceHM.replace(key, p);
    }
  }

  /**
   * Method to turn the piece clockwise.
   *
   * @param key number of the piece.
   */
  public void turnClockwise(int key) {
    Piece p = pieceHM.get(key);
    int[][] value = p.getPiece();
    int[][] tcc = new int[7][7];
    for (int row = 0; row < 7; row++) {
      for (int column = 0; column < 7; column++) {
        tcc[row][column] = value[6 - column][row];
      }
    }
    p.setPiece(tcc);
    pieceHM.replace(key, p);
  }

  /**
   * Method to flip the piece horizontally.
   *
   * @param key piece number received from the client as argument.
   */
  public void flipPiece(int key) {
    Piece p = pieceHM.get(key);
    int[][] value = p.getPiece();
    int[][] flip = new int[7][7];
    for (int row = 0; row < 7; row++) {
      for (int column = 0; column < 7; column++) {
        flip[row][column] = value[row][6 - column];
      }
    }
    p.setPiece(flip);
    pieceHM.replace(key, p);
  }

  /**
   * For changing or creating new username.
   *
   * @see edgy.usermanagment.UserManagement
   * @param username for new username.
   */
  public void setUsername(String username) {
    this.username = username;
    isLoggedIn = true;
  }

  /**
   * Changes password. To do: Not yet connect to user management.
   *
   * @param pwd new password.
   */
  public void setPassword(String pwd) {
    password = pwd;
  }

  /**
   * Getter method for username.
   *
   * @return String username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Getter method for password.
   *
   * @return String password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Getter method for computerLoginName.
   *
   * @return String computerLoginName.
   */
  public String getComputerLoginName() {
    return computerLoginName;
  }

  /**
   * Getter method for computername.
   *
   * @return String computername.
   */
  public String getComputername() {
    return this.computername;
  }

  /**
   * Getter method for IP.
   *
   * @return String IP.
   */
  public String getIP() {
    return this.IP;
  }

  /**
   * Getter method for ID.
   *
   * @return user specific (unique) userID.
   */
  public int getID() {
    return id;
  }

  /**
   * Getter method for highscore.
   *
   * @return actual highscore of an user.
   */
  public int getHighscore() {
    return highscore;
  }

  /**
   * Setter method for id.
   *
   * @param id for user id.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Setter method for highscore.
   *
   * @param highscore userscore.
   */
  public void setHighscore(int highscore) {
    this.highscore = highscore;
  }

  /**
   * Getter method for the color of the pieces.
   *
   * @return color of the pieces.
   */
  public int getColor() {
    return color;
  }

  /**
   * Setter method for color.
   *
   * @param color of a piece.
   */
  public void setColor(int color) {
    this.color = color;
  }

  /**
   * Getter method of the pieces.
   *
   * @return pieces.
   */
  public Pieces getPieces() {
    return pieces;
  }

  /**
   * Setter method for pieces.
   *
   * @param pieces from type Pieces.
   * @see Pieces for all values.
   */
  public void setPieces(Pieces pieces) {
    this.pieces = pieces;
  }

  /**
   * Getter method for set of pieces.
   *
   * @return Hashmap of type integer with the number of pieces stored.
   */
  public HashMap<Integer, Piece> getPieceHM() {
    return pieceHM;
  }

  /**
   * Setter method for set of pieces.
   *
   * @param pieceHM as a set of pieces.
   */
  public void setPieceHM(HashMap<Integer, Piece> pieceHM) {
    this.pieceHM = pieceHM;
  }

  /**
   * Getter method for number of matches.
   *
   * @return an int for the number of matches already played.
   */
  public int getMatchID() {
    return matchID;
  }

  /**
   * Setter method for number of matches.
   *
   * @param matchID already played.
   */
  public void setMatchID(int matchID) {
    this.matchID = matchID;
  }
  // REVIEW: Javadoc missing

  /**
   * Checks if it is the turn of a certain player, if yes he is allowed to move his piece, if not,
   * he is not allowed and has to wait.
   *
   * @return a boolean: true if it's the turn of the player, false if not.
   */
  public boolean isYourTurn() {
    return yourTurn;
  }

  /**
   * Setter method for YourTurn.
   *
   * @param yourTurn the boolean; true if it's the player's turn, false if not.
   */
  public void setYourTurn(boolean yourTurn) {
    this.yourTurn = yourTurn;
  }

  /**
   * Checks wether player has skipped or not.
   *
   * @return a boolean: true: player didn't skipp.
   */
  public boolean isNotSkipped() {
    return notSkipped;
  }

  /**
   * Setter method for NoSkip.
   * @param status boolean the private boolean notSkipped should be set to
   */
  public void setNotSkipped(boolean status) {
    this.notSkipped = status;
  }

  /**
   * method to determine whether the player is still online.
   *
   * @return false if username equals null, true if player still online
   */
  public boolean isOnline() {
    return isLoggedIn;
  }

  /** User is set offline. */
  public void setOffline() {
    // username = "Client: " + computername;
    isLoggedIn = false;
  }

  /**
   * method that returns whether the player should play, wait or is already skipped.
   *
   * @return player status in form of a String
   */
  public String getStatus() {
    if (isYourTurn()) {
      return "turn";
    } else if (isNotSkipped()) {
      return "wait";
    } else {
      return "skipped";
    }
  }
}
