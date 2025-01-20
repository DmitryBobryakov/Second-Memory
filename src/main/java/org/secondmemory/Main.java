package org.secondmemory;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.secondmemory.controller.FilesController;
import org.secondmemory.controller.FreemarkerController;
import org.secondmemory.controller.UserController;
import org.secondmemory.repository.FilesRepository;
import org.secondmemory.repository.InMemoryUserRepository;
import org.secondmemory.service.FilesService;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import ch.qos.logback.classic.util.ContextInitializer;
import org.secondmemory.service.UserService;
import org.secondmemory.template.TemplateFactory;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        try (FileInputStream propsInput = new FileInputStream("config.properties")) {
            properties.load(propsInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback.xml");

        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();
        UserService userService = new UserService(new InMemoryUserRepository());
        FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();
        Application application = new Application(
                List.of(new FilesController(service, new FilesService(new FilesRepository()), objectMapper,
                        freeMarkerEngine), new FreemarkerController(service, userService, freeMarkerEngine), new UserController(service, userService, objectMapper, freeMarkerEngine)));
        Postgres bd = new Postgres(properties);
        application.start();
    }
}