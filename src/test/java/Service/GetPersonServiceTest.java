package Service;

import Dao.*;
import Model.*;
import Requests.PersonRequest;
import Response.PersonResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class GetPersonServiceTest {
  private Database db;
  private Person personSample;
  private PersonResponse personRes;
  private PersonRequest personReq;

  @BeforeEach
  void setUp() throws Exception {
    db = new Database();

    // create user, token and a person for one user.
    User userSample = new User("Sean", "seanpassword", "seanpark@gmail.com",
            "Sean", "Park", "m", "personid123");
    AuthToken tokenSample = new AuthToken("token123", "Sean", "seanpassword");
    personSample = new Person("Sean", "personid123", "Sean", "Park", "m", null, null, null);
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      PersonsDAO pDAO = new PersonsDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);
      uDAO.create(userSample);
      pDAO.create(personSample);
      aDAO.create(tokenSample);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
  }

  @AfterEach
  void tearDown() throws Exception {
    db.openConnection();
    db.clearTables();
    db.closeConnection(true);
  }

  @Test
  void getPersonByIDPass() throws Exception {
    personRes = null;
    personReq = new PersonRequest("personid123", "token123");
    GetPersonService getPerson = new GetPersonService(personReq);
    personRes = getPerson.getPersonByID();

    assertNotNull(personRes);
    assertTrue(personRes.isSuccess());
    assertNotNull(personRes.getPerson());
    assertEquals(personRes.getPerson().getPersonID(), personReq.getPersonID());
  }

  @Test
  void getPersonByIDFail() throws Exception {
    personRes = null;
    personReq = new PersonRequest("randompersonID", "token123");
    GetPersonService getPerson = new GetPersonService(personReq);
    personRes = getPerson.getPersonByID();

    assertNotNull(personRes);
    assertFalse(personRes.isSuccess());
    assertNull(personRes.getPerson());

    personRes = null;
    personReq = new PersonRequest("personid123", "randomtoken");
    getPerson = new GetPersonService(personReq);
    personRes = getPerson.getPersonByID();

    assertNotNull(personRes);
    assertFalse(personRes.isSuccess());
    assertNull(personRes.getPerson());
  }

  @Test
  void getAllPersonsPass() throws Exception {
    personRes = null;
    personReq = new PersonRequest("token123");
    GetPersonService getPerson = new GetPersonService(personReq);
    personRes = getPerson.getAllPersons();

    assertNotNull(personRes);
    assertNotNull(personRes.getPersonsList());
    assertEquals(personRes.getPersonsList()[0].getPersonID(), personSample.getPersonID());
  }

  @Test
  void getAllPersonsFail() throws Exception {
    personRes = null;
    personReq = new PersonRequest("randomtoken");
    GetPersonService getPerson = new GetPersonService(personReq);
    personRes = getPerson.getAllPersons();

    assertNotNull(personRes);
    assertFalse(personRes.isSuccess());
    assertNull(personRes.getPersonsList());
  }
}