package tree.implementation;

import lombok.Getter;
import lombok.Setter;
import resource.DBNode;
import resource.DBNodeComposite;
import resource.implementation.InformationResource;
import tree.Tree;
import tree.TreeItem;

import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;
import java.util.List;
@Getter
@Setter
public class TreeImplementation implements Tree {

    TreeItem<DBNode> koren=null;
    DefaultTreeModel df = null;
    @Override
    public DefaultTreeModel generateTree(InformationResource informationResource) {

        TreeItem root = new TreeItem(informationResource, informationResource.getName());
        koren = root;
        connectChildren(root);
        df =  new DefaultTreeModel(root);
        return df;
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
