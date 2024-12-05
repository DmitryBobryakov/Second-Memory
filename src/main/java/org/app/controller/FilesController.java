package org.app.controller;

import static spark.Spark.get;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.app.exception.DBSelectException;
import org.app.exception.NoSuchFileException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

public class FilesController implements Controller {

  static String selectQuery =
      "SELECT file_owner, created_date, last_update, tags, access_rights FROM FileInfo WHERE Id = ?";

  private final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
  private final String username = "postgres";
  private final String password = "738212";

  private final String accessKey = "<AWS Access Key>";
  private final String secretKey = "<AWS Secret Key>";
  private final AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
  private final String regionName = "<AWS Region>";
  S3Client s3Client = S3Client
      .builder()
      .region(Region.of(regionName))
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .build();

  @Override
  public void initializeEndpoints() {
    getFileInfo();
    getFilesInDirectory();
  }

  private void getFileInfo() {
    get("/files/info/:id", (req, res) -> {
      ArrayList<String> result = new ArrayList<>();

      try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
          PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
      ) {

        preparedStatement.setString(1, req.params(":id"));

        try (ResultSet resultSet = preparedStatement.executeQuery();) {
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
        } catch (NoSuchFileException e) {
          throw new NoSuchFileException("No such file");
        } catch (DBSelectException e) {
          throw new DBSelectException("Cannot select info from DB");
        }
      }

      return result;
    });
  }

  private void getFilesInDirectory() {
    get("/directory/info", (req, res) -> {
      String path = req.body();
      String bucket = req.body();
      ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
          .bucket(bucket)
          .prefix(path)
          .build();

      ListObjectsV2Iterable listObjectsV2Iterable = s3Client.listObjectsV2Paginator(listObjectsV2Request);
      // Обработать ответ, вернуть только нужные поля

      return "Папка пуста";
    });
  }
}