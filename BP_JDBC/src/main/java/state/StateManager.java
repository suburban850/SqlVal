package state;

import lombok.Getter;

import javax.swing.text.View;
@Getter
public class StateManager {

    private State curr;
    private ViewState viewState;
    private EditorState editorState;

    public StateManager()
    {
        viewState = new ViewState();
        editorState = new EditorState();

        curr=viewState;
    }
    public void setEditorState(){curr=editorState;}
    public void setViewState(){curr=viewState;}
}
