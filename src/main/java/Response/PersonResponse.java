package Response;

import Model.Person;

public class PersonResponse extends MessageResponse {
  Person[] personsList = null;
  Person person = null;

  PersonResponse(Person[] personsList) {
    super("", true);
    this.personsList = personsList;
  }

  PersonResponse(Person person) {
    super("", true);
    this.person = person;
  }

  PersonResponse(String message, boolean success) {
    super(message, success);
  }

  public Person[] getPersonsList() {
    return personsList;
  }

  public Person getPerson() {
    return person;
  }

}
