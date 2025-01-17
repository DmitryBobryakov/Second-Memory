package org.example;

import io.minio.MinioClient;

public class MyS3Client {
    private static final MinioClient client;

    static {
        String accessKey = "minioadmin";
        String secretKey = "minioadmin";
        client =
                MinioClient.builder()
                        .endpoint("https://play.min.io")
                        .credentials(accessKey, secretKey)
                        .build();
    }
    public static MinioClient getClient() {
        return client;
    }
}
