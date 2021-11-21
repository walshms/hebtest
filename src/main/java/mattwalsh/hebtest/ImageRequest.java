package mattwalsh.hebtest;

import org.springframework.web.multipart.MultipartFile;

public class ImageRequest {

    private boolean enableObjectDetection;
    private MultipartFile image;

    public boolean isEnableObjectDetection() {
        return enableObjectDetection;
    }

    public void setEnableObjectDetection(boolean enableObjectDetection) {
        this.enableObjectDetection = enableObjectDetection;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
