package Generators;

import java.util.UUID;

/**
 * Generates unique string for personID and eventID
 */
public class GenerateID {

  /**
   * Generates a unique ID for a person
   * @return unique String ID
   */
  public static String generatePersonID() {
    return UUID.randomUUID().toString().substring(0,10);
  }

  /**
   * Generates a unique ID for an event
   * @return unique String ID
   */
  public static String generateEventID() {
    return UUID.randomUUID().toString().substring(0,10);
  }

  /**
   * Generates a unique token for a user
   * @return unique String token
   */

  public static String generateToken() {
    return UUID.randomUUID().toString().substring(0,10);
  }
}
