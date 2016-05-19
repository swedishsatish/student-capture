package studentcapture.datalayer.database;

import org.apache.catalina.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import studentcapture.config.StudentCaptureApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configurates the  h2 database.
 *
 * @author Timmy Olsson
 */
@Configuration
public class H2DataSource {

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

}