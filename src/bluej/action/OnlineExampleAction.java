/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluej.action;

import bluej.env3d.BEnv;
import bluej.action.AbstractEnvAction;
import bluej.extensions.BlueJ;
import bluej.extensions.MissingJavaFileException;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import env3d.util.Sysutil;

/**
 *
 * @author jmadar
 */
public class OnlineExampleAction extends AbstractEnvAction {
    private BEnv env;
    private String exampleDir;

    public OnlineExampleAction(String menuName, String msg) {

        putValue(AbstractAction.NAME, menuName);
        exampleDir = msg;
    }
        
    public void actionPerformed(ActionEvent ae) {
        processLocalExample();
    }
    
    
    /**
     * Copy all files from the local examples directory to the current project
     * 
     */
    public void processLocalExample() {
        try {
            File fromDir = new File(bluej.getCurrentPackage().getDir().getAbsolutePath()+"/examples/lessons/"+exampleDir);
            File toDir = new File(bluej.getCurrentPackage().getDir().getAbsolutePath());
            Logger.getLogger(OnlineExampleAction.class.getName()).log(Level.INFO, 
                    "Copying from "+fromDir+" to "+toDir);
            
            for (File f : FileUtils.listFiles(fromDir, null, true)) {
                FileUtils.copyFileToDirectory(f, toDir);
                String fileName = f.getName();
                if (fileName.endsWith(".java")) {
                    try {
                        bluej.getCurrentPackage().newClass(fileName.substring(0, fileName.indexOf(".")));
                    } catch (MissingJavaFileException ex) {
                        Logger.getLogger(OnlineExampleAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(OnlineExampleAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProjectNotOpenException ex) {
            Logger.getLogger(OnlineExampleAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PackageNotFoundException ex) {
            Logger.getLogger(OnlineExampleAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void processOnlineExample() {
        try {
            String exampleUrl = prefs.getServerUrl()+"/examples/?dir="+exampleDir;
            String [] files = Sysutil.readUrl(exampleUrl).split("\n");

            for (String file : files) {
                File dir = bluej.getCurrentPackage().getDir();
                String fileName = dir.getAbsolutePath()+File.separator+file;

                createFile(file.substring(0, file.indexOf(".")), fileName, prefs.getServerUrl()+"/examples/"+exampleDir+"/"+file);
            }

            // Compile it
            //bluej.getCurrentPackage().compileAll(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(bluej.getCurrentFrame(), e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Create a file from the fileURL
     *
     * @param fileName
     * @param fileURL
     */
    public void createFile(String className, String fileName, String fileURL) throws Exception {
        System.out.println("Creating "+fileName);
        System.out.println("    from "+fileURL);

        File outFile = new File(fileName);
        if (!outFile.exists() || fileName.equals("world.env") || fileName.equals("world.txt")) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

            bw.write(Sysutil.readUrl(fileURL));
            bw.close();

            if (fileName.endsWith(".java")) {
                bluej.getCurrentPackage().newClass(className);
            }
        } else {
            throw new Exception("File already exists");
        }
    }

}
