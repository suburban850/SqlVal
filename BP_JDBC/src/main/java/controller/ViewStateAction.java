package controller;


import gui.MainFrame;
import observer.Notification;
import observer.enums.NotificationCode;
import state.ViewState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ViewStateAction extends DBAbstractAction{

    public ViewStateAction()
    {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
        ImageIcon icon = new ImageIcon(ViewStateAction.class.getResource("/img/dbb.png"));

        putValue(LARGE_ICON_KEY,icon);
        putValue(NAME,"Database mode");
        putValue(SHORT_DESCRIPTION, "Database mode");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       // MainFrame.getInstance().startViewState();
        Notification notification = new Notification(NotificationCode.VIEW,1);
        try {
            MainFrame.getInstance().update(notification);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
