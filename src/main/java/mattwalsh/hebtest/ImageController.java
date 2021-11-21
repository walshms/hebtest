package mattwalsh.hebtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

@RestController
// todo specify json for return types
public class ImageController {

    @Autowired
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/")
    public String test() {
        return "howdy";
    }

    @GetMapping(value = "/images")
    public List<ImageResponse> getAllImages() {
        return this.imageService.getAllImages();
    }

    @GetMapping(value = "/images/{imageId}")
    public ImageResponse getImageById(@PathParam("imageId") UUID imageId) {
        logMessage("Received imageId:" + imageId.toString());
        return this.imageService.getImageById(imageId);
    }

    @PostMapping(value = "/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageResponse postImage(@ModelAttribute ImageRequest imageRequest) {
        System.out.println("found file: " + imageRequest.getImage());
        return this.imageService.createImage(imageRequest);
    }

    private String getDefaultLabel() {
        return "IMAGE_" + System.currentTimeMillis();
    }

    // todo make logger
    private void logMessage(String message) {
        System.out.println(message);
    }

}
