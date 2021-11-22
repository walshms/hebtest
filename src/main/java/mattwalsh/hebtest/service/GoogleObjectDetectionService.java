package mattwalsh.hebtest.service;

import org.springframework.stereotype.Component;

@Component
public class GoogleObjectDetectionService implements ObjectDetectionService {

    @Override
    public String[] detectObjects(byte[] imageData) {
        return new String[0];
    }

}
