package org.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.List;
import org.app.controller.FilesController;
import org.app.repository.FilesRepository;
import org.app.service.FilesService;
import spark.Service;

public class Main {

  public static void main(String[] args) throws SQLException {
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
        List.of(new FilesController(service, new FilesService(new FilesRepository()), objectMapper)));
    Postgres.initializeDb();
    application.start();
  }
}