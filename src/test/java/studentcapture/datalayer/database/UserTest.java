package studentcapture.datalayer.database;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
import static org.junit.Assert.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by tfy12hsm.
 */
public class UserTest  extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    User user;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Before
    public void setUp() {
        //Over writes the actual database with fake.
        H2DataSource src = new H2DataSource();
        jdbcMock.setDataSource(src.dataSource());



    }


    @Test
    public void testAddUser() {

       boolean res =  user.addUser("userPelle","Pelle","Jönsson","1944","mypassword123");

        String sql = "SELECT * FROM users WHERE username = 'userPelle'";

        //Getting values from table user
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcMock.queryForObject(sql, new UserWrapper());

        assertEquals("userPelle",info.get("username"));
        assertEquals("Pelle",info.get("fName"));
        assertEquals("Jönsson",info.get("lName"));
        assertEquals("1944",info.get("SNN"));
    }


    /**
     *  Used to collect user information, and return a hashmap.
     */
    protected class UserWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            HashMap<String,String> info = new HashMap();
            info.put("userID",rs.getString("userid"));
            info.put("username",rs.getString("UserName"));
            info.put("fName",rs.getString("FirstName"));
            info.put("lName",rs.getString("LastName"));
            info.put("SNN",rs.getString("persnr"));
            info.put("pswd",rs.getString("pswd"));
            return info;
        }
    }
}
