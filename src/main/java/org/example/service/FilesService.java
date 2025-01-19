package org.example.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.example.MyMinIOClient;
import org.example.Postgres;
import org.example.exception.NoSuchFileException;
import org.example.exception.NoSuchPathException;
import org.example.repository.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FilesService {
  private final FilesRepository filesRepository;
  private static final Logger log = LoggerFactory.getLogger(FilesService.class);
  private static final MinioClient client = MyMinIOClient.getClient();

  public FilesService(FilesRepository filesRepository) {
    this.filesRepository = filesRepository;
  }

  public void download(URL currentUrl, String file_id, String userId)
      throws NoSuchFileException, SQLException {
    try (ResultSet rs = Postgres.checkTheUserSeeTheFile(userId, file_id)) {
      boolean flag = rs.getBoolean(1);
      if (!flag) {
        return;
      }
      try (ResultSet resultSet = Postgres.selectPathToFileFromFileInfo(file_id)) {
        String bucketName = resultSet.getString(1);
        String fileName = resultSet.getString(2);
        filesRepository.download(currentUrl, bucketName, fileName);
      } catch (Exception e) {
        log.error("ERROR: {}", e.getMessage());
        throw new NoSuchFileException("File not found");
      }
    }
  }

  public void upload(String bucketName, Part file) throws NoSuchPathException {
    try {
      boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!found) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }
      filesRepository.upload(bucketName, file);
    } catch (Exception e) {
      log.error("ERROR: {}", e.getMessage());
      throw new NoSuchPathException("File not found");
    }
  }
}
