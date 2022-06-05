package resource.implementation;


import lombok.*;
import resource.DBNode;
import resource.DBNodeComposite;
import resource.enums.AttributeType;

import java.util.List;

@Getter
@Setter
//
public class Attribute extends DBNodeComposite {

    //enum za tip podataka
    private AttributeType attributeType;
    private int length;//duzina tipa
    private Attribute inRelationWith;//da li je u relaciji sa jos nekom tabelom strani kljuc

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
        //da li ima jedno ili vise ogranicenja
        if (child != null && child instanceof AttributeConstraint){
            AttributeConstraint attributeConstraint = (AttributeConstraint) child;
            this.getChildren().add(attributeConstraint);
        }
    }


}

