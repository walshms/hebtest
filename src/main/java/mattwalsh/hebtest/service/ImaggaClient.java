package mattwalsh.hebtest.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface ImaggaClient {

    String API_KEY = "acc_e65f93fbc757296";
    String API_SECRET = "8d630694569995777a4478ce44add59a";
    String API_IMAGGA = "https://api.imagga.com/v2";
    String API_TAGS_URL = "/tags";
    String API_UPLOAD_URL = "/uploads";

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

    record UploadRequest(@JsonProperty("image") String image) {
    }

    @RequestLine("POST " + API_UPLOAD_URL)
    UploadResponse upload(UploadRequest uploadRequest);

    @RequestLine("GET " + API_TAGS_URL)
    TagsResponse getTags(@Param("image_upload_id") String uploadId);

}
