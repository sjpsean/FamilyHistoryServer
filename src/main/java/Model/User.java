package Model;

import java.util.Objects;

public class User {
  private String userName;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String gender;
  private String personID;

  /**
   * <pre>
   *   Model for one User.
   *   Use this to transfer User information in the server.
   *   All the parameters are the columns from Users table in the database.
   * </pre>
   *
   * @param userName Unique user name (not null string)
   * @param password User's password (not null string)
   * @param email User's email address (not null string)
   * @param firstName User's first name (not null string)
   * @param lastName User's last name (not null string)
   * @param gender User's gender (f or m string)
   * @param personID Unique Person ID assigned to this user (not null string)
   */
  public User(String userName, String password, String email,
              String firstName, String lastName, String gender, String personID) {
    this.userName = userName;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.personID = personID;
  }

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getPersonID() {
    return personID;
  }
  public void setPersonID(String personID) {
    this.personID = personID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user=(User) o;
    return userName.equals(user.userName) &&
            password.equals(user.password) &&
            email.equals(user.email) &&
            firstName.equals(user.firstName) &&
            lastName.equals(user.lastName) &&
            gender.equals(user.gender) &&
            personID.equals(user.personID);
  }
}
