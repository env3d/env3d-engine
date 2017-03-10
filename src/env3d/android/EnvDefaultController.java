package env3d.android;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import env3d.util.Sysutil;

/**
 * The on-screen controller
 * @author jmadar
 * @deprecated 
 */
public class EnvDefaultController {

    public static AssetManager assetManager;
    // The png file containing the buttons layout
    private String layout;
    // The png file to show when the button is depressed
    private String buttonPressed;
    
    // This controller has 6 keys
    private HashMap<String, KeyMap> controllerKey;

    public EnvDefaultController() {
        try {
            layout = "textures/controller/buttons.png";
            buttonPressed = "textures/controller/touched.png";

            // Retrieve the width and the height of buttons.png and use that
            // to calculate the relative positions later on
            
            TextureKey key = new TextureKey(layout, true);
            Texture2D tex = (Texture2D) assetManager.loadTexture(key);
            int height = tex.getImage().getHeight();
            int width = tex.getImage().getWidth();
            
            controllerKey = new HashMap<String, KeyMap>();
            
            BufferedReader br;
            try {
                URL url = Sysutil.getURL("textures/controller/layout.txt");
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (FileNotFoundException e) {
                URL url = Sysutil.getURL("assets/textures/controller/layout.txt");
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            }

            String line;
            while ((line = br.readLine()) != null) {            
            
                String [] tokens = line.split(",");
                // Must have 5 tokens
//                controllerKey.put(tokens[0], 
//                        new KeyMap(tokens[0], Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]), Float.parseFloat(tokens[4])));
                controllerKey.put(tokens[0], 
                        new KeyMap(tokens[0], 
                                Float.parseFloat(tokens[1])/width, 
                                Float.parseFloat(tokens[2])/height, 
                                Float.parseFloat(tokens[3])/width, 
                                Float.parseFloat(tokens[4])/height));
            }
        } catch (Exception ex) {
            Logger.getLogger(EnvDefaultController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * returns the key mappings array for this controller
     * @return 
     */
    public HashMap<String, KeyMap> getKeyMappings() {
        return controllerKey;
    }
    
    /**
     * If the keycode is currently being pressed down, return true
     * @param keycode
     * @return 
     */
    public boolean getKeyDown(String keycode) {
        return controllerKey.get(keycode).down;
    }

    /**
     * @return the layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     * @param layout the layout to set
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * @return the buttonPressed
     */
    public String getButtonPressed() {
        return buttonPressed;
    }

    /**
     * @param buttonPressed the buttonPressed to set
     */
    public void setButtonPressed(String buttonPressed) {
        this.buttonPressed = buttonPressed;
    }
}
