package org.example;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

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
    public static Iterable<Result<Item>> getFilesInDirectory(String path, String bucket) {
        Iterable<Result<Item>> results = client.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(path)
                        .build());
        return results;
    }
}
