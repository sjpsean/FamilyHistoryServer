package Service;

import Dao.*;
import Model.Event;
import Requests.EventRequest;
import Response.EventResponse;

import java.sql.Connection;

/**
   * <pre>
   *  Returns the single Event object that has a specific eventID.
   *  AuthToken Required: Yes
   * </pre>
   */
public class GetEventService {
  private EventRequest eventReq;
  private EventResponse eventRes;
  private Database db;

  public GetEventService(EventRequest eventReq) {
    this.eventReq = eventReq;
    db = new Database();
  }

  /**
   * Use EventDAO to find Event with the eventID.
   * @return EventResponse that only has one event
   */
  public EventResponse getEventByID () throws DataAccessException {
    // check if the token is valid.
    if (!isValidToken(eventReq.getToken())) {
      eventRes = new EventResponse("Error: token is not valid", false);
      return eventRes;
    }

    Event[] events = null;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      events = eDAO.getEventsByUsername(aDAO.getUsernameByToken(eventReq.getToken()));
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    // get Event by eventID.
    Event event = null;
    if (events != null) {
      for (Event e : events) {
        if (e.getEventID().equals(eventReq.getEventID())) {
          event = e;
          break;
        }
      }
    }

    if (event == null) eventRes = new EventResponse("Error: event with this eventID is not found", false);
    else eventRes = new EventResponse(event);
    return eventRes;
  }

  /**
   * Find userName with AuthTokensDAO.
   * Get all Persons containing the username using PersonsDAO.
   * Get all Events containing the username using EventDAO
   * @return Array of Person objects
   */
  public EventResponse getAllEvents () throws DataAccessException {
    if (!isValidToken(eventReq.getToken())) {
      eventRes = new EventResponse("Error: token is not valid", false);
      return eventRes;
    }

    Event[] events = null;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      events = eDAO.getEventsByUsername(aDAO.getUsernameByToken(eventReq.getToken()));
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    if (events == null) eventRes = new EventResponse("Error: event not found", false);
    else eventRes = new EventResponse(events);
    return eventRes;
  }

  private boolean isValidToken (String token) throws DataAccessException {
    boolean isRegistered = false;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      String username = aDAO.getUsernameByToken(token);

      UsersDAO uDAO = new UsersDAO(conn);
      isRegistered = uDAO.isRegistered(username);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    return isRegistered;
  }
}
