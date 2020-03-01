package Dao;

import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersonsDAOTest {
  private  Database db;
  private Person personSample;

  @BeforeEach
  public void setUp() throws Exception {
    db = new Database();
    personSample = new Person("person123ID", "Sean", "Sean", "Park", "m",
            "fatherParkID", "motherParkID", "SeanSpouseID");
  }

  @AfterEach
  public void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  // Testing create function if it passes.
  @Test
  void createPass() throws Exception {
    Person comparePerson = null;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      // create a person and try to find that person using personID.
      pDAO.create(personSample);
      comparePerson = pDAO.getPersonByID(personSample.getPersonID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    // check comparePerson is not null and equals to personSample.
    assertNotNull(comparePerson);
    assertEquals(personSample, comparePerson);
  }

  // Testing create function if it fails.
  @Test
  void createFail() throws Exception {
    boolean isCreated = true;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      // create two same persons and see if it returns DataAccessException.
      pDAO.create(personSample);
      pDAO.create(personSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      isCreated = false;
    }

    assertFalse(isCreated);

    Person comparePerson = null;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      // Try to get a person with a personID that is not in the database.
      comparePerson = pDAO.getPersonByID(personSample.getPersonID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(comparePerson);
  }

  //Testing getPersonByID function if it successfully returns a Person with a specific personID.
  @Test
  void getPersonByIDPass() throws Exception {
    // check if it successfully returns the right Person.
    boolean isEqual = false;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      // create a person
      pDAO.create(personSample);
      // assign comparePerson to return value of getPersonByID with the personID of personSample.
      Person comparePerson = pDAO.getPersonByID(personSample.getPersonID());
      if (comparePerson.equals(personSample)) {
        isEqual = true;
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isEqual);

    // check if two different personID will get different Persons.
    Person personSample2 = new Person("person456ID", "Sez", "Sez", "Park", "m",
            "faParkID", "moParkID", "SeSpouseID");
    isEqual = false;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      // create one more person in the table and get both of them using different ID.
      pDAO.create(personSample2);
      Person comparePersonSample = pDAO.getPersonByID(personSample.getPersonID());
      Person comparePersonSample2 = pDAO.getPersonByID(personSample2.getPersonID());
      // check if they both works and also they are different persons.
      assertNotNull(comparePersonSample);
      assertNotNull(comparePersonSample2);
      if (comparePersonSample.equals(comparePersonSample2)) {
        isEqual = true;
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertFalse(isEqual); // two different personID has to return two different Persons.
  }

  // Testing getPersonByID function if it fails to return when a Person is not found.
  @Test
  void getPersonByIDFail() throws Exception {
    boolean found = true;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      // create a person
      pDAO.create(personSample);
      // try to get a person using ID that would not return any person.
      Person comparePerson = pDAO.getPersonByID("randomID");
      if (comparePerson == null) {
        found = false;
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertFalse(found);
  }


  @Test
  void getPersonsByUserNamePass() throws DataAccessException {
    Person personSample2 = new Person("person456ID", "Sean", "Sez", "Park", "m",
            "faParkID", "moParkID", "SeSpouseID");
    Person personSample3 = new Person("person789ID", "Sez", "Sez", "Park", "m",
            "faParkID", "moParkID", "SeSpouseID");
    Person[] comparePerson = null;

    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      pDAO.create(personSample);
      pDAO.create(personSample2);
      pDAO.create(personSample3);
      comparePerson = pDAO.getPersonsByUserName(personSample.getAssociatedUsername());

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(comparePerson);
    assertEquals(comparePerson.length, 2);
    assertEquals(comparePerson[0], personSample);
    assertEquals(comparePerson[1], personSample2);

  }

  @Test
  void deleteByUserNamePass() throws DataAccessException {
    Person personSample2 = new Person("person456ID", "Sean", "Sez", "Park", "m",
            "faParkID", "moParkID", "SeSpouseID");
    Person personSample3 = new Person("person789ID", "Sez", "Sez", "Park", "m",
            "faParkID", "moParkID", "SeSpouseID");
    Person[] comparePerson = null;
    Person[] notDeletedPerson = null;

    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      pDAO.create(personSample);
      pDAO.create(personSample2);
      pDAO.create(personSample3);
      pDAO.deleteByUserName(personSample.getAssociatedUsername());
      comparePerson = pDAO.getPersonsByUserName(personSample.getAssociatedUsername());
      notDeletedPerson = pDAO.getPersonsByUserName(personSample3.getAssociatedUsername());

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(comparePerson);
    assertNotNull(notDeletedPerson);
    assertEquals(notDeletedPerson[0], personSample3);
  }

  // Testing deleteAll function if it doesn't leave any data in the table after the execution.
  @Test
  void deleteAllPass() throws Exception {
    ResultSet rs = null;
    boolean isEmpty = false;
    Person personSample2 = new Person("person456ID", "Sez", "Sez", "Park", "m",
            "faParkID", "moParkID", "SeSpouseID");
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);

      // create two persons and delete them all.
      pDAO.create(personSample);
      pDAO.create(personSample2);
      pDAO.deleteAll();

      // get all Persons from the table and see if it returns empty ResultSet.
      try (Statement stmt = conn.createStatement()) {
        String sql = "SELECT * FROM Persons";
        rs = stmt.executeQuery(sql);
        if (!rs.next()) {
          isEmpty = true;
        }
      } catch (SQLException e) {
        throw new DataAccessException("SQL Error encountered while querying Persons table");
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isEmpty);
  }
}
