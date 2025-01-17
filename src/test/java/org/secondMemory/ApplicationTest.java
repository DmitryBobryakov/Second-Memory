package org.secondMemory;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.secondMemory.controller.UserController;
import org.secondMemory.repository.InMemoryUserRepository;
import org.secondMemory.service.UserService;
import spark.Service;

class ApplicationTest {
  private Service service;

  @BeforeEach
  void befofeEach() {
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void E2ETest() throws Exception {
    Properties properties = new Properties();
    try (FileInputStream propsInput = new FileInputStream("config.properties")) {
      properties.load(propsInput);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    Application application =
        new Application(
            List.of(
                new UserController(
                    service, new UserService(new InMemoryUserRepository()), objectMapper)));
    DbUtils db = new DbUtils(properties);
    DbUtils.initializeDB();
    application.start();
    service.awaitInitialization();

    HttpResponse<String> responseForRegistration =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                    { "email": "anotherperson@mail.com", "password": "Another~!!", "username": "Another Person" }"""))
                    .uri(
                        URI.create(
                            "http://localhost:%d/SecondMemory/signup".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(201, responseForRegistration.statusCode());

    HttpResponse<String> responseForLogin =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                                            { "email": "anotherperson@mail.com", "password": "Another~!!" }"""))
                    .uri(
                        URI.create(
                            "http://localhost:%d/SecondMemory/signin".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(200, responseForLogin.statusCode());

    HttpResponse<String> responseForRegisteringExistingUser =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                                            { "email": "anotherperson@mail.com", "password": "Another~!!", "username": "Another Person" }"""))
                    .uri(
                        URI.create(
                            "http://localhost:%d/SecondMemory/signup".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(400, responseForRegisteringExistingUser.statusCode());

    HttpResponse<String> responseForWrongPassword =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                                            { "email": "anotherperson@mail.com", "password": "wrongPassword" }"""))
                    .uri(
                        URI.create(
                            "http://localhost:%d/SecondMemory/signin".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(401, responseForWrongPassword.statusCode());

    HttpResponse<String> responseForInvalidPassword =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                                            { "email": "anotherperson123@mail.com", "password": "wrongpassword", "username": "Another Person" }"""))
                    .uri(
                        URI.create(
                            "http://localhost:%d/SecondMemory/signup".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(400, responseForInvalidPassword.statusCode());

    HttpResponse<String> responseForNonExistentUser =
            HttpClient.newHttpClient()
                    .send(
                            HttpRequest.newBuilder()
                                    .POST(
                                            HttpRequest.BodyPublishers.ofString(
                                                    """
                                                                                    { "email": "anotherperson123@mail.com", "password": "wrongpassword" }"""))
                                    .uri(
                                            URI.create(
                                                    "http://localhost:%d/SecondMemory/signin".formatted(service.port())))
                                    .build(),
                            HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(401, responseForNonExistentUser.statusCode());
  }
}
