package org.example.repository;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Spark;

import org.example.MyMinIOClient;
import org.example.exception.NoSuchFileException;
import org.example.exception.NoSuchPathException;

import javax.servlet.http.Part;

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

  public void upload(String bucketName, Part file)
      throws IOException,
          InsufficientDataException,
          ErrorResponseException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          NoSuchAlgorithmException,
          InternalException,
          NoSuchPathException {

    try {
      String fileName = file.getSubmittedFileName();
      InputStream fileInputStream = file.getInputStream();
      client.putObject(
          PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                  fileInputStream, -1, 10485760)
              .build());

    } catch (ServerException e) {
      throw new NoSuchPathException("Файл слишком большого размера");
    }
  }
}
