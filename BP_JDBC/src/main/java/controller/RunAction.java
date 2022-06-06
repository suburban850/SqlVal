package controller;

import database.Database;
import database.DatabaseImplementation;
import database.MYSQLrepository;
import gui.MainFrame;
import lombok.Data;
import resource.DBNode;
import resource.implementation.InformationResource;
import state.EditorState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.*;

@Data
public class RunAction extends DBAbstractAction{

    public RunAction()  {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
        ImageIcon icon = new ImageIcon(EditorState.class.getResource("/img/run.png"));
        putValue(SMALL_ICON, icon);
        putValue(NAME,"Run");
        putValue(SHORT_DESCRIPTION, "Run");
    }

    //za brisanje


    @Override
    public void actionPerformed(ActionEvent e) {
       String sqlStatement =  MainFrame.getInstance().getEditorView().getTextArea().getText();
       InformationResource ir = (InformationResource) MainFrame.getInstance().getAppCore().getInformationResource();
        System.out.println(ir);
        //MainFrame.getInstance().getAppCore().smt(sqlStatement);
        sqlStatement.toLowerCase();
        MainFrame.getInstance().getAppCore().run(sqlStatement.trim(), ir);

    }
}
