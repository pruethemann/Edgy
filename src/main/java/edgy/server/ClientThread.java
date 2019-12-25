/**
 * edgy.server is a package for the network communication.
 */

package edgy.server;

import edgy.ping.ServerPingReceiver;
import edgy.ping.ServerPingSender;
import edgy.mechanics.Lobby;
import edgy.mechanics.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/** Every Client is assigned to a Thread which is handling the user requests. */
public class ClientThread extends Thread {
  public PrintStream os = null;
  private BufferedReader is = null;
  private DataInputStream dataInputStream = null;
  private DataOutputStream dataOutputStream = null;
  private Socket clientSocket = null;
  ArrayList<ClientThread> threads;
  public Player player = null;
  public static final Lobby lobby = new Lobby();
  public Packet packetCommunication;
  private boolean closed = false;
  public Broadcast broadcast;
  public ServerPingReceiver receiver;

  // Initiate rolling Server Logger
  static Logger log = LogManager.getLogger(ClientThread.class.getName());


  /** Constructor gets informed over all running Threads and sockets.
   * @param clientSocket allows the connection between client-server.
   * @param threads list for all the clients connected to the same server.
   */
  public ClientThread(Socket clientSocket, ArrayList<ClientThread> threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;

    packetCommunication = new Packet(this);

    /* reset online states in DB in case server crashed in the past */
    if (threads.size() == 0) {
      packetCommunication.userManagement.resetStates();
    }

    /* start Server ping Thread */
    /* start the Thread of ping Receiver */
    receiver = new ServerPingReceiver(this);
    receiver.start();

    /* start the Thread of PingSender */
    ServerPingSender sender = new ServerPingSender(this, receiver);
    sender.start();

    /* define sender in ping receiver */
    receiver.setSender(sender);
  }

  /**
   * Starts connection, the server itself and waits constantly for input.
   */
  public void run() {
    /* Create Input and output streams to and from Client */
    initStreams();

    /* Imports all client specific metrics */
    receiveClientAddress();

    /* Server assigns username based system specific computer login name */
    printToClient("\n   Welcome! It's a great day for playing!\n\n" + "Please tell me "
            + "what you want to do. If you don't have any idea what to do, press 'help'.");

    /* Constantly waits for user inputs unless user quits */
    synchronized (this) {
      while (!closed) {
        String input = "";
        try {
          input = receiveFromClient();

          /* Log every input except pings */
          if (!input.startsWith("pingg") && !input.startsWith("pongg")) {
            log.info(
                "Received from " + player.getComputername() + "/" + player.getIP() + ": " + input);
          }

          /*The incoming String[] query is split into its parts separated by blank spaces. */
          // The first 5 characters of the input specify the command
          String command = input.substring(0, 5);

          // input words separated by a space following the inital 5 characters are the arguments
          String[] args = null;
          if (input.contains("&")) {
            input = input.substring(6);
            args = input.split("&");
          }

          packetCommunication.parseClientInput(command, args);

        } catch (ArrayIndexOutOfBoundsException e) {
          log.fatal("IndexOutOfbounds Error. Input: " + input);

        } catch (NullPointerException e) {
          //log.fatal("Nullpointer from " + player.getUsername());

        } catch (Exception e) {
          log.fatal("Input couldn't be handled by server. Input " + input + " Error: " + e);
          e.printStackTrace();
        }
      }

      log.info("Client thread end");
    }
  }

  /** Fully disconnect the client from the server.
   *
   */
  public synchronized void disconnect()  {
    /* in case player is in a match: skip */
    if (player.getMatchID() != -1) {
      System.out.println(player.getMatchID());
      packetCommunication.skip();
    }

    String playerName = player.getUsername();

    /* Client logs out */
    packetCommunication.userManagement.logout(player);

    /* delete active thread */
    threads.remove(this);

    String disconnectMessage = playerName
            + " successfully disconnected. \n Bye Bye \n\n Press ENTER key to continue.";
    printToClient(disconnectMessage);

    /* inform Client to kill himself */
    sendToClient("kill_");

    /* close all connections */
    closeStreams();

    // Server Log
    log.info(playerName + " successfully disconnected from server.");
    closed = true;
  }

