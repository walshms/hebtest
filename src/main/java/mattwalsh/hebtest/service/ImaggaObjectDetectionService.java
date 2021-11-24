package mattwalsh.hebtest.service;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static mattwalsh.hebtest.service.ImaggaClient.*;

@Component
public class ImaggaObjectDetectionService implements ObjectDetectionService {

    @Override
    public Optional<List<String>> detectObjects(byte[] imageData) {
        ImaggaClient client = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(API_KEY, API_SECRET))
                .target(ImaggaClient.class, API_IMAGGA);
        UploadRequest uploadRequest = new UploadRequest(new String(imageData));
        ImaggaClient.UploadResponse upload = client.upload(uploadRequest);
        System.out.println(upload);
        return Optional.empty();
    }

}
