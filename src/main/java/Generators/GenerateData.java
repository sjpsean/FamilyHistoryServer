package Generators;

import Dao.DataAccessException;
import Dao.Database;
import Dao.EventsDAO;
import Handlers.Convert.JsonToObject;
import Handlers.ReadWrite.ReadString;
import Model.Event;
import Model.Person;

import java.io.*;
import java.sql.Connection;
import java.util.Random;

public class GenerateData {
  String[] femaleName;
  String[] maleName;
  String[] surName;
  Locations[] location;
  private Database db;
  // Constructor?


  public GenerateData() throws IOException {
    File femaleNamesFile = new File("json/fnames.json");
    InputStream readNames = new FileInputStream(femaleNamesFile);
    StringBuilder femaleNamesString = new StringBuilder(ReadString.rs(readNames));
    String femaleData = femaleNamesString.substring(femaleNamesString.indexOf("["),femaleNamesString.indexOf("]")+1);
    femaleName = JsonToObject.jsonToObject(femaleData, String[].class);

    File maleNamesFile = new File("json/mnames.json");
    readNames = new FileInputStream(maleNamesFile);
    StringBuilder maleNameString = new StringBuilder((ReadString.rs(readNames)));
    String maleData = maleNameString.substring(maleNameString.indexOf("["),maleNameString.indexOf("]")+1);
    maleName = JsonToObject.jsonToObject(maleData,String[].class);

    File surnamesFile = new File("json/snames.json");
    readNames = new FileInputStream(surnamesFile);
    StringBuilder surnameString = new StringBuilder((ReadString.rs(readNames)));
    String surnameData = surnameString.substring(surnameString.indexOf("["),surnameString.indexOf("]")+1);
    surName = JsonToObject.jsonToObject(surnameData,String[].class);

    File locationsFile = new File("json/locations.json");
    readNames = new FileInputStream(locationsFile);
    StringBuilder locationString = new StringBuilder((ReadString.rs(readNames)));
    String locationData = locationString.substring(locationString.indexOf("["),locationString.indexOf("]")+1);
    location = JsonToObject.jsonToObject(locationData, Locations[].class);

    db = new Database();
  }


  /*
  each user must have at least
  1. birth

  each person must have at least three events
  1. birth
    - parents should be born at least 13 years before child is born.
  2. marriage
    - reasonable age. (18?)
  3. death
    - not die before child's birth.
    - before 120 years old. (less then birthYear + 120)

  location can be anywhere. Possibly closer to family members or birth place but not required.

  My code needs to account for any possible event type.

   */
  public Event generateOneEvent(Person person, String eventType) throws DataAccessException {
    int myBirthYear = 1000;
    int myDeathYear = 1000;
    int year;

    // get birth year for this person.
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      Event[] myEvents = eDAO.getEventsByPersonID(person.getPersonID());
      for (Event myEvent : myEvents) {
        if (myEvent.getEventType().equals("Birth")) myBirthYear = myEvent.getYear();
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    // if event is Death, use birth year to give them valid year to die.
    if (eventType.equals("Death")) {
      assert myBirthYear != 1000;
      int num = new Random().nextInt(70);
      year = myBirthYear + 50 + num; // they lived at least 50 years and + 0-70 years.
    } // if event is Marriage, use birth and death year to make
    else if (eventType.equals("Marriage")) {
      try {
        Connection conn = db.openConnection();
        EventsDAO eDAO = new EventsDAO(conn);
        Event[] myEvents = eDAO.getEventsByPersonID(person.getPersonID());
        for (Event myEvent : myEvents) {
          if (myEvent.getEventType().equals("Death")) myDeathYear = myEvent.getYear();
        }
        db.closeConnection(true);
      } catch (DataAccessException e) {
        db.closeConnection(false);
      }

      assert myDeathYear != 1000;
      int num = new Random().nextInt(myDeathYear - myBirthYear - 18);
      year = myBirthYear + 18 + num;
    }
    else {
      int num = new Random().nextInt(myDeathYear - myBirthYear);
      year = myBirthYear + num;
    }

    int idx = new Random().nextInt(location.length);

    return new Event(GenerateID.generateEventID(), person.getAssociatedUsername(), person.getPersonID(),
            location[idx].getLatitude(), location[idx].getLongitude(), location[idx].getCountry(),
            location[idx].getCity(), eventType, year);
  }

  public Event generateBirthEvent(Person person, Person child) throws DataAccessException {
    int childBirthYear = 2010;
    try {
      Connection conn = db.openConnection();
      EventsDAO eDAO = new EventsDAO(conn);
      Event[] childEvents = eDAO.getEventsByPersonID(child.getPersonID());
      Event childBirth = null;
      for (Event childEvent : childEvents) {
        if (childEvent.getEventType().equals("Birth")) childBirth = childEvent;
      }
      assert childBirth != null;
      childBirthYear = childBirth.getYear();
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    int numYear = new Random().nextInt(20);
    int year = childBirthYear - 18 - numYear;

    int idx = new Random().nextInt(location.length);
    return new Event(GenerateID.generateEventID(), person.getAssociatedUsername(), person.getPersonID(),
            location[idx].getLatitude(), location[idx].getLongitude(), location[idx].getCountry(),
            location[idx].getCity(), "Birth", year);
  }

  public Event generateBirthEvent(Person person) {
    int year = (int)(2010 - (Math.random() * 90)); // at least 10 years old, at most 100.

    int idx = new Random().nextInt(location.length);
    return new Event(GenerateID.generateEventID(), person.getAssociatedUsername(), person.getPersonID(),
            location[idx].getLatitude(), location[idx].getLongitude(), location[idx].getCountry(),
            location[idx].getCity(), "Birth", year);
  }

  public String[] getFemaleName() {
    return femaleName;
  }

  public String[] getMaleName() {
    return maleName;
  }

  public String[] getSurName() {
    return surName;
  }

}