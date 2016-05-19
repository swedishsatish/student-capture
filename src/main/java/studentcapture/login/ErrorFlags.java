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
    NOERROR
}
