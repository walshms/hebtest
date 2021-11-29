package mattwalsh.hebtest.database;

import mattwalsh.hebtest.rest.ImageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {

    @Query("SELECT i FROM ImageEntity i WHERE i.detectedObjects IN :objects")
    List<ImageResponse> findAllByDetectedObjects(String[] objects);

}
