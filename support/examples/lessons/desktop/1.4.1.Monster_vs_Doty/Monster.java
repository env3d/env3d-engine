import java.util.ArrayList;
 
/**
 * A monster with various states
 * 
 */
public class Monster
{
 
    private double x;
    private double y;
    private double z;
    private String texture;
    private String model;
    private double scale;
    private double rotateX;
    private double rotateY;
    private double rotateZ;
     
    // Keeps tract of the current frame
    private int frame;
     
    // Keeps track of the monster's current state
    private String state;
     
    // Lists to keep track of all the models
    private ArrayList<String> idleModels;    
    private ArrayList<String> swipeModels;
    private ArrayList<String> runModels;
     
    /**
     * Constructor for objects of class Monster
     */
    public Monster(double x, double y, double z)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.z = z;
        texture = "models/Boss/boss.png";
        scale = 1;
        rotateY = 0;
         
        frame = 0;
         
        // Start state
        state = "idle";
        loadModels();
        model = idleModels.get(0);
    }
     
    /**
     * Load all the models into the proper ArrayList
     */
    public void loadModels() 
    {
        idleModels = new ArrayList<String>();
        idleModels.add("models/Boss/Idle/bossidle00.obj");
        idleModels.add("models/Boss/Idle/bossidle01.obj");
        idleModels.add("models/Boss/Idle/bossidle02.obj");
        idleModels.add("models/Boss/Idle/bossidle03.obj");
        idleModels.add("models/Boss/Idle/bossidle04.obj");
        idleModels.add("models/Boss/Idle/bossidle05.obj");
        idleModels.add("models/Boss/Idle/bossidle06.obj");
        idleModels.add("models/Boss/Idle/bossidle07.obj");
        idleModels.add("models/Boss/Idle/bossidle08.obj");
        idleModels.add("models/Boss/Idle/bossidle09.obj");
        idleModels.add("models/Boss/Idle/bossidle10.obj");
        idleModels.add("models/Boss/Idle/bossidle11.obj");
        idleModels.add("models/Boss/Idle/bossidle12.obj");
        idleModels.add("models/Boss/Idle/bossidle13.obj");
        idleModels.add("models/Boss/Idle/bossidle14.obj");
        idleModels.add("models/Boss/Idle/bossidle15.obj");
        idleModels.add("models/Boss/Idle/bossidle16.obj");
        idleModels.add("models/Boss/Idle/bossidle17.obj");
        idleModels.add("models/Boss/Idle/bossidle18.obj");
        idleModels.add("models/Boss/Idle/bossidle19.obj");
                 
        swipeModels = new ArrayList<String>();
        swipeModels.add("models/Boss/Swipe/bossswipe00.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe01.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe02.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe03.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe04.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe05.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe06.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe07.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe08.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe09.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe10.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe11.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe12.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe13.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe14.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe15.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe16.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe17.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe18.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe19.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe20.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe21.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe22.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe23.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe24.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe25.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe26.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe27.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe28.obj");
        swipeModels.add("models/Boss/Swipe/bossswipe29.obj");
         
        runModels = new ArrayList<String>();
        runModels.add("models/Boss/Run/bossrun00.obj");
        runModels.add("models/Boss/Run/bossrun01.obj");
        runModels.add("models/Boss/Run/bossrun02.obj");
        runModels.add("models/Boss/Run/bossrun03.obj");
        runModels.add("models/Boss/Run/bossrun04.obj");
        runModels.add("models/Boss/Run/bossrun05.obj");
        runModels.add("models/Boss/Run/bossrun06.obj");
        runModels.add("models/Boss/Run/bossrun07.obj");
        runModels.add("models/Boss/Run/bossrun08.obj");
        runModels.add("models/Boss/Run/bossrun09.obj");
        runModels.add("models/Boss/Run/bossrun10.obj");
        runModels.add("models/Boss/Run/bossrun11.obj");
        runModels.add("models/Boss/Run/bossrun12.obj");
        runModels.add("models/Boss/Run/bossrun13.obj");
        runModels.add("models/Boss/Run/bossrun14.obj");
        runModels.add("models/Boss/Run/bossrun15.obj");
        runModels.add("models/Boss/Run/bossrun16.obj");
        runModels.add("models/Boss/Run/bossrun17.obj");
        runModels.add("models/Boss/Run/bossrun18.obj");
        runModels.add("models/Boss/Run/bossrun19.obj");
        runModels.add("models/Boss/Run/bossrun20.obj");
        runModels.add("models/Boss/Run/bossrun21.obj");
        runModels.add("models/Boss/Run/bossrun22.obj");
        runModels.add("models/Boss/Run/bossrun23.obj");
        runModels.add("models/Boss/Run/bossrun24.obj");
        runModels.add("models/Boss/Run/bossrun25.obj");
        runModels.add("models/Boss/Run/bossrun26.obj");
        runModels.add("models/Boss/Run/bossrun27.obj");
        runModels.add("models/Boss/Run/bossrun28.obj");
        runModels.add("models/Boss/Run/bossrun29.obj");
         
    }
     
    /**
     * The move method is called every frame.
     * It's purpose is to check the different state and 
     * mutate monster accordingly
     */
    public void move()
    {
        frame++;
        if (state.equals("idle")) {
            idleAction();
        } else if (state.equals("swipe")) {
            swipeAction();
        } else if (state.equals("run")) {
            runAction();
        }
    }
     
    /**
     * Change the state to idle
     */
    public void idle() 
    {
        frame = 0;
        state = "idle";
    }
     
    /**
     * Change the state to swipe
     */
    public void swipe()
    {
        frame = 0;
        state = "swipe";
    }
     
     
    /**
     * Load the correct model when monster is idle
     */
    public void idleAction()
    {
 
        if (frame < idleModels.size()) {
            model = idleModels.get(frame);
        } else {
            frame = 0;
        }
    }
     
    /**
     * Load the correct model when monster is swiping
     */
    public void swipeAction()
    {
        if (frame < swipeModels.size()) {
            model = swipeModels.get(frame);            
        } else {
            idle();
        }
    }
     
    /**
     * Load the correct model when monster is running
     */
    public void runAction()
    {
        if (frame < runModels.size()) {            
            model = runModels.get(frame);
        } else {
            idle();
        }        
    }    
     
    /**
     * Move monster in the x direction, change model as needed
     */
    public void moveX(double delta)
    {
        if (!state.equals("run")) {
            state = "run";
            frame = 0;
        }
 
        x += delta;
        if (delta < 0) {
            rotateY = -90;
        } else {
            rotateY = 90;
        }
    }
     
    /**
     * Move monster in the z direction, change model as needed
     */
    public void moveZ(double delta) 
    {
        if (!state.equals("run")) {
            state = "run";
            frame = 0;
        }        
        z += delta;
        if (delta < 0) {
            rotateY = 180;
        } else {
            rotateY = 0;
        }
         
    }
 
    /**
     * Accessor for x, y, z, and rotateY, and state
     */
    public double getX()
    {
        return x;
    }
     
    public double getY()
    {
        return y;
    }
     
    public double getZ()
    {
        return z;
    }
     
    public double getRotateY()
    {
        return rotateY;
    }
     
    public String getState()
    {        
        return state;
    }
}