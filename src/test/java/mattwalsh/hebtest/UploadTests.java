package mattwalsh.hebtest;

import mattwalsh.hebtest.rest.ImageController;
import mattwalsh.hebtest.rest.ImageRequest;
import mattwalsh.hebtest.rest.ImageResponse;
import mattwalsh.hebtest.service.ObjectDetectionService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
class UploadTests {

    @Autowired
    ImageController imageController;

    @Autowired
    ObjectDetectionService objectDetectionService;

    private final static String validImageFile = "https://imagga.com/static/images/tagging/wolf-725380_640.jpg";
    private final static String validImageFileChecksum = "CA921455A98F4A4C02E4D0F192C307D4";
    private final static int validImageFileSize = 71913;

    enum UploadTestParam {
        // user label is provided
        _00(true, "mynewLabel", List.of("dog"), "mynewLabel"),
        _01(true, "mynewLabel", Collections.emptyList(), "mynewLabel"),
        _02(true, "mynewLabel", null, "mynewLabel"),
        _03(false, "mynewLabel", null, "mynewLabel"),

        // user label is NOT provided
        _10(true, "", List.of("cat"), "cat"),
        _11(true, null, List.of("cat"), "cat"),
        _12(true, " ", List.of("aardvark", "cat", "dog"), "aardvark"),
        _13(false, " ", List.of("aardvark", "cat", "dog"), validImageFileChecksum),

        // user label is NOT provided and object detection finds nothing
        _20(true, "", Collections.emptyList(), validImageFileChecksum),
        _21(true, " ", Collections.emptyList(), validImageFileChecksum),
        _22(true, null, Collections.emptyList(), validImageFileChecksum),
        _23(true, "", null, validImageFileChecksum),
        _24(true, " ", null, validImageFileChecksum),
        _25(true, null, null, validImageFileChecksum),
        _26(false, null, null, validImageFileChecksum),
        ;

        public final boolean objectDetectionEnabled;
        public final String labelProvided;
        public final List<String> objectsDetected;
        public final String labelExpected;

        UploadTestParam(boolean objectDetectionEnabled, String labelProvided,
                        List<String> objectsDetected, String labelExpected) {
            this.objectDetectionEnabled = objectDetectionEnabled;
            this.labelProvided = labelProvided;
            this.objectsDetected = objectsDetected;
            this.labelExpected = labelExpected;
        }
    }

    @ParameterizedTest
    @EnumSource(UploadTestParam.class)
    void testUpload(UploadTestParam param) {
        Mockito.when(objectDetectionService.detectObjects(any(byte[].class)))
                .thenReturn(Optional.ofNullable(param.objectsDetected));

        ImageRequest imageRequest = new ImageRequest(
                param.objectDetectionEnabled,
                param.labelProvided,
                validImageFile);

        ImageResponse imageResponse = imageController.postImage(imageRequest);

        assertThat(imageResponse.label()).isEqualTo(param.labelExpected);
        assertThat(imageResponse.imageData().length).isEqualTo(validImageFileSize);
        assertThat(imageResponse.imageDataChecksum()).isEqualTo(validImageFileChecksum);
    }

}
