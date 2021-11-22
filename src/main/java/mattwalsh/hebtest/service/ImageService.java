package mattwalsh.hebtest.service;

import mattwalsh.hebtest.ChecksumUtil;
import mattwalsh.hebtest.database.ImageRepository;
import mattwalsh.hebtest.database.ImageEntity;
import mattwalsh.hebtest.rest.ImageRequest;
import mattwalsh.hebtest.rest.ImageResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static java.lang.Boolean.FALSE;

@Component
public class ImageService {

    private final static String[] DEFAULT_DETECTED_OBJECTS = {};
    private final static Boolean DEFAULT_ENABLE_OBJECT_DETECTION = FALSE;

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
        UUID id = UUID.randomUUID();
        byte[] imageData = getBytesOrFail(imageRequest.image());
        String[] detectedObjects = getDetectedObjectsIfEnabled(imageRequest);
        String checksum = ChecksumUtil.getChecksum(imageData);
        String label = getLabel(imageRequest, detectedObjects, checksum);
        ImageEntity newEntity = new ImageEntity(
                id,
                imageData,
                checksum,
                label,
                detectedObjects
        );
        return this.imageRepository.save(newEntity).asImageResponse();
    }

    /**
     * @param imageRequest the constructed image request from the front-end
     * @return 1) provided label if exists
     * 2) detected objects if enabled
     * 3) checksum ???
     */
    private String getLabel(ImageRequest imageRequest,
                            String[] detectedObjects,
                            String checksum) {
        if (FALSE.equals(ObjectUtils.isEmpty(imageRequest.label()))) {
            return imageRequest.label();
        } else if(FALSE.equals(ObjectUtils.isEmpty(detectedObjects))){
            return detectedObjects[0];
        } else {
            return checksum;
        }
    }

    // todo finish this
    private String[] getDetectedObjectsIfEnabled(ImageRequest imageRequest) {
        return DEFAULT_DETECTED_OBJECTS;
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
