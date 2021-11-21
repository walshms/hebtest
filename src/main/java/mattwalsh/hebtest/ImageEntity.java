package mattwalsh.hebtest;

import java.util.UUID;

public class ImageEntity {

    public final UUID id;
    public final byte[] imageData;
    public final String label;
    public final String[] detectedObjects;

    public ImageEntity(UUID id, byte[] imageData, String label, String[] detectedObjects) {
        this.id = id;
        this.imageData = imageData;
        this.label = label;
        this.detectedObjects = detectedObjects;
    }

    public ImageResponse asImageResponse() {
        return new ImageResponse(this.id, this.imageData, this.label, this.detectedObjects);
    }
}
