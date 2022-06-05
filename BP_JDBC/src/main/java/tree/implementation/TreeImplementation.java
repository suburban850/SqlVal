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

    /***DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
     DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel()
     .getRoot();
     DefaultMutableTreeNode child = new DefaultMutableTreeNode(nodeToAdd);
     model.insertNodeInto(child, root, root.getChildCount());
     tree.scrollPathToVisible(new TreePath(child.getPath()));
     */

    public void addtoroot(TreeItem root, TreeItem node)
    {
        if(df == null) return;
        else{
            System.out.println(root.getName());
            System.out.println(node.getName());
            System.out.println(root == null);
            System.out.println(node==null);
            //df.insertNodeInto(node,root,root.getChildCount()-1);
            root.add(node);
        }
    }


}
