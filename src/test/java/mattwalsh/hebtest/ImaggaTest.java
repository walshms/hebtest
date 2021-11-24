package mattwalsh.hebtest;

import mattwalsh.hebtest.service.ImaggaObjectDetectionService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import static mattwalsh.hebtest.HebtestApplication.LOG;

public class ImaggaTest {

    ImaggaObjectDetectionService imaggaObjectDetectionService = new ImaggaObjectDetectionService();

    byte[] byteArray;

    @BeforeEach
    public void setup() throws IOException {
        InputStream inputStream = new FileInputStream(new ClassPathResource("./wind-farm.jpg").getFile());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byteArray = Base64.getEncoder().encode(IOUtils.toByteArray(bufferedInputStream));
    }

    @Test
    public void testImaggaTagging() {
        Optional<List<String>> strings = imaggaObjectDetectionService.detectObjects(byteArray);
//        LOG.info("received: " + strings.orElse(Collections.emptyList()));
    }

}
