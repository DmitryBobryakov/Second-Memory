package org.secondMemory;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.secondMemory.controller.FreemarkerController;
import org.secondMemory.controller.UserController;
import org.secondMemory.repository.InMemoryUserRepository;
import org.secondMemory.service.UserService;
import org.secondMemory.template.TemplateFactory;
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
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    UserService userService = new UserService(new InMemoryUserRepository());
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();
    Application application =
        new Application(
            List.of(
                new UserController(service, userService, objectMapper, freeMarkerEngine),
                new FreemarkerController(service, userService, freeMarkerEngine)));
    DbUtils db = new DbUtils(properties);
    DbUtils.initializeDB();
    application.start();
  }
}
