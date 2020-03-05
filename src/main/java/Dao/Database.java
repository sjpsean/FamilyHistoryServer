package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
  private Connection conn;

  public Connection openConnection() throws DataAccessException{
    try {
      final String CONNECTION_URL = "jdbc:sqlite:FMS.db";
      conn = DriverManager.getConnection(CONNECTION_URL);
      conn.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Unable to open connection to database");
    }
    return conn;
  }

  public Connection getConnection() throws DataAccessException {
    if(conn == null) {
      return openConnection();
    } else {
      return conn;
    }
  }

  public void closeConnection(boolean commit) throws DataAccessException {
    try {
      if (commit) {
        //This will commit the changes to the database
        conn.commit();
      } else {
        //If we find out something went wrong, pass a false into closeConnection and this
        //will rollback any changes we made during this connection
        conn.rollback();
      }
      conn.close();
      conn = null;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error: Unable to close database connection");
    }
  }

  public void clearTables() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "DELETE FROM Users; DELETE FROM Events; DELETE FROM Persons; DELETE FROM AuthTokens;";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while clearing tables");
    }
  }

}
