import env3d.Env;

/*
 * A simple env3d program
 */
public class Game {

    public void play() {
        Env env = new Env();
        while (env.getKey() != 1) {
            env.advanceOneFrame();
        }
        env.exit();
    }
    
    public static void main(String args[]) {
        (new Game()).play();
    }
}
