/**
 * edgy.usermanagement is a package for the implementation of the sqlite-database.
 */

package edgy.usermanagment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;

/**
 * The SQLServer implements the proper Sql-functions.
 * @author While(true){Do nothing}.
 * @version 1.0
 */
public class SQLServer {

  /* Variables to connect to SQL database: connection / statment */
  private static Statement statement = null;
  private static Connection conn = null;
  /** Initialisation of the rolling server logger. */
  private static Logger log = LogManager.getLogger(SQLServer.class.getName());

  public HashMap<Integer, Metrics> highscore;

  public SQLServer() {
    connectSql();
    getHighscoreSql();
  }

  /** The basic method for the connection itself with the SQL-database. */
  public static void connectSql() {
    // connection to userdata.db file
    String connection = "jdbc:sqlite:userdata.db";
    try {
      conn = DriverManager.getConnection(connection);
      statement = conn.createStatement();
      System.out.println("You're successfully connected");

    } catch (SQLException e) {
      log.fatal("Server couldn't establish connection to SQL database " + e.getMessage());
    }
  }

  /**
   * Method for inserting a new user into the table.
   *
   * @param username with the name of the new user.
   * @param password chosen from user.
   * @return confirms whether User insertion into SQL was successfull
   */
  public boolean insertUserSql(String username, String password) {
    getHighscoreSql();

    PreparedStatement ps = null;

    try {
      /* determine the largest current userID */
      int userID = findLargestUserID() + 1;

      /* Insert username, userID and 0 score into SQL database */
      String sqlCommand =
          "INSERT INTO users(userId, username, password, score, online) VALUES (?, ?, ?, ?, ?)";
      ps = conn.prepareStatement(sqlCommand);
      ps.setInt(1, userID);
      ps.setString(2, username);
      ps.setString(3, password);
      ps.setInt(4, 0);
      ps.setInt(5, 0);
      ps.executeUpdate();

      ps.close();
      log.info(username + " has successfully been saved in our database");
      return true;

    } catch (SQLIntegrityConstraintViolationException e) {
      log.warn("Method/username/password + insertUser " + username + password);
      return false;
    } catch (SQLException e) {
      log.fatal(
          "SQL overload. Unsynchronized access to the SQL database from "
              + username
              + " at Highscore :"
              + e);
      return false;
      /* to make sure the PrpearedStatement will be closed in any case */
    } finally {
      try {
        ps.close();
      } catch (NullPointerException e) {
        // Just do nothing
      } catch (SQLException e) {
        // Just do nothing
      }
    }
  }

  /**
   * A username can be changed.
   *
   * @param newUsername is the new username.
   * @param userID the user's ID in form of an int
   * @return boolean if username successful changed.
   */
  public boolean changeUsernameSql(String newUsername, int userID) {
    PreparedStatement ps = null;

    try {
      /* changes username of a specific userID in SQL database */
      String sqlCommand = "UPDATE users SET username = ? WHERE userId = ?";
      ps = conn.prepareStatement(sqlCommand);
      ps.setString(1, newUsername);
      ps.setInt(2, userID);
      ps.executeUpdate();
      ps.close();

      return true;
      /* In case the prefered username is already taken, the operation is aborted */
    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
      log.warn("Username is not unique. (Change username)" + e);
      return false;
    } catch (SQLException e) {
      log.fatal(
          "SQL overload. Unsynchronized access to the SQL database from "
              + newUsername
              + " at Highscore :"
              + e);
      return false;

      /* to make sure the PrpearedStatement will be closed in any case */
    } finally {
      try {
        ps.close();
      } catch (NullPointerException e) {
        // Just do nothing
      } catch (SQLException e) {
        // Just do nothing
      }
    }
  }

  /**
   * User can change his password.
   *
   * @param newPassword the new password he chose.
   * @param userID the current userID which is performing the action (with the actual password.)
   * @return boolean: true if the password has been successfully changed, false if not.
   */
  public synchronized boolean changePasswordSql(String newPassword, int userID) {
    PreparedStatement ps = null;

    try {
      /* changes password of a specific userID in SQL database */
      String sqlCommand = "UPDATE users SET password = ? WHERE userId = ?";
      ps = conn.prepareStatement(sqlCommand);
      ps.setString(1, newPassword);
      ps.setInt(2, userID);
      ps.executeUpdate();
      ps.close();
      return true;

      /* In case connection to DB failed */
    } catch (SQLException e) {
      log.warn("Password couldn't be changed" + e);
      return false;

      /* to make sure the PrpearedStatement will be closed in any case */
    } finally {
      try {
        ps.close();
      } catch (NullPointerException e) {
        // Just do nothing
      } catch (SQLException e) {
        // Just do nothing
      }
    }
  }

  /**
   * Deletes an existing user from the database table. Not implemented yet.
   * @param username which will be removed.
   * @throws SQLException if the user doesn't exist.
   * @deprecated will be added later.
   */
  public void removeUserSql(String username) throws SQLException {
    try {
      statement.execute("DELETE FROM users WHERE " + username);
    } catch (SQLException e) {
      log.warn("This user doesn't exist. Error: " + e);
    }
    statement.close();
  }

