package org.app.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import io.minio.errors.*;
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

  public List<String> searchFilesInDirectory(String path, String bucket, String serchTerm) throws NoSuchPathException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    try {
      List<String> result = filesRepository.searchFilesInDirectory(path, bucket, serchTerm);
      return result;
    } catch (Exception e) {
      throw new NoSuchPathException("No such directory");
    }
  }
}