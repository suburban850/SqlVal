package BP_JDBC.src.main.java.tree;


import BP_JDBC.src.main.java.resource.implementation.InformationResource;

import javax.swing.tree.DefaultTreeModel;

public interface Tree {

    DefaultTreeModel generateTree(InformationResource informationResource);

}
