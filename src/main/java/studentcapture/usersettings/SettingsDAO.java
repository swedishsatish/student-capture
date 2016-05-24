package studentcapture.usersettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by c13elt on 2016-05-16.
 */

@Repository
public class SettingsDAO {
    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    /**
     * Returns saved user settings for the config page
     * @author c13elt, sanna
     * @param userID Identifier of the user to get settings for
     * @return A Settings object containing all the information
     */
    public Settings getUserConfig(int userID){
        Settings c = new Settings();
        Map<String, Object> map = null;
        String query = "SELECT Language, EmailNotify, TextSize FROM Config WHERE UserId = ?";
        try {
            map = jdbcTemplate.queryForMap(query, userID);
        } catch(IncorrectResultSizeDataAccessException e) {
            System.out.println("\nIncorrectStuff " + e);
        } catch(DataAccessException e) {
            System.out.println("\nDataAccess " + e);
        }
        c.setLanguage((String)map.get("Language"));
        c.setMailUpdate((boolean)map.get("EmailNotify"));
        c.setTextSize((int)map.get("TextSize"));
        return c;
    }
    /**
     * Changes a user's settings
     * @author c13elt, sanna
     * @param userID Identifier of the user to modify
     * @param c An object containing all settings to be changed
     * @return True if everything went well, otherwise false
     */
    public boolean setUserConfig(int userID, Settings c) {
        String sqlUpdate = "UPDATE Config SET Language = ?, EmailNotify = ?, textSize = ? WHERE UserID = ?";
        String sqlCreate = "INSERT INTO Config VALUES (?, ?, ?, ?)";
        int status;
        try {
            status = jdbcTemplate.update(sqlUpdate, c.getLanguage(), c.getMailUpdate(), c.getTextSize(), userID);
        } catch(Exception e) {
            return false;
        }
        if (status == 0) {
            jdbcTemplate.update(sqlCreate, userID, c.getLanguage(), c.getMailUpdate(), c.getTextSize());
        }
        return true;
    }
}