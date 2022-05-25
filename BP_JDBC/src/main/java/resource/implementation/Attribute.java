package BP_JDBC.src.main.java.resource.implementation;

import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.DBNodeComposite;
import BP_JDBC.src.main.java.resource.enums.AttributeType;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class Attribute extends DBNodeComposite {


    private AttributeType attributeType;
    private int length;
    private Attribute inRelationWith;

    public Attribute(String name, DBNode parent) {
        super(name, parent);
    }

    public Attribute(String name, DBNode parent, AttributeType attributeType, int length) {
        super(name, parent);
        this.attributeType = attributeType;
        this.length = length;
    }

    @Override
    public void addChild(DBNode child) {
        if (child != null && child instanceof AttributeConstraint){
            AttributeConstraint attributeConstraint = (AttributeConstraint) child;
            this.getChildren().add(attributeConstraint);
        }
    }


}

