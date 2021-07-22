package org.eoghancorp.Controllers;

import org.eoghancorp.ReadFile;
import org.eoghancorp.VideoStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;


@RestController
public class MediaController {

    VideoStreamingService service = new VideoStreamingService();

    // private String videoPath    = "/Users/owenmcpartlan/boost_lib/plex/videos/taskmaster.mp4";
    private String videoDir     = "/Users/owenmcpartlan/boost_lib/plex/";

    @GetMapping
    public String index() {
        return "index route";
    }

    @GetMapping(value = "/video", produces = "application/octet-stream")
    public ResponseEntity<ResourceRegion> serveVideo(@RequestHeader(value = "Range", required = false) String rangeHeader, @RequestParam String fileName ) throws IOException {
        return service.getVideoRegion(rangeHeader, videoDir, fileName);
    }

}
