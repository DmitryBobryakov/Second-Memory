package org.app.repository;

import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.app.Minio;
import org.app.Postgres;
import org.app.exception.DbSelectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesRepository {

  private static final Logger log = LoggerFactory.getLogger(FilesRepository.class);

  public List<String> getFileInfo(String id) throws DbSelectException {
    List<String> result = new ArrayList<>();
    try (ResultSet resultSet = Postgres.selectAllFromFileInfo(id);) {

      if (resultSet.next()) {

        String fileOwner = resultSet.getString("file_owner");
        String createdDate = resultSet.getString("created_date");
        String lastUpdate = resultSet.getString("last_update");
        String tags = resultSet.getString("tags");
        String accessRights = resultSet.getString("access_rights");

        result.add(fileOwner);
        result.add(createdDate);
        result.add(lastUpdate);
        result.add(tags);
        result.add(accessRights);

      }
    } catch (SQLException e) {
      throw new DbSelectException("Cannot select data from DB");
    }

    return result;
  }

  public void getFilesInDirectory(String path, String bucket) {
    Iterable<Result<Item>> results = Minio.getFilesInDirectory(path, bucket);
    List<List<String>> processedData = new ArrayList<>();
    results.forEach(itemResult -> {

    });

  }

  public List<String> searchFilesInDirectory(String path, String bucket, String searchTerm) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    List<String> matchingFiles = new ArrayList<>();
    Iterable<Result<Item>> results = Minio.getFilesInDirectory(path, bucket);
      for (Result<Item> result : results) {
        Item item = result.get();

        if (!item.isDir() && item.objectName().contains(searchTerm)) {
          matchingFiles.add(item.objectName());
        }
      }
    return matchingFiles;
  }
}