package Service;

import Dao.AuthTokenDAO;
import Dao.DataAccessException;
import Dao.Database;
import Dao.UsersDAO;
import Model.AuthToken;
import Requests.LoginRequest;
import Response.LoginResponse;

import java.sql.Connection;

/**
 * <pre>
 *  Logs in the user and returns an auth token.
 *  Auth Token Required: NO
 * </pre>
 */
public class LoginService {
  private LoginRequest logReq;
  private LoginResponse logRes;
  LoginResponse loginResponse;
  private Database db;
  private UsersDAO uDAO;

  /**
   * Receive login request data from the user and process login to return response.
   * @param logReq request of the login service
   */
  public LoginService(LoginRequest logReq) {
    this.logReq = logReq;
    db = new Database();
  }

  /**
   * Calls all the functions needed to login a user and returns LoginResponse.
   * @return LoginResponse
   */
  public LoginResponse loginUser() throws DataAccessException {

    boolean isRegistered = false;
    try {
      Connection conn = db.openConnection();
      uDAO = new UsersDAO(conn);
      isRegistered = uDAO.isRegistered(logReq.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    try {
      AuthToken token = findUserToken();
      if (token != null && isRegistered) {
        loginResponse = new LoginResponse(token.getAuthToken(), token.getAssociatedUsername(), getPersonID());
      }
      else {
        loginResponse = new LoginResponse("Wrong Username or password", false);
      }
    } catch (DataAccessException e) {
      e.printStackTrace();
      loginResponse = new LoginResponse(e.getMessage(), false);
    }
    return loginResponse;
  }

  /**
   * Query one authToken from the database using AuthTokenDAO.
   * If not able to find the user in the AuthTokens, throw appropriate exception.
   * @return AuthToken data for the user
   */
  private AuthToken findUserToken() throws DataAccessException {
    AuthToken token = null;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      token = aDAO.getTokenByUserName(logReq.getUserName());

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      throw new DataAccessException("Error encountered while getting authToken");
    }

    return token;
  }

  /**
   * Get personID from the UsersDAO using username
   * @return personID of the user
   */
  private String getPersonID() throws DataAccessException {
    String personID = null;
    try {
      Connection conn = db.openConnection();
      uDAO = new UsersDAO(conn);
      personID = uDAO.getPersonIDByUserName(logReq.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      throw new DataAccessException("Error encountered while getting PersonID");
    }

    return personID;
  }
}
