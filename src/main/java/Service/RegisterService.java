package Service;

import Dao.AuthTokenDAO;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Requests.RegisterRequest;
import Response.RegisterResponse;

/**
 * <pre>
 *  Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 *  Auth Token Required: NO
 * </pre>
 */
public class RegisterService {
  private RegisterRequest regReq;

  /**
   * Receive register request data from the user and process registration to return response.
   * @param regReq RegisterRequest came from RequestHandler
   */
  public RegisterService(RegisterRequest regReq) {
    this.regReq=regReq;
  }


  /**
   * Calls all the functions needed to register a user and returns RegisterResponse.
   * if something goes wrong, throw an error
   * @return RegisterResponse
   */
  public RegisterResponse registerUser() {
    return null;
  }

  /**
   * if the request missing or have invalid value, username already taken by another user, internal server error,
   * throws the right exception if it is not valid.
   * @param regReq registerRequest
   */
  public void checkValid(RegisterRequest regReq) {
    throw null;
  }

  /**
   * Generate unique token for the user.
   * @return generated string of token
   */
  private String generateToken() {
    return null;
  }

  /**
   * Fill in User object to use it in calling UsersDAO.
   * @return User object filled with information needed
   */
  private User makeUserObject() {
    return null;
  }

  /**
   * Fill in AuthToken object for to use it in calling AuthTokenDAO.
   * @return AuthToken object filled with information needed
   */
  private AuthToken makeAuthToken() {
    return null;
  }

  /**
   * use UsersDAO to create a user in the table.
   * @param user the User object that has been created by makeUserObject()
   */
  private void createUser (User user) {
//    UsersDAO usersDAO = new UsersDAO();
//    usersDAO.create(user);
  }

  /**
   * use AuthTokenDAO to create a authToken in the table.
   * @param authToken the AuthToken object that has been created by makeAuthToken()
   */
  private void createAuthToken (AuthToken authToken) {
//    AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
//    authTokenDAO.create(authToken);
  }

  /**
   * use FillService class to fill 4 generations of ancestor data to this user.
   * returns Person object with the personID information that we need for the user.
   * @param userName string value from regReq.getUserName()
   * @return Person object of the user
   */
  private Person fillGenerations(String userName) {
    // call FillService with a string from regReq.getUserName();
    return null;
  }

}
