package org.example;

import File.File;
import FileOperations.FileDAO;
import database.Database;
import org.jdbi.v3.core.Jdbi;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Jdbi jdbi = Database.getJdbi();
        Scanner scanner = new Scanner(System.in);

        String fileName = scanner.nextLine();
        String username = "alex";

        File file = jdbi.withExtension(FileDAO.class, dao ->
                dao.findFileByName(username, "%" + fileName + "%")
        );
        System.out.println(file);
    }
}