/**
 * edgy.server is a package for the network communication.
 */

package edgy.server;

import edgy.mechanics.Match;
import edgy.mechanics.Player;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * Implements the access to the chat with all his functions and messages possibilities.
 *
 * @author While(true){ do nothing }.
 * @version 1.0
 */
public class Broadcast {
  private ArrayList<ClientThread> threads;
  private ClientThread ownClient;
  /**
   *  Initialisation of rolling Server Logger.
   */
  private static Logger log = LogManager.getLogger(Broadcast.class.getName());


  /**
   * Constructor with the chat attributes.
   * @param threads all ClientThreads on server.
   * @param ownClient specifies the own ClientThread of user.
   */
  public Broadcast(ArrayList<ClientThread> threads, ClientThread ownClient) {
    this.threads = threads;
    this.ownClient = ownClient;
  }

  /**
   * Sends message to all clients except own.
   *
   * @param msg the message which is sent.
   * @param command sent to all players except own.
   */

  public void nonreciprocalBroadcast(String command, String msg) {
    synchronized (this) {
      for (ClientThread client : threads) {
        if (client != ownClient) {
          client.sendToClient(command + "&" + msg);
        }
      }
      log.info("Message: '" + msg + "' sent from " + ownClient.player.getUsername()
             + " to everyone (excluding itself.");
    }
  }

  /**
   * Sends message to only a specific client.
   *
   * @param msg the message which is sent.
   * @param command sent specific user.
   * @param username specific recipient.
   */
  public void privateBroadcast(String username, String command, String msg) {
    synchronized (this) {
      for (ClientThread client : threads) {
        if (username.equals(client.player.getUsername())) {
          log.info("Message: '" + command + "&" + msg + "' sent from "
                  + ownClient.player.getUsername() + " to " + username);
          client.sendToClient(command + "&" + msg);
          break;
        }
      }
    }
  }

  /**
   * Sends message to all clients including the own.
   *
   * @param msg message which is sent.
   * @param command sent to all players on server.
   */
  public void publicBroadcast(String command, String msg) {
    synchronized (this) {
      log.info("Message: '" + msg + "' sent from " + ownClient.player.getUsername()
              + " to everyone");
      for (ClientThread client:threads) {
        client.sendToClient(command + "&" + msg);
      }
    }
  }

  /**
   * Sends message to all clients in a specific lobby.
   *
   * @param msg message which is sent.
   * @param matchID defines unique lobby.
   * @param command sent to all players in a Lobby.
   */
  public void lobbyBroadcast(int matchID, String command, String msg) {
    /* player is not in a match */
    if (matchID == -1) {
      return;
    }

    synchronized (this) {
      /* Extracts all players of a lobby this client is a member of */
      Match lobby = ownClient.lobby.lobbys.get(matchID);
      log.info("Message: '" + msg + "' sent from " + ownClient.player.getUsername()
              + " to Match " + matchID);
      for (Player p : lobby.getPlayers().values()) {
        p.ct.sendToClient(command + "&" + msg);
      }
    }
  }

  /**
   * Sends message to all clients in a specific lobby except own.
   *
   * @param msg message which is sent.
   * @param matchID defines unique lobby.
   * @param command specifies command which will be sent to everyone in lobby except own client.
   */
  public void lobbyNonreciprocalBroadcast(int matchID, String command, String msg) {
    synchronized (this) {
      /* Extracts all players of a lobby this client is a member of */
      Match lobby = ownClient.lobby.lobbys.get(matchID);
      log.info("Message: '" + msg + "' sent from " + ownClient.player.getUsername()
              + " Match " + matchID + "(nonreciprocal)");
      for (Player p : lobby.getPlayers().values()) {
        if (!p.getUsername().equals(ownClient.player.getUsername())) {
          p.ct.sendToClient(command + "&" + msg);
        }
      }
    }
  }
}