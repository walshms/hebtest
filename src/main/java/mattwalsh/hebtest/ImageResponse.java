package mattwalsh.hebtest;

import java.util.UUID;

public class ImageResponse {

    public final UUID id;
    public final byte[] imageData;
    public final String label;
    public final String[] detectedObjects;

    public ImageResponse(UUID id, byte[] imageData, String label, String[] detectedObjects) {
        this.id = id;
        this.imageData = imageData;
        this.label = label;
        this.detectedObjects = detectedObjects;
    }
}
