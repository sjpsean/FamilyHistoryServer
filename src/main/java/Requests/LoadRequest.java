package Requests;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * Request for LoadService
 */
public class LoadRequest {
  private User[] users;
  private Person[] persons;
  private Event[] events;

  /**
   * All users, persons, events are using User, Person, Event model class
   * @param users array of Users
   * @param persons array of Persons
   * @param events array of Events
   */
  public LoadRequest(User[] users, Person[] persons, Event[] events) {
    this.users=users;
    this.persons=persons;
    this.events=events;
  }

  public User[] getUsers() {
    return users;
  }
  public void setUsers(User[] users) {
    this.users=users;
  }

  public Person[] getPersons() {
    return persons;
  }
  public void setPersons(Person[] persons) {
    this.persons=persons;
  }

  public Event[] getEvents() {
    return events;
  }
  public void setEvents(Event[] events) {
    this.events=events;
  }
}
