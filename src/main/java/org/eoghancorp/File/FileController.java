package org.eoghancorp.File;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
public class FileController {

    private FileService service = new FileService();


    // example  directoryPath = file/series/the_office/season_04/
    // or       directoryPath = file/series/the_office
    @GetMapping(path = "/file")
    public String getContents(@RequestParam String directoryPath) {
        Resource something = service.getDirectoryContents(directoryPath);
        return "fixt";
    }



}
