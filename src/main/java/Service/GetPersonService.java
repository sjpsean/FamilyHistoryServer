package Service;

import Dao.*;
import Requests.PersonRequest;
import Response.PersonResponse;
import Model.Person;

import java.sql.Connection;

/**
 * <pre>
 *  Returns the single Person object that has a specific ID.
 *  AuthToken Required: Yes
 * </pre>
 */
public class GetPersonService {
  private PersonRequest personReq;
  private PersonResponse personRes;
  private Database db;

  public GetPersonService(PersonRequest personReq) {
    this.personReq = personReq;
    db = new Database();
  }

  /**
   * User PersonsDAO to find Persons with the personID.
   * @return Person
   */
  public PersonResponse getPersonByID() throws DataAccessException {
    // first check if the token is valid.
    if (!isValidToken(personReq.getToken())) {
      personRes = new PersonResponse("Error: token is not valid", false);
      return personRes;
    }

    // get all persons with that personID.
    Person[] persons = null;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      persons = pDAO.getPersonsByUserName(aDAO.getUsernameByToken(personReq.getToken()));
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    // next, get person by personID.
    Person person = null;
    if (persons != null) {
      for (Person p : persons) {
        if (p.getPersonID().equals(personReq.getPersonID())) person = p;
      }
    }
    // if there is no person for the personID, return Error: person not found.
    if (person == null) personRes = new PersonResponse("Error: person with this personID is not found", false);
    else personRes = new PersonResponse(person);
    return personRes;
  }

  /**
   * Find userName with AuthTokensDAO.
   * Return all Persons containing the username using PersonsDAO.
   * @return Array of Person objects
   */
  public PersonResponse getAllPersons() throws DataAccessException {
    // first, check if the token is valid.
    if (!isValidToken(personReq.getToken())) {
      personRes = new PersonResponse("Error: token is not valid", false);
      return personRes;
    }

    // get all persons with that personID.
    Person[] persons = null;
    try {
      Connection conn = db.openConnection();
      PersonsDAO pDAO = new PersonsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      persons = pDAO.getPersonsByUserName(aDAO.getUsernameByToken(personReq.getToken()));
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    if (persons == null) personRes = new PersonResponse("Error: person not found", false);
    else personRes = new PersonResponse(persons);
    return personRes;
  }

  private boolean isValidToken (String token) throws DataAccessException {
    boolean isRegistered = false;
    if (token == null) {
      return false;
    }
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      String username = aDAO.getUsernameByToken(token);

      UsersDAO uDAO = new UsersDAO(conn);
      isRegistered = uDAO.isRegistered(username);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    return isRegistered;
  }
}
