package org.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.app.controller.FilesController;
import org.app.repository.FilesRepository;
import org.app.service.FilesService;
import org.app.template.TemplateFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class FilesControllerTest {
  private Service service;

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void should404IfFileWithSuchIdDoesNotExist() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    FilesRepository filesRepository = new FilesRepository();

    final FilesService filesService = new FilesService(filesRepository);
    Application application =
        new Application(List.of(new FilesController(service, filesService, objectMapper, TemplateFactory.freeMarkerEngine())));

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:%d/files/info/52".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(404, response.statusCode());
  }
}
