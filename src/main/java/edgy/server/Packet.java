/** edgy.server is a package for the network communication. */
package edgy.server;

import edgy.mechanics.Match;
import edgy.mechanics.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Parses the input from the Client.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */
public class Packet {

  /** new ClientThread. */
  private ClientThread ct;
  /** Initialisation of the Usermanagement and the connection with the database. */
  public edgy.usermanagment.UserManagement userManagement;

  /** Initialisation of the rolling server logger. */
  private static Logger log = LogManager.getLogger(Packet.class.getName());

  /**
   * Constructor for the ClientThread.
   *
   * @param ct Clientthread of specific player.
   */
  public Packet(ClientThread ct) {
    this.ct = ct;
    userManagement = new edgy.usermanagment.UserManagement(ct);
  }

  /**
   * Parses input Strings and initiates the required methods.
   *
   * @param command Network command requested from client
   * @param args All arguments if required for command.
   * @throws Exception if input can't be parsed.
   */
  public void parseClientInput(String command, String[] args) throws Exception {
    /* The command redirects to the specific methods */
    switch (command) {

      /* Creates a new user in database and performs login. Spaces in names get ignored. */
      case "nuser":
        if (pleaseLogout()) {
          break;
        }
        userManagement.createUser(args[0], args[1], ct.player);
        break;

        /* command for changing the username again manually, tested for uniqueness */
      case "cuser":
        if (pleaseLogin()) {
          break;
        }
        userManagement.changeUsername(args[0], ct.player);
        break;

        /* command for changing the username again manually, tested for uniqueness */
      case "chpwd":
        if (pleaseLogin()) {
          break;
        }
        userManagement.changePassword(args[0], ct.player);
        break;

        /* Sends same message back to Client and prints it on terminal */
      case "echo_":
        ct.sendToClient("print&" + args[0]);
        break;

        /* Sends public message and sends it to all Client including the one it came from. */
      case "pumsg":
        String message = ct.player.getUsername() + "&" + args[0];
        ct.broadcast.publicBroadcast("chat_", message);
        break;

        /* Sends nonreciprocal message and sends it to all Client including the one it came from. */
      case "nonmg":
        message = ct.player.getUsername() + "&" + args[0];
        ct.broadcast.nonreciprocalBroadcast("chat_", message);
        break;

        /* Sends private message to a specific client*/
      case "prmsg":
        String username = args[0];
        message = ct.player.getUsername() + "&" + args[1];
        ct.broadcast.privateBroadcast(username, "priva", message);
        break;

        /* Sends lobby message to everyone in lobby*/
      case "lomsg":
        message = ct.player.getUsername() + "&" + args[0];
        ct.broadcast.lobbyBroadcast(ct.player.getMatchID(), "chat_", message);
        break;

        /* command helps shows a menu of all possible commands that can be used by the client */
      case "help_":
        help();
        break;

        /* Lists all clients currently logged in to the server. */
      case "list_":
        userManagement.list(ct.threads);
        break;

        /* Disconnects client from server */
      case "quit_":
        ct.disconnect();
        break;

        /* Logs out client form server */
      case "lgout":
        if (pleaseLogin()) {
          break;
        }
        userManagement.logout(ct.player);
        break;

      /* Pulls Match specific board string and sends it to all players in the same Match */
      case "board":
        sendBoard();
        break;

        /*command that moves particular piece to given position */
      case "moved":
        try {
          ct.lobby.move(ct.player, args[0], args[1], args[2]);
        } catch (NullPointerException e) {
          ct.printToClient(
              "error&Please first enter a game so the piece " + "of your piece set can be moved.");
        }
        break;

        /* Player surrenders */
      case "skipp":
        skip();
        break;

        /* Player is forced to surrenders */
      case "force":
        ct.broadcast.privateBroadcast(args[0], "force", "I am sorry");
        break;

        /* Client gets activley disconnected */
      case "kill_":
        ct.broadcast.privateBroadcast(args[0], "force", "I am sorry");
        ct.broadcast.privateBroadcast(args[0], "kill_", "At least you tried");
        break;

        /* Client requests Rank information and points during a match */
      case "rankm":
        ct.sendToClient("rankm&" + getRanks());
        break;

        /* Logs in specific user */
      case "login":
        userManagement.login(args[0], args[1], ct.player);
        break;

        /* print all users who ever joined the server in the past. HIGHSCORE for console*/
      case "lsort":
        userManagement.printHighscore();
        break;

        // command to create a new match
      case "nwmat":
        if (pleaseLogin()) {
          break;
        }
        ct.lobby.createNewMatch(ct.player);
        break;

        // command to join an already existing match
      case "joinm":
        if (pleaseLogin()) {
          break;
        }
        int matchID = Integer.parseInt(args[0]);
        ct.lobby.joinMatch(matchID, ct.player);

        if (ct.lobby.lobbys.get(ct.player.getMatchID()).isFull()) {
          for (Player p : ct.lobby.lobbys.get(matchID).getPlayers().values()) {
            ct.broadcast.privateBroadcast(
                p.getUsername(), "start&" + matchID + "&" + p.getColor(), "Let's Go");
          }
          /* send turn information */
          String activePlayer = ct.lobby.lobbys.get(matchID).whosTurn();
          ct.broadcast.privateBroadcast(activePlayer, "turn_", "It's your turn");
        }
        break;

        // command to list all games currently on the server including the player amount
      case "lstgm":
        if (pleaseLogin()) {
          break;
        }
        ct.printToClient(ct.lobby.listGames());
        break;

        // lists high score
      case "highs":
        ct.sendToClient("highs&" + userManagement.getHighscore());
        break;

        // command to list all options the player has while being in a match
      case "maopt":
        ct.printToClient(ct.lobby.helpMatch());
        break;

        // command to turn the piece counterclockwise (to the left)
      case "turnl":
        int pieceID = Integer.parseInt(args[0]);
        ct.player.turnCounterClockwise(pieceID);
        ct.sendToClient("piece&turnleft&" + pieceID);
        break;

        // command to turn the piece clockwise (to the right)
      case "turnr":
        pieceID = Integer.parseInt(args[0]);
        ct.player.turnClockwise(pieceID);
        ct.sendToClient("piece&turnright&" + pieceID);
        break;

        // command to flip the piece upside- down
      case "flipp":
        pieceID = Integer.parseInt(args[0]);
        ct.player.flipPiece(pieceID);
        ct.sendToClient("piece&flip&" + pieceID);
        break;

        // command to print all remaining piece graphically
      case "prtpc":
        ct.lobby.printRemainingPieces(ct.player);
        break;

        // command to refresh lobbies on Client
      case "lobby":
        if (pleaseLogin()) {
          break;
        }
        /* send this information to everyone */
        ct.broadcast.publicBroadcast("lslob", ct.lobby.getLobbies());
        break;

        /* actives ping from Client to Server */
      case "pingg":
        sendPong();
        break;

        /* predicts a possible new move */
      case "hint_":
        int color = ct.player.getColor();
        String hint = ct.lobby.lobbys.get(ct.player.getMatchID()).predictHint(color);
        ct.sendToClient(hint);
        break;

        /* pong arrives from Client */
      case "pongg":
        //log.info("ping returned from Client: " + args[0]);
        ct.receiver.setSave();
        break;

        /* Find out yourself */
      case "trolo":
        sendTroll();
        break;

        /*if the command cannot be parsed,
         *the client is informed that it wasn't possible execute command*/
      default:
        String errorMessage =
            "The request '"
                + command
                + "' from the client couldn't be parsed. ("
                + ct.player.getComputername()
                + "/"
                + ct.player.getIP()
                + ")";
        log.warn(errorMessage);
        ct.printToClient("'" + command + "' is not recognised as command!");
    }
  }

