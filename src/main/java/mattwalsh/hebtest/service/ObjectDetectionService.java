package mattwalsh.hebtest.service;

import java.util.List;
import java.util.Optional;

public interface ObjectDetectionService {

    Optional<List<String>> detectObjects(byte[] imageData);

}
