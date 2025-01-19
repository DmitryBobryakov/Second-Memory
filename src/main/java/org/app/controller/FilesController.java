package org.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.app.controller.request.DirectoryInfoRequest;
import org.app.exception.NoDirectoriesFound;
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

  public FilesController(Service service, FilesService filesService, ObjectMapper objectMapper,
      FreeMarkerEngine freeMarkerEngine) {
    this.filesService = filesService;
    this.service = service;
    this.objectMapper = objectMapper;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    getFileInfo();
    getFilesInDirectory();
    getRootDirectories();
  }

  private void getFileInfo() {
    service.get("/files/info/:fileId/:userId", (req, res) -> {
      try {
        res.type("text/html; charset=utf-8");
        List<String> result = filesService.getFileInfo(req.params(":userId"), req.params(":fileId"));
        Map<String, Object> model = new HashMap<>();
        if (!result.isEmpty()) {
          model.put("fileInfo", result);
          res.status(200);
          return freeMarkerEngine.render(new ModelAndView(model, "fileInfo.ftl"));
        } else {
          model.put("forbidden", "У Вас нет прав доступа для просмотра информации об этом файле");
          res.status(403);
          return freeMarkerEngine.render(new ModelAndView(model, "noAccessRights.ftl"));
        }
      } catch (NoSuchFileException e) {
        log.error("File doesn't exist with ID: {}", req.params(":id"), e);
        Map<String, Object> model = new HashMap<>();
        model.put("error", e.getMessage());
        res.status(404);
        return freeMarkerEngine.render(new ModelAndView(model, "noSuchFileError.ftl"));
      }
    });
  }

  private void getFilesInDirectory() {
    service.get("/directory/info", (req, res) -> {
      try {
        res.type("text/html; charset=utf-8");
        DirectoryInfoRequest directoryInfoRequest = objectMapper.readValue(req.body(),
            DirectoryInfoRequest.class);
        List<List<String>> result = filesService.getFilesInDirectory(directoryInfoRequest.path(),
            directoryInfoRequest.bucket());
        Map<String, Object> model = new HashMap<>();
        model.put("directoryInfo", result);
        res.status(200);
        return freeMarkerEngine.render(new ModelAndView(model, "directoryInfo.ftl"));
      } catch (NoSuchPathException e) {
        log.error("ERROR: {}", e.getMessage());
        Map<String, Object> model = new HashMap<>();
        model.put("error", e.getMessage());
        res.status(404);
        return freeMarkerEngine.render(new ModelAndView(model, "noSuchPathError.ftl"));
      }
    });
  }

  private void getRootDirectories() {
    service.get("/root/directories/:bucket", (req, res) -> {
      try {
        res.type("text/html; charset=utf-8");
        List<String> result = filesService.getRootDirectories(req.params(":bucket"));
        Map<String, Object> model = new HashMap<>();
        model.put("rootDirectories", result);
        res.status(200);
        return freeMarkerEngine.render(new ModelAndView(model, "sideMenu.ftl"));
      } catch (NoDirectoriesFound e) {
        log.error("ERROR: {}", e.getMessage());
        Map<String, Object> model = new HashMap<>();
        model.put("error", e.getMessage());
        res.status(404);
        return freeMarkerEngine.render(new ModelAndView(model, "noDirectoriesFound.ftl"));
      }
    });
  }
}