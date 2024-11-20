package org.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.deadline.model.AwsCredentials;
import software.amazon.awssdk.services.s3.S3Client;

public class MyS3Client {
    private final static S3Client client;

    static {
        String regionName = ConfigUtils.getConfig().getString("app.cs3-client.region-name");
        String accessKey =  ConfigUtils.getConfig().getString("app.cs3-client.access-key");
        String secretKey =  ConfigUtils.getConfig().getString("app.cs3-client.region-name");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        client = S3Client
                                .builder()
                                .region(Region.of(regionName))
                                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                                .build();
    }

    public static S3Client getClient() {
        return client;
    }
}
