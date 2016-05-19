package studentcapture.login;

/**
 * Enum for sending error flags between the db and spring.
 * Used during login.
 * @author dv13ean
 */
public enum ErrorFlags {
    USERNAMELENGTH,
    PASSWORDFORMAT,
    PASSWORDMATCH,
    EMAILFORMAT,
    EMAILEXISTS,
    USEREXISTS,
    NOERROR;
    
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
            
        case NOERROR:
            return "success";
            
        default:
            return "success";

        }
        
    }
    
}
