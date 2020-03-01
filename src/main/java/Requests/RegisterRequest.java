package Requests;

/**
 * Request for register service.
 */
public class RegisterRequest {
  private String userName;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String gender;

  /**
   * All variables are elements of the Json request from the web API
   * @param userName username of the user
   * @param password password that goes with the username
   * @param email email of the user
   * @param firstName firstName of the user
   * @param lastName lastName of the user
   * @param gender gender of the user 'm' or 'f'
   */
  public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
    this.userName = userName;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
  }

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName=userName;
  }

  public String getPassWord() {
    return password;
  }
  public void setPassWord(String passWord) {
    this.password=passWord;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email=email;
  }

  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName=firstName;
  }

  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName=lastName;
  }

  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender=gender;
  }
}

