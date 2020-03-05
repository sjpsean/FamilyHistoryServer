package Dao;

import Model.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object for Persons table
 */
public class PersonsDAO {
  private final Connection conn;

  public PersonsDAO(Connection conn) {
    this.conn=conn;
  }

  /**
   * Create a person in Person table.
   * @param person Person variable with all the information of a person.
   */
  public void create(Person person) throws DataAccessException {
    String sql = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, " +
            "Gender, FatherID, MotherID, SpouseID)" +
            "VALUES(?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, person.getPersonID());
      stmt.setString(2, person.getAssociatedUsername());
      stmt.setString(3, person.getFirstName());
      stmt.setString(4, person.getLastName());
      stmt.setString(5, person.getGender());
      stmt.setString(6, person.getFatherID());
      stmt.setString(7, person.getMotherID());
      stmt.setString(8, person.getSpouseID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting a Person into the Persons table");
    }
  }

  /**
   * Get Person that has a specific personID.
   * @param personID unique string
   * @return single Person object
   */
  public Person getPersonByID(String personID) throws DataAccessException {
    Person person;
    ResultSet rs = null;
    String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, personID);
      rs = stmt.executeQuery();
      if (rs.next()) {
        person = new Person(rs.getString("AssociatedUsername"), rs.getString("PersonID"),
                rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
        return person;
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while finding a Person with a personID");
    }
    return null;
  }

  /**
   * Get Persons that has a specific username.
   * @param userName unique string
   * @return Array of Persons
   */
  public Person[] getPersonsByUserName(String userName) throws DataAccessException {
    ResultSet rs = null;
    ArrayList<Person> personsList = new ArrayList<>();
    Person[] persons = null;
    Person person;
    String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, userName);
      rs = stmt.executeQuery();
      while (rs.next()) {
        person = new Person(rs.getString("AssociatedUsername"), rs.getString("PersonID"),
                rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
        personsList.add(person);
      }

      if (personsList.isEmpty()) return null;

      persons = new Person[personsList.size()];
      for (int i = 0; i < personsList.size(); i++) {
        persons[i] = personsList.get(i);
      }
      return persons;
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while getting persons by username");
    }
  }

  /**
   * delete all persons related to this userName
   * @param userName user's name :String
   */
  public void deleteByUserName(String userName) throws DataAccessException {
    String sql = "DELETE FROM Persons WHERE AssociatedUsername = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1,userName);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while deleting Persons by Associated Username");
    }
  }

  /**
   * delete every data in the table.
   */
  public void deleteAll() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "DELETE FROM Persons";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while clearing Persons table");
    }
  }
}
