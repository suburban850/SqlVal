package BP_JDBC.src.main.java.resource.implementation;

import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.DBNodeComposite;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
public class Entity extends DBNodeComposite {

    public Entity(String name, DBNode parent) {
        super(name, parent);
    }

    @Override
    public void addChild(DBNode child) {
        if (child != null && child instanceof Attribute){
            Attribute attribute = (Attribute) child;
            this.getChildren().add(attribute);
        }

    }
}
