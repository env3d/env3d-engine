import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvTerrain;
import env3d.advanced.EnvSkyRoom;
import env3d.Env;
  
public class Game
{
    public Game() {        
    }
      
    public void play() {        
        Env env = new EnvAdvanced();        
 
        // Create a the sky box.  The directory specified in the
        // constructor contains 6 images: north.png, east.png,
        // south.png, west.png, top.png, and bottom.png
        //
        // The camera will always be in the center of the skybox        
        EnvSkyRoom skyroom = new EnvSkyRoom("textures/skybox/default/");
         
        // EnvSkyRoom is a room, so we use env.setRoom() to set make it visible in our
        // environment.
        env.setRoom(skyroom);
                 
        // Use an image to provide height data, this image is 256x256
        EnvTerrain terrain = new EnvTerrain("textures/terrain/termap1.png");
        // Terrain needs a texture
        terrain.setTexture("textures/mud.png");
          
        // Flatten the hills and valleys
        terrain.setScale(1, 0.2, 1);
          
        // Add the terrain object to the environemnt.
        env.addObject(terrain);        
          
        // Start in the middle of the terrain
        env.setCameraXYZ(60, 0, 60);
          
        // Exit when the escape key is pressed
        while (env.getKey() != 1) {
            // Get the height of the camera
            double height = terrain.getHeight(env.getCameraX(), env.getCameraZ());

            // Make the y value a little higher than the terrain, to create the
            // illusion that we are "walking" on the surface.
            env.setCameraXYZ(env.getCameraX(), height+1, env.getCameraZ());
  
            env.advanceOneFrame();
        }
        env.exit();
    }
      
    public static void main(String [] args) {
        (new Game()).play();
    }
}