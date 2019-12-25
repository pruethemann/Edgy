/**
 * edgy.client is a package for command parsing.
 */

package edgy.ping;

import edgy.client.Client;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Implements a sender for a ping.
 *
 * @author While(true){Do nothing}.
 * @version 1.0
 */
public class PingSender extends Thread {

  private Client c;
  public boolean crashed = false;
  private PingReceiver receiver;
  private LocalTime sendTime = LocalTime.now();

  /**
   * Constructor for the sender.
   * @param c is a client Thread.
   * @param receiver receives the signal of the ping.
   */
  public PingSender(Client c, PingReceiver receiver) {
    this.c = c;
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
        //System.out.println("sent: " + LocalTime.now() + "  " + sendTime);
        c.sendToServer("pingg");
        sendTime = LocalTime.now();
        receiver.setCountdown(sendTime);
      }
    }

  }
}