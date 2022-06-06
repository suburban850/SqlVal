package controller;

import com.opencsv.CSVReader;
import database.DatabaseImplementation;
import gui.MainFrame;
import resource.DBNode;
import utils.DBReader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class ExportAction extends DBAbstractAction{

    public ExportAction()
    {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
        ImageIcon icon = new ImageIcon(ExportAction.class.getResource("/img/exp.png"));

        putValue(SMALL_ICON,icon);
        putValue(NAME,"Export file");
        putValue(SHORT_DESCRIPTION, "Export action");
    }

    //todo!!!
    @Override
    public void actionPerformed(ActionEvent e) {
        //DBNode selected = MainFrame.getInstance().getSelectedNode();
        //model
        //model trigeruje database dodje do repositorya uzme result set za taj query i napise ga
        //DBReader dbReader = new DBReader();
        //uzmi result set
        DBNode selected = MainFrame.getInstance().getSelectedNode();
        System.out.println("NAZIV                  " + selected.getName());
       DatabaseImplementation dbi = (DatabaseImplementation) MainFrame.getInstance().getAppCore().getDatabase();
        JFileChooser fileChooser = new JFileChooser();
        File file=null;
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
             file = fileChooser.getSelectedFile();
            // save to file
        }
       dbi.zabr(selected.getName(),file);
    }
}
