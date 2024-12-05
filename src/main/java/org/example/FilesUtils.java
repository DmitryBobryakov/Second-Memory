package org.example;

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

public final class FilesUtils {

  private static final MinioClient client = MyMinIOClient.getClient();

  public static void download(URL currentUrl, String bucketName, String fileName)
      throws ServerException,
          InsufficientDataException,
          ErrorResponseException,
          IOException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {

    String url =
        client.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .expiry(1, TimeUnit.DAYS)
                .build());

    Spark.get(
        currentUrl.getPath(),
        (Request request, Response response) -> {
          response.redirect(url);
          return response.raw();
        });
  }

  public static void upload(String bucketName, String filePath)
      throws IOException,
          ServerException,
          InsufficientDataException,
          ErrorResponseException,
          NoSuchAlgorithmException,
          InvalidKeyException,
          InvalidResponseException,
          XmlParserException,
          InternalException {

    String fileName = Paths.get(filePath).getFileName().toString();
    boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

    if (!found) {
      client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    client.uploadObject(
        UploadObjectArgs.builder().bucket(bucketName).object(fileName).filename(filePath).build());
  }
}
