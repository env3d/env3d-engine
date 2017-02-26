/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package env3d;

import com.jme3.system.lwjgl.LwjglDisplay;

/**
 *
 * @author jmadar
 */
public class EnvDisplay extends LwjglDisplay{

    @Override
    public void create(boolean waitFor) {
        //super.create(waitFor);
        initInThread();         
    }


}
