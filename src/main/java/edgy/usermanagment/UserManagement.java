/** edgy.usermanagement is a package for the implementation of the sqlite-database. */

package edgy.usermanagment;

import edgy.mechanics.Player;
import edgy.server.ClientThread;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * handles the SQL-database for saving all the userdata after each game.
 *
 * @author while(true){do nothing}.
 * @version 1.0.
 */
public class UserManagement {

  public static SQLServer sql;

  /**
   * Initialisation of the Hashmap with the key beeing the user-id as an integer and the values
   * username and score. The username is of type String and the score of type integer. The values
   * are stored in an Array-List.
   */
  private ClientThread ct;

  /** Initialisation of the rolling server logger. */
  private static Logger log = LogManager.getLogger(UserManagement.class.getName());

  /**
   * Constructor for the ClientThread.
   *
   * @param ct gives access to client specific thread an all its attributes.
   */
  public UserManagement(ClientThread ct) {
    this.ct = ct;
    if (sql == null) {
      sql = new SQLServer();
    }
  }

  /**
   * method that allows the player to change the current username.
   *
   * @param newUsername String containing the new username
   * @param p Player who wants to change the username
   */
  public void changeUsername(String newUsername, Player p) {
    if (sql.changeUsernameSql(newUsername, p.getID())) {
      ct.sendToClient(
          "cuser&OK&You have been successfully be renamed to " + newUsername + "&" + newUsername);
      /* Changes username in current session of game */
      p.setUsername(newUsername);
    } else {
      ct.sendToClient(
          "cuser&FAILUNIQUE&Your prefered username is already taken. Please choose another one.& ");
    }
  }

  /**
   * Creates a new user with unique username. Gives some suggestions based on the computer and
   * computer login name.
   *
   * @param player gives access to all player specific attributes.
   * @param username new name which will be saved in DB.
   * @param password chosen by new user.
   */
  public void createUser(String username, String password, Player player) {
    /* Try to insert new name into Database. In case name is unique the user will be logged in */
    if (sql.insertUserSql(username, password)) {
      ct.sendToClient("nuser&OK&You successfully created a new user account on Edgy&" + username);
      /* directly login user */
      login(username, password, player);
    } else {

      String msg =
          "The username '" + username + "' already exists. Please create another name or login.";
      ct.sendToClient("nuser&NOTUNIQUE&" + msg + "&" + username);
      log.warn(
          "The client "
              + player.getComputername()
              + " tried to create the existing username :"
              + username);
    }
  }

  /**
   * User can change his password.
   *
   * @param newPassword the new password he chose.
   * @param p the current user which is performing the action (with the actual password.)
   */
  public void changePassword(String newPassword, Player p) {
    /* test for minimal requirements of passwort */
    if (newPassword.length() < 4) {
      ct.sendToClient("chpwd&FAILREQUIREMENT&The password should be at least 4 characters long.");
      return;
    }
    int userID = getUserID(p.getUsername());
    if (sql.changePasswordSql(newPassword, userID)) {
      ct.sendToClient("chpwd&OK&You have successfully changed your password.");
    } else {
      ct.sendToClient("chpwd&FAILSQL&The password couldn't be saved. Please try again!");
    }
  }

  public String getHighscore() {
    return sql.getHighscoreSql();
  }

  /** Print HIGH score list. */
  public void printHighscore() {
    ct.printToClient(sql.getHighscoreSql());
  }

  public void resetStates() {
    sql.resetStatesSql();
  }

  /**
   * Returns all the user specific metrics.
   *
   * @param username is the unique username of a player
   * @return Metrics define the user specific attributes like score, userID and online status.
   */
  public int getRank(String username) {
    sql.getHighscoreSql();

    try {
      int userID = getUserID(username);
      int rank = sql.highscore.get(userID).rank;
      return rank;
      /* in case SQL can't be accessed */
    } catch (NullPointerException e) {
      return 0;
    }
  }

