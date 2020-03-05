package Dao;

import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class EventsDAOTest {
  private Database db;
  private Event eventSample;

  @BeforeEach
  public void setUp() throws Exception {
    db = new Database();
    eventSample = new Event ("eventID","SeanPark","personID1", 12.34f,56.78f,
            "Korea","Seoul","Birth",1997);
  }

  @AfterEach
  public void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  // Testing create function if it passes.
  @Test
  void createPass() throws DataAccessException {
    Event compareEvent = null;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);

      eDAO.create(eventSample);
      compareEvent = eDAO.getEventByID(eventSample.getEventID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(compareEvent);
    assertEquals(eventSample, compareEvent);
  }

  @Test
  void createFail() throws DataAccessException {
    boolean isCreated = true;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);

      eDAO.create(eventSample);
      eDAO.create(eventSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      isCreated = false;
    }

    assertFalse(isCreated);
  }

  @Test
  void getEventByIDPass() throws DataAccessException {
    Event compareEvent = null;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);

      eDAO.create(eventSample);
      compareEvent = eDAO.getEventByID(eventSample.getEventID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(compareEvent);
    assertEquals(eventSample, compareEvent);
  }

  @Test
  void getEventByIDFail() throws DataAccessException {
    Event compareEvent = null;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);

      eDAO.create(eventSample);
      compareEvent = eDAO.getEventByID("thisisnotid");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(compareEvent);
  }

  @Test
  void getEventsByIDPass() throws Exception {
    Event[] events = null;
    Event eventSample2 = new Event ("eventID2","SeanPark","personID1", 12.34f,56.78f,
            "Korea","Seoul","Birth",1997);
    Event eventSample3 = new Event ("eventID3","SeanPark","personID3", 12.34f,56.78f,
            "Korea","Seoul","Birth",1997);

    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      eDAO.create(eventSample);
      eDAO.create(eventSample2);
      eDAO.create(eventSample3);
      db.closeConnection(true);
    } catch (DataAccessException e ) {
      db.closeConnection(false);
    }

    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      events = eDAO.getEventsByPersonID("personID1");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(events);
    assertEquals(events[0], eventSample);
    assertEquals(events[1], eventSample2);
  }

  @Test
  void getEventsByIDFail() throws Exception {
    Event[] events = { eventSample };

    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      eDAO.create(eventSample);
      db.closeConnection(true);
    } catch (DataAccessException e ) {
      db.closeConnection(false);
    }

    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      events = eDAO.getEventsByPersonID("thisisnotvalid");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(events);
  }

  @Test
  void deleteAllPass() throws DataAccessException {
    ResultSet rs = null;
    Event eventSample2 = new Event ("eventID2","SeanPark","personID1", 12.34f,56.78f,
            "Korea","Seoul","Birth",1997);
    boolean isEmpty = false;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);

      eDAO.create(eventSample);
      eDAO.create(eventSample2);
      eDAO.deleteAll();

      try (Statement stmt = conn.createStatement()) {
        String sql = "SELECT * FROM Events";
        rs = stmt.executeQuery(sql);
        if (!rs.next()) {
          isEmpty = true;
        }
      } catch (SQLException e) {
        throw new DataAccessException("SQL Error encountered while querying Events table");
      }

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isEmpty);
  }
}