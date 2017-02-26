import env3d.android.EnvMobileGame;
import java.util.ArrayList;
  
/**
 * A predator and prey simulation.  Fox is the predator and Tux is the prey.
 */
public class Game extends EnvMobileGame
{
      
    private ArrayList<Creature> creatures;

    // A list to keep track of dead tuxes.
    private ArrayList<Creature> dead_creatures = new ArrayList<Creature>();    

    /**
     * Constructor for the Game class. It sets up the foxes and tuxes.
     */
    public void setup()
    {
        // we use a separate ArrayList to keep track of each animal. 
        // our room is 50 x 50.
        creatures = new ArrayList<Creature>();
        for (int i = 0; i < 55; i++) {
            if (i < 5) {
                creatures.add(new Fox((int)(Math.random()*48)+1, 1, (int)(Math.random()*48)+1));        
            } else {
                creatures.add(new Tux((int)(Math.random()*48)+1, 1, (int)(Math.random()*48)+1));
            }
        }
        
        env.setRoom(new Room());
          
        // Add all the animals into to the environment for display
        for (Creature c : creatures) {
            env.addObject(c);
        }
          
        // Sets up the camera
        env.setCameraXYZ(25, 50, 55);
        env.setCameraPitch(-63);
          
        // Turn off the default controls
        env.setDefaultControl(false);
        env.setShowController(false);
         
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
            env.removeObject(c);
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
        (new Game()).start();
    }
}