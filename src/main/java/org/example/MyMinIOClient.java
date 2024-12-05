package org.example;

import io.minio.MinioClient;

public class MyMinIOClient {
  private static final MinioClient client;

  static {
    String accessKey = "minioadmin";
    String secretKey = "minioadmin";
    client =
        MinioClient.builder()
            .endpoint("http://127.0.0.1:9000")
            .credentials(accessKey, secretKey)
            .build();
  }

  public static MinioClient getClient() {
    return client;
  }
}
