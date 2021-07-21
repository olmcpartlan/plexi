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
    private ResourceLoader loader;
    private static final long CHUNK_SIZE = 1000000L;
    public ResponseEntity<ResourceRegion> getVideoRegion(String rangeHeader , String directory) throws IOException {
        // Resource videoResource = new Resource(directory + "/taskmaster.mp4");
        Resource videoResource = new FileSystemResource(directory + "/taskmaster.mp4");
        ResourceRegion resourceRegion = getResourceRegion(videoResource, rangeHeader);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(videoResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
    }

    private ResourceRegion getResourceRegion(Resource video, String httpHeaders) throws IOException {
        ResourceRegion resourceRegion = null;

        long contentLength = video.contentLength();
        int fromRange = 0;
        int toRange = 0;
        if (StringUtils.isNotBlank(httpHeaders)) {
            String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
            fromRange = Integer.valueOf(ranges[0]);
            if (ranges.length > 1) {
                toRange = Integer.valueOf(ranges[1]);
            } else {
                toRange = (int) (contentLength - 1);
            }
        }

        if (fromRange > 0) {
            long rangeLength = min(CHUNK_SIZE, toRange - fromRange + 1);
            resourceRegion = new ResourceRegion(video, fromRange, rangeLength);
        } else {
            long rangeLength = min(CHUNK_SIZE, contentLength);
            resourceRegion = new ResourceRegion(video, 0, rangeLength);
        }

        return resourceRegion;
    }
}
