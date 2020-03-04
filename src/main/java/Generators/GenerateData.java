package Generators;

import Handlers.Convert.JsonToObject;
import Handlers.ReadWrite.ReadString;
import Model.Event;
import Model.Person;
import Model.User;

import java.io.*;
import java.util.Random;

public class GenerateData {
  String[] femaleName;
  String[] maleName;
  String[] surName;
  Locations[] location;

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
  public Event generateOneEvent(Person person, String eventType) {
    int num = new Random().nextInt(location.length);
    int year = (int)(2010 - (Math.random() * 90));

    return new Event(GenerateID.generateEventID(), person.getAssociatedUsername(), person.getPersonID(),
            location[num].getLatitude(), location[num].getLongitude(), location[num].getCountry(),
            location[num].getCity(), eventType, year);
  }

  public Event generateBirthEvent(Person person) {
    int num = new Random().nextInt(location.length);
    int year = (int)(2010 - (Math.random() * 90));

    return new Event(GenerateID.generateEventID(), person.getAssociatedUsername(), person.getPersonID(),
            location[num].getLatitude(), location[num].getLongitude(), location[num].getCountry(),
            location[num].getCity(), "Birth", year);
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

  public Locations[] getLocation() {
    return location;
  }

  private int getBirthYear(Person person) {
    return 0;
  }
}