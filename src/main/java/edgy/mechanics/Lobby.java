/** edgy.mechanics is a package for the game logic. */

package edgy.mechanics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * the class Lobby holds a HashMap with all matches and enables Clients to create new matches and
 * join existing ones.
 *
 * @author while(true){do nothing}.
 * @version 1.0
 */
public class Lobby {

  /** HashMap that can be accessed from all other classes holding the lobby instances. */

  /* Hashmap to organize all lobbys: key: LobbyID value Match */
  public HashMap<Integer, Match> lobbys = new HashMap<Integer, Match>();

  // Initiate rolling Server Logger
  private static Logger log = LogManager.getLogger(Lobby.class.getName());

  /**
   * Creates a new match.
   *
   * @param player from class Player filled with all credentials for a player.
   */
  public void createNewMatch(Player player) {
    /* Creating a new matchID and adds new match into Lobby list */
    int matchID = lobbys.size() + 1;
    lobbys.put(matchID, new Match(player.getColor(), player));

    /* Assigns player to match */
    player.setMatchID(matchID);

    /* informs client about success of login */
    player.ct.sendToClient("print&You successfully created the new match " + matchID + ".");
  }

  /**
   * Player can join a match.
   *
   * @param player from Player class, includes necessary credentials for player.
   * @param matchID number of the current match.
   * @return message.
   */
  public boolean joinMatch(int matchID, Player player) {

    /* This game does not exist */
    if (!lobbys.containsKey(matchID)) {
      player.ct.sendToClient("error&The following Match doesn't exist: " + matchID);
      log.error("Client tried to join to a non-existing match. MatchID " + matchID);
      return false;
    }

    /* Player gets assigned to matchID and is added to Match */
    player.setMatchID(matchID);
    lobbys.get(matchID).addPlayer(lobbys.get(matchID).getPlayerAmount() + 1, player);

    if (lobbys.get(matchID).isFull()) {
      lobbys.get(matchID).getPlayers().get(1).setYourTurn(true);
      welcomeMessage(matchID);
    }
    player.ct.printToClient("You successfully joined match " + matchID);
    return true;
  }

  /**
   * Prints the welcome message for the user.
   *
   * @param matchNumber the current match where the user is welcomed.
   */
  private void welcomeMessage(int matchNumber) {
    for (Integer key : lobbys.get(matchNumber).getPlayers().keySet()) {
      lobbys
          .get(matchNumber)
          .getPlayers()
          .get(key)
          .ct
          .printToClient("The match " + "is complete- the match starts!");
    }
    lobbys.get(matchNumber).getPlayers().get(1).ct.printToClient("Your turn!");
  }

  /**
   * Moves the chosen piece from the player.
   *
   * @param player the actual player in a lobby.
   * @param pieceID the unique Id of the piece which has to be moved.
   * @param x the row position of the piece on the board.
   * @param y the column position of the piece on the board.
   */
  public void move(Player player, String pieceID, String x, String y) {
    int matchNumber = player.getMatchID();
    lobbys
        .get(matchNumber)
        .movePiece(player, Integer.parseInt(pieceID), Integer.parseInt(x), Integer.parseInt(y));
  }

  /**
   * method to determine the next player and set the respective yourTurn true in the method called.
   *
   * @param player current player who already moved
   */
  public void nextTurn(Player player) {
    int mnr;
    try {
      mnr = player.getMatchID();
      log.info("mnr= " + mnr);
      lobbys.get(mnr).setNextYourTurn(player);
      log.info("player.isYourTurn= " + player.isYourTurn());
    } catch (NullPointerException e) {
      log.warn("nextTurn() could not be executed.");
    }
  }

  /**
   * method that returns a String containing all pieces that are left by calling printPieces.
   *
   * @param player whose pieces should be printed
   * @return the printed pieces.
   */
  public String printRemainingPieces(Player player) {
    return player.printPiecesOfPlayer(player.getColor());
  }

  /**
   * method to calculate the current score.
   *
   * @return String with exact number of points
   */

  /**
   * Prints a list of all matches and the number of players within.
   *
   * @return a String with all matches and their numbers.
   */
  public String listGames() {
    if (lobbys.isEmpty()) {
      return "No game has been created yet.";
    }
    String list = "The following games are currently active: \n";
    for (Integer key : lobbys.keySet()) {
      String playerPlural = "player";
      if (lobbys.get(key).getPlayerAmount() > 1) {
        playerPlural = "players";
      }
      list +=
          "Match "
              + key
              + " with "
              + lobbys.get(key).getPlayerAmount()
              + " "
              + playerPlural
              + " at the moment.\n";
    }
    return list;
  }

  /**
   * Prints all options while logged in match.
   *
   * @return String of all options during a match.
   */
  public String helpMatch() {
    String matchOptions =
        "The following list contains all commands " + "you can enter specifically for the match.\n";
    matchOptions += "turnl: turn one of your pieces counterclockwise, please add number.\n";
    matchOptions += "turnr: turn one of your pieces clockwise, please add number.\n";
    matchOptions += "flipp: flip one of your pieces, please add number.\n";
    // list (player), quit, move, skip, points
    return matchOptions;
  }

  /**
   * Returns a lists of all Players currently in the match as a String.
   *
   * @param matchNr numbers of all matches.
   * @return all the members.
   */
  public String listMatchMembers(int matchNr) {
    String members = "";
    HashMap<Integer, Player> hmListedPlayers = lobbys.get(matchNr).getPlayers();
    for (Integer playerNr : hmListedPlayers.keySet()) {
      members += "Player " + playerNr + " = " + hmListedPlayers.get(playerNr).getUsername() + "\n";
    }
    return members;
  }

  /**
   * Returns a String of all currently running Matches.
   *
   * @return all the members in the form: number of players:status status: "1" waiting status: "2"
   *     playing status: "3" finished
   */
  public String getLobbies() {
    /* Network command + Number of current open lobbies */
    String output = "";

    /* If no lobbies have been created */
    if (lobbys == null || lobbys.size() == 0) {
      return output + "0:1;";
    }

    /* extract amount of players per lobby and the current ongoing match status */
    for (Match match : lobbys.values()) {
      output += match.getPlayerAmount() + ":" + match.getMatchStatus() + ";";
    }
    return output;
  }
}
