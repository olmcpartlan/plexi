package org.eoghancorp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFile {
    public String readFileFromPath(String path) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            System.out.println(content.length());
        }
        catch(Exception e) {
            System.out.println("There was a problem: " + e.getMessage());
        }

        return content;

    }


}
