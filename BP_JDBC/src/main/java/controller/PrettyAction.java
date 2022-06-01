package controller;


import gui.MainFrame;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class PrettyAction extends DBAbstractAction{

    public PrettyAction()
    {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
        ImageIcon icon = new ImageIcon(PrettyAction.class.getResource("/img/prrr.png"));

        putValue(SMALL_ICON,icon );
        putValue(NAME,"Pretty action");
        putValue(SHORT_DESCRIPTION, "Pretty");
    }
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    // todo group by
    private boolean checkSQL(String s)
    {
        s=s.toLowerCase();
        return s.equals
                ("select") || s.equals("from") || s.equals("where")
                || s.equals("in") || s.equals("groupby") || s.equals("max")
                || s.equals("min") || s.equals("avg") || s.equals("count")
                || s.equals("having") || s.equals("order by") || s.equals("on");

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String s = MainFrame.getInstance().getEditorView().getTextArea().getText();
        System.out.println(s);

        String[] arr = s.split(" ");
        MainFrame.getInstance().getEditorView().getTextArea().removeAll();
        MainFrame.getInstance().getEditorView().getTextArea().setText("");


        for(int i =0;i<arr.length;i++)
        {
            if(checkSQL(arr[i]))
            {
                arr[i] = arr[i].toUpperCase();
                appendToPane(MainFrame.getInstance().getEditorView().getTextArea(),arr[i],Color.BLUE);
            }else{
                appendToPane(MainFrame.getInstance().getEditorView().getTextArea(),arr[i],Color.BLACK);
            }
            appendToPane(MainFrame.getInstance().getEditorView().getTextArea()," ",Color.BLACK);

        }

    }
}
