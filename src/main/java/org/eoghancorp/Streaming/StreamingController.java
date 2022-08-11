package org.eoghancorp.Streaming;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class StreamingController {

    private StreamingService service = new StreamingService();


    // http://localhost:8080/streaming/series/the_office/season_04/
    // filePath could be series/the_office/season_04/episode_name.mp4

    @GetMapping(path = "/streaming", produces = "application/octet-stream")
    public ResponseEntity<ResourceRegion> serveVideo(@RequestHeader(value = "Range", required = false) String rangeHeader, @RequestParam String fileName ) throws IOException {
        return service.getVideoRegion(rangeHeader, fileName);
    }

}
