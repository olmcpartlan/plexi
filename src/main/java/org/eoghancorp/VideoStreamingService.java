package org.eoghancorp;

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
public class VideoStreamingService {
    private static final long chunk_size = 1000000L;
    public ResponseEntity<ResourceRegion> getVideoRegion(String rangeHeader , String directory, String fileName) throws IOException {

        // Read the desired file into memory as a resource.
        // This will have to be provided by url query parameters or request body.
        Resource fileSystemResource = new FileSystemResource(directory + "/" + fileName);

        // Find the requested region WITHIN the video resource.
        ResourceRegion resourceRegion = getResourceRegion(fileSystemResource, rangeHeader);

        // Specify that this a partial response.
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                // Set content type.
                .contentType(MediaTypeFactory.getMediaType(fileSystemResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                // Attach the requested region.
                .body(resourceRegion);
    }

    private ResourceRegion getResourceRegion(Resource video, String httpHeaders) throws IOException {
        ResourceRegion resourceRegion = null;

        // Find the length (size) of the video.
        long contentLength = video.contentLength();
        int from = 0;
        int to = 0;
        if (StringUtils.isNotBlank(httpHeaders)) {
            // This is coming from the initial request.
            // The headers will contain the amount and position of the data required.
            String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
            from = Integer.valueOf(ranges[0]);

            // If there ARE specified bytes     - extract from the file object.
            if (ranges.length > 1) {
                to = Integer.valueOf(ranges[1]);
            }
            // If there AREN'T specified bytes  - send the whole thing
            else {
                to = (int) (contentLength - 1);
            }
        }

        if (from > 0) {
            // Find out where the actual chunk position is and set it to the return.
            long rangeLength = min(chunk_size, to - from + 1);
            resourceRegion = new ResourceRegion(video, from, rangeLength);
        } else {
            long rangeLength = min(chunk_size, contentLength);
            resourceRegion = new ResourceRegion(video, 0, rangeLength);
        }

        // Send the region!
        return resourceRegion;
    }
}
