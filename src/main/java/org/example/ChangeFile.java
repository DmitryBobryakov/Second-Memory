package org.example;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectNotInActiveTierErrorException;
import software.amazon.awssdk.services.s3.model.S3Exception;;




public class ChangeFile {
    public static void changeName(S3Client s3client, String bucketName, String oldKey, String newKey) throws ObjectNotInActiveTierErrorException {
        try {
            s3client.copyObject(request -> request
                    .sourceBucket(bucketName)
                    .sourceKey(oldKey)
                    .destinationBucket(bucketName)
                    .destinationKey(newKey));
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void copyInOtherPlace(S3Client s3client, String oldBucketName, String oldKey, String newBucketName, String newKey) {
        try {
            s3client.copyObject(request -> request
                    .sourceBucket(oldBucketName)
                    .sourceKey(oldKey)
                    .destinationBucket(newBucketName)
                    .destinationKey(newKey));
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void deleteOne(S3Client s3client, String bucketName, String key) {
        try {
            s3client.deleteObject(request -> request
                    .bucket(bucketName)
                    .key(key));
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void replace(S3Client s3client, String oldBucketName, String oldKey, String newBucketName, String newKey) {
        copyInOtherPlace(s3client, oldBucketName, oldKey, newBucketName, newKey);
        deleteOne(s3client, oldBucketName, oldKey);
    }
}

