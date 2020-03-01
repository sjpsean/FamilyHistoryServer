package Service;

import Dao.AuthTokenDAO;
import Dao.DataAccessException;
import Dao.Database;

import java.sql.Connection;

public class VerifyAuthToken {
  public boolean verify(String token) throws DataAccessException {
    Database db = new Database();

    try {
      Connection conn=db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
      return false;
  }
}
