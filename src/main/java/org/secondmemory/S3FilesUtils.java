package org.secondmemory;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@UtilityClass
@Slf4j

public final class S3FilesUtils {

    private static final MinioClient client = Minio.getClient();

    public static void changeName(String bucketName, String oldKey, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        client.copyObject(
                CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newKey)
                        .source(
                                CopySource.builder()
                                        .bucket(bucketName)
                                        .object(oldKey)
                                        .build())
                        .build());
        deleteOne(bucketName, oldKey);
    }

    public static void copyInOtherPlace(String oldBucketName, String oldKey, String newBucketName, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        client.copyObject(
                CopyObjectArgs.builder()
                        .bucket(newBucketName)
                        .object(newKey)
                        .source(
                                CopySource.builder()
                                        .bucket(oldBucketName)
                                        .object(oldKey)
                                        .build())
                        .build());
        deleteOne(oldBucketName, oldKey);
    }

    public static void deleteOne(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        client.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(key).build());
    }

    public static void deleteSet(String bucketName, List<DeleteObject> set) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Iterable<Result<DeleteError>> results =
                client.removeObjects(
                        RemoveObjectsArgs.builder().bucket(bucketName).objects(set).build());
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            System.out.println(
                    "Error in deleting object " + error.objectName() + "; " + error.message());
        }
    }

    public static void replace(String oldBucketName, String oldKey, String newBucketName, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        copyInOtherPlace(oldBucketName, oldKey, newBucketName, newKey);
        deleteOne(oldBucketName, oldKey);
    }
}

