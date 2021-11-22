package mattwalsh.hebtest.service;

import mattwalsh.hebtest.ChecksumUtil;
import mattwalsh.hebtest.database.ImageEntity;
import mattwalsh.hebtest.database.ImageRepository;
import mattwalsh.hebtest.rest.ImageRequest;
import mattwalsh.hebtest.rest.ImageResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Component
public class ImageService {

    @Autowired
    private final ObjectDetectionService objectDetectionService;

    @Autowired
    private final ImageRepository imageRepository;

    public ImageService(ObjectDetectionService objectDetectionService, ImageRepository imageRepository) {
        this.objectDetectionService = objectDetectionService;
        this.imageRepository = imageRepository;
    }

    public List<ImageResponse> getAllImages() {
        return imageRepository.findAll().stream().map(ImageEntity::asImageResponse).toList();
    }

    public List<ImageResponse> getAllImagesByObject(String[] objects) {
        return null;
    }

    public ImageResponse getImageById(UUID imageId) {
        return imageRepository.findById(imageId)
                .map(ImageEntity::asImageResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    public ImageResponse processImageRequest(ImageRequest imageRequest) {
        byte[] imageData = getBytesOrFail(imageRequest.image());
        List<String> detectedObjects = getDetectedObjectsIfEnabled(imageRequest.enableObjectDetection(), imageData);
        ImageEntity newEntity = new ImageEntity(
                imageData,
                imageRequest.label(),
                detectedObjects);
        return this.imageRepository.save(newEntity).asImageResponse();
    }

    private List<String> getDetectedObjectsIfEnabled(boolean enableObjectDetection, byte[] imageData) {
        if (enableObjectDetection) {
            return objectDetectionService.detectObjects(imageData)
                    .map(stringList -> stringList.stream()
                            .map(String::trim)
                            .filter(StringUtils::hasText)
                            .toList())
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    private byte[] getBytesOrFail(String image) {
        return imageAsFile(image)
                .orElse(imageFromUrl(image)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    }

    private Optional<byte[]> imageAsFile(String image) {
        try {
            return Optional.of(Base64.getDecoder().decode(image));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Optional<byte[]> imageFromUrl(String image) {
        try {
            InputStream inputStream = new URL(image).openStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] byteArray = IOUtils.toByteArray(bufferedInputStream);
            return Optional.of(byteArray);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
