/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluej.action;

import bluej.ProgressBar;
import bluej.action.AbstractEnvAction;
import bluej.extensions.BlueJ;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import env3d.util.Sysutil;

/**
 *
 * @author jmadar
 */
public class ExtractAssetsAction extends AbstractEnvAction {

    public ExtractAssetsAction(String menuName) {
        
        putValue(AbstractAction.NAME, menuName);        
    }    

    public void actionPerformed(ActionEvent ae) {
        extractModels();
    }
    
    public static void extractModels() {
        try {
            // Check to see if unpacking is necessary
            String projectDir = "";
            Frame currentFrame = null;
            if (bluej != null) {                
                projectDir = bluej.getCurrentPackage().getDir().toString() + File.separator;
                currentFrame = bluej.getCurrentFrame();
            }
            File characterPak = new File(projectDir + "env3d_characterpack.jar.lzma");
            File charaacterJar = new File(projectDir + "env3d_characterpack.jar");
            System.out.println("Extracting package " + characterPak);
            if (characterPak.isFile()) {
                ProgressBar progress = new ProgressBar(currentFrame, "Extracting models");
                progress.setProgress(30, "Uncompressing models");
                Sysutil.unCompressFile(characterPak.getCanonicalPath());
                progress.setProgress(50, "Unpacking models");
                Sysutil.unjar(charaacterJar, new File(projectDir + "models"));
                progress.setProgress(90, "Cleaning up");
                // Delete it as it is no longer needed
                characterPak.delete();
                charaacterJar.delete();
                progress.setProgress(100, "Done");
                JOptionPane.showMessageDialog(currentFrame, "Models extracted successfully", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(currentFrame, "Models already extracted", "", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public static void main(String[] args) {
        ExtractAssetsAction.extractModels();
    }
            
}
