package studentcapture.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Configurates the  h2 database. And manage it.
 * @author c12ton
 */
@Configuration
public class H2DB {

    /**
     * Creates local memory based database in current process.
     * @return
     */
    public static DriverManagerDataSource dataSource() {

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("org.h2.Driver");

        //Configurates the settings for the h2 database
        String confUrl = "jdbc:h2:mem:testdb;DATABASE_TO_UPPER=FALSE;"
                + "MODE=PostgreSQL;DB_CLOSE_DELAY=-1;";

        //Path to tables to be created
        String pathTables = "'"+StudentCaptureApplication.ROOT
                +"/database/test/create-tables.sql"+"'";

        String initUrl = "INIT=runscript from  "+ pathTables +";";

        driverManagerDataSource.setUrl(confUrl+initUrl);

        return driverManagerDataSource;
    }

    /**
     *  Returns Current datatables
     * @param dataSource
     * @return arraylist of table names
     */
    private static ArrayList<String> getAllTableNames(DataSource dataSource) {

        ResultSet rs = null;
        ArrayList<String>tables = new ArrayList<>();
        try {
            Connection c = dataSource.getConnection();
            DatabaseMetaData md = c.getMetaData();
            rs = md.getTables(c.getCatalog(),null,null,null);

            while(rs.next()) {
                String test = rs.getString(2);
                if(test.equalsIgnoreCase("public")) {
                    tables.add(rs.getString(3));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;
    }

    /**
     *  Used to clear everything in all tables, including serialize.
     *  Refrence: http://stackoverflow.com/questions/8523423/
     *            reset-embedded-h2-database-periodically
     * @param jdbcMock
     *
     */
    public static void TearDownDB(JdbcTemplate jdbcMock) throws SQLException {

        Connection c = jdbcMock.getDataSource().getConnection();
        Statement s = c.createStatement();

        // Disable FK
        s.execute("SET REFERENTIAL_INTEGRITY FALSE");

        // Find all tables and truncate them
        Set<String> tables = new HashSet<String>();
        ResultSet rs = s.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES" +
                                     "  where TABLE_SCHEMA='PUBLIC'");
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        rs.close();
        for (String table : tables) {
            s.executeUpdate("TRUNCATE TABLE " + table);
        }

        // Idem for sequences
        Set<String> sequences = new HashSet<String>();
        rs = s.executeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES" +
                " WHERE SEQUENCE_SCHEMA='PUBLIC'");
        while (rs.next()) {
            sequences.add(rs.getString(1));
        }

        rs.close();
        for (String seq : sequences) {
            s.executeUpdate("ALTER SEQUENCE " + seq + " RESTART WITH 1");
        }
        // Enable FK
        s.execute("SET REFERENTIAL_INTEGRITY TRUE");
        s.close();
    }


    /**
     * Prints h2 database table
     * @param jdbcMock
     * @param table to be printed
     */
    public static void printTable(JdbcTemplate jdbcMock, String table) {
        System.out.println("\n!!!!!!!!!!!!!!!!!TABLE: "+ table +"!!!!!!!!!!!!!!!!!!!!!!!!!\n");

        String sql = "SELECT * FROM " + table;
        List<Map<String,Object>> users = jdbcMock.queryForList(sql);

        if(users != null && !users.isEmpty()) {
            for(Map<String,Object> user: users) {
                String columns ="";
                String line = "";
                for(Iterator<Map.Entry<String, Object>> it = user.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String,Object> entry = it.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key + " = " + value);
                }
                System.out.println();
            }
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
    }
}