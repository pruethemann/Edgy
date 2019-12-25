/**
 * edgy.server is a package for the network communication.
 */

package edgy.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


/**
 * This class handles clients which are trying to connect to the game edgy. The server waits for
 * clients and creates a Socket which allows the flow of dataStream via Strings. Server handles
 * connection of clients to Server via Sockets. * This class implements server It performs some
 * operations based on their requests, and then possibly returns a result to the requester.
 * The port number of the socket connection can be defined as an argument.
 * In case of a missing portNumber,
 * the predetermined portNumber 2012 is taken instead.
 * @author while(true){do nothing}.
 * @version 1.0.
 */
public class Server {

  /**
   * Initialisation of the server socket.
   */
  private static ServerSocket serverSocket = null;
  /**
   * Initialisation of the client socket.
   */
  private static Socket clientSocket = null;
  /**
   * The server address.
   */
  private static InetAddress serverAddress;
  /**
   * The server port.
   */
  private static int portNumber;
  /**
   * Initialisation of a new Thread.
   */
  private static ArrayList<ClientThread> threads = new ArrayList<ClientThread>();

  /**
   * Initialisation of the rolling server logger.
   */
  private static Logger log = LogManager.getLogger(Server.class.getName());

  /**
   * Server waits for Client connections and handles various amounts of clients. Connections are
   * based on sockets.
   *
   * @param args program arguments
   */
  public static void main(String[] args) {
    portNumber = startServer(args);

    /* Start server socket. */
    try {
      serverSocket = new ServerSocket(portNumber);
      log.info("Server started on \n      Host IP: "
              + serverAddress.getHostAddress() + "\n      Port number: "
              + portNumber + "\n      Computer name: " + serverAddress.getHostName());
    } catch (BindException e) {
      log.fatal("Port number '" + portNumber + "' is already occupied. Please use another port"
              + "or close the running on the specific port!\nServer quits.");
      System.exit(-1);

    } catch (IOException e) {
      log.error(e);
    }

    /*
     * Server is constantly listening to clients trying to connect.
     */
    while (true) {
      try {
        clientSocket = serverSocket.accept();
        ClientThread newClient = new ClientThread(clientSocket, threads);
        newClient.start();
        threads.add(newClient);
      } catch (IOException e) {
        log.warn(e);
      }
    }
  }

  /**
   * Methods extracts Host IP in the current network and opens a defines a port number based on user
   * input (argument) or chooses a default port number '2012' if no information is given.
   *
   * @param args the port number to use a port
   * @throws NumberFormatException if required portNumber is not a number or is smaller than 1000 or
   *     larger than 4000.
   * @throws UnknownHostException if Server computer does not allow the access to the Host IP.
   */
  private static int startServer(String[] args) {
    /* Determine Server IP */
    try {
      serverAddress = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      log.fatal("Error: Server Host IP couldn't be determined!");
      System.exit(-1);
    }

    /* Use user defined port number or set pre-defined port */
    int portNumber = 2012;
    if (args.length >= 1) {
      try {
        portNumber = Integer.parseInt(args[0]);
        if (portNumber <= 1024 || portNumber > 8000) {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException e) {
        log.fatal(
            "'"
                + args[0]
                + "'"
                + " is not a valid argument. Please enter a port number between 1025 and 8000.");
        System.exit(-1);
      }
    }
    return portNumber;
  }
}