package Service;

import Dao.AuthTokenDAO;
import Dao.DataAccessException;
import Dao.Database;
import Model.AuthToken;
import Requests.RegisterRequest;
import Response.RegisterResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
  private Database db;

  @BeforeEach
  void setUp() {
    db = new Database();
  }

  @AfterEach
  void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  void registerUserPass() throws Exception {
    RegisterResponse regRes = null;
    RegisterRequest regReq = null;

    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("Sean", "park", "sean@park", "Sean", "Park", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertEquals(regRes.getUserName(), regReq.getUserName());

    AuthToken compareToken = null;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      compareToken = aDAO.getTokenByUserName(regReq.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(compareToken);
    assertEquals(compareToken.getAuthToken(), regRes.getAuthToken());
  }

  @Test
  void registerUserFail() throws Exception {
    RegisterResponse regRes = null;
    RegisterRequest regReq = null;

    // try inserting user with no username.
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("", "park", "sean@park", "Sean", "Park", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertFalse(regRes.isSuccess());

    regRes = null;
    // try inserting user with no password.
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("username", "", "sean@park", "Sean", "Park", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertFalse(regRes.isSuccess());

    regRes = null;
    // try inserting user with no password.
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("username", "", "sean@park", "Sean", "Park", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertFalse(regRes.isSuccess());

    regRes = null;
    // try inserting user with no email.
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("username", "password", "", "Sean", "Park", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertFalse(regRes.isSuccess());

    regRes = null;
    // try inserting user with no first or last name.
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("username", "password", "sean@park", "", "", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertFalse(regRes.isSuccess());

    regRes = null;
    // try inserting user with no gender.
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("username", "password", "sean@park", "Sean", "Park", "");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertFalse(regRes.isSuccess());

    regRes = null;
    // try inserting user that is already Registered.
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("username", "password", "sean@park", "Sean", "Park", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    regRes = null;
    try {
      Connection conn = db.openConnection();
      regReq = new RegisterRequest("username", "password", "sean@park", "Sean", "Park", "m");
      RegisterService regService = new RegisterService(regReq);
      regRes = regService.registerUser();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(regRes);
    assertFalse(regRes.isSuccess());
  }
}