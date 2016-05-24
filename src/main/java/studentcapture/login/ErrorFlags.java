package studentcapture.login;

/**
 * Enum for sending error flags between the db and spring.
 * Used during login.
 * @author dv13ean
 */
public enum ErrorFlags {
    USERNAMELENGTH, //when username is too short
    PASSWORDFORMAT, //when password is incorrectly formatted
    PASSWORDMATCH, //when non matching passwords
    EMAILFORMAT, //when email format is incorrect
    EMAILEXISTS, //when email exists in the database
    USEREXISTS, //when username exist in the database
    USERCONTAINNULL, //when user parameters has null values
    NOERROR; //if ok

    //used in loginScript.js If any changes made,
    // make sure to update loginScript.js Okay?
    @Override
    public String toString(){
        
        switch(this){
        case USERNAMELENGTH:
            return "usernamelength";
            
        case PASSWORDFORMAT:
            return "passwordformat";
            
        case PASSWORDMATCH:
            return "passwordmatch";
            
        case EMAILFORMAT:
            return "emailformat";
            
        case EMAILEXISTS:
            return "emailexists";
            
        case USEREXISTS:
            return "userexists";

        case USERCONTAINNULL:
            return "success";
            
        case NOERROR:
            return "success";
            
        default:
            return "success";

        }
    }
}

