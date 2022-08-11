package org.eoghancorp.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileService {

    private static final String randy_path = "/Users/owenmcpartlan/Documents/randy/";
    public Resource getDirectoryContents(String path) {
        Resource directory = new FileSystemResource(randy_path + path);

        Boolean isDirectory = directory.isFile();

        System.out.println();

        return directory;

    }
}
