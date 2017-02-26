import java.util.ArrayList;
import env3d.Env;
import org.lwjgl.input.Keyboard;
 
/**
 * 
 * The Game class.  This class is the entry point
 * of the program and contains a controller loop.
 * 
 */
public class Game 
{
    private boolean finished;
    private Doty doty1;
    private Env env;
    private Room roomLeft, roomRight;
    private Room currentRoom;
     
     
    /**
     * The constructor.  It sets up all the rooms and necessary 
     * objects in each room.
     * 
     */
    public Game()
    {
        roomLeft = new Room(10, 13.001, 10, "The left room");
        roomRight = new Room(10, 13.001, 10, "The right room");
         
        roomLeft.setExit("east", roomRight);
        roomLeft.setTextureEast("textures/door.png");
        roomLeft.addBlock(new Block(1, 1, 4, "Normal Block"));
        roomLeft.addBlock(new Block(4, 1, 8, "Normal Block"));
        roomLeft.addBlock(new Block(7, 1, 3, "Normal Block"));
         
        roomRight.setExit("west", roomLeft);
        roomRight.setTextureWest("textures/door.png");                
        roomRight.addBlock(new Block(3, 1, 4, "Normal Block"));
        roomRight.addBlock(new Block(5, 1, 2, "Breakable Block"));
        roomRight.addBlock(new Block(6, 1, 8, "Normal Block"));        
         
        doty1 = new Doty(5, 1, 1);
         
    }
     
     
    /**
     * The play method contains the controller loop.
     */
    public void play()
    {
        finished = false;
         
        // Create a new environment
        env = new Env();
         
        // Starts with roomLeft
        setCurrentRoom(roomLeft);        
        // Adding doty to the environment
        doty1.setRoomDim(currentRoom.getWidth(), currentRoom.getDepth());        
        env.addObject(doty1);
                 
        // Position the camera
        env.setCameraXYZ(5, 13, 9);        
        env.setCameraPitch(-75);
         
        // Disable mouse and camera control
        env.setDefaultControl(false);
         
        // The "loop"
        while (finished == false)
        {            
            // Ask for user input
            processInput();
             
            // Process the various objects
            checkWall();            
            checkCollision();
             
            // Update the screen
            env.advanceOneFrame();
        }
         
        // Just a little cleanup
        env.exit();
    }
     
    /**
     * Helper method for setting the current room to a 
     * particular room.
     * 
     * @param room The room to set the current room to.
     */
    private void setCurrentRoom(Room room)
    {
        if (room != null) {
            currentRoom = room;
            env.setRoom(currentRoom);
            for (Block block : currentRoom.getBlocks()) {
                env.addObject(block);
            }
        }
    }
     
    /**
     * Process the user input
     * 
     */
    private void processInput() 
    {        
        // The getKeyDown() method can detect which
        // button is being held down.  Can only detect
        // one button.
        int currentKey = env.getKeyDown();
         
        // Reset the display string
        if (currentKey != 0) {
            env.setDisplayStr("");
        }
                 
        // Note the use of class constants.  Much more readable!
        if (currentKey == Keyboard.KEY_ESCAPE) {
            finished = true;
        } else if (currentKey == Keyboard.KEY_UP) {
            doty1.moveZ(-0.05);
        } else if (currentKey == Keyboard.KEY_DOWN) {
            doty1.moveZ(0.05);
        } else if (currentKey == Keyboard.KEY_LEFT) {
            doty1.moveX(-0.05);
        } else if (currentKey == Keyboard.KEY_RIGHT) {
            doty1.moveX(0.05);
        } else if (currentKey == Keyboard.KEY_L) {
            // This is the "L"ook command.  So we display the description of the room.
            env.setDisplayStr(currentRoom.getDescription());
             
            // If Doty is close to an object, display the description of the object instead.
            for (Block block : currentRoom.getBlocks()) {
                double dist = distance(doty1.getX(), block.getX(), doty1.getY(), block.getY(), doty1.getZ(), block.getZ());
                if (dist <= doty1.getScale()*1.2+block.getScale()) {
                    env.setDisplayStr(block.getDesc());
                }
            }
        }         
    }
     
    /**
     * Check to see if Doty is close to a wall, and exit to the next room if necessary
     */
    private void checkWall()
    {
        if (doty1.getX() > currentRoom.getWidth()-doty1.getScale()) {
            // Going east
            exitTo("east");
        } else if (doty1.getX() < doty1.getScale()) {
            // Going west
            exitTo("west");
        }        
    }
     
    /**
     * A helper method to reduce duplicated code in checkWall
     */
    private void exitTo(String dir)
    {
        if (currentRoom.getExit(dir) != null) {                
            setCurrentRoom(currentRoom.getExit(dir));
            doty1.setRoomDim(currentRoom.getWidth(), currentRoom.getDepth());
            doty1.setExitFrom(dir);           
            env.addObject(doty1);
        } else {
            // Doty has hit a wall
            doty1.revert();
        }
    }
     
    /**
     * Check to see if any collision occur between Doty and the objects in the current room
     */   
    private void checkCollision()
    {
        // For every wall in the current room
        for (Block block : currentRoom.getBlocks()) {
            // Stop doty from moving if doty hits a wall
            double dist = distance(block.getX(), doty1.getX(), block.getY(), doty1.getY(), block.getZ(), doty1.getZ());
            if (dist <= block.getScale()+doty1.getScale()) {                
                doty1.revert();
            }
        }
 
    }
     
    /**
     * The private distance method
     */
    private double distance(double x1, double x2, double y1, double y2, double z1, double z2) {
        double xdiff, ydiff, zdiff;
        xdiff = x2 - x1;
        ydiff = y2 - y1;
        zdiff = z2 - z1;
        return (double) Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
    }    
     
    public static void main (String [] args) {
        (new Game()).play();
    }
}