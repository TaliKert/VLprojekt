package com.kmk.imageboard.controller.api;

import com.kmk.imageboard.model.DTO.ImageInfoDTO;
import com.kmk.imageboard.model.DTO.SocialDTO;
import com.kmk.imageboard.service.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;

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

    @GetMapping("/image/{fileName}")
    public void getFullImage(HttpServletResponse response, @PathVariable String fileName) throws IOException {
        InputStream in = new FileInputStream("imagerepository" + File.separator + fileName);
        String fileExtMIME = imageService.getImageExtension(fileName.substring(0, fileName.indexOf('.')));
        if (fileExtMIME == null) {
            fileExtMIME = MediaType.IMAGE_JPEG_VALUE;
        }
        response.setContentType(fileExtMIME);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping(value = "/thumb/after/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageInfoDTO getThumbnailDataAfter(HttpServletResponse response, @PathVariable String id) {
        if (id.equals("undefined") || id.equals("null")) {
            return imageService.getNewestThumbnail();
        }

        ImageInfoDTO nextThumbnail = imageService.getNextThumbnail(id);
        if (nextThumbnail == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        return nextThumbnail;
    }

    @GetMapping(value = "/thumb/exact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageInfoDTO getThumbnailData(HttpServletResponse response, @PathVariable String id) {
        return imageService.getThumbnail(id);
    }

    @GetMapping(value = "/image/{id}/info")
    public SocialDTO getSocialInfo(Principal principal, @PathVariable String id) {
        return imageService.getSocialInfo(principal, Long.parseLong(id));
    }
}
