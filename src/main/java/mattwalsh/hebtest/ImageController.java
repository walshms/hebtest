package mattwalsh.hebtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

@RestController
// todo specify json for return types
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

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
        logger.info("Received imageId:" + imageId.toString());
        return this.imageService.getImageById(imageId);
    }

    @PostMapping(value = "/images",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageResponse postImage(@RequestBody ImageRequest imageRequest) {
        logger.info("found file: " + imageRequest.getImage());
        logger.info(imageRequest.toString());
        return this.imageService.processImageRequest(imageRequest);
    }

    private String getDefaultLabel() {
        return "IMAGE_" + System.currentTimeMillis();
    }

}