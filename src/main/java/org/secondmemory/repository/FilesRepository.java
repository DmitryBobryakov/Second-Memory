package org.secondmemory.repository;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;
import org.secondmemory.MyS3Client;
import org.secondmemory.Postgres;
import org.secondmemory.exception.DbSelectException;
import org.secondmemory.exception.NoSuchPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesRepository {
    private static final MinioClient client = MyS3Client.getClient();
    private static final Logger log = LoggerFactory.getLogger(FilesRepository.class);

    public List<String> getFileInfo(String id) throws DbSelectException {
        List<String> result = new ArrayList<>();
        try (ResultSet resultSet = Postgres.selectAllFromFileInfo(id);) {

            if (resultSet.next()) {

                String fileOwner = resultSet.getString("file_owner");
                String createdDate = resultSet.getString("created_date");
                String lastUpdate = resultSet.getString("last_update");
                String tags = resultSet.getString("tags");
                String accessRights = resultSet.getString("access_rights");

                result.add(fileOwner);
                result.add(createdDate);
                result.add(lastUpdate);
                result.add(tags);
                result.add(accessRights);

            }
        } catch (SQLException e) {
            log.error("ERROR: ", e);
            throw new DbSelectException("Cannot select data from DB");
        }

        return result;
    }

    public List<List<String>> getFilesInDirectory(String path, String bucket) throws Exception {
        Iterable<Result<Item>> results = MyS3Client.getFilesInDirectory(path, bucket);
        List<List<String>> processedData = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            processedData.add(
                    List.of(item.objectName(), item.lastModified().toString(), item.owner().toString(),
                            item.etag()));
        }

        return processedData;
    }
    public void upload(String bucketName, Part file)
            throws IOException,
            InsufficientDataException,
            ErrorResponseException,
            InvalidKeyException,
            InvalidResponseException,
            XmlParserException,
            NoSuchAlgorithmException,
            InternalException,
            NoSuchPathException {

        try {
            String fileName = file.getSubmittedFileName();
            InputStream fileInputStream = file.getInputStream();
            client.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                                    fileInputStream, -1, 10485760)
                            .build());

        } catch (ServerException e) {
            throw new NoSuchPathException("Файл слишком большого размера");
        }
    }

}