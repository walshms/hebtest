package mattwalsh.hebtest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class ImageService {

    private final static String[] DEFAULT_DETECTED_OBJECTS = {};
    private final static Boolean DEFAULT_ENABLE_OBJECT_DETECTION = Boolean.FALSE;

    @Autowired
    private final ObjectDetectionService objectDetectionService;

    public ImageService(ObjectDetectionService objectDetectionService) {
        this.objectDetectionService = objectDetectionService;
    }

    private final Map<UUID, ImageEntity> tempMap = new HashMap<>();

    public List<ImageResponse> getAllImages() {
        return tempMap.values().stream().map(ImageEntity::asImageResponse).toList();
    }

    public ImageResponse getImageById(UUID imageId) {
        ImageEntity image = tempMap.get(imageId);
        if (image != null) {
            return image.asImageResponse();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    public ImageResponse processImageRequest(ImageRequest imageRequest) {
        UUID id = UUID.randomUUID();
        byte[] imageData = getBytesOrFail(imageRequest.getImage());
        String label = getLabel(imageRequest);
        String[] detectedObjects = getDetectedObjectsIfEnabled(imageRequest);
        ImageEntity newEntity = new ImageEntity(
                id,
                imageData,
                label,
                detectedObjects
        );
        this.tempMap.put(newEntity.id, newEntity);
        return newEntity.asImageResponse();
    }

    // todo finish this
    private String getLabel(ImageRequest imageRequest) {
        return "IMAGE" + System.currentTimeMillis();
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
        } catch (IllegalArgumentException e){
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
