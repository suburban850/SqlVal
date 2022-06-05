package gui;



import app.AppCore;
import controller.ActionManager;
import gui.toolbar.ToolBar;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import main.java.app.Main;
import observer.Notification;
import observer.Subscriber;
import observer.enums.NotificationCode;
import resource.DBNode;
import resource.implementation.Entity;
import resource.implementation.InformationResource;
import state.EditorState;
import state.StateManager;
import state.ViewState;
import tree.Tree;
import tree.TreeItem;
import tree.implementation.SelectionListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

@Getter
@Setter
@Data
public class MainFrame extends JFrame implements Subscriber {

    private static MainFrame instance = null;

    private AppCore appCore;
    private JTable jTable;
    private JScrollPane jsp;
    private JTree jTree;
    private JPanel left;
    private ToolBar toolBar;
    JLabel jLabel = new JLabel("Sta");

    private StateManager stateManager;
    private ActionManager actionManager;
    private EditorView editorView;


    private MainFrame() {
    }
    private void initActionManager()
    {
        actionManager = new ActionManager();
    }
    private void initStateManager(){stateManager = new StateManager();}


    public static MainFrame getInstance(){
        if (instance==null){
            instance=new MainFrame();
            instance.lookandfeel();
            instance.initActionManager();
            instance.initStateManager();
            instance.initialise();
            //instance.getAppCore().getInformationResource().addSubscriber(instance);
        }
        return instance;
    }

    private void lookandfeel()
    {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(MainFrame.getInstance(),"Are you sure you want to exit?","Exit",JOptionPane.YES_NO_OPTION);
                if(reply == JOptionPane.YES_OPTION)
                {
                    System.out.println("EXIT");
                    System.exit(0);
                }
            }

        });
        this.setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        UIDefaults uiDefaults = UIManager.getDefaults();
        uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.BLACK));
        uiDefaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.BLACK));
        this.setDefaultLookAndFeelDecorated(true);
        this.setUndecorated(false);
        setTitle("BP");
        repaint();
        revalidate();
    }

    public void startEditorState()
    {
        stateManager.setEditorState();
    }

    public void startViewState()
    {
        stateManager.setViewState();
    }

    private void initialise() {
        this.
        toolBar = new ToolBar();
        add(toolBar,BorderLayout.NORTH);
        jTable = new JTable();
        //jTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        //jTable.setFillsViewportHeight(true);
        this.add(new JScrollPane(jTable));
       // this.pack();
        this.setVisible(true);
        this.repaint();
        this.revalidate();

    }


    public void setAppCore(AppCore appCore) {

        this.appCore = appCore;
        this.jTable.setModel(appCore.getTableModel());

        initialiseTree();
        instance.treeIcon();
        this.appCore.addSubscriber(this);




        repaint();
        revalidate();
    }

    public void treeIcon() {

        UIManager.put("Tree.closedIcon", new ImageIcon("icons/nz1.png"));
        UIManager.put("Tree.openIcon", new ImageIcon("icons/nz2.png"));
        UIManager.put("Tree.leafIcon", new ImageIcon("icons/nz3.png"));
        SwingUtilities.updateComponentTreeUI(this.getJTree());
        repaint();
        revalidate();
    }
    private void initialiseTree() {
       // ((InformationResource)appCore.getInformationResource()).addSubscriber(this);
        DefaultTreeModel defaultTreeModel = appCore.loadResource();

        //addsubroot((InformationResource) appCore.getInformationResource());
        //instance.getAppCore().getInformationResource().addSubscriber(instance);
        jTree = new JTree(defaultTreeModel);
        jTree.setBackground(new Color(0xB9EBF7FF, true));
        jTree.addTreeSelectionListener(new SelectionListener());
        jsp = new JScrollPane(jTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        left = new JPanel(new BorderLayout());
        left.add(jsp, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(jTree);

        add(left, BorderLayout.WEST);

    }


    private void insertnode(TreeItem root,TreeItem node)
    {
        root.insert(node, 1);
    }

    public DBNode getSelectedNode()
    {
        return ((TreeItem<DBNode>)this.jTree.getLastSelectedPathComponent()).getDbNode();
    }


    public static void visitAllNodes(TreeNode node) {
        System.out.println(node);
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeItem n = (TreeItem) e.nextElement();
                visitAllNodes(n);
            }
        }
    }



    public void traverse(JTree tree) {
        TreeModel model = tree.getModel();
        if (model != null) {
            Object root = model.getRoot();
            System.out.println(root.toString());
            walk(model,root);
        }
        else
            System.out.println("Tree is empty.");
    }

    protected void walk(TreeModel model, Object o){
        int  cc;
        cc = model.getChildCount(o);
        for( int i=0; i < cc; i++) {
            Object child = model.getChild(o, i );
            if (model.isLeaf(child))
                System.out.println(child.toString());
            else {
                System.out.print(child.toString()+"--");
                walk(model,child );
            }
        }
    }

    @Override
    public void update(Notification notification) throws IOException {
        if(notification.getCode().equals(NotificationCode.EDITOR) && stateManager.getCurr() instanceof ViewState)
        {
            System.out.println("Treba sve da se obrise");
            this.getContentPane().removeAll();
            actionManager.getBulkImportAction().setEnabled(false);
            actionManager.getExportAction().setEnabled(false);
            actionManager.getPrettyAction().setEnabled(true);
            actionManager.getRunAction().setEnabled(true);
            startEditorState();
            if(toolBar==null)
            {
                toolBar=new ToolBar();
                this.add(toolBar, BorderLayout.NORTH);
            }else this.add(toolBar,BorderLayout.NORTH);
            editorView = new EditorView(jTable);
            this.add(editorView);

            this.repaint();
            this.revalidate();

        }else if(notification.getCode().equals(NotificationCode.VIEW) && (stateManager.getCurr() instanceof EditorState))
        {//
            this.getContentPane().removeAll();
            actionManager.getBulkImportAction().setEnabled(true);
            actionManager.getExportAction().setEnabled(true);
            actionManager.getPrettyAction().setEnabled(false);
            actionManager.getRunAction().setEnabled(false);
            startViewState();
            initialise();
            setAppCore(appCore);
            //this.pack();
            this.repaint();
            this.revalidate();

        }else if(notification.getCode().equals(NotificationCode.BULK))
        {
            try {

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }else if(notification.getCode().equals(NotificationCode.CREATE_NODE))
        {
            if(!(notification.getData() instanceof Entity)) return;
            else {
                appCore.addNode((Entity)notification.getData());
                DefaultTreeModel df = (DefaultTreeModel) jTree.getModel();
                traverse(jTree);

                jTree.expandRow(0);
            }
        }
    }
}
