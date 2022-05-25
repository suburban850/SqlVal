package BP_JDBC.src.main.java.tree.implementation;


import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.DBNodeComposite;
import BP_JDBC.src.main.java.resource.implementation.InformationResource;
import BP_JDBC.src.main.java.tree.Tree;
import BP_JDBC.src.main.java.tree.TreeItem;

import javax.swing.tree.DefaultTreeModel;
import java.util.List;

public class TreeImplementation implements Tree {

    @Override
    public DefaultTreeModel generateTree(InformationResource informationResource) {

        TreeItem root = new TreeItem(informationResource, informationResource.getName());
        connectChildren(root);
        return new DefaultTreeModel(root);
    }


    private void connectChildren(TreeItem current){

        if (!(current.getDbNode() instanceof DBNodeComposite)) return;

        List<DBNode> children = ((DBNodeComposite) current.getDbNode()).getChildren();
        for (int i = 0; i<children.size();i++){
            TreeItem child = new TreeItem(children.get(i), children.get(i).getName());
            current.insert(child,i);
            connectChildren(child);
        }

    }

}
