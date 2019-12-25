/** edgy.client is a package for command parsing. */

package edgy.client;

import edgy.ping.PingReceiver;
import edgy.ping.PingSender;
import edgy.gui.*;
import edgy.mechanics.Player;
import edgy.sound.Sounds;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;

/**
 * implements all the necessary client functions for the connection with the server.
 *
 * @author While(true) {do nothing}.
 * @version 1.0
 */
public class Client extends Thread {

  /**
   * Client waits constantly for serverinput.
   *
   * @see Runnable from java.lang.
   */
  private static Socket clientSocket = null;

  private static PrintStream os = null;
  private static BufferedReader is = null;
  private static DataOutputStream dataOutputStream = null;
  private static DataInputStream dataInputStream = null;
  private static BufferedReader inputLine = null;
  private static boolean closed = false;
  private HashMap<String, String> alias;
  public static final LoginGui loginGUI = new LoginGui();
  private static LobbyGui lobbyGUI;
  private static MatchGui match;
  private static HighScoreGui highscoreGUI;
  public static final Player player = new Player();
  private BufferedImage logo;
  // private String nr;
  private PingReceiver receiver;
  protected Sounds sounds;

  public Client() {
    /* iniate sound system*/
    sounds = new Sounds();
  }

  /** Client constantly waits for Server input. */
  public void run() {
    while (!closed) {

      String input = receiveFromServer();

      // The first 5 characters of the input specify the command
      try {
        String command = input.substring(0, 5);

        // The following arguments are separated by '&'
        String[] args = null;
        if (input.contains("&")) {
          input = input.substring(6);
          args = input.split("&");
        }
        parseServerInput(command, args);

      } catch (NullPointerException e) {
        System.err.println("The Server sends null. If you read this: "
                + "This is a serious bug which needs fixing!" + e);
        break;
      }
    }
  }

  /**
   * Counts number of actual lobbies and players and does update the GUI.
   *
   * @param lobbyString the number of lobbies and their status.
   */
  private void updateLobby(String lobbyString) {
    /* Define how many lobbies with how many players exist */
    String[] matches = lobbyString.split(";");
    int[] playerCount = new int[matches.length];
    String[] matchStatus = new String[matches.length];

    int c = 0;
    for (String match : matches) {
      playerCount[c] = Integer.parseInt(match.split(":")[0]);
      matchStatus[c] = match.split(":")[1];
      c++;
    }
    /* updates and refreshes the Lobby GUI */
    lobbyGUI.updateLobbys(playerCount, matchStatus);
  }

