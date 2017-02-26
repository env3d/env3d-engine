/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author jmadar
 */
public class SCTextEditor extends JDialog{

    private RSyntaxTextArea textArea = new RSyntaxTextArea();
    private String fileName;    
    
    public SCTextEditor(JFrame parent, String directory, String fileToEdit, boolean modal) {
        super(parent, fileToEdit, modal);
        this.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {
            }

            public void windowClosing(WindowEvent we) {
                save();
            }

            public void windowClosed(WindowEvent we) {
            }

            public void windowIconified(WindowEvent we) {
            }

            public void windowDeiconified(WindowEvent we) {
            }

            public void windowActivated(WindowEvent we) {
            }

            public void windowDeactivated(WindowEvent we) {
            }
        });
        
        fileName = fileToEdit;        
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        RTextScrollPane pane = new RTextScrollPane(textArea);
        try {
            String worldText = FileUtils.readFileToString(new File(directory+fileToEdit));
            textArea.setText(worldText);
        } catch (IOException e) {

        } 
            
        setLocation(parent.getLocation().x + 5, parent.getLocation().y + 5);
        setSize(640, 480);
        add(pane);
        setVisible(true);        
    }    
    
    public String getContent() {
        return textArea.getText();
    }
    
    /**
     * save the file
     */
    public void save() {
        try {
            FileUtils.writeStringToFile(new File(fileName), getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
