package controller;


import gui.MainFrame;
import observer.Notification;
import observer.enums.NotificationCode;
import state.EditorState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class EditorStateAction extends DBAbstractAction{

    private String getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            File f =  new File(resource.toURI());
            return f.getAbsolutePath();
        }

    }
    public EditorStateAction()  {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
        ImageIcon icon = new ImageIcon(EditorState.class.getResource("/img/sql.png"));

        putValue(LARGE_ICON_KEY, icon);
        putValue(NAME,"Editor mode");
        putValue(SHORT_DESCRIPTION, "Editor mode");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Notification notification = new Notification(NotificationCode.EDITOR,1);
        try {
            MainFrame.getInstance().update(notification);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
