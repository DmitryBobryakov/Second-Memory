package org.app;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

public class Minio {

  private static final String accessKey = "minioadmin";
  private static final String secretKey = "minioadmin";
  private static final MinioClient minioClient =
      MinioClient.builder()
          .endpoint("http://127.0.0.1:9000")
          .credentials(accessKey, secretKey)
          .build();

  public static Iterable<Result<Item>> getFilesInDirectory(String path, String bucket) {
    Iterable<Result<Item>> results = minioClient.listObjects(
        ListObjectsArgs.builder()
            .bucket(bucket)
            .prefix(path)
            .build());

    return results;
  }
}