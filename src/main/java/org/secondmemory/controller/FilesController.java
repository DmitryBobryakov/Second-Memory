package org.secondmemory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.secondmemory.Minio;
import org.secondmemory.S3FilesUtils;
import org.secondmemory.controller.request.DirectoryInfoRequest;
import org.secondmemory.exception.NoDirectoriesFound;
import org.secondmemory.exception.NoSuchFileException;
import org.secondmemory.exception.NoSuchPathException;
import org.secondmemory.service.FilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Service;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

@Slf4j
public class FilesController implements Controller {
    private static final MinioClient client = Minio.getClient();
    private static final Logger log = LoggerFactory.getLogger(FilesController.class);
    private final FilesService filesService;
    private static Service service;
    private final ObjectMapper objectMapper;
    private static FreeMarkerEngine freeMarkerEngine;

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
        postUploadPage();
        getUploadPage();
        postRename();
        postReplace();
        postDelite();
        getRename();
        getReplace();
        getDelite();
    }

    private void getFileInfo() {
        service.get("files/info/:id/:bucketName/", (req, res) -> {
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
    private void postUploadPage() {
        service.post(
                "/files/:id/upload/:bucketName",
                (req, res) -> {
                    try {
                        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                        Part file = req.raw().getPart("file");
                        String bucketName = req.params("bucketName");
                        filesService.upload(bucketName, file);
                        return "Файл загружен!";
                    } catch (NoSuchPathException e) {
                        res.status(404);
                        log.error("File doesn't exist with bucketName: {}", req.params(":bucketName"), e);
                        Map<String, Object> model = new HashMap<>();
                        model.put("error", e.getMessage());
                        return freeMarkerEngine.render(new ModelAndView(model, "noSuchFileError.ftl"));
                    }
                });
    }

    private void getUploadPage() {
        service.get(
                "/files/:id/upload/:bucketName",
                (req, res) -> {
                    try {
                        res.type("text/html; charset=utf-8");
                        Map<String, Object> model = new HashMap<>();
                        model.put("bucketName", req.params(":bucketName"));
                        res.status(200);
                        return freeMarkerEngine.render(new ModelAndView(model, "upload.ftl"));
                    } catch (Exception e) {
                        res.status(500);
                        log.error("The server is down", e);
                        return "error";
                    }
                });
    }

    private static void postRename() {
        service.post(
                "/files/:id/rename/:bucketName/:filename",
                (request, response) -> {
                    String newName = request.raw().getParameter("name");
                    S3FilesUtils.changeName(request.params(":bucketName"), request.params(":filename"), newName);
                    return "Файл переименовен!";
                });
    }

    private static void postReplace() {
        service.post(
                "/files/:id/replace/:filename/:bucketName",
                (request, response) -> {
                    String newName = request.raw().getParameter("name");
                    String newDir = request.raw().getParameter("direct");
                    System.out.println(newName);
                    S3FilesUtils.copyInOtherPlace(request.params(":bucketName"), request.params(":filename"), newDir, newName);
                    return "Файл перемещен!";
                });
    }

    private static void postDelite() {
        service.post(
                "/files/:id/delite/:filename/:bucketName",
                (request, response) -> {
                    S3FilesUtils.deleteOne(request.params(":bucketName"), request.params(":filename"));
                    return "Файл удален!";
                });
    }
    private static void getRename() {
        service.get(
                "/files/:id/rename/:filename/:bucketName",
                (request, response) -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("filename", request.params(":filename"));
                    model.put("bucketName", request.params(":bucketName"));
                    return freeMarkerEngine.render(new ModelAndView(model, "PointRename.ftl"));
                });
    }

    private static void getReplace() {
        service.get(
                "/files/:id/replace/:filename/:bucketName",
                (request, response) -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("filename", request.params(":filename"));
                    model.put("bucketName", request.params(":bucketName"));
                    return freeMarkerEngine.render(new ModelAndView(model, "PointReplace.ftl"));
                });
    }

    private static void getDelite() {
        service.get(
                "/files/:id/delite/:filename/:bucketName",
                (request, response) -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("filename", request.params(":filename"));
                    model.put("bucketName", request.params(":bucketName"));
                    return freeMarkerEngine.render(new ModelAndView(model, "PointFilesUtiles.ftl"));
                });
    }


    private void getFilesInDirectory() {
        service.get("/directory/info", (req, res) -> {
            try {
                DirectoryInfoRequest directoryInfoRequest = objectMapper.readValue(req.body(),
                        DirectoryInfoRequest.class);
                List<List<String>> result = filesService.getFilesInDirectory(directoryInfoRequest.path(),
                        directoryInfoRequest.bucket());
                Map<String, Object> model = new HashMap<>();
                model.put("directoryInfo", result);
                res.status(200);
                return freeMarkerEngine.render(new ModelAndView(model, "directoryInfo.ftl"));
            } catch (NoSuchPathException e) {
                res.status(404);
                log.error("ERROR: {}", e.getMessage());
                Map<String, Object> model = new HashMap<>();
                model.put("error", e.getMessage());
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