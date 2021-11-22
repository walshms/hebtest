package mattwalsh.hebtest.database;

import mattwalsh.hebtest.rest.ImageResponse;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
    @ManyToMany
    public List<DetectedObjectEntity> detectedObjects;

    public ImageEntity() {
    }

    public ImageEntity(UUID id, byte[] imageData, String label, String[] detectedObjects) {
        this.id = id;
        this.imageData = imageData;
        this.imageDataChecksum = getChecksum(imageData);
        this.label = label;
        this.detectedObjects = Arrays.stream(detectedObjects)
                .map(DetectedObjectEntity::new)
                .toList();
    }

    private String getChecksum(byte[] imageData) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(imageData);
            return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("");
        }
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
