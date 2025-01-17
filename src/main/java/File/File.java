package File;

public class File {
    private int file_id;
    private int user_id;
    private String file_name;
    private String file_path;
    private String uploaded_at;

    public File(String file_name, String file_path, String uploaded_at, int file_id, int user_id) {
        this.file_name = file_name;
        this.file_path = file_path;
        this.uploaded_at = uploaded_at;
        this.file_id = file_id;
        this.user_id = user_id;
    }
}
