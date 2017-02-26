import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvNode;

public class Game 
{
    private EnvNode node;
    
    public void play() {
        EnvAdvanced env = new EnvAdvanced();
         
        env.setDefaultControl(false);
        
        // Build the screens, attach controller to the screen
        ScreenFactory.buildScreen("HelloScreen", new MyController(this), env.getNiftyGUI());
         
        // Activate the screen
        env.getNiftyGUI().gotoScreen("HelloScreen");
         
        // put a simple object in our environment.  
        node = new EnvNode();
        env.addObject(node);
        node.setX(5); node.setY(1); node.setZ(0);
        node.setScale(3);
        
        // Render the screen until escape key is pressed
        while (env.getKey() != 1) {
            // Rotate our object
            node.setRotateY(node.getRotateY()+1);
            env.advanceOneFrame();
        }
         
        env.exit();
    }
     
    public EnvNode getNode() 
    {
        return node;
    }

    
    public static void main(String args[]) {
        (new Game()).play();
    }
}