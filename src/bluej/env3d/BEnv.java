package bluej.env3d;

import bluej.extensions.BObject;
//import bluej.env3d.Env;
import env3d.Env;
import env3d.EnvObject;
import env3d.RoomAdapter;
import env3d.SimpleRoom;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import java.io.File;
import java.lang.reflect.Method;

/*
 * BEnv.java
 *
 * Created on June 1, 2007, 5:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Jason Madar
 */
public class BEnv extends Env{

    public static String resourceDir;

    /** Creates a new instance of BEnv */
    public BEnv() {        
    }
    
    public void addObject(BObject obj) {
        //game.addObject(new GameBObjectAdapter(obj, resourceDir));

        addObject(new GameBObjectAdapter(obj));
    }

    public void setRoomDim(float w, float h, float d) {
        //game.getGameObjectManager().setWorld(new BRoom(0, w, 0, h, 0, d));
        this.setRoom(new SimpleRoom(w, h, d));
    }

    @Override
    public void setRoom(Object o) {
        if (o instanceof RoomAdapter) {
            super.setRoom(o);
        } else {
            super.setRoom(new GameBRoomAdapter(o));
        }
    }
    
    
    @Override
    public void advanceOneFrame() {        
       
        // Automatically call to move method
        for (Object obj : objectsMap.keySet()) {
            if (obj instanceof GameBObjectAdapter) {
                ((GameBObjectAdapter) obj).callMoveFromObject();
            }
        }
        super.advanceOneFrame();
    }

}
