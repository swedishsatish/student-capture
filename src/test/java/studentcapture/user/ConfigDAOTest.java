package studentcapture.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by c13elt on 2016-05-19.
 */
public class ConfigDAOTest extends StudentCaptureApplicationTests {

    @Autowired
    private JdbcTemplate jdbcMock;

    @Autowired
    ConfigDAO configDAO;

    /**
     * Inserts dummy data to test against h2Database
     */
    @Before
    public void setUp() {
        String sql1 = "INSERT INTO Users VALUES (1, 'mkyong', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword',null, false);";
        String sql2 = "INSERT INTO Config VALUES (1, 'Klingon', true, 10);";
        String sql3 = "INSERT INTO Users VALUES (2, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword',null, false);";


        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
        jdbcMock.update(sql3);
    }

    /**
     * Remove all content from the database
     */
    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Users;";
        String sql2 = "DELETE FROM Config;";

        jdbcMock.update(sql2);
        jdbcMock.update(sql1);
    }

    @Test
    public void getUserConfigReturnsCorrectly(){
        Config c = configDAO.getUserConfig(1);

        assertEquals("Klingon", c.getLanguage());
        assertEquals(true, c.getMailUpdate());
        assertEquals(10, c.getTextSize());
    }

    @Test
    public void setUserConfigDoesntCrash(){
        Config c = new Config();
        c.setLanguage("English");
        c.setMailUpdate(false);
        c.setTextSize(50);

        boolean result = configDAO.setUserConfig(2, c);

        assertTrue(result);
    }

    @Test
    public void setUserConfigInsertsCorrectly(){
        Config c = new Config();
        c.setLanguage("English");
        c.setMailUpdate(false);
        c.setTextSize(50);

        configDAO.setUserConfig(2, c);
        Config config = configDAO.getUserConfig(2);

        assertEquals("English", config.getLanguage());
        assertEquals(false, config.getMailUpdate());
        assertEquals(50, config.getTextSize());
    }

    @Test
    public void setUserConfigUpdatesCorrectly(){
        Config c = new Config();
        c.setLanguage("Swedish");
        c.setMailUpdate(false);
        c.setTextSize(13);

        configDAO.setUserConfig(1, c);
        Config config = configDAO.getUserConfig(1);

        assertEquals("Swedish", config.getLanguage());
        assertEquals(false, config.getMailUpdate());
        assertEquals(13, config.getTextSize());
    }

}
