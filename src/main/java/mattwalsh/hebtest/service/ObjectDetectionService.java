package mattwalsh.hebtest.service;

import java.util.List;
import java.util.Optional;

public interface ObjectDetectionService {

    /**
     * implementing classes should return a list if successful
     *
     * it's expected to {@code throw new ResponseStatusException(HttpStatus.BAD_REQUEST)} can be thrown at any
     * point to indicate failure during transmission
     *
     * returning an empty list, or the Optional.empty() is seen as a valid response where the service received the image
     * but was unable to detect anything
     */
    Optional<List<String>> detectObjects(byte[] imageData);

}
