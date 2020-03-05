package Service;

import Dao.*;
import Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ClearAllServiceTest {
  private Database db;
  private User userSample;
  private Person personSample;
  private Event eventSample;
  private AuthToken tokenSample;

  /**
   * practice using BeforeEach and AfterEach.
   * Since there is only one function to test, I don't think we need BeforeEach or AfterEach.
   */
  @BeforeEach
  public void setUp() {
    db = new Database();
    userSample = new User("Sean", "seanpassword", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
    personSample = new Person("Sean", "person123ID", "Sean", "Park", "m",
            "fatherParkID", "motherParkID", "SeanSpouseID");
    eventSample = new Event ("eventID","SeanPark","personID1", 12.34f,56.78f,
            "Korea","Seoul","Birth",1997);
    tokenSample = new AuthToken("abcde12345",
            "Sean", "password");
  }

  @AfterEach
  public void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  void deleteAll() throws Exception {
    ResultSet rsUser = null;
    ResultSet rsPerson = null;
    ResultSet rsEvent = null;
    ResultSet rsToken = null;
    boolean isEmpty = false;

    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      PersonsDAO pDAO = new PersonsDAO(conn);
      EventsDAO eDAO = new EventsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      uDAO.create(userSample);
      pDAO.create(personSample);
      eDAO.create(eventSample);
      aDAO.create(tokenSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    try {
      Connection conn = db.openConnection();
      ClearAllService caService = new ClearAllService();
      caService.deleteAll();

      try (Statement stmt = conn.createStatement()) {
        String sqlUser = "SELECT * FROM Users;";
        String sqlPerson = "SELECT * FROM Persons;";
        String sqlEvent = "SELECT * FROM Events;";
        String sqlToken = "SELECT * FROM AuthTokens";
        rsUser = stmt.executeQuery(sqlUser);
        rsPerson = stmt.executeQuery(sqlPerson);
        rsEvent = stmt.executeQuery(sqlEvent);
        rsToken = stmt.executeQuery(sqlToken);
        if (!rsUser.next() && !rsPerson.next() && !rsEvent.next() && !rsToken.next()) {
          isEmpty = true;
        }
      } catch (SQLException e) {
        throw new DataAccessException("SQL Error encountered while querying all tables");
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isEmpty);
  }
}