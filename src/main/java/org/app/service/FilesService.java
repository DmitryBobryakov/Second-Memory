package org.app.service;

import java.util.List;
import org.app.exception.DbSelectException;
import org.app.exception.NoSuchFileException;
import org.app.exception.NoSuchPathException;
import org.app.repository.FilesRepository;

public class FilesService {

  private final FilesRepository filesRepository;

  public FilesService(FilesRepository filesRepository) {
    this.filesRepository = filesRepository;
  }

  public List<String> getFileInfo(String id) throws NoSuchFileException {
    try {
      List<String> result = filesRepository.getFileInfo(id);
      return result;
    } catch (DbSelectException e) {
      throw new NoSuchFileException("No such file");
    }
  }

  public void getFilesInDirectory(String path, String bucket) throws NoSuchPathException {
    try {
      filesRepository.getFilesInDirectory(path, bucket);
    } catch (Exception e) {
      throw new NoSuchPathException("No such directory");
    }
  }
}