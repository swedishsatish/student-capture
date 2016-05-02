package studentcapture.datalayer.database;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by c12osn on 2016-05-02.
 */
public class SubmissionTest  extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    //jdbcTemplate.queryForObject(s[1], new Object[]{studIDInt, assIDInt}, String.class);

    @Autowired
    Submission sub;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Before
    public void setUp() {
        Mockito.reset(jdbcMock);
    }

    @Test
    public void shouldBeAbleToConnectToDB(){
        String sqlQuery = "SELECT grade, submissiondate as time, concat(firstname,' ', lastname) as teacher" +
                " FROM submission JOIN users ON (teacherid = userid) WHERE (studentid = ? AND assignmentid = ?)";

        Map responseFromMock = new HashMap();
        responseFromMock.put("grade", "vg");
        responseFromMock.put("time", "10100101");


        when(jdbcMock.queryForMap(sqlQuery, 1, 1)).
                thenReturn(responseFromMock);
        Map<String, Object> response = sub.getGrade(1, 1);

        assertEquals("vg", response.get("grade"));
    }

}