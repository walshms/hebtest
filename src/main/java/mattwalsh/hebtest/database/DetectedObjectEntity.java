package mattwalsh.hebtest.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "detected_object")
public class DetectedObjectEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    public DetectedObjectEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
