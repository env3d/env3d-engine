package bluej;

import bluej.action.StartEnvAction;
import bluej.action.AbstractEnvAction;
import bluej.extensions.*;
import bluej.extensions.event.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/*
 * This is the starting point of a BlueJ Extension
 */
public class Bluejenv extends Extension {
    
    public static BlueJ bluej;
    /*
     * When this method is called, the extension may start its work.
     */
    public void startup(BlueJ bluej) {
        this.bluej = bluej;        
        // Add a menu item
        Preferences myPrefs = new Preferences(bluej);

        MenuBuilder myMenu = new MenuBuilder(myPrefs);        
        bluej.setMenuGenerator(myMenu);
        
        AbstractEnvAction.setPrefs(myPrefs);
//        StartEnvAction.setPrefs(myPrefs);
//        CreateAppletAction.setPrefs(myPrefs);
//        DeployAndroidAction.setPrefs(myPrefs);
//        OnlineExampleAction.setPrefs(myPrefs);
//        CreateModelAction.setPrefs(myPrefs);

        bluej.setPreferenceGenerator(myPrefs);
        // pass the bluej object to StartEnvAction, so it can find out
        // the current package.
        AbstractEnvAction.setBluej(bluej);
        
              
        bluej.addInvocationListener(new InvocationListener() {            

            public void invocationFinished(InvocationEvent ie) {                
                try {
                    if (StartEnvAction.getUI() != null) {   
                        System.out.println("Current Package: "+StartEnvAction.getBluej().getCurrentPackage());
                        for (BObject obj : StartEnvAction.getBluej().getCurrentPackage().getObjects()) {                            
                            StartEnvAction.getUI().getGame().addObject(obj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });                

    }

    /*
     * This method must decide if this Extension is compatible with the                
     * current release of the BlueJ Extensions API
     */
    public boolean isCompatible() { 
        return true; 
    }

    /*
     * Returns the version number of this extension
     */
    public String getVersion () { 
        return ("2010.12");
    }

    /*
     * Returns the user-visible name of this extension
     */
    public String getName () { 
        return ("Env3D");
    }

    public void terminate() {
    }
               
    public String getDescription () {
        return ("Env3D Extension");
    }

    /*
     * Returns a URL where you can find info on this extension.
     * The real problem is making sure that the link will still be alive 
     * in three years...
     */
    public URL getURL () {
        try {
            return new URL("http://env3d.org/");
        } catch ( Exception e ) {
            // The link is either dead or otherwise unreachable
            System.out.println ("Env3D: getURL: Exception="+e.getMessage());
            return null;
        }
    }
}
