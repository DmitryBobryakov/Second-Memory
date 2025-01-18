package org.example.repository;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Spark;

import org.example.MyMinIOClient;
import org.example.exception.NoSuchFileException;
import org.example.exception.NoSuchPathException;

public class FilesRepository {

  private static final MinioClient client = MyMinIOClient.getClient();

  public void download(URL currentUrl, String bucketName, String fileName)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          NoSuchFileException {

    try {
      String url =
          client.getPresignedObjectUrl(
              GetPresignedObjectUrlArgs.builder()
                  .method(Method.GET)
                  .bucket(bucketName)
                  .object(fileName)
                  .expiry(1, TimeUnit.MINUTES)
                  .build());

      Spark.get(
          currentUrl.getPath(),
          (Request request, Response response) -> {
            response.redirect(url);
            return response.raw();
          });
    } catch (NoSuchAlgorithmException e) {
      throw new NoSuchFileException("File not found");
    }
  }

  public void upload(String bucketName, String filePath)
      throws IOException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException,
          NoSuchPathException {

    String fileName = Paths.get(filePath).getFileName().toString();
    try {
      boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

      if (!found) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }

      client.uploadObject(
          UploadObjectArgs.builder()
              .bucket(bucketName)
              .object(fileName)
              .filename(filePath)
              .build());
    } catch (NoSuchAlgorithmException e) {
      throw new NoSuchPathException("There is no file in this path");
    }
  }
}
