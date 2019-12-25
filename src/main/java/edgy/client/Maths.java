/** edgy.client is a package for command parsing. */

package edgy.client;

/**
 * Implements a bonus-feature which tells you
 * if a number is prime or not.
 * @author While(true){Do nothing}.
 * @version 1.0
 */

public class Maths {

  /**
   * method that calculates whether a number is prime or not.
   * @param number number that should be tested for being prime or not
   * @return original number plus whether number is prime or not
   */
  public static String isPrime(int number) {
    boolean[] sieve = new boolean[number + 1];

    for (int start = 1; start <= number; start++) {
      int prime = getNextPrime(sieve, start);

      for (int i = 2; prime * i <= number; i++) {
        sieve[prime * i - 1] = true; /* is not a prime */
      }
    }

    if (sieve[number - 1]) {
      return number + " is not a prime";
    }
    return number + " is prime";
  }

  private static int getNextPrime(boolean[] sieve, int start) {
    for (int i = start; i < sieve.length; i++) {
      if (!sieve[i]) {
        return i + 1;
      }
    }
    return 0;
  }
}
