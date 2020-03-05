package Response;

import Model.Person;

public class PersonResponse extends MessageResponse {
  Person[] data = null;
  Person person = null;

  public PersonResponse(Person[] personsList) {
    super(null, true);
    this.data = personsList;
  }

  public PersonResponse(Person person) {
    super(null, true);
    this.person = person;
  }

  public PersonResponse(String message, boolean success) {
    super(message, success);
  }

  public Person[] getPersonsList() {
    return data;
  }

  public Person getPerson() {
    return person;
  }

}
