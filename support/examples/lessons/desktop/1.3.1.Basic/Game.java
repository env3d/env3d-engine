
import env3d.Env;

public class Game {

    private Env env;
    private boolean finished;

    public Game() {
    }

    private void setup() {
        // Create the env object so that we can manipulate
        // the 3D environment.
        env = new Env();
        
        // A variable to determine if the game is finished
        finished = false;
    }

    private void loop() {
        // Gather the user input
        int key = env.getKey();

        // Terminate program when user press escape
        if (key == 1) {
            finished = true;
        }

    }

    public void play() {

        // Setup your game
        setup();

        while (!finished) {
            loop();
            // Update the environment
            env.advanceOneFrame();
        }

        // Exit the program cleanly
        env.exit();
    }
}
