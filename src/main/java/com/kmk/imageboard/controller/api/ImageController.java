package com.kmk.imageboard.controller.api;

import com.kmk.imageboard.model.DTO.ImageDTO;
import com.kmk.imageboard.service.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping("/thumb/{id}")
    public void getThumbnailImage(HttpServletResponse response, @PathVariable String id) throws IOException {
        InputStream in = new FileInputStream("imagerepository" + File.separator + "thumbnails" + File.separator + id);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping(value = "/thumb/after/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageDTO getThumbnailDataAfter(HttpServletResponse response, @PathVariable String id) {
        if (id.equals("undefined") || id.equals("null")) {
            return imageService.getNewestThumbnail();
        }

        ImageDTO nextThumbnail = imageService.getNextThumbnail(id);
        if (nextThumbnail == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        return nextThumbnail;
    }


}
