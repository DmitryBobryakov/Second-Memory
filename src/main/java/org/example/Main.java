package org.example;

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
    Application application =
        new Application(
            List.of(
                new FilesController(
                    service,
                    new FilesService(new FilesRepository()),
                    TemplateFactory.freeMarkerEngine())));
    Postgres.createTables();
    application.start();
  }
}
