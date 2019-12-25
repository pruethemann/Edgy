/**
 * edgy.client is the package for the command parsing.
 */

package edgy.client;

import java.util.HashMap;

/**
 * Alias converts user readable commands into standardized
 * netowrk commands with 5 characters length.
 *
 * @author While(true){do nothing}.
 * @version 1.0
 */

public class Alias {

  /**
   * Getter method for all the chat and server functions
   * to change them into 5-lettered abreviations.
   * @return a String Hashmap filled with the values.
   */
  static HashMap<String, String> getAlias() {
    HashMap<String, String> alias = new HashMap<String, String>();

    /* information specific commands */
    alias.put("help", "help_");
    alias.put("options", "maopt");

    /* Match specific commands */
    alias.put("matchlist", "lstgm");
    alias.put("newmatch", "nwmat");
    alias.put("nwmat", "nwmat");
    //alias.put("joinmatch", "joinm");
    //alias.put("joinm", "joinm");

    /* Deactivated for console */
    /*
    alias.put("skip", "skipp");
    alias.put("move", "moved");
    alias.put("moved", "moved");
    alias.put("flip", "flipp");
    alias.put("flipp", "flipp");
    alias.put("turnright", "turnr");
    alias.put("turnr", "turnr");
    alias.put("turnleft", "turnl");
    alias.put("turnl", "turnl");
    alias.put("board", "board");
    */

    /* Communication specific commands */
    alias.put("echo", "echo_");

    /* Deactivated for console */
    /*
    alias.put("nonmg", "nonmg");
    alias.put("nonreciprocal", "nonmg");
    alias.put("pumsg", "pumsg");
    alias.put("publicMessage", "pumsg");
    alias.put("public", "pumsg");
    alias.put("private", "prmsg");
    alias.put("privatemessage", "prmsg");
    alias.put("prmsg", "prmsg");
    */

    /* User managment specific commands */
    alias.put("list", "list_");
    alias.put("newuser", "nuser");
    alias.put("nuser", "nuser");
    alias.put("changeuser", "cuser");
    alias.put("changeusername", "cuser");
    alias.put("cuser", "cuser");
    alias.put("changepassword", "chpwd");
    alias.put("chpwd", "chpwd");
    alias.put("chpassword", "chpwd");
    alias.put("highscore", "lsort");
    alias.put("logout", "lgout");
    alias.put("login", "login");
    alias.put("quit", "quit_");

    /* Start ping */
    alias.put("ping", "pingg");

    return alias;
  }
}