package gui.toolbar;

import gui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ToolBar extends JToolBar {

    public ToolBar()
    {
        setFloatable(false);
        add(MainFrame.getInstance().getActionManager().getViewStateAction());
        add(MainFrame.getInstance().getActionManager().getEditorStateAction());
        add(MainFrame.getInstance().getActionManager().getBulkImportAction());
        add(MainFrame.getInstance().getActionManager().getExportAction());
        this.setBackground(new Color(0xB9EBF7FF, true));
        add(MainFrame.getInstance().getActionManager().getPrettyAction());

        add(MainFrame.getInstance().getActionManager().getRunAction());
    }

}
;