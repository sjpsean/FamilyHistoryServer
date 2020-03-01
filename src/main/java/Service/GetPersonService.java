package Service;

import Response.PersonResponse;

/**
 * <pre>
 *  Returns the single Person object that has a specific ID.
 *  AuthToken Required: Yes
 * </pre>
 */
public class GetPersonService {

  /**
   * User PersonsDAO to find Persons with the personID.
   * @param personID person's ID
   * @return Person
   */
  public PersonResponse getPersonByID(String personID) {
    return null;
  }

  /**
   * Find userName with AuthTokensDAO.
   * Return all Persons containing the username using PersonsDAO.
   * @param authToken key to find a username
   * @return Array of Person objects
   */
  public PersonResponse getAllPersons(String authToken) {
    return null;
  }
}
