/**
 *  edgy.sound is a package for playing sounds during the game.
 */

package edgy.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/** Implementation of a short music sound for the player who won. */
public class Sounds implements LineListener {

  /**
   * Updates constantly the listener.
   *
   * @param event position of listener.
   * @see LineListener
   */
  public void update(LineEvent event) {}

  /**
   * Main method. Only for testing.
   *
   * @param args input.
   */
  public static void main(String[] args) {
    Sounds sounds = new Sounds();
    sounds.playWin();
    sounds.playLost();
    sounds.playPlace();
    sounds.playTurn();
    sounds.playTrololo();
    sounds.playPieceWrong();
  }

  /** Plays the sound of the Winners. */
  public void playWin() {
    playSound("Win.wav", 1200);
  }

  /** Plays the sound if piece isn't set right.. */
  public void playPieceWrong() {
    playSound("MoveFailed.wav", 500);
  }

  /** Plays sound for the unlucky. */
  public void playTrololo() {
    playSound("trololo.wav", 7100);
  }

  /** Plays sound for the Loser. */
  public void playLost() {
    playSound("Lost.wav", 4200);
  }


  /** Plays Your turn sound. */
  public void playTurn() {
    playSound("Turn.wav", 1100);
  }

  /** Place piece sound. */
  public void playPlace() {
    playSound("Tick.wav", 150);
  }

  /**
   * Plays the sound for the winner. Implementation of the audioClip and Listener.
   * @param filename location of sound
   * @param duration longest duration of sound play.
   */
  public void playSound(String filename, int duration) {
    try {
      String home = new java.io.File(".").getCanonicalPath();
      String soundPath = Paths.get(home, "src", "main", "resources", "Sounds", filename).toString();

      File audioFile = new File(soundPath);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
      AudioFormat format = audioStream.getFormat();
      DataLine.Info info = new DataLine.Info(Clip.class, format);
      Clip audioClip = (Clip) AudioSystem.getLine(info);
      audioClip.addLineListener(this);

      audioClip.open(audioStream);
      audioClip.start();

      // audioClip.loop(0);

      // wait for the playback completes
      try {
        Thread.sleep(duration);
      } catch (InterruptedException ex) {
        System.out.println("Interruption. " + ex);
      }

      audioClip.close();

    } catch (UnsupportedAudioFileException ex) {
      System.out.println("The specified audio file is not supported. " + ex);
    } catch (LineUnavailableException ex) {
      System.out.println("Audio line for playing back is unavailable. " + ex);
    } catch (IOException ex) {
      System.out.println("Error playing the audio file. " + ex);
    }
  }
}