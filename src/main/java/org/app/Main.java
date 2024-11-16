package org.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.controller.UserController;
import org.app.repository.InMemoryUserRepository;
import spark.Service;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();
        Application application = new Application(
                List.of(
                        new UserController(
                                service,
                                new UserService(
                                        new InMemoryUserRepository()
                                ),
                                objectMapper
                        )
                )
        );
        Postgres.initializeDB();
        application.start();
    }
}