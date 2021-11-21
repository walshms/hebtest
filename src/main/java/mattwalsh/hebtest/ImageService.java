package mattwalsh.hebtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

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


    public ImageResponse createImage(ImageRequest imageRequest) {
        UUID id = UUID.randomUUID();
        byte[] imageData = new byte[] {}; // getBytesOrFail(imageRequest.getImage());
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

    private String getLabel(ImageRequest imageRequest) {
        return "IMAGE" + System.currentTimeMillis();
    }

    private String[] getDetectedObjectsIfEnabled(ImageRequest imageRequest) {
        return DEFAULT_DETECTED_OBJECTS;
    }

    private byte[] getBytesOrFail(MultipartFile image) {
        try {
            return image.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read image data.");
        }
    }


}
