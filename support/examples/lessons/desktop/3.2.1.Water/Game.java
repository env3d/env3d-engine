import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvWater;
import env3d.Env;
import env3d.EnvObject;
   
public class Game
{
    public Game() {        
    }
       
    public void play() {        
        // EnvAdvanced must be used for water to work
        Env env = new EnvAdvanced();
                  
        // Need to use a skybox for our environment, so the water
        // has something to reflect.
        env.setRoom(new EnvSkyRoom("textures/skybox/default"));
                  
        // Create the water object
        EnvWater water = new EnvWater();
         
        // Make the water plain a 10x10 square
        water.setScale(10);
         
        // Add the wataer object into our environment.
        env.addObject(water);        
         
        // Add an object another our environment
        EnvObject o = new EnvObject();
        o.setScale(1);
        o.setZ(1);
        o.setX(5);
        o.setY(5);

        env.addObject(o);
         
        // Look at the water plain from the side
        env.setCameraXYZ(-10, 5, 5);
        env.setCameraYaw(-55);
           
        // Exit when the escape key is pressed
        while (env.getKey() != 1) {
            // Rotate the water
            env.advanceOneFrame();
        }
        env.exit();
    }     
     
    public static void main(String [] args) {
        (new Game()).play();
    }
}