  /**
   * Getter method for the userId.
   *
   * @param username the username of the wanted player.
   * @return an int which is the unique userId. Or 0, if no Id was found.
   */
  public int getUserID(String username) {
    sql.getHighscoreSql();
    for (Metrics m : sql.highscore.values()) {
      if (m.username.equals(username)) {
        return m.userID;
      }
    }
    return 0;
  }

  /**
   * Logs in user and check whether a client is not already using this username.
   *
   * @param player gives access to player specific attributes.
   * @param checkPassword password which should be equal to password saved in DB.
   * @param username of player who is logging in.
   * @return boolean information about login success.
   */
  public boolean login(String username, String checkPassword, Player player) {
    /* check whether requested username exists in database */
    sql.getHighscoreSql();
    int userID = getUserID(username);
    Metrics m = sql.highscore.get(userID);

    /* In case username is not in the database */
    if (m == null) {
      String msg =
          "The username '" + username + "' doesn't exist in our database. Please try again.";
      log.warn("The user with IP " + player.getIP() + " tried login with a non existing username.");
      ct.sendToClient("login&WRONGUSR&" + msg + "&X&0");
      return false;
    }

    /* Check whether another client already connected with the specific username */
    if (m.isOnline == 1) {
      String msg =
          "The player "
              + username
              + " already connected to the server."
              + " Please logout out from this specific client.";
      ct.sendToClient("login&ONLINE&" + msg + "&X&0");
      log.warn(
          "The player "
              + username
              + " already connected to the server. "
              + "Please logout out from this specific client.");
      return false;
    }

    if (!checkPassword(m, checkPassword)) {
      String msg = "The password is wrong. Please try again!";
      ct.sendToClient("login&WRONGPWD&" + msg + "&X&0");
      return false;
    }

    /* Set all the attributes for the current session */
    player.setHighscore(m.score);
    player.setUsername(m.username);
    player.setId(m.userID);

    /* Updates the online status in the database */
    sql.updateOnlineStatusSql(m.userID, true);
    String msg = "The player " + username + " successfully connected to the server.";
    ct.sendToClient("login&OK&" + msg + "&" + m.username + "&" + m.score);
    return true;
  }

  /**
   * Checks the password of a user by comparing the input with the data from the database.
   *
   * @param m the data from the database.
   * @param checkPassword the password from the input.
   * @return boolean: true if the input equals the password stored in the database, false if not.
   */
  private boolean checkPassword(Metrics m, String checkPassword) {
    return checkPassword.equals(m.password);
  }

  /**
   * Logs out current user.
   *
   * @param player gives access to player specific attributes.
   */
  public void logout(Player player) {
    if (!player.isOnline()) {
      ct.printToClient("You are not logged in. Please login in or create a new user.");
      return;
    }

    sql.updateOnlineStatusSql(player.getID(), false);

    /* resets username to computername */
    ct.player.setOffline();

    log.info("User " + player.getUsername() + " successfully logged out.");
    /* Inform Client that he logged out */
    ct.printToClient("You " + player.getUsername() + " successfully logged out from server.");
  }

  /**
   * List of all the users who are online.
   *
   * @param threads allows access to any other client threads and their streams.
   */
  public void list(ArrayList<ClientThread> threads) {
    String list = "The following " + threads.size() + " users are online:\n\n";

    /* list all players on server */
    synchronized (this) {
      for (ClientThread client : threads) {
        if (!client.player.getUsername().equals("")) {
          list +=
              "    "
                  + client.player.getUsername()
                  + "  "
                  + client.player.getIP()
                  + "  Score: "
                  + client.player.getHighscore()
                  + "\n";
        }
      }
      ct.printToClient(list);
    }
  }

  /**
   * method that updates the score form a particular user.
   * @param username name of the user who's score should be updated
   * @param score number that should be added to the previous score in the user database
   */
  public void updateScore(String username, int score) {
    if (sql.updateScoreSql(username, score)) {
      log.info(username + " has now +" + score + " points.");
    } else {
      log.warn("Saving of score failed. Username/score " + username + "/" + score);
    }
  }
}
