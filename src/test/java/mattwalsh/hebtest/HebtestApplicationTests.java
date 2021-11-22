package mattwalsh.hebtest;

import mattwalsh.hebtest.rest.ImageController;
import mattwalsh.hebtest.rest.ImageRequest;
import mattwalsh.hebtest.rest.ImageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HebtestApplicationTests {

    @Autowired
    ImageController imageController;

    @Test
    void testUpload() {
        ImageResponse imageResponse = imageController.postImage(new ImageRequest(
                true,
                "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
        ));
        assertThat(imageResponse.imageData.length).isEqualTo(7108);
        assertThat(imageResponse.imageDataChecksum).isEqualTo("354C465D4DD665D5E0D9E1840D6E516A");
    }

}