  /**
   * Skips a player if he want so. So he can't play anymore in the current game. Active client
   * surrenders.
   */
  public void skip() {
    try {
      ct.player.setNotSkipped(false);
      if (ct.player.isYourTurn()) {
        ct.player.setYourTurn(false);

        ct.lobby.lobbys.get(ct.player.getMatchID()).setNextYourTurn(ct.player);
      }
      ct.broadcast.lobbyBroadcast(
          ct.player.getMatchID(),
          "info_",
          "<SYSTEM>Player " + ct.player.getColor() + ":" + ct.player.getUsername() + " skipped.");

    } catch (NullPointerException e) {
      ct.printToClient("Please first enter a game.");
    }
    /* update the Match information for everyone in Lobby */
    ct.broadcast.lobbyBroadcast(getMatchID(), "rankm", getRanks());
  }

  /** The famous Easteregg.... Unknown suprise function. */
  private void sendTroll() {
    ct.broadcast.lobbyNonreciprocalBroadcast(getMatchID(), "trolo", "Have Fun!");
  }

  /** Board will be sent in case Client requests board or server needs to update board. */
  public void sendBoard() {
    String board = ct.lobby.lobbys.get(getMatchID()).getBoard();
    ct.broadcast.lobbyBroadcast(getMatchID(), "board", board);
  }

