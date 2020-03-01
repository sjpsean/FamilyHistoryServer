package Service;

import Dao.*;
import Model.*;
import Requests.LoadRequest;
import Response.LoadResponse;
import Response.LoginResponse;

import java.sql.Connection;

/**
 * <pre>
 *  Clear all data from the data base and fill in Users, Persons, Events table
 * </pre>
 */
public class LoadService {
  private LoadRequest loadRequest;

  /**
   * Receive loadRequest data from the handler.
   * @param loadRequest contains arrays of users, persons, events.
   */
  public LoadService(LoadRequest loadRequest) {
    this.loadRequest = loadRequest;
  }

  /**
   * Call all the functions needed to execute loading.
   * Use ClearAllService to clear the data.
   * Use UsersDAO, PersonsDAO, EventsDAO to load all data to the database.
   * @return MessageResponse with a message indicating how many users, persons and events were added
   */
  public LoadResponse load() throws DataAccessException {
    LoadResponse loadResponse;
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      PersonsDAO pDAO = new PersonsDAO(conn);
      EventsDAO eDAO = new EventsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      // First delete all data from the database.
      uDAO.deleteAll();
      pDAO.deleteAll();
      eDAO.deleteAll();
      aDAO.deleteAll();

      for (User user : loadRequest.getUsers()) {
        uDAO.create(user);
      }
      for (Person person : loadRequest.getPersons()) {
        pDAO.create(person);
      }
      for (Event event : loadRequest.getEvents()) {
        eDAO.create(event);
      }
      db.closeConnection(true);
      loadResponse = new LoadResponse(MessageBuilder(), true);
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      loadResponse = new LoadResponse(e.getMessage(), false);
    }

    return loadResponse;
  }

  private String MessageBuilder () {
    StringBuilder respMessage = new StringBuilder();
    respMessage.append("Successfully added ");
    respMessage.append(loadRequest.getUsers().length);
    respMessage.append(" users, ");
    respMessage.append(loadRequest.getPersons().length);
    respMessage.append(" persons, and ");
    respMessage.append(loadRequest.getEvents().length);
    respMessage.append(" events to the database.");
    return respMessage.toString();
  }
}
