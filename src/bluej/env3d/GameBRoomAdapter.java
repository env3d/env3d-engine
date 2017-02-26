/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluej.env3d;

import bluej.env3d.BEnv;
import env3d.RoomAdapter;
import java.io.File;

/**
 *
 * @author jmadar
 */
public class GameBRoomAdapter extends RoomAdapter{

    public GameBRoomAdapter(Object o) {
        super(o);        
    }

    @Override
    protected String getTextureFromRoom(String direction) {
        String resourceLocation = super.getTextureFromRoom(direction);
        if (resourceLocation != null) {
            resourceLocation = BEnv.resourceDir+"/"+resourceLocation;
        }
        return resourceLocation;
    }

}
