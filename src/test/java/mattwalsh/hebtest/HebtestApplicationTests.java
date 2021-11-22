package mattwalsh.hebtest;

import mattwalsh.hebtest.rest.ImageController;
import mattwalsh.hebtest.rest.ImageRequest;
import mattwalsh.hebtest.rest.ImageResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HebtestApplicationTests {

    @Autowired
    ImageController imageController;

    private final static String imageFile = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png";
    private final static String imageFileChecksum = "354C465D4DD665D5E0D9E1840D6E516A";

    enum UploadTestParam {
        _0(true, "mynewLabel", "dog", "mynewLabel"),
        _1(true, "", "cat", "cat"),
        _2(true, "", "", imageFileChecksum);

        public final boolean objectDetectionEnabled;
        public final String labelProvided;
        public final String labelExpected;
        public final String objectDetected;

        UploadTestParam(boolean objectDetectionEnabled, String labelProvided,
                        String objectDetected, String labelExpected) {
            this.objectDetectionEnabled = objectDetectionEnabled;
            this.labelProvided = labelProvided;
            this.labelExpected = labelExpected;
            this.objectDetected = objectDetected;
        }
    }

    @ParameterizedTest
    @EnumSource(UploadTestParam.class)
    void testUpload(UploadTestParam param) {
        ImageResponse imageResponse = imageController.postImage(new ImageRequest(
                param.objectDetectionEnabled,
                param.labelProvided,
                imageFile
        ));
        assertThat(imageResponse.imageData.length).isEqualTo(7108);
        assertThat(imageResponse.label).isEqualTo(param.labelExpected);
        assertThat(imageResponse.imageDataChecksum).isEqualTo(imageFileChecksum);
    }

}
