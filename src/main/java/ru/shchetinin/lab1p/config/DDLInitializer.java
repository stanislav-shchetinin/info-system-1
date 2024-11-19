package ru.shchetinin.lab1p.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.naming.InitialContext;
import javax.sql.DataSource;
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
                stmt.execute(new String(Files.readAllBytes(Paths.get("C:\\ЛабораторныеРаботы\\ИС\\lab1p\\src\\main\\java\\ru\\shchetinin\\lab1p\\config\\scripts\\ddl.sql"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
