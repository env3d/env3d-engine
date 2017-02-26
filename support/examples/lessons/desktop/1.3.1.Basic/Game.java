import env3d.Env;
 
public class Game
{
 
    public Game()
    { 
    }
 
 
    public void play()
    {
        // A variable to determine if the game is finished
        boolean finished = false;
         
        // Create the env object so that we can manipulate
        // the 3D environment.
        Env env = new Env();        
         
        while (!finished)
        {
            // Gather the user input
            int key = env.getKey();
 
            // Terminate program when user press escape
            if (key == 1) {
                finished = true;
            } 
             
            // Update the environment
            env.advanceOneFrame();
        }
         
 
        // Exit the program cleanly
        env.exit();
    }
}