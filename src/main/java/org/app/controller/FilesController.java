package org.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.app.controller.request.FileInfoRequest;
import org.app.exception.NoSuchFileException;
import org.app.exception.NoSuchPathException;
import org.app.service.FilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

public class FilesController implements Controller {

  private static final Logger log = LoggerFactory.getLogger(FilesController.class);
  private final FilesService filesService;
  private final Service service;
  private final ObjectMapper objectMapper;

  public FilesController(Service service, FilesService filesService, ObjectMapper objectMapper) {
    this.filesService = filesService;
    this.service = service;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    getFileInfo();
    getFilesInDirectory();
  }

  private void getFileInfo() {
    service.get("/files/info/:id", (req, res) -> {
      try {
        List<String> result = filesService.getFileInfo(req.params(":id"));
        res.status(200);
        return result;
      } catch (NoSuchFileException e) {
        res.status(401);
        log.error("File doesn't exist with ID: {}", req.params(":id"), e);
        return e;
      }
    });
  }

  private void getFilesInDirectory() {
    service.get("/directory/info", (req, res) -> {
      FileInfoRequest fileInfoRequest = objectMapper.readValue(req.body(), FileInfoRequest.class);
      try {
        filesService.getFilesInDirectory(fileInfoRequest.path(), fileInfoRequest.bucket());
        res.status(200);
        return "ANSWER";
      } catch (NoSuchPathException e) {
        res.status(401);
        log.error("Path doesn't exist: {}", fileInfoRequest.path(), e);
        return e;
      }
    });
  }
}