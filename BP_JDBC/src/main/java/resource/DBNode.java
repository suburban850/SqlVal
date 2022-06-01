package resource;


import lombok.*;
import observer.implementation.PublisherImplementation;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;

@Getter
@Setter
@NoArgsConstructor
public abstract class DBNode extends PublisherImplementation {

    private String name;
    @ToString.Exclude
    private DBNode parent;


    public DBNode(String name, DBNode parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof DBNode) {
            DBNode otherObj = (DBNode) obj;
            return this.getName().equals(otherObj.getName());
        }
        return false;
    }
}
