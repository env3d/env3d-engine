import env3d.Env;
import java.util.ArrayList;
  
/**
 * A predator and prey simulation.  Fox is the predator and Tux is the prey.
 */
public class Game
{
    private Env env;    
    private boolean finished;
      
    private ArrayList<Creature> creatures;
  
    /**
     * Constructor for the Game class. It sets up the foxes and tuxes.
     */
    public Game()
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
    }
      
    /**
     * Play the game
     */
    public void play()
    {
          
        finished = false;
          
        // Create the new environment.  Must be done in the same
        // method as the game loop
        env = new Env();
          
        // Make the room 50 x 50.
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
          
        // A list to keep track of dead tuxes.
        ArrayList<Creature> dead_creatures = new ArrayList<Creature>();
          
        // The main game loop
        while (!finished) {            
          
            if (env.getKey() == 1)  {
                finished = true;
             }
          
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
          
            // Update display
            env.advanceOneFrame();
        }
          
        // Just a little clean up
        env.exit();
    }
      
      
    /**
     * Main method to launch the program.
     */
    public static void main(String args[]) {
        (new Game()).play();
    }
}