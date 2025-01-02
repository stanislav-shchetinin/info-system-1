package ru.shchetinin.lab1p.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

@WebListener
public class DDLInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/postgres");
            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement()) {
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("scripts/ddl.sql")) {
                    if (is == null) {
                        throw new FileNotFoundException("Resource 'scripts/ddl.sql' not found");
                    }
                    stmt.execute(new String(is.readAllBytes(), StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
