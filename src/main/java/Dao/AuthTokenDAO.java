package Dao;

import Model.AuthToken;

import java.sql.*;

/**
 * Data Access Object for AuthToken table.
 */
public class AuthTokenDAO {
  private final Connection conn;

  public AuthTokenDAO(Connection conn) {
    this.conn = conn;
  }

  /**
   * insert an authToken to the table
   * @param authToken AuthToken model with all data needed to create a row
   */
  public void create(AuthToken authToken) throws DataAccessException {
    String sql = "INSERT INTO AuthTokens (AssociatedUsername, Password, Token) " +
            "VALUES(?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authToken.getAssociatedUsername());
      stmt.setString(2, authToken.getPassword());
      stmt.setString(3, authToken.getAuthToken());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting a AuthToken into the AuthTokens table");
    }
  }

  /**
   * Get token for the username.
   * @param userName unique string for the username
   * @return unique string token
   */
  public AuthToken getTokenByUserName (String userName) throws DataAccessException {
    AuthToken authToken;
    ResultSet rs = null;
    String sql = "SELECT * FROM AuthTokens WHERE AssociatedUsername = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, userName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        authToken = new AuthToken(rs.getString("Token"), rs.getString("AssociatedUsername"),
                rs.getString("Password"));
        return authToken;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding token for username");
    }
    return null;
  }

  public String getUsernameByToken (String token) throws DataAccessException {
    AuthToken authToken;
    ResultSet rs = null;
    String sql = "SELECT * FROM AuthTokens WHERE Token = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, token);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getString("AssociatedUsername");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding token for username");
    }
    return null;
  }

  /**
   * delete all tokens related to this userName
   * @param userName user's name :String
   */
  public void deleteByUserName(String userName) throws DataAccessException{
    String sql = "DELETE FROM AuthTokens WHERE AssociatedUsername = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1,userName);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while clearing tokens by username");
    }

  }

  /**
   * check if token is found in the table
   * @param token random unique string for a user
   * @return true if token is found in the table, false if not
   */
  public boolean isRegistered (String token) throws DataAccessException {
    boolean isRegistered = false;
    ResultSet rs = null;
    String sql = "SELECT * FROM AuthTokens WHERE Token = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, token);
      rs = stmt.executeQuery();
      if (rs.next()) {
        isRegistered =  rs.getString("Token").equals(token);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while checking if the token is valid");
    }
    return isRegistered;
  }

  /**
   * delete every data in the table.
   */
  public void deleteAll() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "DELETE FROM AuthTokens;";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while clearing AuthToken table");
    }
  }
}
