package Service;

import Dao.*;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.FillRequest;
import Response.FillResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {
  private Database db;
  private User userSample;

  @BeforeEach
  void setUp() {
    db = new Database();
    userSample = new User("Sean", "seanpassword", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
  }

  @AfterEach
  void tearDown() throws Exception{
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  void fillPass() throws DataAccessException {
    FillResponse fillRes;
    FillRequest fillReq;

    // create a user first.
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      uDAO.create(userSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    // fill using that user.
    fillReq = new FillRequest(userSample.getUserName(), 4);
    FillService fillService = new FillService(fillReq);
    fillRes = fillService.fill();

    // check if it created 31 person associated with the username.
    Person[] persons = null;
    Person person = null;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);
      persons = pDAO.getPersonsByUserName(userSample.getUserName());
      person = pDAO.getPersonByID(userSample.getPersonID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(person);
    assertNotNull(persons);
    assertEquals(persons.length, 31);

    Event[] events = null;
    Event[] momEvents = null;
    Event[] dadEvents = null;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      events = eDAO.getEventsByPersonID(userSample.getPersonID());
      momEvents = eDAO.getEventsByPersonID(person.getMotherID());
      dadEvents = eDAO.getEventsByPersonID(person.getFatherID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(events);
    assertNotNull(momEvents);
    assertNotNull(dadEvents);
    assertEquals(events[0].getEventType(), "Birth");
    assertEquals(momEvents.length, 3);
    assertEquals(dadEvents.length, 3);
  }

  @Test
  void fillFail() throws DataAccessException {
    FillResponse fillRes;
    FillRequest fillReq;

    // try to fill user that is not registered.
    fillReq = new FillRequest(userSample.getUserName(), 4);
    FillService fillService = new FillService(fillReq);
    fillRes = fillService.fill();

    assertFalse(fillRes.isSuccess());
  }
}