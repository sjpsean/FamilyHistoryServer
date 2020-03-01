package Service;

import Dao.*;
import Response.ClearResponse;

import java.sql.Connection;

/**
 * <pre>
 *   Deletes all data from the database.
 * </pre>
 */
public class ClearAllService {
  /**
   * Call all DAO's removeAll functions from all of them
   * @return MessageResponse
   */
  public ClearResponse deleteAll() throws DataAccessException {
    ClearResponse clearResponse;
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      // call all DAO's
      UsersDAO uDAO = new UsersDAO(conn);
      PersonsDAO pDAO = new PersonsDAO(conn);
      EventsDAO eDAO = new EventsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      // call deleteAll functions from all DAO's
      uDAO.deleteAll();
      pDAO.deleteAll();
      eDAO.deleteAll();
      aDAO.deleteAll();
      db.closeConnection(true);
      clearResponse = new ClearResponse("Clear succeeded", true);
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      clearResponse = new ClearResponse(e.getMessage(), false);
    }
    return clearResponse;
  }
}
