/** edgy.client is a package for command parsing. */

package edgy.ping;

// import java.time.Duration;
import edgy.client.Client;
import edgy.gui.ErrorGui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Implementing of a ping receiver.
 *
 * @author While(true){Do nothing}.
 * @version 1.0
 */
public class PingReceiver extends Thread {

  public boolean crashed = false;
  private boolean end = false;
  private PingSender ps;
  private Client c;
  private int deathCounter = 0;

  /** Initialisation of the rolling server logger. */
  private static Logger log = LogManager.getLogger(PingReceiver.class.getName());

  /**
   * Constructor for the client.
   *
   * @param c is a Client Thread.
   */
  public PingReceiver(Client c) {
    this.c = c;
  }

  /** Starts ping connection. */
  public void run() {

    while (!end) {
      // long duration = Duration.between(now,after).getSeconds();
    }
  }

  /**
   * Counts time needed by signal from sender to receiver.
   *
   * @param sendTime time where signal was sent.
   */
  public void setCountdown(LocalTime sendTime) {


    /* Wait 2 seconds */
    //System.out.println("Before: " + LocalTime.now());
    while (Duration.between(sendTime, LocalTime.now()).getSeconds() <= 3) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {

      }

    }

    //System.out.println("AFter: " + LocalTime.now() + "  " + sendTime);

    /* crashed boolean need to be false now, otherwise server crashed */
    if (crashed) {
      /* if more than 3 pings got lost, the Client is lost */
      deathCounter++;
      log.warn("ping fail:  " + c.player.getUsername() + "  " + deathCounter);

      if (deathCounter >= 3) {
        /* Finish while loop in Sender Thread */
        System.err.println(
            "The Server crashed. Please close Edgy and try to reconnect. "
                + c.player.getUsername());
        end = true;
        ps.crashed = true;
        new ErrorGui().setText("The Server crashed. Please close Edgy and try to reconnect.");
        return;
      }

    } else {
      // log.info("The following client did NOT crash: " + ct.player.getUsername());
      deathCounter = 0;
    }
    /* reset death Countdown */
    crashed = true;
  }

  /** Reconstructs a ping. */
  public void setSave() {
    // System.out.println("Set save: " + c.player.getUsername() + " : " + LocalTime.now());
    crashed = false;
  }

  /**
   * Setter method for the Sender.
   * @param ps PingSender which constantly sends a ping to Client.
   */
  public void setSender(PingSender ps) {
    this.ps = ps;
  }
}
