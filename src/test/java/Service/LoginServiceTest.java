package Service;

import Dao.AuthTokenDAO;
import Dao.DataAccessException;
import Dao.Database;
import Dao.UsersDAO;
import Model.AuthToken;
import Model.User;
import Requests.LoginRequest;
import Response.LoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
  private Database db;
  private User userSample;
  private AuthToken tokenSample;

  @BeforeEach
  public void setUp() {
    db = new Database();
    userSample = new User("Sean", "password", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
    tokenSample = new AuthToken("abcde12345", "Sean", "password");

  }

  @AfterEach
  public void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }


  @Test
  void loginUserPass() throws Exception {

    try {
      Connection conn = db.openConnection();

      UsersDAO uDAO = new UsersDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      uDAO.create(userSample);
      aDAO.create(tokenSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    LoginResponse loginResponse = null;
    try {
      Connection conn = db.openConnection();
      LoginRequest loginRequest = new LoginRequest(userSample.getUserName(), userSample.getPassword());
      LoginService loginService = new LoginService(loginRequest);
      loginResponse = loginService.loginUser();

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(loginResponse);
    assertEquals(loginResponse.getAuthToken(), tokenSample.getAuthToken());
    assertEquals(loginResponse.getPersonID(), userSample.getPersonID());
    assertEquals(loginResponse.getUserName(), userSample.getUserName());
    assertEquals(loginResponse.getUserName(), tokenSample.getAssociatedUsername());

  }

  @Test
  void loginUserFail() throws Exception {

    try {
      Connection conn = db.openConnection();

      UsersDAO uDAO = new UsersDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      uDAO.create(userSample);
      aDAO.create(tokenSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    LoginResponse loginResponse = null;
    LoginResponse loginResponse2 = null;
    try {
      Connection conn = db.openConnection();
      LoginRequest wrongUsernameRequest = new LoginRequest("random", userSample.getPassword());
      LoginRequest wrongPasswordRequest = new LoginRequest(userSample.getUserName(), "wrongpassword");
      LoginService loginService = new LoginService(wrongUsernameRequest);
      loginResponse = loginService.loginUser();
      loginService = new LoginService(wrongUsernameRequest);
      loginResponse2 = loginService.loginUser();

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(loginResponse);
    assertNotNull(loginResponse2);
    assertFalse(loginResponse.isSuccess());
    assertFalse(loginResponse2.isSuccess());
  }
}