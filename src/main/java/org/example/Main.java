package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.controller.FilesController;
import org.example.repository.FilesRepository;
import org.example.service.FilesService;

import java.sql.SQLException;
import java.util.List;

import ch.qos.logback.classic.util.ContextInitializer;
import org.example.template.TemplateFactory;
import spark.Service;

public class Main {

  public static void main(String[] args) throws SQLException {
    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback.xml");

    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();

    Application application =
        new Application(
            List.of(
                new FilesController(
                    service,
                    new FilesService(new FilesRepository()),
                    objectMapper,
                    TemplateFactory.freeMarkerEngine())));
    Postgres.createTables();
    application.start();
  }
}
