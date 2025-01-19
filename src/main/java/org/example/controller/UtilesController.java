package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.example.MyS3Client;
import org.example.S3FilesUtils;
import org.example.controller.request.DirectoryInfoRequest;
import org.example.exception.NoSuchFileException;
import org.example.exception.NoSuchPathException;
import org.example.service.FilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Service;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

@Slf4j
public class UtilesController implements Controller {
    private static final MinioClient client = MyS3Client.getClient();
    private static final Logger log = LoggerFactory.getLogger(UtilesController.class);
    private final FilesService filesService;
    private final Service service;
    private final ObjectMapper objectMapper;
    private final FreeMarkerEngine freeMarkerEngine;

    public UtilesController(Service service, FilesService filesService, ObjectMapper objectMapper,
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

    private static void postRename() {
        Spark.post(
                "/files/rename/:file",
                (request, response) -> {
                    request.attribute(
                            "org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    String bucketName = request.params("bucketName");
                    Part file = request.raw().getPart("file");
                    Part newName = request.params("input");
                    S3FilesUtils.changeName(bucketName, file.getName(), newName);
                    return "Файл переименовен!";
                });
    }
    private static void postReplace() {
        Spark.post(
                "/files/replace/:file",
                (request, response) -> {
                    request.attribute(
                            "org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    String bucketName = request.params("bucketName");
                    Part file = request.raw().getPart("file");
                    Part newName = request.params("name");
                    Part newDir = request.params("direct");
                    S3FilesUtils.copyInOtherPlace(bucketName, file.getName(), newDir, newName);
                    return "Файл перемещен!";
                });
    }
    private static void postDelite() {
        Spark.post(
                "/files/delite/:file",
                (request, response) -> {
                    request.attribute(
                            "org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    String bucketName = request.params("bucketName");
                    Part file = request.raw().getPart("file");
                    S3FilesUtils.deleteOne(bucketName, file.getName());
                    return "Файл удален!";
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
}