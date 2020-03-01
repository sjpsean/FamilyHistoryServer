package Dao;

import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDAOTest {
  private  Database db;
  private AuthToken tokenSample;

  @BeforeEach
  void setUp() throws Exception {
    db = new Database();
    tokenSample = new AuthToken("abcde12345",
            "Sean", "password");
  }

  @AfterEach
  void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  void createPass() throws Exception {
    AuthToken compareToken = null;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      aDAO.create(tokenSample);
      compareToken = aDAO.getTokenByUserName(tokenSample.getAssociatedUsername());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(compareToken);
    assertEquals(tokenSample, compareToken);
  }

  @Test
  void createFail() throws Exception {
    boolean success = true;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      aDAO.create(tokenSample);
      aDAO.create(tokenSample);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      success = false;
    }

    assertFalse(success);
  }

  @Test
  void getTokenByUserNamePass() throws Exception {
    AuthToken compareToken = null;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      aDAO.create(tokenSample);
      compareToken = aDAO.getTokenByUserName(tokenSample.getAssociatedUsername());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNotNull(compareToken);
    assertEquals(tokenSample, compareToken);
  }

  @Test
  void getTokenByUserNameFail() throws Exception {
    AuthToken compareToken = tokenSample;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      aDAO.create(tokenSample);
      compareToken = aDAO.getTokenByUserName("notSean");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(compareToken);
  }

  @Test
  void deleteByUserNamePass() throws Exception {
    AuthToken tokenSample2 = new AuthToken("abcd1234", "Sean", "password");
    AuthToken tokenSample3 = new AuthToken("abc123", "Seany", "password");
    AuthToken seanToken = tokenSample;
    AuthToken seanyToken = null;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      aDAO.create(tokenSample); // Sean
      aDAO.create(tokenSample2); // Sean
      aDAO.create(tokenSample3); // Seany
      aDAO.deleteByUserName(tokenSample.getAssociatedUsername());

      seanToken = aDAO.getTokenByUserName("Sean");
      seanyToken = aDAO.getTokenByUserName("Seany");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertNull(seanToken);
    assertNotNull(seanyToken);
  }

  @Test
  void isRegisteredPass() throws Exception {
    boolean isReg = false;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      aDAO.create(tokenSample);
      isReg = aDAO.isRegistered(tokenSample.getAuthToken());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isReg);
  }

  @Test
  void isRegisteredFail() throws Exception {
    boolean isReg = true;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      aDAO.create(tokenSample);
      isReg = aDAO.isRegistered("notExistingToken");
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertFalse(isReg);
  }

  @Test
  void deleteAll() throws Exception {
    ResultSet rs = null;
    boolean isEmpty = false;
    AuthToken tokenSample2 = new AuthToken("abcd1234", "Sean", "password");
    AuthToken tokenSample3 = new AuthToken("abc123", "Seany", "password");
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      aDAO.create(tokenSample);
      aDAO.create(tokenSample2);
      aDAO.create(tokenSample3);
      aDAO.deleteAll();

      try (Statement stmt = conn.createStatement()) {
        String sql = "SELECT * FROM AuthTokens";
        rs = stmt.executeQuery(sql);
        if (!rs.next()) {
          isEmpty = true;
        }
      } catch (SQLException e) {
        throw new DataAccessException("SQL Error encountered while querying AuthTokens table");
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    assertTrue(isEmpty);
  }
}