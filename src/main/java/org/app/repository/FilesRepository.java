package org.app.repository;

import io.minio.Result;
import io.minio.messages.Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.app.Minio;
import org.app.Postgres;
import org.app.exception.DbSelectException;

public class FilesRepository {

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
    // Обработать ответ, вернуть только нужные поля

  }
}