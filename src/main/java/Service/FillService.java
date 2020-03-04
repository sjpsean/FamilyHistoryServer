package Service;

import Dao.*;
import Generators.GenerateData;
import Generators.GenerateID;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.FillRequest;
import Response.FillResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.Random;

/**
 * <pre>
 *   Populates the data with generated data for a user.
 *   If there is any data associated with the username, delete all of them.
 *   Generations parameter must be a non-negative integer (default is 4)
 * </pre>
 */
public class FillService {
  private FillRequest fillReq;
  private FillResponse fillRes;
  private Database db;
  private GenerateData randomData;
  private int currentGen;
  private Connection conn;
  private GenerateData getRandomData;
  private User user;

  private int numCreatedEvents;
  private int numCreatedPersons;

  /**
   * if there are two parameters with userName and generations, fill in that number of generation.
   * @param fillRequest unique userName and number of generations to fill
   */
  public FillService(FillRequest fillRequest) {
    fillReq = fillRequest;
    db = new Database();
  }

  /**
   * Call all the functions needed to execute fill request.
   * Use UsersDAO to check if the user is registered.
   * Use PersonsDAO, EventsDAO, AuthTokensDAO to delete and create new data.
   * use fillRequest that contains String userName and int generations (optional)
   * @return MessageResponse with a message indicating how many persons and events were added
   */
  public FillResponse fill() throws DataAccessException {
    Person userPerson;

    // first, prepare to fill, which includes :
    // check if the user is registered.
    // if the user is, delete all data associated with that username.
    // if not, throw an error with a message.
    fillPrepare();

    // now that everything is ready to go, let's make a person and an event for the user.
    try {
      conn = db.openConnection();
      String fatherID = GenerateID.generatePersonID();
      String motherID = GenerateID.generatePersonID();
      userPerson = new Person(user.getPersonID(), user.getUserName(), user.getFirstName(), user.getLastName(),
              user.getGender(), fatherID, motherID, null);

      PersonsDAO pDAO = new PersonsDAO(conn);
      pDAO.create(userPerson); numCreatedPersons++;

      // make birth event for me.
      // Event userEvent = randomData.generateBirthEvent(userPerson);

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      fillRes = new FillResponse(e.getMessage(), false);
      return fillRes;
    }

    try {
      getRandomData = new GenerateData();
      currentGen = 0;
      conn = db.openConnection();

      makeParents(userPerson);

      db.closeConnection(true);
      fillRes = new FillResponse(MessageBuilder(), true);
    } catch (DataAccessException | IOException e) {
      e.printStackTrace();
      db.closeConnection(false);
      fillRes = new FillResponse(e.getMessage(), false);
    }

    return fillRes;
  }

  private void makeParents(Person child) throws DataAccessException {
    // if currently working generation is equal to where I need to stop, stop the recursion.
    if (currentGen == fillReq.getGenerations()) return;

    int femaleNameIdx = new Random().nextInt(getRandomData.getFemaleName().length); // generate random idx for names.
    int surNameIdx = new Random().nextInt(getRandomData.getSurName().length); // generate random idx for names.
    int maleNameIdx = new Random().nextInt(getRandomData.getMaleName().length); // generate random idx for names.

    // Person (personID, associatedUsername, firstName, lastName, gender, motherID, fatherID, spouseID)
    Person mom = new Person (child.getMotherID(), fillReq.getUserName(),
            getRandomData.getFemaleName()[femaleNameIdx],             // random female name for mom
            getRandomData.getSurName()[surNameIdx], "f",      // random surname //need to match with spouses?
            GenerateID.generatePersonID(), GenerateID.generatePersonID(), child.getFatherID());

    Person dad = new Person (child.getFatherID(), fillReq.getUserName(),
            getRandomData.getMaleName()[maleNameIdx],             // random female name for mom
            child.getLastName(), "f",      // child's surname equals to dad's surname.
            GenerateID.generatePersonID(), GenerateID.generatePersonID(), child.getMotherID());

    PersonsDAO pDAO = new PersonsDAO(conn);
    pDAO.create(mom); numCreatedPersons++;
    pDAO.create(dad); numCreatedPersons++;
    currentGen++; // indicates which generation I am working on currently. Goes up when mom and dad is created.

    // make three events for mom and dad.
    makeEvents(mom, dad);
    // make Parents for mom and dad.
    makeParents(mom);
    makeParents(dad);
    currentGen--; // goes down one because it's returning to the generation before.
  }

  private void makeEvents(Person mom, Person dad) {
    // numCreatedEvents++; // each time creating an event, increase one.

  }


  private void fillPrepare() throws DataAccessException {
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      user = uDAO.getUserByName(fillReq.getUserName());
      if (user == null) {
        throw new DataAccessException("User not registered to fill");
      }
      PersonsDAO pDAO = new PersonsDAO(conn);
      EventsDAO eDAO = new EventsDAO(conn);
      pDAO.deleteByUserName(user.getUserName());
      eDAO.deleteByUserName(user.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
  }

  private String MessageBuilder () {
    StringBuilder respMessage = new StringBuilder();
    respMessage.append("Successfully added ");
//    respMessage.append((int)Math.pow(2, fillReq.getGenerations()+1) - 1);
    respMessage.append(numCreatedPersons);
    respMessage.append(" persons, and ");
//    respMessage.append(3 * ((int)Math.pow(2, fillReq.getGenerations()+1) - 1));
    respMessage.append(numCreatedEvents);
    respMessage.append(" events to the database.");
    return respMessage.toString();
  }

}
