package mattwalsh.hebtest.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

@Component
public class ImaggaObjectDetectionService implements ObjectDetectionService {

    private final Logger logger = LoggerFactory.getLogger(ImaggaObjectDetectionService.class);

    private final Methanol client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String API_KEY = "acc_e65f93fbc757296";
    private final String API_SECRET = "8d630694569995777a4478ce44add59a";
    private final String API_IMAGGA = "https://api.imagga.com/v2";

    record UploadResponse(
            @JsonProperty("result") Result result,
            @JsonProperty("status") Status status) {
        public record Result(
                @JsonProperty("upload_id") String uploadId) {
        }
    }

    record TagsResponse(
            @JsonProperty("result") Result result,
            @JsonProperty("status") Status status) {
        public record Result(
                @JsonProperty("tags") List<Tag> tags) {
            public record Tag(
                    @JsonProperty("confidence") Double confidence,
                    @JsonProperty("tag") TagDescription tagDescription) {
                public record TagDescription(@JsonProperty("en") String englishDescription) {
                }
            }
        }
    }

    record Status(
            @JsonProperty("text") String text,
            @JsonProperty("type") String type) {
    }
    public ImaggaObjectDetectionService() {
        client = Methanol.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(30))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(API_KEY, API_SECRET.toCharArray());
                    }
                })
                .build();
    }

    @Override
    public Optional<List<String>> detectObjects(byte[] imageData) {
        try {
            UploadResponse response = postImage(imageData);
            return getTagsForImage(response);
        } catch (InterruptedException | IOException e) {
            logger.error("Could not upload image to Imagga", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private UploadResponse postImage(byte[] imageData)
            throws IOException, InterruptedException {
        MultipartBodyPublisher image = MultipartBodyPublisher.newBuilder()
                .formPart("image", ofByteArray(imageData))
                .build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_IMAGGA + "/uploads"))
                .POST(image)
                .build();
        HttpResponse<String> response = client.send(httpRequest, ofString());
        return objectMapper.readValue(response.body(), UploadResponse.class);
    }

    private Optional<List<String>> getTagsForImage(UploadResponse uploadResponse)
            throws IOException, InterruptedException {
        String uploadId = uploadResponse.result().uploadId();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_IMAGGA + "/tags?image_upload_id=" + uploadId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(httpRequest, ofString());
        TagsResponse tagsResponse = objectMapper.readValue(response.body(), TagsResponse.class);
        List<String> value = tagsResponse.result().tags().stream()
                .map(t -> t.tagDescription().englishDescription())
                .toList();
        return Optional.of(value);
    }

}
