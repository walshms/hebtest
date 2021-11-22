package mattwalsh.hebtest.rest;

public record ImageRequest(
        boolean enableObjectDetection,
        String label,
        String image) {

    @Override
    public String toString() {
        return "ImageRequest{" +
                "enableObjectDetection=" + enableObjectDetection +
                ", label='" + label + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
