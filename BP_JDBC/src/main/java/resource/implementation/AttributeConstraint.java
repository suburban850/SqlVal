package BP_JDBC.src.main.java.resource.implementation;

import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.enums.ConstraintType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class AttributeConstraint extends DBNode {

    private ConstraintType constraintType;

    public AttributeConstraint(String name, DBNode parent, ConstraintType constraintType) {
        super(name, parent);
        this.constraintType = constraintType;
    }

}
