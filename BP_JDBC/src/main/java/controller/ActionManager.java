package controller;

import lombok.Getter;
import lombok.Setter;

import java.awt.desktop.PreferencesEvent;
import java.net.URISyntaxException;

@Getter
@Setter
public class ActionManager {

    private EditorStateAction editorStateAction;
    private ViewStateAction viewStateAction;
    private BulkImportAction bulkImportAction;
    private ExportAction exportAction;
    private PrettyAction prettyAction;
    private RunAction runAction;

    public ActionManager()  {
        initActions();
    }

    private void initActions() {
        editorStateAction = new EditorStateAction();
        viewStateAction = new ViewStateAction();
        bulkImportAction = new BulkImportAction();
        exportAction =new ExportAction();
        prettyAction= new PrettyAction();
        runAction = new RunAction();
        exportAction.setEnabled(true);
        bulkImportAction.setEnabled(true);
        prettyAction.setEnabled(false);
        runAction.setEnabled(false);
    }

}
