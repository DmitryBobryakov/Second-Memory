package org.example.service;

import org.example.Postgres;
import org.example.exception.NoSuchFileException;
import org.example.exception.NoSuchPathException;
import org.example.repository.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FilesService {
  private final FilesRepository filesRepository;
  private static final Logger log = LoggerFactory.getLogger(FilesService.class);

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

  public void upload(String bucketName, String filePath) throws NoSuchPathException {
    try {
      filesRepository.upload(bucketName, filePath);
    } catch (Exception e) {
      log.error("ERROR: {}", e.getMessage());
      throw new NoSuchPathException("File not found");
    }
  }
}