  /**
   * method that lists all scores from the user database in descending order.
   * @return String containing all clients with scores in descending order separated by semicolons
   */
  public synchronized String getHighscoreSql() {
    String highscoreStr = "";
    highscore = new HashMap<Integer, Metrics>();
    ResultSet results = null;

    try {
      /* Selects all users in database in decreasing score order */
      statement.execute("SELECT * from users ORDER BY " + "score" + " " + "DESC ");
      results = statement.getResultSet();

      int rank = 1;
      /* Extracts username, userID and score and checks for current online state */
      while (results.next()) {
        Metrics m = new Metrics();
        m.userID = results.getInt("userID");
        m.username = results.getString("username");
        m.score = results.getInt("score");
        m.isOnline = results.getInt("online");
        m.password = results.getString("password");
        m.rank = rank;
        highscore.put(m.userID, m);
        highscoreStr +=
            m.userID + ":" + m.username + ":" + m.score + ":" + m.isOnline + ":" + m.rank + ";";
        rank++;
      }
      results.close();
      statement.close();
      return highscoreStr;

    } catch (SQLException e) {
      log.fatal("SQL overload. Unsynchronized access to the SQL database from Highscore :" + e);
      e.printStackTrace();
      return "ifyoureadthis&1000:text:1000:0:1;1001:Peter:900:1:2;";
    } finally {
      try {
        results.close();
      } catch (Exception e) {
        log.fatal("Highscore method: " + e);
      }
    }
  }

  /**
   * Updates the online status of every user in the database.
   *
   * @param userID is the unique userID of every player.
   * @param isOnline defines whether a player is online.
   * @return informs about the success of the operation.
   */
  public synchronized boolean updateOnlineStatusSql(int userID, boolean isOnline) {
    int status = 0;
    if (isOnline) {
      status = 1;
    }

    /* Updates status in database at specific userID */
    PreparedStatement ps = null;

    try {
      String sqlCommand = "UPDATE users SET online = ? WHERE userId = ?";
      ps = conn.prepareStatement(sqlCommand);
      ps.setInt(1, status);
      ps.setInt(2, userID);
      ps.executeUpdate();
      ps.close();
      return true;
    } catch (SQLException e) {
      log.fatal(
          "SQL overload. Unsynchronized access to the SQL database from "
              + userID
              + " at Online Status :"
              + e);
      return false;
      /* to make sure the PrpearedStatement will be closed in any case */
    } finally {
      try {
        ps.close();
      } catch (NullPointerException e) {
        // Just do nothing
      } catch (SQLException e) {
        // Just do nothing
      }
    }
  }

  /**
   * Updates the score of a user after a game.
   *
   * @param username of player who updates score.
   * @param score added to total score.
   * @return the total score from the user after the last game.
   */
  public synchronized boolean updateScoreSql(String username, int score) {
    PreparedStatement ps = null;

    try {
      String sqlCommand = "SELECT score FROM users WHERE username = ?";
      ps = conn.prepareStatement(sqlCommand);
      ps.setString(1, username);
      ResultSet result = ps.executeQuery();

      while (result.next()) {
        int currentScore = result.getInt("score");
        int total = currentScore + score;
        statement.execute(
            "UPDATE users SET score =" + total + " WHERE username = '" + username + "'");
      }
      /* close all SQL connections */
      statement.close();
      ps.close();
      result.close();

      return true;

    } catch (SQLException e) {
      log.fatal(
          "SQL overload. Unsynchronized access to the SQL database from "
              + username
              + " at updateScoreSql :"
              + e);
      return false;
      /* to make sure the PrpearedStatement will be closed in any case */
    } finally {
      try {
        ps.close();
      } catch (NullPointerException e) {
        // Just do nothing
      } catch (SQLException e) {
        // Just do nothing
      }
    }
  }

  /** Closes the connection with the database at the end of use. */
  public void closeSql() {
    try {
      statement.close();
      conn.close();
    } catch (SQLException e) {
      log.warn("Database couldn't be closed! Error: " + e);
    }
  }

  /**
   * Resets online stats in database when a new server starts. Necessary if a server session
   * previously crashed.
   */
  public void resetStatesSql() {
    PreparedStatement ps = null;

    try {
      String sqlCommand = "UPDATE users SET online = 0 WHERE userId > 0";
      ps = conn.prepareStatement(sqlCommand);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      log.warn("Online states couldn't be reseted." + e);

      /* to make sure the PrpearedStatement will be closed in any case */
    } finally {
      try {
        ps.close();
      } catch (NullPointerException e) {
        // Just do nothing
      } catch (SQLException e) {
        // Just do nothing
      }
    }
  }

  /**
   * Determines the largest userID in database.
   *
   * @return largest userID.
   */
  private int findLargestUserID() {
    int largestUserID = 0;
    for (int userID : highscore.keySet()) {
      if (userID > largestUserID) {
        largestUserID = userID;
      }
    }
    return largestUserID;
  }
}
