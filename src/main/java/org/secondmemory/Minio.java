package org.secondmemory;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

public class Minio {
    private static final MinioClient client;

    static {
        String accessKey = "miniominio";
        String secretKey = "miniominio";
        client =
                MinioClient.builder()
                        .endpoint("http://localhost:9000")
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
    public static Iterable<Result<Item>> getRootDirectories(String bucket) {
        Iterable<Result<Item>> results = client.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix("/")
                        .build());

        return results;
    }
}
