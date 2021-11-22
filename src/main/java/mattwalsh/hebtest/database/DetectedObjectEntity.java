package mattwalsh.hebtest.database;

import javax.persistence.*;

@Entity
@Table(name = "detected_object")
public class DetectedObjectEntity {

    @Id
    private String name;

    public DetectedObjectEntity() {
    }

    public DetectedObjectEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
