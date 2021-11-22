package mattwalsh.hebtest.database;

import mattwalsh.hebtest.rest.ImageResponse;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "image")
public class ImageEntity {

    @Id
    @Column(name = "id", nullable = false)
    public UUID id;
    public byte[] imageData;
    public String imageDataChecksum;
    public String label;
    @ManyToMany(cascade = CascadeType.ALL)
    public List<DetectedObjectEntity> detectedObjects;

    public ImageEntity() {
    }

    public ImageEntity(UUID id, byte[] imageData, String checksum, String label, List<String> detectedObjects) {
        this.id = id;
        this.imageData = imageData;
        this.imageDataChecksum = checksum;
        this.label = label;
        this.detectedObjects = detectedObjects.stream()
                .map(DetectedObjectEntity::new)
                .toList();
    }

    public ImageResponse asImageResponse() {
        return new ImageResponse(
                this.id,
                this.imageData,
                this.imageDataChecksum,
                this.label,
                this.detectedObjects.stream()
                        .map(DetectedObjectEntity::getName)
                        .toList()
        );
    }
}
