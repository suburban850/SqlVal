package controller;


import gui.MainFrame;
import lombok.SneakyThrows;
import observer.Notification;
import observer.enums.NotificationCode;
import resource.DBNode;
import resource.implementation.Entity;
import resource.implementation.InformationResource;
import state.EditorState;
import tree.TreeItem;
import utils.DBReader;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BulkImportAction extends DBAbstractAction{

    public BulkImportAction()
    {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
        ImageIcon icon = new ImageIcon(BulkImportAction.class.getResource("/img/imp.png"));
        putValue(SMALL_ICON,icon);
        putValue(NAME,"Bulk .csv import");
        putValue(SHORT_DESCRIPTION, "Bulk import");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);
        File selectedFile = null;
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
        }
        DBReader dbReader = new DBReader();
        List<String[]> rows;
        //InformationResource informationResource = (InformationResource) MainFrame.getInstance().getAppCore().getInformationResource();
        DBNode node = MainFrame.getInstance().getSelectedNode();
        if(!(node instanceof Entity)) return;

        try {
            rows = dbReader.read(new FileReader(selectedFile));
            MainFrame.getInstance().getAppCore().bulk(rows,(Entity)node);

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}







        /***
         *
        System.out.println("1");
        informationResource.addSubscriber(MainFrame.getInstance());
        System.out.println("                                       "+ selectedFile.getName());
        MainFrame.getInstance().getAppCore().addEntity(selectedFile.getName(),informationResource);


        try {
           rows = dbReader.read(new FileReader(selectedFile));

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("file not found or selected");
            fileNotFoundException.printStackTrace();
        }
    }

}
         */
