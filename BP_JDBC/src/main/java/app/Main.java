package BP_JDBC.src.main.java.app;


import BP_JDBC.src.main.java.gui.MainFrame;

import javax.swing.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AppCore appCore = new AppCore();
        MainFrame mainFrame = MainFrame.getInstance();
        mainFrame.setAppCore(appCore);

    }

}
