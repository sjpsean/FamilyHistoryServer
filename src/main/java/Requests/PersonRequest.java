package Requests;

import Model.User;
import Response.PersonResponse;

public class PersonRequest {
  String personID;
  String token;

  public PersonRequest(String token) {
    this.token = token;
    personID = null;
  }

  public PersonRequest(String personID, String token) {
    this.personID = personID;
    this.token = token;
  }

  public String getPersonID() {
    return personID;
  }

  public String getToken() {
    return token;
  }
}
