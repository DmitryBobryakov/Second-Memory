package FileOperations;

import org.jdbi.v3.core.Jdbi;
import File.File;

import java.util.List;

public class FileDAO {

    private final Jdbi jdbi;

    public FileDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public File findFileByName(String username, String fileName) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT f.file_id, f.file_name, f.file_path, f.uploaded_at
                FROM files f
                JOIN users u ON f.user_id = u.user_id
                WHERE u.username = :username AND f.file_name ILIKE :fileName
            """)
                        .bind("username", username)
                        .bind("fileName", "%" + fileName + "%")
                        .mapToBean(File.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public List<File> findAllFilesByUser(String username) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT f.file_id, f.file_name, f.file_path, f.uploaded_at
                FROM files f
                JOIN users u ON f.user_id = u.user_id
                WHERE u.username = :username
            """)
                        .bind("username", username)
                        .mapToBean(File.class)
                        .list()
        );
    }

    public void addFile(int userId, String fileName, String filePath) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                INSERT INTO files (user_id, file_name, file_path) 
                VALUES (:userId, :fileName, :filePath)
            """)
                        .bind("userId", userId)
                        .bind("fileName", fileName)
                        .bind("filePath", filePath)
                        .execute()
        );
    }
}

