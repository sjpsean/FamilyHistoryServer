package Service;

import Dao.*;
import Generators.GenerateID;
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

    if (!checkVaild()) {
      return new LoadResponse("Error: There are no values to load", false);
    }

    try {
      Connection conn=db.openConnection();
      db.clearTables();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    try {
      Connection conn=db.openConnection();
      UsersDAO uDAO=new UsersDAO(conn);
      PersonsDAO pDAO=new PersonsDAO(conn);
      EventsDAO eDAO=new EventsDAO(conn);
      AuthTokenDAO aDAO=new AuthTokenDAO(conn);
      for (User user : loadRequest.getUsers()) {
        uDAO.create(user);
        aDAO.create(new AuthToken(GenerateID.generateToken(), user.getUserName(), user.getPassword()));
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

  private boolean checkVaild () {
    if (loadRequest.getEvents() == null
    || loadRequest.getPersons() == null
    || loadRequest.getUsers() == null) {
      return false;
    }

    if (loadRequest.getEvents().length == 0
    && loadRequest.getPersons().length == 0
    && loadRequest.getUsers().length == 0) {
      return false;
    }

    return true;
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
