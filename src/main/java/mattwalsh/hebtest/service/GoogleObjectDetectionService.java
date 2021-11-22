package mattwalsh.hebtest.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GoogleObjectDetectionService implements ObjectDetectionService {

    @Override
    public Optional<List<String>> detectObjects(byte[] imageData) {
        throw new UnsupportedOperationException();
    }

}
