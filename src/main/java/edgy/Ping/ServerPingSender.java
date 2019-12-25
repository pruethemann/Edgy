/**
 * edgy.client is a package for command parsing.
 */

package edgy.ping;

//original
import edgy.server.ClientThread;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Implements a sender for a ping.
 *
 * @author While(true){Do nothing}.
 * @version 1.0
 */
public class ServerPingSender extends Thread {

  private ClientThread ct;
  public boolean crashed = false;
  private ServerPingReceiver receiver;
  private LocalTime sendTime = LocalTime.now();

  /**
   * Constructor for the sender.
   * @param ct is a client Thread.
   * @param receiver receives the signal of the ping.
   */
  public ServerPingSender(ClientThread ct, ServerPingReceiver receiver) {
    this.ct = ct;
    this.receiver = receiver;
  }

  /** starts and runs the ping of the signal. */
  public void run() {
    while (!crashed) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {

      }

      /* send a ping every 3 seconds */
      if (Duration.between(sendTime, LocalTime.now()).getSeconds() >= 3) {
        ct.sendToClient("pingg");
        sendTime = LocalTime.now();
        receiver.setCountdown(sendTime);
      }
    }
  }
}