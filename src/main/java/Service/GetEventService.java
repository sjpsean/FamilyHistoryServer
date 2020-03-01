package Service;

import Model.Event;

  /**
   * <pre>
   *  Returns the single Event object that has a specific eventID.
   *  AuthToken Required: Yes
   * </pre>
   */
public class GetEventService {

  /**
   * Use EventDAO to find Event with the eventID.
   * @param eventID unique string
   * @return Person
   */
  public Event getEventByID (String eventID) {
    return null;
  }

  /**
   * Find userName with AuthTokensDAO.
   * Get all Persons containing the username using PersonsDAO.
   * Get all Events containing the username using EventDAO.
   * @param authToken key to find a username
   * @return Array of Person objects
   */
  public Event[] getAllEvents (String authToken) {
    return null;
  }
}
