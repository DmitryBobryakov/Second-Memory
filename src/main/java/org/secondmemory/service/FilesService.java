package org.secondmemory.service;

import java.util.List;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import javax.servlet.http.Part;
import org.secondmemory.MyS3Client;
import org.secondmemory.exception.DbSelectException;
import org.secondmemory.exception.NoSuchFileException;
import org.secondmemory.exception.NoSuchPathException;
import org.secondmemory.repository.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesService {
    private static final MinioClient client = MyS3Client.getClient();
    private static final Logger log = LoggerFactory.getLogger(FilesService.class);
    private final FilesRepository filesRepository;

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public List<String> getFileInfo(String id) throws NoSuchFileException {
        try {
            return filesRepository.getFileInfo(id);
        } catch (DbSelectException e) {
            log.error("ERROR: ", e);
            throw new NoSuchFileException("Файл не найден");
        }
    }

    public List<List<String>> getFilesInDirectory(String path, String bucket) throws NoSuchPathException {
        try {
            return filesRepository.getFilesInDirectory(path, bucket);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new NoSuchPathException("Папка не найдена");
        }
    }
    public void upload(String bucketName, Part file) throws NoSuchPathException {
        try {
            boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            filesRepository.upload(bucketName, file);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw new NoSuchPathException("File not found");
        }
    }

}