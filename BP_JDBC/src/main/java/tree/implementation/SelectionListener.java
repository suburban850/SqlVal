package BP_JDBC.src.main.java.tree.implementation;


import BP_JDBC.src.main.java.gui.MainFrame;
import BP_JDBC.src.main.java.resource.implementation.Entity;
import BP_JDBC.src.main.java.tree.TreeItem;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class SelectionListener implements TreeSelectionListener {
    @Override
    public void valueChanged(TreeSelectionEvent e) {

        JTree tree = (JTree) e.getSource();
        TreeItem node = (TreeItem) tree.getLastSelectedPathComponent();
        /* if nothing is selected */
        if (node == null || !(node.getDbNode() instanceof Entity)) return;

        MainFrame.getInstance().getAppCore().readDataFromTable(node.getName());


    }
}
