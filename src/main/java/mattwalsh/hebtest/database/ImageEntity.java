package mattwalsh.hebtest.database;

import mattwalsh.hebtest.ChecksumUtil;
import mattwalsh.hebtest.rest.ImageResponse;
import org.springframework.util.StringUtils;

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

    public ImageEntity(byte[] imageData, String imageLabel, List<String> detectedObjects) {
        this.id = UUID.randomUUID();
        this.imageData = imageData;
        this.imageDataChecksum = ChecksumUtil.getChecksum(imageData);
        this.label = getLabel(imageLabel, detectedObjects, this.imageDataChecksum);
        this.detectedObjects = detectedObjects.stream()
                .map(DetectedObjectEntity::new)
                .toList();
    }

    private String getLabel(String label,
                            List<String> detectedObjects,
                            String checksum) {
        if (StringUtils.hasText(label)) {
            return label;
        }
        return detectedObjects.stream()
                .map(String::trim)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(checksum);
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
