package org.example.service;

import java.util.List;
import org.example.exception.DbSelectException;
import org.example.exception.NoSuchFileException;
import org.example.exception.NoSuchPathException;
import org.example.repository.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesService {

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
}