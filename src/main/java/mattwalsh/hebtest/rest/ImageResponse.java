package mattwalsh.hebtest.rest;

import java.util.List;
import java.util.UUID;

public class ImageResponse {

    public final UUID id;
    public final byte[] imageData;
    public final String imageDataChecksum;
    public final String label;
    public final List<String> detectedObjects;

    public ImageResponse(UUID id, byte[] imageData, String imageDataChecksum,
                         String label, List<String> detectedObjects) {
        this.id = id;
        this.imageData = imageData;
        this.imageDataChecksum = imageDataChecksum;
        this.label = label;
        this.detectedObjects = detectedObjects;
    }
}