  /**
   * Offers options for the client he can chose.
   *
   * @param command Network command requested from client
   * @param args All arguments if required for command.
   */
  private void parseServerInput(String command, String[] args) {

    switch (command) {

      /* Prints a message on the terminal of the Client */
      case "print":
        System.out.println(args[0]);
        break;

        /* Prints a message on the terminal of the Client */
      case "chat_":
        String senderUsername = args[0];
        String message = args[1];
        match.printMsg("<" + senderUsername + "> " + message);
        break;

        /* Prints a message on the terminal of the Client */
      case "priva":
        senderUsername = args[0];
        message = args[1];
        match.printMsg("<" + senderUsername + "> whispers: " + message);
        break;

        /* Server either confirms or rejects move */
      case "moved":
        String moveStatus = args[0];
        String moveMessage = args[1];
        movePiece(moveStatus, moveMessage);
        break;

        /* Status about login: String status, String message, String username, int score */
      case "login":
        login(args[0], args[1], args[2], Integer.parseInt(args[3]));
        break;

        /* Updates the current total score of a player */
      case "score":
        int highScore = Integer.parseInt(args[0]);
        player.setHighscore(highScore);
        lobbyGUI.updateScore();
        break;

        /* status about register: String status, String message, String username*/
      case "nuser":
        register(args[0], args[1], args[2]);
        break;

        /* User changed Username */
      case "cuser":
        String state = args[0];
        message = args[1];
        String newUsername = args[2];

        if (state.equals("OK")) {
          lobbyGUI.changeUsername(newUsername);
          lobbyGUI.setText(message);
        } else {
          lobbyGUI.setText(message);
        }
        break;

        /* change password */
      case "chpwd":
        // String stat = args[0];
        message = args[1];
        lobbyGUI.setText(message);
        break;

        /* User is forced to skip */
      case "force":
        sendToServer("skipp");
        break;

        /* lists lobby information */
      case "lslob":
        /* Only broadcast if User already logged in */
        if (lobbyGUI == null) {
          break;
        }
        updateLobby(args[0]);
        break;

        /* Highscore. Gets highscore string from server */
      case "highs":
        String[] highscore = args[0].split(":");
        String[] username = new String[highscore.length / 4 + 1];
        String[] score = new String[highscore.length / 4 + 1];
        String[] onlineStatus = new String[highscore.length / 4 + 1];
        int i = 0;

        while (i < highscore.length - 1) {
          i++;
          username[i / 4] = highscore[i];
          i++;
          score[i / 4] = highscore[i];
          i++;
          onlineStatus[i / 4] = highscore[i];
          i++;
        }
        if (highscoreGUI == null) {
          highscoreGUI = new HighScoreGui(username, score, onlineStatus);
        } else {
          highscoreGUI.updateHighscore(username, score, onlineStatus);
        }

        break;

      case "trolo":
        troll();
        break;

        /* sets up MatchGui when four Players joined */
      case "start":
        int color = Integer.parseInt(args[1]);
        String col = null;
        switch (color) {
          case 1:
            col = "Yellow";
            break;
          case 2:
            col = "Red";
            break;
          case 3:
            col = "Blue";
            break;
          default:
            col = "Green";
            break;
        }

        System.out.println("Color " + color);
        match = new MatchGui(args[0], col);

        match.printMsg("Hello " + player.getUsername() + "\nGood luck!");
        lobbyGUI.mainFrame.setVisible(false);
        break;

        /* Server sends match specific information:
        TotalRank:username:currentPoints:username:gamestatus */
      case "rankm":
        updateGameStatus(args[0]);
        break;

        /* Disconnects client from server */
      case "kill_":
        disconnect();
        break;

        /* print error message in chat */
      case "error":
        new ErrorGui().setText(args[0]);
        break;

        /* print this message in chat console */
      case "info_":
        match.printMsg(args[0]);
        break;

        /* Suggests a user the next piece to place */
      case "hint_":
        if (args[0].equals("0")) {
          match.printMsg("<SYSTEM> GAME OVER - No moves possible!");
          new SkipGui();
          break;
        } else if (args[0].equals("start")) {
          match.printMsg("<SYSTEM> Place a piece in a corner");
          break;
        }
        int pieceID = Integer.parseInt(args[0]);
        match.drawBorder(pieceID);
        int row = Integer.parseInt(args[1]);
        int colu = Integer.parseInt(args[2]);
        match.hintQuadrant(row, colu);
        // match.printMsg("PieceID: " + pieceID + " Row: " + row + " Col: " + colu);
        break;

        /* Player won*/
      case "win__":
        match.printMsg(args[0]);
        sounds.playWin();
        if (!args[0].equals("")) {
          new WinGui(1);
          lobbyGUI.setVisibility(true);
          lobbyGUI.setInMatch(false);
          match.disposeMainframe();
        }
        break;

        /* Player lost*/
      case "lost_":
        match.printMsg(args[0]);
        sounds.playLost();
        int x = Integer.parseInt(args[0].split(":")[1]);
        if (!args[0].equals("") && (x == 2 || x == 3 || x == 4)) {
          new WinGui(x);
          lobbyGUI.setVisibility(true);
          lobbyGUI.setInMatch(false);
          match.disposeMainframe();
        }
        break;

      case "board":
        match.updateBoard(args[0]);
        System.out.println(args[0]);
        break;

      case "delpi":
        match.deletePiece(args[0]);
        break;

      case "pongg":
        //System.out.println("ping returned");
        if (receiver == null) {
          /* start Server ping Thread */
          receiver = new PingReceiver(this);
          receiver.start();

          /* start the Thread of PingSender */
          PingSender sender = new PingSender(this, receiver);
          sender.start();

          /* define sender in ping receiver */
          receiver.setSender(sender);
        }
        receiver.setSave();
        break;

        /* actives ping from Client to Server */
      case "pingg":
        sendPong();
        break;

        /* its your turn to play */
      case "turn_":
        // match.printMsg(args[0]);
        sounds.playTurn();
        break;

        /* Command either rotates or flips a specific piece */
      case "piece":
        String pieceAction = args[0];
        pieceID = Integer.parseInt(args[1]);
        mutatePiece(pieceAction, pieceID);
        break;

      default:
        System.out.println("'" + command + "' is not recognized as server command");
    }
  }

