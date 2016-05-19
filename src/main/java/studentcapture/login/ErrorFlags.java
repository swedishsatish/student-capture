package studentcapture.login;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import studentcapture.user.User;

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

