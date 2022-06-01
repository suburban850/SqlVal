package gui;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
@Getter
@Setter
@Data

public class EditorView extends JPanel {

    private JTextPane textArea;
    private JTable jTable;
    private JLabel jLabel;
    private Highlighter.HighlightPainter cyanPainter;
    private Highlighter.HighlightPainter redPainter;
    private JScrollPane jScrollPane;
    private JScrollPane scrollTable;

    public EditorView(JTable jTable)
    {
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        textArea = new JTextPane();
        //!!!

        jScrollPane= new JScrollPane(textArea);
        scrollTable = new JScrollPane(jTable);
        textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350 ));
         //jScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, MainFrame.getInstance().getHeight()/2));
        jLabel = new JLabel("database:  hr " + "row count: " + jTable.getRowCount() + " tim: 68");
        jLabel.setForeground(Color.BLUE);
        scrollTable.setMaximumSize(new Dimension(Integer.MAX_VALUE, MainFrame.getInstance().getHeight()/2 ));
        jLabel.setBackground(Color.BLACK);
        textArea.setAlignmentX(CENTER_ALIGNMENT);
        jLabel.setAlignmentX(CENTER_ALIGNMENT);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jScrollPane,scrollTable);
        splitPane.setOneTouchExpandable(true);

        splitPane.setDividerLocation(MainFrame.getInstance().getHeight()/2-20);
        splitPane.setEnabled(true);

        this.setAlignmentX(CENTER_ALIGNMENT);

        this.add(splitPane);

        //this.add(jLabel);

        this.repaint();
        this.revalidate();
    }

}
