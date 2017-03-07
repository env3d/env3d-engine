/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluej.action;

import static bluej.action.AbstractEnvAction.bluej;
import static bluej.action.AbstractEnvAction.executeCommand;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;
import bluej.messageconsole.EnvConsole;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

/**
 *
 * @author jmadar
 */
public class CreateWebAppAction extends AbstractEnvAction {

    public CreateWebAppAction() {        
        String menuName = "Create Web Application";
        putValue(AbstractAction.NAME, menuName);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        final EnvConsole mc = new EnvConsole(bluej.getCurrentFrame(), "Creating web application");
        Thread t = new Thread() {
            @Override
            public void run() {                
                try {
                    String path = bluej.getCurrentPackage().getDir().getCanonicalPath()+"/transpiler";                    
                    executeCommand("./gradlew dist", new File(path));
                    mc.reset();
                } catch (ProjectNotOpenException ex) {
                    Logger.getLogger(CreateWebAppAction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PackageNotFoundException ex) {
                    Logger.getLogger(CreateWebAppAction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CreateWebAppAction.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }            
        };
        t.start();
        
    }
    
    public static void main(String[] args) {
        executeCommand("./gradlew dist", new File("/Users/jmadar/Documents/env3d/env3d-engine/dist/env3d_template/transpiler"));
    }
    
}
