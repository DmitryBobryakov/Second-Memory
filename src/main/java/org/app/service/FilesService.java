package org.app.service;

import java.util.ArrayList;
import java.util.List;
import org.app.exception.DbSelectException;
import org.app.exception.NoDirectoriesFound;
import org.app.exception.NoSuchFileException;
import org.app.exception.NoSuchPathException;
import org.app.repository.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesService {

  private static final Logger log = LoggerFactory.getLogger(FilesService.class);
  private final FilesRepository filesRepository;

  public FilesService(FilesRepository filesRepository) {
    this.filesRepository = filesRepository;
  }

  public List<String> getFileInfo(String userId, String fileId) throws NoSuchFileException {
    try {
      if (filesRepository.checkAccessRights(userId, fileId)) {
        return filesRepository.getFileInfo(fileId);
      } else {
        return new ArrayList<>();
      }
    } catch (DbSelectException e) {
      log.error("ERROR: ", e);
      throw new NoSuchFileException("Файл не найден");
    }
  }

  public List<List<String>> getFilesInDirectory(String path, String bucket) throws NoSuchPathException {
    try {
      return filesRepository.getFilesInDirectory(path, bucket);
    } catch (Exception e) {
      log.error("ERROR: {}", e.getMessage());
      throw new NoSuchPathException("Папка не найдена");
    }
  }

  public List<String> getRootDirectories(String bucket) throws NoDirectoriesFound {
    try {
      return filesRepository.getRootDirectories(bucket);
    } catch (Exception e) {
      log.error("ERROR: ", e);
      throw new NoDirectoriesFound("Папки не найдены");
    }
  }
}