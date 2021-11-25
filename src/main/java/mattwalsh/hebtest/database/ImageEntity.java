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
    private UUID id;
    private byte[] imageData;
    private String imageDataChecksum;
    private String label;
    @ElementCollection(targetClass = String.class)
    @CollectionTable()
    private List<String> detectedObjects;

    public ImageEntity() {
    }

    public ImageEntity(byte[] imageData, String imageLabel, List<String> detectedObjects) {
        this.id = UUID.randomUUID();
        this.imageData = imageData;
        this.imageDataChecksum = ChecksumUtil.getChecksum(imageData);
        this.label = getLabel(imageLabel, detectedObjects);
        this.detectedObjects = detectedObjects;
    }

    public ImageResponse asImageResponse() {
        return new ImageResponse(
                this.id,
                this.imageDataChecksum,
                this.label,
                this.detectedObjects
        );
    }

    private String getLabel(String label,
                            List<String> detectedObjects) {
        if (StringUtils.hasText(label)) {
            return label;
        }
        return detectedObjects.stream()
                .map(String::trim)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(this.imageDataChecksum);
    }
}
