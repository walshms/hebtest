package mattwalsh.hebtest;

public class ImageRequest {

    private boolean enableObjectDetection;
    private String image;

    public boolean isEnableObjectDetection() {
        return enableObjectDetection;
    }

    public void setEnableObjectDetection(boolean enableObjectDetection) {
        this.enableObjectDetection = enableObjectDetection;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ImageRequest{" +
                "enableObjectDetection=" + enableObjectDetection +
                ", image='" + image + '\'' +
                '}';
    }
}