  private void troll() {
    sounds.playTrololo();
    /* create a secure random number generator */
    try {
      SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");

      for (int i = 1; i < 500; i++) {
        int pieceID = secureRandomGenerator.nextInt(21) + 1;
        try {
          if (i % 3 == 0) {
            match.getPiecesGui().rotateNrLeft(pieceID);
          } else if (i % 2 == 0) {
            match.getPiecesGui().rotateNrRight(pieceID);
          } else {
            match.getPiecesGui().flipNr(pieceID);
          }
        } catch (NullPointerException e) {
          System.err.println("Piece has already been laid.");
        }

        try {
          Thread.sleep(10);
        } catch (Exception e) {
          System.err.println(e);
        }
      }

    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private void updateGameStatus(String matchStatusString) {
    String[] users = matchStatusString.split(";");
    String[] usernames = new String[4];
    String[] ranks = new String[4];
    String[] points = new String[4];
    String[] status = new String[4];

    for (int j = 0; j < 4; j++) {
      String[] content = users[j].split(":");
      ranks[j] = content[0];
      usernames[j] = content[1];
      points[j] = content[2];
      status[j] = content[3];
    }

    match.updateRank(ranks, usernames, points, status);
  }

  /**
   * Resets Activated pieces in case move got accepted or failed.
   *
   * @param moveStatus move either failed (FAIL) or got accepted (OK)
   * @param moveMessage write acceptance or failing message in chat
   */
  private void movePiece(String moveStatus, String moveMessage) {
    /* Print Accepted or Failed message in chat */
    // match.printMsg("<SYSTEM> " + moveMessage);

    /* Move failed */
    if (moveStatus.equals("FAIL")) {
      match.resetChoice();
      /* play negative fail sound */
      sounds.playPieceWrong();
      return;

      /* move successful */
    } else {
      /* play positive place sound */
      sounds.playPlace();
      return;
    }
  }

  /**
   * Calls the different methods for flipping and rotating of the actual piece.
   *
   * @param pieceAction the piece which was chosen.
   * @param pieceID the unique Id of the chosen piece.
   */
  private void mutatePiece(String pieceAction, int pieceID) {
    switch (pieceAction) {
      case "turnleft":
        match.getPiecesGui().rotateNrLeft(pieceID);
        break;

      case "turnright":
        match.getPiecesGui().rotateNrRight(pieceID);
        break;

      case "flip":
        match.getPiecesGui().flipNr(pieceID);
        break;

      default:
        System.out.println("Yo no how to print!! " + pieceAction);
    }
  }

  /**
   * Creates and opens a new login window.
   *
   * @param status if user is only or not.
   * @param message text in the window.
   * @param username of user who wants to login.
   * @param score the score of the user, taken from the database.
   * @see LoginGui
   */
  private void login(String status, String message, String username, int score) {
    if (message.startsWith("The player")) {
      loginGUI.mainFrame.setVisible(true);
    }
    /* check whether login was successfull */
    if (status.equals("OK")) {

      loginGUI.setText(message);

      /* create Player instance */
      player.setPlayer(username, score);

      /* Start Lobby-GUI and closes Login GUI */
      lobbyGUI = new LobbyGui();
      sendToServer("lobby");
      loginGUI.mainFrame.dispose();

    } else {
      /* Either username or password is wrong. Or User is already connected with another client */
      loginGUI.setText(message);
    }
  }

  /**
   * Checks coordinates of user and if they correspond to the stored one in the database, opens
   * lobby window.
   *
   * @param status online status of user.
   * @param message message which is printed on GUI.
   * @param username of the logged in user.
   */
  private void register(String status, String message, String username) {
    /* check whether login was successfull */
    if (status.equals("OK")) {
      loginGUI.setText(message);

      /* create Player instance */
      player.setPlayer(username, 0);

      /* Start Tutorial for new user */
      new TutorialGui();

      sendToServer("lobby");
      loginGUI.mainFrame.dispose();

    } else {
      /* Username is not unique */
      loginGUI.setText(message);
    }
  }

  /**
   * A main method for initiating a new client. Client connects with server.
   *
   * @param args input.
   */
  public static void main(String[] args) {
    Client c = new Client();
    c.init(args);
  }

  /**
   * Converts user readable network commands into standardized and unique network commands of length
   * 5.
   *
   * @param input are the commands and arguments the client entered.
   * @see Alias
   */
  public void parseClientInput(String input) {
    /* split user input */
    String[] commands = input.split(" ");

    /* only uses the first 5 chars for the command */
    commands[0] = alias.get(commands[0]);

    /* prevents client from sending non-existing commands to server */
    if (commands[0] == null) {
      System.out.println("'" + input + "' is not recognized as an internal or external command");
      return;
    }

    /* generate the network command separated with & */
    String output = "";
    for (String command : commands) {
      output += "&" + command;
    }

    /* Server sends command to Server and deletes first '&' */
    sendToServer(output.substring(1));
  }

  /** Import Logo png for Icon.
   * @return BufferedImage it returns the logo. */
  protected BufferedImage getLogo() {
    if (logo == null) {
      try {
        String home = new java.io.File(".").getCanonicalPath();
        String path = Paths.get(home, "src", "main", "resources", "LogoIcon.png").toString();
        logo = ImageIO.read(new File(path));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return logo;
  }

  /**
   * Starts connection with server including all necessery functions for the required values.
   *
   * @param arguments an array with the server address (port).
   * @see Alias for function words.
   */
  public void init(String[] arguments) {
    /* Create socket */
    connectToServer(arguments);

    /* Inititate Datainput and Output streams */
    initStreams();

    /* Send computername, IP and login name to Server */
    sendClientAddress();

    /* Start Thread */
    new Thread(new Client()).start();

    /* define user specific server command Aliases */
    alias = Alias.getAlias();

    /* Check whether client args include a username.
    Username is automatically logged in in case name exists. */

    /* init GUI for Login
     * argument 1: IP:Host
     * arg 2: username (optional)
     * arg 3: passwort (optional)
     * */

    /* IP / username and Pwd */
    if (arguments.length == 3) {
      sendToServer("login&" + arguments[1] + "&" + arguments[2]);
      loginGUI.show();
    } else if (arguments.length == 2) { /* localhost /username and pwd */
      sendToServer("login&" + arguments[0] + "&" + arguments[1]);
      loginGUI.show();

      /* No username / Localhost */
    } else {
      loginGUI.show();
    }


    /* start ping from Client to server */
    //sendToServer("pingg");

    /* permanently wait for input from Client */
    while (!closed) {
      String input = "e";
      try {
        input = inputLine.readLine();

        if (input == null) {
          System.out.println("Error: Server command was null");
          return;
        }

      } catch (IOException e) {
        System.out.println("Input couldn't be read.");
      }
      parseClientInput(input);
    }
  }

  /**
   * Connects client to the server.
   *
   * @param args input.
   * @throws IOException if connection fails because of wrong data.
   * @throws UnknownHostException if host can't be found.
   */
  private void connectToServer(String[] args) {
    int portNumber = 2012;
    String host = "localhost";

    /* Chose localHost and port 2012 in case user did not specify */
    if (args.length >= 1 && args[0].contains(":") && args[0].charAt(args[0].length() - 1) != ':') {
      host = args[0].split(":")[0];
      portNumber = Integer.parseInt(args[0].split(":")[1]);
    }

    /* connect Socket with Server */
    try {
      clientSocket = new Socket(host, portNumber);
    } catch (UnknownHostException e) {
      System.err.println("Unknown host: " + host);
    } catch (IOException e) {
      System.err.println("Connection failed. Host: " + host);
      System.exit(-1);
    }

    System.out.println("Client is trying to connect to " + host + " portNumber: " + portNumber);
  }

  /**
   * Sends input to server and output to client.
   *
   * @throws IOException if input and output stream to server fails.
   */
  private void initStreams() {
    try {
      inputLine = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
      /* Initiate Input Reader and Output stream to Server */
      os = new PrintStream(clientSocket.getOutputStream(), false, StandardCharsets.UTF_8);
      is =
          new BufferedReader(
              new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

      /* Initiate data output and data input stream to server */
      dataOutputStream = new DataOutputStream(os);
      InputStream inputStream = clientSocket.getInputStream();
      dataInputStream = new DataInputStream(inputStream);
    } catch (IOException e) {
      System.err.println("Error: Couldn't establish input and output stream to server.");
    }
  }

  /**
   * Sends a String to the Server. Check Network protocol for detailed commands.
   *
   * @param output message sent to the server.
   * @return true or false, true if message is successfully sent, false if not.
   */
  public boolean sendToServer(String output) {
    try {
      if (!output.startsWith("pingg") && !output.startsWith("pongg")) {
        System.out.println("From Client to Server: " + output);
      }
      dataOutputStream.writeUTF(output);
      dataOutputStream.flush(); // send message
      return true;
    } catch (IOException e) {
      System.out.println("You lost the connection to the server" + e);
      return false;
    }
  }

  /**
   * Receives Strings from Server. Check Network protocol for detailed commands.
   *
   * @return the datainput.
   */
  protected String receiveFromServer() {
    try {
      String input = dataInputStream.readUTF();

      if (!input.startsWith("pingg") && !input.startsWith("print") && !input.startsWith("kill")) {
        System.out.println("From Server: " + input);
      }
      return input;

    } catch (SocketException e) {
      String fatalError = "The server crashed. Please reconnect to a new server";
      System.err.println(fatalError);

      /* to much time passed since the last signal from server.
       Wait for ping server to handle this exception*/
    } catch (SocketTimeoutException e) {
      System.err.println("Error: SocketTimeout" + e);

    } catch (IOException e) {
      System.err.println("Error: Server input couldn't be read. " + e);
    }
    return null;
  }

  /** Sends all required parameters of the client to the server. */
  private void sendClientAddress() {
    /* Requests system specfic computer login name.*/
    String computerLoginname = System.getProperty("user.name");

    /* sends Computername, IP and login name of client to server. To do: Send independent */
    String clientAddress =
        getClientAddress().getHostName()
            + " "
            + getClientAddress().getHostAddress()
            + " "
            + computerLoginname;
    sendToServer(clientAddress);
  }

  /**
   * Get client address (Computername and IP).
   *
   * @return the address.
   * @throws Exception if address not found.
   */
  private InetAddress getClientAddress() {
    try {
      InetAddress address = InetAddress.getLocalHost();
      return address;
    } catch (Exception e) {
      System.out.println("Can't get Computername and IP.");
      return null;
    }
  }

  /**
   * Close the output stream, close the input stream, close the socket.
   *
   * @throws IOException if connection with server fails.
   */
  private void disconnect() {
    try {
      dataInputStream.close();
      dataOutputStream.close();
      clientSocket.close();
      closed = true;
      System.exit(0);
    } catch (IOException e) {
      System.err.println("IOException: Connection loss" + e);
    }
  }

  /** Sends the pong to the server. */
  private void sendPong() {
    sendToServer("pongg&" + player.getUsername());
  }
}
