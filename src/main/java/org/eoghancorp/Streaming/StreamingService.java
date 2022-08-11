package org.eoghancorp.Streaming;

import org.springframework.core.io.*;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import static java.lang.Math.min;

@Service
public class StreamingService {
    private static final String randy_path = "/Users/owenmcpartlan/Documents/randy/";
    private static final long chunk_size = 1000000L;
    public ResponseEntity<ResourceRegion> getVideoRegion(String rangeHeader , String fileName) throws IOException {

        // read file.
        Resource fileSystemResource = new FileSystemResource(randy_path + "/" + fileName);

        // send back a chunk of the file.
        ResourceRegion resourceRegion = getResourceRegion(fileSystemResource, rangeHeader);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                // Set content type.
                .contentType(MediaTypeFactory.getMediaType(fileSystemResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
    }

    private ResourceRegion getResourceRegion(Resource video, String httpHeaders)  {
        try {
        ResourceRegion resourceRegion = null;

        long contentLength = video.contentLength();
        long from = 0;
        long to = 0;

        if (StringUtils.isNotBlank(httpHeaders)) {
            String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
            from = Integer.valueOf(ranges[0]);

            if (ranges.length > 1) {
                to = Integer.valueOf(ranges[1]);
            }
            else {
                to =  (contentLength - 1);
            }
        }

        // not the first iteration.
        if (from > 0) {
            long rangeLength = (min(chunk_size, to - from + 1));

            System.out.println();

            resourceRegion = new ResourceRegion(video, from, rangeLength);
        }
        // first iteration.
        else {
            long rangeLength = min(chunk_size, contentLength);
            resourceRegion = new ResourceRegion(video, 0, rangeLength);
        }

        return resourceRegion;

        }
        catch(Exception exception) {
            System.out.println();

            return null;

        }
    }




}
