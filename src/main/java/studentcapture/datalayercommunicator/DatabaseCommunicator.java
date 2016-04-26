package studentcapture.datalayercommunicator;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * Created by c13gan on 2016-04-25.
 */

public class DatabaseCommunicator {

    private BasicDataSource dataSource = new BasicDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    /**
     * Connects to the database in use in MC343. Call this function in the beginning of every method
     */
    public void connectToDB() {
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("postgres_user");
        dataSource.setPassword("postgres");
        dataSource.setUrl("jdbc:postgresql://int-nat.cs.umu.se:25432/postgres_db");
        dataSource.setMaxActive(10);
        dataSource.setMaxIdle(5);
        dataSource.setInitialSize(5);
        dataSource.setValidationQuery("SELECT 1");

        jdbcTemplate.setDataSource(dataSource);
    }

    /*  // TODO tried to test to connect to a DB from the database course, unsuccessfully
      public void testDB ()  {
          dataSource.setDriverClassName("org.postgresql.Driver");
          dataSource.setUsername("c5dv119_vt16_c13gan");
          dataSource.setPassword(password);
          dataSource.setUrl("jdbc:postgresql://postgres.cs.umu.se/c5dv119_vt16_c13gan");
          dataSource.setMaxActive(10);
          dataSource.setMaxIdle(5);
          dataSource.setInitialSize(5);
          dataSource.setValidationQuery("SELECT 1");

          jdbcTemplate.setDataSource(dataSource);
      }*/

    /**
     * Just a method we used to insert some of the testvalues, feel free to add more or remove them from the DB
     */
    public void insertTestValues() {
        connectToDB();
        jdbcTemplate.update("INSERT INTO users (userid , firstname , lastname , personnummer , casid , password) " +
                "VALUES ('1337',   'Gustav',   'Gustavsson','1234567890', 'c13ggn', 'X')"

        );

        jdbcTemplate.update("INSERT INTO course (courseid , term , coursecode , coursename ) " +
                "VALUES ('1',   'vt18',   '5dv742','Databaser i skärmar')");

        jdbcTemplate.update("INSERT INTO assignment (assignmentid , courseid , startdate , enddate, pathtovideo, " +
                "published ) " +
                "VALUES ('10', '1', '2018-10-19 10:23:54','2018-10-19 10:23:55', '/video/techervid', 'true')");

        jdbcTemplate.update("INSERT INTO Submission (assignmentid, studentid, pathtovideo, grade, teacherid) " +
                "VALUES ('10', '1337', '/videopath/video', 'vg', '1337');");
    }

    /**
     * Not sure if this matches the new database tables we set up, this is just an example to extract one value from
     * the DB
     * @param studID        User ID for the student
     * @param assignmentID  Assigment ID for the specific assignment
     * @return              The grade for that specific assignment and user or //TODO what if no grade exists
     */
    public String returnGrade(int studID, int assignmentID) {
        connectToDB();
        String getGrade = "SELECT grade FROM submission WHERE (studentid = ? AND assignmentid = ?)";
        String grade;
        try {
            grade = jdbcTemplate.queryForObject(getGrade, new Object[] {studID, assignmentID},
                    String.class);
            grade = grade.trim();
        }catch (IncorrectResultSizeDataAccessException e){
            grade = "Missing grade";
        }catch (DataAccessException e1){
            grade = "Missing grade";
        }

        return grade;
    }

}