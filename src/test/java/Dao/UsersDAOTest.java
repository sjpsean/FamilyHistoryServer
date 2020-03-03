package Dao;

import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class UsersDAOTest {
  private Database db;
  private User userSample;

  @BeforeEach
  public void setUp() throws Exception {
    db = new Database();
    userSample = new User("Sean", "seanpassword", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
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
    User compareUser = null;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      // create a User and try to find that user using username.
      uDAO.create(userSample);
      compareUser = uDAO.getUserByName(userSample.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    // check compareUser is not null and equals to userSample.
    assertNotNull(compareUser);
    assertEquals(userSample, compareUser);
  }

  // Testing create function if it fails.
  @Test
  void createFail() throws Exception {
    boolean isCreated = true;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      // create two same users and see if it returns DataAccessException.
      uDAO.create(userSample);
      uDAO.create(userSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      isCreated = false;
    }

    assertFalse(isCreated);

    User compareUser = null;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      // get a user with a userID that is not in the database.
      compareUser = uDAO.getUserByName(userSample.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(compareUser);
  }

  //Testing getUsers function if it successfully returns a User with a specific userName.
  @Test
  void getUserByNamePass() throws Exception {
    // check if it successfully returns the right User.
    User compareUser = null;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      uDAO.create(userSample);

      compareUser = uDAO.getUserByName(userSample.getUserName());

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertEquals(compareUser, userSample);

    // Check if two different userName will get different Users.
    User userSample2 = new User("Seany", "seanpassword", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
    User compareUserSample = null;
    User compareUserSample2 = null;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      uDAO.create(userSample2);
      compareUserSample = uDAO.getUserByName(userSample.getUserName());
      compareUserSample2 = uDAO.getUserByName(userSample2.getUserName());

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }


    assertNotNull(compareUserSample);
    assertNotNull(compareUserSample2);
    assertNotEquals(compareUserSample2, compareUserSample);  // two different UserName has to return two different Users.
  }

  //Testing getUsers function if it fails to return a User when a User is not found.
  @Test
  void getUserByNameFail() throws Exception {
    boolean found = true;
    User compareUser = userSample;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      uDAO.create(userSample);
      compareUser = uDAO.getUserByName("randomName");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(compareUser);
  }
  // Testing deleteAll function if it passes.
  @Test
  void deleteAllPass() throws Exception {
    ResultSet rs = null;
    boolean isEmpty = false;
    User userSample2 = new User("Seany", "seanpassword", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      // Create two new Users before deleting all and execute deleteAll function.
      uDAO.create(userSample);
      uDAO.create(userSample2);
      uDAO.deleteAll();

      // try querying all users from the table after calling deleteAll;
      // if there is nothing in the ResultSet, than it is empty.
      try (Statement stmt = conn.createStatement()) {
        String sql = "SELECT * FROM Users";
        rs = stmt.executeQuery(sql);
        if (!rs.next()) {
          isEmpty = true;
        }
      } catch (SQLException e) {
        throw new DataAccessException("SQL Error encountered while querying Users table");
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isEmpty);
  }

  @Test
  void isRegisteredPass() throws DataAccessException {
    boolean isRegistered = false;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      uDAO.create(userSample);
      isRegistered = uDAO.isRegistered(userSample.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isRegistered);
  }

  @Test
  void isRegisteredFail() throws DataAccessException {
    boolean isRegistered = true;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);

      uDAO.create(userSample);
      isRegistered = uDAO.isRegistered("notRegisteredName");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertFalse(isRegistered);
  }
}