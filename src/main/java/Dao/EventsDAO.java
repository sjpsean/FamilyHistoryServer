package Dao;

import Model.Event;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object for Events table.
 */
public class EventsDAO {
  private final Connection conn;

  public EventsDAO(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create an event in Events table.
   * @param event contains all data needed to create a row in the table
   */
  public void create(Event event) throws DataAccessException {
    String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
            "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      //Using the statements built-in set(type) functions we can pick the question mark we want
      //to fill in and give it a proper value. The first argument corresponds to the first
      //question mark found in our sql String
      stmt.setString(1, event.getEventID());
      stmt.setString(2, event.getAssociatedUsername());
      stmt.setString(3, event.getPersonID());
      stmt.setDouble(4, event.getLatitude());
      stmt.setDouble(5, event.getLongitude());
      stmt.setString(6, event.getCountry());
      stmt.setString(7, event.getCity());
      stmt.setString(8, event.getEventType());
      stmt.setInt(9, event.getYear());

      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while inserting Event into the Events table");
    }
  }

  /**
   * Get Event that has a specific eventID.
   * @param eventID event's ID
   * @return single Event object
   */
  public Event getEventByID(String eventID) throws DataAccessException{
    Event event;
    ResultSet rs = null;
    String sql = "SELECT * FROM Events WHERE EventID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1,eventID);
      rs = stmt.executeQuery();
      if (rs.next()) {
        event = new Event (rs.getString("EventID"), rs.getString("AssociatedUsername"),
                rs.getString("PersonID"), rs.getFloat("Latitude"),rs.getFloat("Longitude"),
                rs.getString("Country"), rs.getString("City"),
                rs.getString("EventType"), rs.getInt("Year"));
        return event;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding an event");
    }

    return null;
  }

  /**
   * Get Events that contains a specific username.
   * @param personID unique string
   * @return Array of Event
   */
  public Event[] getEventsByPersonID(String personID) throws DataAccessException{
    ResultSet rs = null;
    ArrayList<Event> eventsList = new ArrayList<Event>();
    Event[] events = null;
    Event event;
    String sql = "SELECT * FROM Events WHERE PersonID = ?;";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, personID);
      rs = stmt.executeQuery();
      while (rs.next()) {
        event = new Event (rs.getString("EventID"), rs.getString("AssociatedUsername"),
                rs.getString("PersonID"), rs.getFloat("Latitude"),rs.getFloat("Longitude"),
                rs.getString("Country"), rs.getString("City"),
                rs.getString("EventType"), rs.getInt("Year"));
        eventsList.add(event);
      }

      if (eventsList.isEmpty()) return null;

      events = new Event[eventsList.size()];
      for (int i = 0; i < eventsList.size(); i++) {
        events[i] = eventsList.get(i);
//        System.out.println(events[i].toString());
      }
      return events;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("SQL Error encountered while getting events by username");
    }
  }

  public Event[] getEventsByUsername(String username) throws DataAccessException{
    ResultSet rs = null;
    ArrayList<Event> eventsList = new ArrayList<Event>();
    Event[] events = null;
    Event event;
    String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      while (rs.next()) {
        event = new Event (rs.getString("EventID"), rs.getString("AssociatedUsername"),
                rs.getString("PersonID"), rs.getFloat("Latitude"),rs.getFloat("Longitude"),
                rs.getString("Country"), rs.getString("City"),
                rs.getString("EventType"), rs.getInt("Year"));
        eventsList.add(event);
      }

      if (eventsList.isEmpty()) return null;

      events = new Event[eventsList.size()];
      for (int i = 0; i < eventsList.size(); i++) {
        events[i] = eventsList.get(i);
      }
      return events;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("SQL Error encountered while getting events by username");
    }
  }

  /**
   * delete all events related to this userName
   * @param userName user's name :String
   */
  public void deleteByUserName(String userName) throws DataAccessException {
    String sql = "DELETE FROM Events WHERE AssociatedUsername = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1,userName);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while deleting Events by Associated Username");
    }
  }

  /**
   * delete every data in the table.
   */
  public void deleteAll() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "DELETE FROM Events;";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while clearing Events table");
    }
  }
}
