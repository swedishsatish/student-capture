package studentcapture.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by Emil on 2016-05-24.
 */
@Repository
public class AdministratorDAO {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Fetches the support e-mail address from the database.
     *
     * @return The address, or null if something went wrong.
     */
    public String getSupportEmail(){
        String sql = "SELECT Email FROM Administrator LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql,String.class);
        } catch(Exception e) {
            return null;
        }
    }
}