  public void sendRanks() {
    ct.broadcast.lobbyBroadcast(getMatchID(), "rankm", getRanks());
  }

  /**
   * Get MatchID of current Client.
   *
   * @return matchID
   */
  private int getMatchID() {
    return ct.player.getMatchID();
  }

  /** Sends the pong to the client. */
  private void sendPong() {
    //log.warn("pongg sent");
    ct.sendToClient("pongg");
  }

  /**
   * creates a string in form "rank:username1:points;rank:username2:points..."
   *
   * @return a String with the rank, the username and the points.
   */
  public String getRanks() {
    String ranks = "";
    Match m = ct.lobby.lobbys.get(getMatchID());

    try {
      for (int i = 0; i < 4; i++) {
        String username = m.getMatchPlayers().get(i);
        ranks +=
            userManagement.getRank(username)
                + ":"
                + username
                + ":"
                + m.board.countPoints(i + 1)
                + ":"
                + m.players.get(i + 1).getStatus()
                + ";";
      }

    } catch (NullPointerException e) {
        log.warn("A player sneaked out of the match. Just wait for it");
    }
    return ranks;
  }

  /**
   * If user is online and login, this method sends him a warning message to logout if he will
   * perform an action he can only do, if he is logged out.
   *
   * @return boolean: true if player is logged in, false if not.
   */
  boolean pleaseLogout() {
    if (ct.player.isOnline()) {
      ct.printToClient("Error: Please logout first before you perfom this action.");
      return true;
    }
    return false;
  }

  /**
   * Checks if user is logged out. Will send a warning message if he is, telling him to login if
   * user want to perform an action he can only do if he is logged in.
   *
   * @return boolean: true if he is logged in, false if not.
   */
  private boolean pleaseLogin() {
    if (!ct.player.isOnline()) {
      ct.printToClient("Error: Please login first before you perfom this action.");
      return true;
    }
    return false;
  }

  /**
   * Prints the whole networkprotocol which helps the user understanding the different possible
   * options.
   */
  private void help() {
    String helpInfo =
        "\nThe Edgy server offers you the following options: \n\n"
            + "   newuser <username password>: Creates a new user: 'nuser username psw'\n"
            + "   login <username pwd>: Connects existing user to server: \n"
            + "   newmatch: Creates a new Lobby \n"
            + "   logout: Logs out the current user.\n"
            + "   joinmatch <matchID>: Creates a new Lobby \n"
            + "   matchlist: lists all current active lobbies \n"
            + "   quit: Logs out and disconnects client from server.\n"
            + "   list: Lists all clients currently online.\n"
            + "   highscore: Lists Score of all player who ever played Edgy.\n"
            + "   changeusername <newUsername>: Changes username.\n";
    ct.printToClient(helpInfo);
  }

  /**
   * Updates the score in the user database.
   *
   * @param scores points from a player from the last game.
   */
  public void updateScore(HashMap<String, Integer> scores) {
    for (String username : scores.keySet()) {
      userManagement.updateScore(username, scores.get(username));
    }
  }
}
