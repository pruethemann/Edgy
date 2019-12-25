/**
 * edgy.start is a package for starting the server and the client.
 */

package edgy.start;

import edgy.client.Client;
import edgy.server.Server;

/**
 * The class Start starts a server.
 * @author While(true){Do nothing}.
 * @version 1.0
 */

public class Start {

  /**
   * main method is executed.
   *
   * @param args argument that is send when calling to differ between server call and general call
   */

  public static void main(String[] args) {
    /* make sure the jar has been executed with arguments */
    if (args.length == 0) {
      printHelp();
    }

    /* starting the server */
    if (args[0].equals("server")) {
      Server.main(extractArguments(args));

      /* start the client
       argument 1: IP:Port
       argument 2: username (optional)
       argument 3: passwort (optional)
       */
    } else if (args[0].equals("client")) {
      if (args.length == 2 || args.length == 4) {
        Client.main(extractArguments(args));
      } else if (args.length == 3) {
        System.out.println("Please provide a password to your username!");
        printHelp();
      } else {
        printHelp();
      }

      /* Command can't be executed */
    } else {
      System.out.println("'" + args[0] + "' is not recognized as an internal or external command.");
      printHelp();
    }
  }

  /**
   * Inform user how to use the jar commands with arguments.
   */
  private static void printHelp() {
    System.out.println(
        "Please enter one of the following commands:"
            + "\n     java -jar *.jar client [IP:Port[username password]]"
            + "\n     java -jar *.jar server [port]");
    System.exit(-1);
  }

  /**
   * Extract the arguments of the jar execution and direct it to the server or client main class.
   * @param args define the jar execution arguments.
   * @return the extracted arguments for client or server.
   */
  private static String[] extractArguments(String[] args) {
    String[] arguments = new String[args.length - 1];
    for (int i = 1; i < args.length; i++) {
      arguments[i - 1] = args[i];
    }
    return arguments;
  }
}