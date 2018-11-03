package com.kmk.imageboard.service;

import com.kmk.imageboard.model.DTO.ImageDTO;
import com.kmk.imageboard.model.Image;
import com.kmk.imageboard.repository.ImageRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserService userService;

    public ResponseEntity<String> uploadImage(Principal principal, MultipartFile file) throws IOException {
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            return ResponseEntity.unprocessableEntity().body("Bad file type!");
        }
        Long uploaderId = userService.getUser(principal).getId();
        Image newEntity = imageRepository.save(new Image(uploaderId));


        BufferedImage bufferedImage = createImageFromBytes(file.getBytes());
        int edge = Math.min(bufferedImage.getWidth(), bufferedImage.getHeight());
        BufferedImage thumbnailBufferedImage = Scalr.resize(
                Scalr.crop(bufferedImage, (bufferedImage.getWidth() - edge) / 2, (bufferedImage.getHeight() - edge) / 2, edge, edge),
                Scalr.Mode.AUTOMATIC,
                128,
                128);
        ImageIO.write(thumbnailBufferedImage, "jpg", new File("imagerepository" + File.separator + "thumbnails" + File.separator + newEntity.getId()));
        bufferedImage.flush();
        thumbnailBufferedImage.flush();

        try (InputStream inputStream = file.getInputStream()) {

            Files.copy(inputStream, Paths.get("imagerepository", String.valueOf(newEntity.getId())), StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok().body("Image uploaded!");
        }
    }

//    public List<ImageDTO> getInitialThumbnailIds() {
//        List<Image> imageList = imageRepository.findAll();
//        List<ImageDTO> idList = new ArrayList<>();
//        for (Image image : imageList) {
//            idList.add(new ImageDTO(image.getId()));
//        }
//        return idList;
//    }

    public ImageDTO getNextThumbnail(String id) {
        Image imageInfo = imageRepository.getNextThumb(Integer.parseInt(id));
        return new ImageDTO(imageInfo.getId());
    }

    public ImageDTO getNewestThumbnail() {
        Image imageInfo = imageRepository.getFirstThumb();
        return new ImageDTO(imageInfo.getId());
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
