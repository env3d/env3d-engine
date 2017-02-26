package test.foxvstux;

import env3d.android.EnvAndroid;
import env3d.advanced.EnvTerrain;
import java.util.ArrayList;
  
/**
 * A predator and prey simulation.  Fox is the predator and Tux is the prey.
 */
public class Game extends EnvAndroid
{   
    private boolean init = false;          
    private ArrayList<Creature> creatures, dead_creatures;
    
      
    public void setup()
    {
        // we use a separate ArrayList to keep track of each animal. 
        // our room is 50 x 50.
        //setRoom(new Room());
        double [] ary = new double[128*128];
        EnvTerrain terrain = new EnvTerrain(ary);
        terrain.setTexture("textures/marble.png");
        addObject(terrain);
        
        creatures = new ArrayList<Creature>();
        for (int i = 0; i < 55; i++) {
            if (i < 5) {
                creatures.add(new Fox((int)(Math.random()*48)+1, 1, (int)(Math.random()*48)+1));        
            } else {
                creatures.add(new Tux((int)(Math.random()*48)+1, 1, (int)(Math.random()*48)+1));
            }
        }
        
        // Add all the animals into to the environment for display
        for (Creature c : creatures) {
            addObject(c);
        }
        
        // Sets up the camera
        setCameraXYZ(25, 50, 55);
        setCameraPitch(-63);
          
        // Turn off the default controls
        setDefaultControl(true);
        setShowStatus(true);
          
        // A list to keep track of dead tuxes.
        dead_creatures = new ArrayList<Creature>();                
    }
        
    /**
     * Play the game
     */
    public void loop()
    {
          
        // Move each fox and tux.
        for (Creature c : creatures) {
            c.move(creatures, dead_creatures);
        }
          
        // Clean up of the dead tuxes.
        for (Creature c : dead_creatures) {
            removeObject(c);
            creatures.remove(c);
        }
        // we clear the ArrayList for the next loop.  We could create a new one 
        // every loop but that would be very inefficient.
        dead_creatures.clear();
    }
      
      
    /**
     * Main method to launch the program.
     */
    public static void main(String args[]) {
        Game g = new Game();    
        g.start();
    }
}
