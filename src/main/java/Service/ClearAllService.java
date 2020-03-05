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
      if (db.getConnection() != null) db.openConnection();
      db.clearTables();
      clearResponse = new ClearResponse("clear succeeded", true);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      clearResponse = new ClearResponse(e.getMessage(), false);
    }
    return clearResponse;
  }
}
