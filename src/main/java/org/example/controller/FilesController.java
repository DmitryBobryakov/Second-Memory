package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.example.exception.NoSuchFileException;
import org.example.exception.NoSuchPathException;
import org.example.service.FilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

public class FilesController implements Controller {
  private static final Logger log = LoggerFactory.getLogger(FilesController.class);
  private final FilesService filesService;
  private final Service service;
  private final ObjectMapper objectMapper;
  private final FreeMarkerEngine freeMarkerEngine;

  public FilesController(
      Service service,
      FilesService filesService,
      ObjectMapper objectMapper,
      FreeMarkerEngine freeMarkerEngine) {
    this.filesService = filesService;
    this.service = service;
    this.objectMapper = objectMapper;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    download();
    upload();
  }

  private void download() {
    service.get(
        "/files/info/download/:fileId/:userId",
        (req, res) -> {
          try {
            res.type("text/html; charset=utf-8");
            URL currentURL =
                new URL(
                    "https://localhost:4567/files/info/download/"
                        + req.params(":fileId")
                        + req.params(":userId"));
            filesService.download(currentURL, req.params(":fileId"), req.params(":userId"));
            Map<String, Object> model = new HashMap<>();
            res.status(200);
            return freeMarkerEngine.render(new ModelAndView(model, "fileInfoDownload.ftl"));
          } catch (NoSuchFileException e) {
            res.status(404);
            log.error("File doesn't exist with ID: {}", req.params(":fileId"), e);
            Map<String, Object> model = new HashMap<>();
            model.put("error", e.getMessage());
            return freeMarkerEngine.render(new ModelAndView(model, "noSuchFileError.ftl"));
          }
        });
  }

  private void upload() {
    service.get(
        "/files/upload/:bucketName",
        (req, res) -> {
          try {
            res.type("text/html; charset=utf-8");
            filesService.upload("test", "ds");
            Map<String, Object> model = new HashMap<>();
            res.status(200);
            return freeMarkerEngine.render(new ModelAndView(model, "fileUpload.ftl"));
          } catch (NoSuchPathException e) {
            res.status(404);
            log.error("File doesn't exist with ID: {}", req.params(":fileId"), e);
            Map<String, Object> model = new HashMap<>();
            model.put("error", e.getMessage());
            return freeMarkerEngine.render(new ModelAndView(model, "noSuchFileError.ftl"));
          }
        });
  }
}
