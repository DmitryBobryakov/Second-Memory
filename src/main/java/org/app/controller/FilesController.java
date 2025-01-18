package org.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.app.controller.request.DirectoryInfoRequest;
import org.app.exception.NoSuchFileException;
import org.app.exception.NoSuchPathException;
import org.app.service.FilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

@Slf4j
public class FilesController implements Controller {

  private static final Logger log = LoggerFactory.getLogger(FilesController.class);
  private final FilesService filesService;
  private final Service service;
  private final ObjectMapper objectMapper;
  private final FreeMarkerEngine freeMarkerEngine;

  public FilesController(Service service, FilesService filesService, ObjectMapper objectMapper, FreeMarkerEngine freeMarkerEngine) {
    this.filesService = filesService;
    this.service = service;
    this.objectMapper = objectMapper;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    getFileInfo();
    getFilesInDirectory();
  }

  private void getFileInfo() {
    service.get("/files/info/:id", (req, res) -> {
      try {
        res.type("text/html; charset=utf-8");
        List<String> result = filesService.getFileInfo(req.params(":id"));
        Map<String, Object> model = new HashMap<>();
        model.put("fileInfo", result);
        res.status(200);
        return freeMarkerEngine.render(new ModelAndView(model, "fileInfo.ftl"));
      } catch (NoSuchFileException e) {
        res.status(404);
        log.error("File doesn't exist with ID: {}", req.params(":id"), e);
        Map<String, Object> model = new HashMap<>();
        model.put("error", e.getMessage());
        return freeMarkerEngine.render(new ModelAndView(model, "noSuchFileError.ftl"));
      }
    });
  }

  private void getFilesInDirectory() {
    service.get("/directory/info", (req, res) -> {
      DirectoryInfoRequest directoryInfoRequest = objectMapper.readValue(req.body(), DirectoryInfoRequest.class);
      try {
        filesService.getFilesInDirectory(directoryInfoRequest.path(), directoryInfoRequest.bucket());
        res.status(200);
        return "ANSWER";
      } catch (NoSuchPathException e) {
        res.status(404);
        log.error("ERROR: {}", e.getMessage());
        Map<String, Object> model = new HashMap<>();
        model.put("error", e.getMessage());
        return freeMarkerEngine.render(new ModelAndView(model, "noSuchPathError.ftl"));
      }
    });
  }
}