  /**
   * Fully disconnect the client from the server.
   */
  public void removeCrashedClient()  {
    /* Finish the While loop in run */
    closed = true;

    /* In case he is in match: Make sure others can continue playing */
    packetCommunication.skip();

    /* inform all players in same Match that one player left*/
    if (player.getMatchID() != -1) {
      broadcast.lobbyNonreciprocalBroadcast(player.getMatchID(), "error",
              "Unfortunately player " + player.getUsername() + " left us.");
      /* update the Match information for everyone in Lobby */
      broadcast.lobbyBroadcast(player.getMatchID(), "rankm", packetCommunication.getRanks());
    }

    /* delete active thread */
    System.out.println("Amount of threads: " + threads.size());
    threads.remove(this);
    System.out.println("Amount of threads: " + threads.size());

    /* close all connections */
    closeStreams();

    // Server Log
    log.fatal(player.getUsername() + " activley removed from server.");


  }

  /**
   * Create input and output streams for this client.
   *
   * @throws IOException if the connection fails.
   */
  private synchronized void initStreams() {
    try {
      /* Initiate Input Reader and Output stream to Server */
      os = new PrintStream(clientSocket.getOutputStream(),false, StandardCharsets.UTF_8);
      is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
              StandardCharsets.UTF_8));


      /* Initiate data output and data input stream to server */
      dataOutputStream = new DataOutputStream(os);

      InputStream inputStream = clientSocket.getInputStream();
      dataInputStream = new DataInputStream(inputStream);

      /* Initiate Broadcast system */
      broadcast = new Broadcast(threads, this);

    } catch (IOException e) {
      log.fatal("Stream connection failed. To do: implement proper error handling");
    }
  }

  /**
   * Closes all streams in case a client disconnects.
   *
   * @throws IOException if the connection fails.
   */
  private synchronized void closeStreams() {
    try {
      os.close();
      is.close();
      dataInputStream.close();
      dataInputStream.close();
      clientSocket.close();
    } catch (IOException e) {
      log.fatal("Stream connection failed. To do: implement proper error handling");
    }
  }

  /**
   * Imports computer name, IP and login name from client.
   *
   * @throws IOException if Server doesn't receive or can't properly read info sent by Client.
   */
  private synchronized void receiveClientAddress() {
    try {
      /* receives computer name, IP and login name from client */

      String clientAdress = dataInputStream.readUTF();

      String[] address = clientAdress.split(" ");

      String computerName = address[0];
      String ipAddress = address[1];

      String computerLoginname;

      try {
        computerLoginname = address[2];
      } catch (ArrayIndexOutOfBoundsException e) {
        printToClient("Login name couldn't be imported." + e);
        computerLoginname = computerName;
      }

      player = new Player(computerName, ipAddress, computerLoginname, this);

      /* Welcome new user on Server */
      log.info("The user " + computerLoginname + " from " + computerName + "/"
              + ipAddress + " joined the server.");
    } catch (IOException e) {
      log.fatal("Server couldn't receive Client information.");
    }
  }

  /**
   * sends information to the server.
   *
   * @return the information which was sent.
   */
  public synchronized String receiveFromClient() {
    String input = null;
    try {
      input = dataInputStream.readUTF();
      log.info("From Client: " + input);
      /* In case socket closes, the Client crashed */
    } catch (SocketException e) {
      //log.fatal("Client disconnected from server");
      //removeCrashedClient();
      //System.out.println("Wait for ping to decide");

    } catch (SocketTimeoutException e) {
      log.fatal("Time out: " + e);
      //removeCrashedClient();

    } catch (Exception e) {
      log.fatal("Error: " + e);
    }
    return input;
  }

  /**
   * sends a message back to the own client.
   *
   * @param output message coming from the server.
   * @return true or false, true if message was successfully sent to the client and false if not.
   */
  public boolean sendToClient(String output) {
    try {
      /* Log only ping unrelated command */
      if (!output.startsWith("pingg") && !output.startsWith("pongg")) {
        log.info("Sent to " + player.getUsername() + ": " + output);
      }
      dataOutputStream.writeUTF(output);
      dataOutputStream.flush(); // send message
      return true;
    } catch (IOException e) {
      log.fatal("You lost the connection to the server" + e);
      e.printStackTrace();
      return false;
    }
  }

  /**
   * prints the message sent to the client.
   * @param msg the message sent by the server to the client.
   */

  public void printToClient(String msg) {
    sendToClient("print&" + msg);
  }
}