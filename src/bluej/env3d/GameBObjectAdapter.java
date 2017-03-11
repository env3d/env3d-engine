package bluej.env3d;

import bluej.env3d.BEnv;
import bluej.extensions.BField;
import bluej.extensions.BMethod;
import bluej.extensions.BObject;
import env3d.GameObjectAdapter;

/**
 * Write a description of class GameObjectAdapter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GameBObjectAdapter {

    private Object internalObject;
    
    private double x, y, z, scale;
    private double rotateX, rotateY, rotateZ;
    private String texture, model;
    private boolean transparent;
    
    //private BObject internalObject;
    public GameBObjectAdapter(BObject obj) {
        internalObject = obj;

        update();
    }

    public void update() {
        x = getFieldValue("x");
        y = getFieldValue("y");
        z = getFieldValue("z");
        rotateX = getFieldValue("rotateX");
        rotateY = getFieldValue("rotateY");
        rotateZ = getFieldValue("rotateZ");
        scale = getFieldValue("scale");
        texture = getFieldValue("texture");
        model = getFieldValue("model");
        transparent = getFieldValue("transparent");        
//        System.out.println(x+" "+y+" "+z);        
    }
    
    private <T> T getFieldValue(String fname) {
        String resourceDir = BEnv.resourceDir;
        T fieldValue = null;
        try {
            BField field = ((BObject) internalObject).getBClass().getField(fname);

            fieldValue = (T) field.getValue((BObject) internalObject);
            
            if (fieldValue != null) {
                if (fname.equalsIgnoreCase("texture") || fname.equalsIgnoreCase("textureNormal")) {
                    // This is a string field
                    if (!fieldValue.toString().startsWith("http:")) {
                        fieldValue = (T) (resourceDir + "/" + fieldValue);
                    }
                } else if (fname.equalsIgnoreCase("model")) {
                    if (!fieldValue.toString().startsWith("http:") && fieldValue.toString().contains(".")) {
                        fieldValue = (T) (resourceDir + "/" + fieldValue);
                    }
                }
            }
        } catch (Exception e) {
            if (fname.equalsIgnoreCase("texture")) {
                fieldValue = (T) (resourceDir + "/" + "textures" + "/" + "earth.png");
            } else if (fname.equalsIgnoreCase("textureNormal")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("model")) {
                fieldValue = (T) "sphere";
            } else if (fname.equalsIgnoreCase("action")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("repeat")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("scale")) {
                fieldValue = (T) new Float(1);
            } else if (fname.equalsIgnoreCase("transparent")) {
                fieldValue = (T) (Boolean) false;
            } else {
                fieldValue = (T) new Float(0);
            }
        }
        //System.out.println("Getting field "+fname+ ", "+fieldValue);
        return fieldValue;
    }

    /**
     * The bluej extension doesn't call the move method automatically.  It is too slow to
     * interface with the BObject
     */
    public void callMoveFromObject() {
        try {            
            if (internalObject instanceof BObject) {       
                Class [] params = {};
                BMethod move = ((BObject) internalObject).getBClass().getDeclaredMethod("move",params);
                if (move != null)
                    move.invoke((BObject)internalObject, params);
            }
        } catch (Exception e) {
            // nothing
            //e.printStackTrace();
        }

    }
}
