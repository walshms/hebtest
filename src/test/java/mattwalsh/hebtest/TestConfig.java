package mattwalsh.hebtest;

import mattwalsh.hebtest.service.ObjectDetectionService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    @Primary
    public ObjectDetectionService objectDetectionService() {
        return Mockito.mock(ObjectDetectionService.class);
    }

}
