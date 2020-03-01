package Service;

import Dao.*;
import Model.*;
import Requests.LoadRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {
  private Database db;
  private User userSample;
  private Person personSample;
  private Event eventSample;
  User[] users;
  Person[] persons;
  Event[] events;

  @BeforeEach
  public void setUp() {
    db = new Database();
    userSample = new User("Sean", "seanpassword", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
    personSample = new Person("person123ID", "Sean", "Sean", "Park", "m",
            "fatherParkID", "motherParkID", "SeanSpouseID");
    eventSample = new Event("eventID","SeanPark","personID1", 12.34f,56.78f,
            "Korea","Seoul","Birth",1997);
    users = new User[] { userSample };
    persons = new Person[] { personSample };
    events = new Event[] { eventSample };

  }

  @AfterEach
  public void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  void loadPass() throws Exception {
    ResultSet rsUser = null;
    ResultSet rsPerson = null;
    ResultSet rsEvent = null;
    ResultSet rsToken = null;
    User compareUser = null;
    Person comparePerson = null;
    Event compareEvent = null;

    try {
      Connection conn = db.openConnection();
      LoadRequest loadRequest=new LoadRequest(users, persons, events);
      LoadService loadService=new LoadService(loadRequest);
      loadService.load();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    try {
      Connection conn = db.openConnection();
      try (Statement stmt = conn.createStatement()) {
        String sqlUser = "SELECT * FROM Users;";
        String sqlPerson = "SELECT * FROM Persons;";
        String sqlEvent = "SELECT * FROM Events;";
        rsUser = stmt.executeQuery(sqlUser);
        if (rsUser.next()) {
          compareUser = new User(rsUser.getString("UserName"), rsUser.getString("Password"), rsUser.getString("Email"),
                  rsUser.getString("FirstName"), rsUser.getString("LastName"), rsUser.getString("Gender"),
                  rsUser.getString("PersonID"));
        }
        rsPerson = stmt.executeQuery(sqlPerson);
        if (rsPerson.next()) {
          comparePerson = new Person(rsPerson.getString("PersonID"), rsPerson.getString("AssociatedUsername"),
                  rsPerson.getString("FirstName"), rsPerson.getString("LastName"), rsPerson.getString("Gender"),
                  rsPerson.getString("FatherID"), rsPerson.getString("MotherID"), rsPerson.getString("SpouseID"));
        }
        rsEvent = stmt.executeQuery(sqlEvent);
        if (rsEvent.next()) {
          compareEvent = new Event (rsEvent.getString("EventID"), rsEvent.getString("AssociatedUsername"),
                  rsEvent.getString("PersonID"), rsEvent.getFloat("Latitude"),rsEvent.getFloat("Longitude"),
                  rsEvent.getString("Country"), rsEvent.getString("City"),
                  rsEvent.getString("EventType"), rsEvent.getInt("Year"));
        }
      } catch (SQLException e) {
        throw new DataAccessException("SQL Error encountered while querying all tables");
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(compareUser);
    assertNotNull(comparePerson);
    assertNotNull(compareEvent);
    assertEquals(compareUser, userSample);
    assertEquals(comparePerson, personSample);
    assertEquals(compareEvent, eventSample);
  }
}