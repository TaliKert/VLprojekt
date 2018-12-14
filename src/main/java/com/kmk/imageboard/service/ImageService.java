package com.kmk.imageboard.service;

import com.kmk.imageboard.model.DTO.CommentDTO;
import com.kmk.imageboard.model.DTO.ImageInfoDTO;
import com.kmk.imageboard.model.DTO.SocialDTO;
import com.kmk.imageboard.model.Image;
import com.kmk.imageboard.repository.ImageRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
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
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserService userService;

    @Autowired
    CommentRatingService commentRatingService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //    TODO refactor spaghetti
    public ResponseEntity<String> uploadImage(Principal principal, MultipartFile file) throws IOException {
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            return ResponseEntity.status(415).body("Bad file type!");
        }
        Long uploaderId = userService.getUser(principal).getId();
        String fileExtMIME = file.getContentType();
        Image newEntity = imageRepository.save(new Image(uploaderId, fileExtMIME));

        String fileExt = mimeTypeToExtension(fileExtMIME);

        BufferedImage bufferedImage = createImageFromBytes(file.getBytes());
        int edge = Math.min(bufferedImage.getWidth(), bufferedImage.getHeight());
        BufferedImage thumbnailBufferedImage = Scalr.resize(
                Scalr.crop(bufferedImage, (bufferedImage.getWidth() - edge) / 2, (bufferedImage.getHeight() - edge) / 2, edge, edge),
                Scalr.Mode.AUTOMATIC,
                128,
                128);
        try {
            ImageIO.write(thumbnailBufferedImage, "jpg", new File("imagerepository" + File.separator + "thumbnails" + File.separator + newEntity.getId()));
        } catch (IIOException e) {
            imageRepository.delete(newEntity);
            throw e;
        }
        bufferedImage.flush();
        thumbnailBufferedImage.flush();

        messagingTemplate.convertAndSend("/topic/streamupdate", new ImageInfoDTO(newEntity.getId(), newEntity.getId() + fileExt));

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get("imagerepository", String.valueOf(newEntity.getId()) + fileExt), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok().body("/entry/" + String.valueOf(newEntity.getId()));
        }
    }

    public ImageInfoDTO getThumbnail(String id) {
        Image imageInfo = imageRepository.getThumbnail(Integer.parseInt(id));
        if (imageInfo == null) {
            return null;
        }
        return new ImageInfoDTO(imageInfo.getId(), imageInfo.getId() + mimeTypeToExtension(imageInfo.getFileExtension()));
    }

    public ImageInfoDTO getPreviousThumbnail(String id) {
        Image imageInfo = imageRepository.findFirstByIdGreaterThanOrderByIdDesc(Long.parseLong(id));
        if (imageInfo == null) {
            return null;
        }
        return new ImageInfoDTO(imageInfo.getId(), imageInfo.getId() + mimeTypeToExtension(imageInfo.getFileExtension()));
    }

    public ImageInfoDTO getNextThumbnail(String id) {
        Image imageInfo = imageRepository.findFirstByIdLessThanOrderByIdDesc(Long.parseLong(id));
        if (imageInfo == null) {
            return null;
        }
        return new ImageInfoDTO(imageInfo.getId(), imageInfo.getId() + mimeTypeToExtension(imageInfo.getFileExtension()));
    }

    public ImageInfoDTO getNewestThumbnail() {
        Image imageInfo = imageRepository.getFirstThumb();
        return new ImageInfoDTO(imageInfo.getId(), imageInfo.getId() + mimeTypeToExtension(imageInfo.getFileExtension()));
    }

    public Image getImageById(long id) {
        return imageRepository.findImageById(id);
    }

    public boolean imageExists(long id) {
        return imageRepository.existsById(id);
    }

    public Integer getUserUploadCount(String username) {
        return imageRepository.getUploaderUploadCount(username);
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getImageExtension(String id) {
        Optional<Image> image = imageRepository.findById(Long.parseLong(id));
        if (image.isPresent()) return image.get().getFileExtension();
        else return null;
    }

    private String mimeTypeToExtension(String mime) {
        return mime.equals("image/png") ? ".png" : ".jpg";
    }

    public SocialDTO getSocialInfo(Principal principal, long imageId) {

        Optional<Image> imageOptional = imageRepository.findById(imageId);
        Image image;
        if (imageOptional.isPresent()) image = imageOptional.get();
        else throw new RuntimeException("Image not found");

        boolean isAuthor = false;
        Boolean userRating = null;
        if (principal != null) {
            userRating = commentRatingService.getUserRating(userService.getUser(principal), imageRepository.findImageById(imageId));
            isAuthor = image.getUploaderId() == userService.getUser(principal).getId();
        }

        String authorName = userService.getUser(image.getUploaderId()).getUsername();

        long rating = commentRatingService.getRating(image);

        List<CommentDTO> commentDTOs = commentRatingService.getCommentDTOs(image);

        return new SocialDTO(isAuthor, authorName, rating, userRating, commentDTOs);
    }
}
