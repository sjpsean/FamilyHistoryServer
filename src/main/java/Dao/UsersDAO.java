package Dao;

import Model.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object for Users table.
 */
public class UsersDAO {
  private final Connection conn;

  public UsersDAO(Connection conn) {
    this.conn = conn;
  }

  /**
   * insert a new user in to the Users table.
   * @param user User object with all data needed to insert a user
   */
  public void create(User user) throws DataAccessException {
    String sql = "INSERT INTO Users (UserName, Password, Email, " +
            "FirstName, LastName, Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)){
      stmt.setString(1, user.getUserName());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getFirstName());
      stmt.setString(5, user.getLastName());
      stmt.setString(6, user.getGender());
      stmt.setString(7, user.getPersonID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting a User into the Users table");
    }
  }

  public User getUserByName(String userName) throws DataAccessException {
    User user;
    ResultSet rs = null;
    String sql = "SELECT * FROM Users WHERE UserName = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, userName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        user = new User(rs.getString("UserName"), rs.getString("Password"), rs.getString("Email"),
                rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                rs.getString("PersonID"));
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding a user");
    }
    return null;
  }

  /**
   * Get personID assigned to a user
   * @param userName unique string to find which user to use
   * @return personID of the user
   */
  public String getPersonIDByUserName(String userName) throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT * FROM Users WHERE UserName = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, userName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getString("PersonID");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding personID");
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  /**
   * delete every data in the table.
   */
  public void deleteAll() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "DELETE FROM Users;";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while clearing Users table");
    }
  }

  /**
   * check if the input userName is registered in the Users table.
   * @param userName unique userName for the user
   * @return true if it can find the name, false if not
   */
  public boolean isRegistered(String userName) throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT * FROM Users WHERE UserName = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, userName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding personID");
    }
//    finally {
//      if(rs != null) {
//        try {
//          rs.close();
//        } catch (SQLException e) {
//          e.printStackTrace();
//        }
//      }
//    }
    return false;
  }
}
