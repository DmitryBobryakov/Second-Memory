package org.example;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectNotInActiveTierErrorException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@UtilityClass
@Slf4j
public final class S3FilesUtils {

    private static final S3Client client = MyS3Client.getClient();

    public static void changeName(String bucketName, String oldKey, String newKey) throws ObjectNotInActiveTierErrorException {
        try {
            client.copyObject(request -> request
                                             .sourceBucket(bucketName)
                                             .sourceKey(oldKey)
                                             .destinationBucket(bucketName)
                                             .destinationKey(newKey));
        } catch (S3Exception e) {
            log.error("Cannot copy in bucket: {} from: {} to: {}", bucketName, oldKey, newKey, e);
            throw new RuntimeException(e);
        }
    }

    public static void copyInOtherPlace(String oldBucketName, String oldKey, String newBucketName, String newKey) {
        try {
            client.copyObject(request -> request
                                             .sourceBucket(oldBucketName)
                                             .sourceKey(oldKey)
                                             .destinationBucket(newBucketName)
                                             .destinationKey(newKey));
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteOne(String bucketName, String key) {
        try {
            client.deleteObject(request -> request
                                               .bucket(bucketName)
                                               .key(key));
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void replace(S3Client s3client, String oldBucketName, String oldKey, String newBucketName, String newKey) {
        copyInOtherPlace(s3client, oldBucketName, oldKey, newBucketName, newKey);
        deleteOne(oldBucketName, oldKey);
    }
}

