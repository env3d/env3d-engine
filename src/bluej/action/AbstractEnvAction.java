/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluej.action;

import bluej.Preferences;
import bluej.extensions.BlueJ;
import javax.swing.AbstractAction;

/**
 *
 * @author jmadar
 */
public abstract class AbstractEnvAction extends AbstractAction {
    
    protected static BlueJ bluej;
    protected static Preferences prefs;        
    
    /**
     * @return the bluej
     */
    public static BlueJ getBluej() {
        return bluej;
    }

    /**
     * @param aBluej the bluej to set
     */
    public static void setBluej(BlueJ aBluej) {
        bluej = aBluej;
    }

    /**
     * @return the prefs
     */
    public static Preferences getPrefs() {
        return prefs;
    }

    /**
     * @param prefs the prefs to set
     */
    public static void setPrefs(Preferences myPrefs) {
        prefs = myPrefs;
    }
    
}
