package studentcapture.login;

/**
 * Enum for sending error flags between the db and spring.
 * Used during login.
 * @author dv13ean
 */
public enum ErrorFlags {
    USERNAMELENGTH, //Username length is invalid
    PASSWORDFORMAT, //Password does not follow the format
    PASSWORDMATCH,  //The two given passwords do not match
    EMAILFORMAT,    //The given email is not on a valid format
    EMAILEXISTS,    //The given email is already in the database
    USEREXISTS,     //The given username is already in the database
    NOERROR         //No error
}
