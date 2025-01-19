package org.example;

import org.example.controller.FilesController;
import org.example.repository.FilesRepository;
import org.example.service.FilesService;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import ch.qos.logback.classic.util.ContextInitializer;
import org.example.template.TemplateFactory;
import spark.Service;

public class Main {

  public static void main(String[] args) throws SQLException {
    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback.xml");

    Properties properties = new Properties();
    try (FileInputStream propsInput = new FileInputStream("config.properties")) {
      properties.load(propsInput);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Service service = Service.ignite();
    Application application =
        new Application(
            List.of(
                new FilesController(
                    service,
                    new FilesService(new FilesRepository()),
                    TemplateFactory.freeMarkerEngine())));
    Postgres db = new Postgres(properties);

    Postgres.createTables();
    application.start();
  }
}
