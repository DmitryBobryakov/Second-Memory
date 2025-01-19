package org.example;

import io.minio.MinioClient;
import lombok.Getter;

public class MyMinIOClient {
  @Getter private static final MinioClient client;

  static {
    String accessKey = "minio";
    String secretKey = "65732168";
    client =
        MinioClient.builder()
            .endpoint("http://localhost:9000")
            .credentials(accessKey, secretKey)
            .build();
  }
}
