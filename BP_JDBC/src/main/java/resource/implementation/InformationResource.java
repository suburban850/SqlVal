package BP_JDBC.src.main.java.resource.implementation;

import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.DBNodeComposite;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
public class InformationResource extends DBNodeComposite {


    public InformationResource(String name) {
        super(name, null);
    }

    @Override
    public void addChild(DBNode child) {
        if (child != null && child instanceof Entity){
            Entity entity = (Entity) child;
            this.getChildren().add(entity);
        }
    }
}
