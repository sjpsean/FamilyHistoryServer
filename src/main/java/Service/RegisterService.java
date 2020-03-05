package Service;

import Dao.AuthTokenDAO;
import Dao.DataAccessException;
import Dao.Database;
import Dao.UsersDAO;
import Generators.GenerateID;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Requests.FillRequest;
import Requests.RegisterRequest;
import Response.RegisterResponse;

import java.sql.Connection;

/**
 * <pre>
 *  Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 *  Auth Token Required: NO
 * </pre>
 */
public class RegisterService {
  private RegisterRequest regReq;
  private RegisterResponse regRes;
  private Database db;

  /**
   * Receive register request data from the user and process registration to return response.
   * @param regReq RegisterRequest came from RequestHandler
   */
  public RegisterService (RegisterRequest regReq) {
    this.regReq = regReq;
    db = new Database();
  }


  /**
   * Calls all the functions needed to register a user and returns RegisterResponse.
   * if something goes wrong, throw an error
   * @return RegisterResponse
   */
  public RegisterResponse registerUser() throws DataAccessException {
    boolean isRegistered = false;
    boolean success = false;
    String personID;
    String token = null;

    if (isRegistered() || !checkValid()) {
      return regRes;
    }

    personID = GenerateID.generatePersonID();
    token = GenerateID.generateToken();

    User newUser = new User(regReq.getUserName(), regReq.getPassWord(), regReq.getEmail(), regReq.getFirstName(), regReq.getLastName(), regReq.getGender(), personID);
    AuthToken newToken = new AuthToken(token, regReq.getUserName(), regReq.getPassWord());

    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      AuthTokenDAO aDAO = new AuthTokenDAO(conn);

      uDAO.create(newUser);
      aDAO.create(newToken);

      db.closeConnection(true);
      regRes = new RegisterResponse(token, regReq.getUserName(), personID);
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      regRes = new RegisterResponse(e.getMessage(), false);
    }

    fillGenerations(regReq.getUserName());

    return regRes;
  }

  private boolean isRegistered() throws DataAccessException {
    boolean isReg = false;
    try {
      Connection conn = db.openConnection();
      UsersDAO uDAO = new UsersDAO(conn);
      isReg = uDAO.isRegistered(regReq.getUserName());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      throw new DataAccessException(e.getMessage());
    }
    if (isReg) {
      regRes = new RegisterResponse("Error: This username is already registered in our server.", false);
    }
    return isReg;
  }

  /**
   * if the request missing or have invalid value, username already taken by another user, internal server error,
   * throws the right exception if it is not valid.
   */
  private boolean checkValid() {
    if (regReq.getUserName().isEmpty()) {
      regRes = new RegisterResponse("Error: invalid username", false);
      return false;
    }
    else if (regReq.getPassWord().isEmpty()) {
      regRes = new RegisterResponse("Error: invalid password", false);
      return false;
    }
    else if (regReq.getEmail().isEmpty()) {
      regRes = new RegisterResponse("Error: invalid email address", false);
      return false;
    }
    else if (regReq.getFirstName().isEmpty()) {
      regRes = new RegisterResponse("Error: invalid first name", false);
      return false;
    }
    else if (regReq.getLastName().isEmpty()) {
      regRes = new RegisterResponse("Error: invalid last name", false);
      return false;
    }
    else if (regReq.getGender().isEmpty() || !regReq.getGender().equals("m") && !regReq.getGender().equals("f")) {
      regRes = new RegisterResponse("Error: invalid gender", false);
      return false;
    }
    else {
      return true;
    }
  }

  /**
   * use FillService class to fill 4 generations of ancestor data to this user.
   * returns Person object with the personID information that we need for the user.
   * @param userName string value from regReq.getUserName()
   */
  private void fillGenerations(String userName) throws DataAccessException {
    FillRequest fillReq = new FillRequest(userName);
    FillService fillService = new FillService(fillReq);
    fillService.fill();
  }

}
