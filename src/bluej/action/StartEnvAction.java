package bluej.action;


import bluej.env3d.BEnv;
import bluej.action.AbstractEnvAction;
import bluej.extensions.BObject;
import bluej.extensions.BPackage;
import bluej.extensions.BlueJ;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import env3d.scenecreator.UI;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.lwjgl.input.Keyboard;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jmadar
 */
public class StartEnvAction extends AbstractEnvAction {

    private BEnv env;
    private static UI ui;
    private String msgHeader;
    private Logger logger = Logger.getLogger(StartEnvAction.class.getName());
    
    public StartEnvAction(String menuName, String msg) {        
        putValue(AbstractAction.NAME, menuName);
        msgHeader = msg;
    }

    public void actionPerformed(ActionEvent anEvent) {

        if (bluej.getCurrentPackage() != null) {
            
            Thread t = null;
            if (anEvent.getActionCommand().toLowerCase().contains("scene")) {
                t = new EnvThread(true);
            } else {
                t = new EnvThread(false);
            }
            t.start();
        }
    }
    
    public static UI getUI() {
        return ui;
    }

    class EnvThread extends Thread {
        
        private boolean sceneCreator;
        
        public EnvThread(boolean sceneCreator) {
            this.sceneCreator = sceneCreator;
        }

        public void run() {

            try {
                System.out.println("Current BlueJ Proxy: "+bluej);
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                BPackage bp = bluej.getCurrentPackage();
                String resourceDir = null;
                try {
                    resourceDir = bp.getDir().toString();
                } catch (Exception e) {
                    resourceDir = null;
                }
                
                logger.log(Level.INFO, "Resource directory: {0}", resourceDir);

                System.setProperty("org.lwjgl.librarypath", resourceDir);

                BEnv.resourceDir = resourceDir;
                //env = new BEnv();
                //Logger.getLogger("").setLevel(Level.ALL);
                //env.setDefaultControl(false);
                // Set the camera to see as much of the room as possible
                //env.setCameraXYZ(getPrefs().getCameraX(), getPrefs().getCameraY(), getPrefs().getCameraZ());
                
                //env.setDefaultControl(getPrefs().getCameraControl());
                //env.setRoomDim(getPrefs().getRoomWidth(), getPrefs().getRoomHeight(), getPrefs().getRoomDepth());
                
                Thread t = new Thread() {

                    @Override
                    public void run() {
                        if (sceneCreator) {
                            ui = new UI(UI.Mode.SCENE_CREATOR);
                        } else {
                            ui = new UI(UI.Mode.BLUEJ);
                        }
                        ui.setBluej(bluej);
                        ui.start();
                        //System.out.println("UI exiting");
                        ui = null;
                    }
                    
                };             
                t.start();
                
                while (ui == null || ui.getGame() == null || ui.getGame().getEnv() == null) {
                    // wait until ui is fully initialized.
                    //System.out.println("UI init loop");
                    sleep(100);
                }
                
                // If not in scene creator mode, must be used in simple visualization, so set the camera
                if (!sceneCreator) {
                    ui.getGame().getEnv().setCameraXYZ(getPrefs().getCameraX(), getPrefs().getCameraY(), getPrefs().getCameraZ());
                }
                
                if (ui.getMode() == UI.Mode.BLUEJ) {
                    System.out.println("Current Package: "+StartEnvAction.getBluej().getCurrentPackage());
                    for (BObject obj : StartEnvAction.getBluej().getCurrentPackage().getObjects()) {
                        StartEnvAction.getUI().getGame().addObject(obj);
                    }
                }

                // Commented out to test env3d.scenecreator
//                boolean finished = false;
//                while (!finished) {
//                    if (env.getKey() == Keyboard.KEY_ESCAPE) {
//                        finished = true;
//                    }
//                    env.advanceOneFrame();
//                }
//                env.destroy();                
//                env = null;
                

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
