package org.example;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3FilesUtilsTest {

    private static final String TEST_BUCKET = "test-bucket";
    private static final String TEST_BUCKET_2 = "test-bucket-2";
    private static final String TEST_FILE = "test-file.txt";
    private static final String TEST_FILE_CONTENT = "Test file content";
    private static final MinioClient client = MyS3Client.getClient();

    @BeforeEach
    void setUp() throws Exception {
        createTestBucketAndFile(TEST_BUCKET, TEST_FILE);
        createTestBucketAndFile(TEST_BUCKET_2, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        deleteTestBucketAndFile(TEST_BUCKET, TEST_FILE);
        deleteTestBucketAndFile(TEST_BUCKET, TEST_FILE.replace(".txt", "_renamed.txt"));
        deleteTestBucketAndFile(TEST_BUCKET, "copied_" + TEST_FILE);
        deleteTestBucketAndFile(TEST_BUCKET_2, TEST_FILE);
        deleteTestBucketAndFile(TEST_BUCKET, "delete_1.txt");
        deleteTestBucketAndFile(TEST_BUCKET, "delete_2.txt");
        deleteTestBucketAndFile(TEST_BUCKET, "delete_3.txt");
    }

    private void createTestBucketAndFile(String bucketName, String fileName) throws Exception {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        if (fileName != null) {
            InputStream stream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
            client.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                            stream, -1, 10485760)
                    .build());
        }
    }

    private void deleteTestBucketAndFile(String bucketName, String fileName) throws Exception {
        if (client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {

            if (fileName != null) {
                try {
                    client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
                } catch (Exception ignored) {
                }
            }
        }

    }

    @Test
    void changeName_success() throws Exception {
        String newFileName = TEST_FILE.replace(".txt", "_renamed.txt");

        S3FilesUtils.changeName(TEST_BUCKET, newFileName, TEST_FILE);

        verify(client, times(1)).copyObject(any(CopyObjectArgs.class));

    }

    @Test
    void changeName_exception() throws Exception {
        String newFileName = TEST_FILE.replace(".txt", "_renamed.txt");
        assertThrows(ServerException.class, () -> S3FilesUtils.changeName(TEST_BUCKET, newFileName, TEST_FILE));
    }

    @Test
    void copyInOtherPlace_success() throws Exception {
        S3FilesUtils.copyInOtherPlace(TEST_BUCKET_2, TEST_FILE, TEST_BUCKET, "copied_" + TEST_FILE);

        verify(client, times(1)).copyObject(any(CopyObjectArgs.class));

    }

    @Test
    void copyInOtherPlace_exception() throws Exception {
        assertThrows(ServerException.class, () -> S3FilesUtils.copyInOtherPlace(TEST_BUCKET_2, TEST_FILE, TEST_BUCKET, "copied_" + TEST_FILE));
    }

    @Test
    void deleteOne_success() throws Exception {
        S3FilesUtils.deleteOne(TEST_BUCKET, TEST_FILE);
        verify(client, times(1)).removeObject(any(RemoveObjectArgs.class));

    }

    @Test
    void deleteOne_exception() throws Exception {

        assertThrows(ServerException.class, () -> S3FilesUtils.deleteOne(TEST_BUCKET, TEST_FILE));
    }

    @Test
    void deleteSet_success() throws Exception {
        List<DeleteObject> objects = new ArrayList<>();
        objects.add(new DeleteObject("delete_1.txt"));
        objects.add(new DeleteObject("delete_2.txt"));
        objects.add(new DeleteObject("delete_3.txt"));

        for (DeleteObject object : objects) {
            createTestBucketAndFile(TEST_BUCKET, object.toString());
        }

        List<Result<DeleteError>> mockResults = objects.stream()
                .map(obj -> new Result<>((DeleteError) null))
                .collect(Collectors.toList());
        when(client.removeObjects(any(RemoveObjectsArgs.class))).thenReturn(mockResults);
        S3FilesUtils.deleteSet(TEST_BUCKET, objects);

        verify(client, times(1)).removeObjects(any(RemoveObjectsArgs.class));

    }

    @Test
    void deleteSet_exception() throws Exception {
        List<DeleteObject> objects = new ArrayList<>();
        objects.add(new DeleteObject("delete_1.txt"));
        objects.add(new DeleteObject("delete_2.txt"));
        objects.add(new DeleteObject("delete_3.txt"));

        for (DeleteObject object : objects) {
            createTestBucketAndFile(TEST_BUCKET, object.toString());
        }


        assertThrows(ServerException.class, () -> S3FilesUtils.deleteSet(TEST_BUCKET, objects));

    }


    @Test
    void replace_success() throws Exception {
        S3FilesUtils.replace(TEST_BUCKET, TEST_FILE, TEST_BUCKET_2, TEST_FILE);

        verify(client, times(1)).copyObject(any(CopyObjectArgs.class));
        verify(client, times(1)).removeObject(any(RemoveObjectArgs.class));

    }

    @Test
    void replace_exception_copy() throws Exception {
        assertThrows(ErrorResponseException.class, () -> S3FilesUtils.replace(TEST_BUCKET, TEST_FILE, TEST_BUCKET_2, TEST_FILE));
    }

    @Test
    void replace_exception_delete() throws Exception {
        assertThrows(ServerException.class, () -> S3FilesUtils.replace(TEST_BUCKET, TEST_FILE, TEST_BUCKET_2, TEST_FILE));
    }
}