package mattwalsh.hebtest.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
}
