import java.util.ArrayList;
import env3d.Env;
 
/**
 * This is the main Game class.  It is the main entry point of our game and contains the control 
 * loop and all the various support methods.  
 * 
 * Only the play method is meant to be called by the caller to start the game.
 * 
 */
public class Game
{
    private boolean finished;
    private Doty doty;
    private Env env;
    private Room roomLeft, roomRight;
    private Room currentRoom;
     
    /**
     * The constructor of the game.  It sets up all the room information 
     * and the doty's position.
     */
    public Game()
    {
        roomLeft = new Room(10, 15, 10);
        roomRight = new Room(10, 15, 10);
         
        roomLeft.setExits(null, roomRight);
        roomLeft.setTextureEast("textures/door.png");
         
        roomRight.setExits(roomLeft, null);
        roomRight.setTextureWest("textures/door.png");               
         
        doty = new Doty(5, 1, 1);
    }
     
    /**
     * This is the main game loop.  Try to keep it as short
     * as possible.
     */
    public void play()
    {
        finished = false;
         
        // Create a new environment
        env = new Env();
         
        // Starts with roomLeft
        currentRoom = roomLeft;
        env.setRoom(currentRoom);
         
        // Adding doty to the environment
        env.addObject(doty);
                 
        // Position the camera
        env.setCameraXYZ(5, 14, 9);
        env.setCameraPitch(-75);
         
        // Disable mouse and camera control
        env.setDefaultControl(false);
         
        while (finished == false)
        {            
            processInput();
 
            checkWall();
             
            env.advanceOneFrame();
        }
         
        env.exit();
    }    
     
    /**
     * Checks keyboard input
     */
    private void processInput() 
    {
        int currentKey = env.getKey();
             
        if (currentKey == 1) {
            finished = true;
        } else if (currentKey == 200) {
            // Up arrow
            doty.moveZ(-1);
        } else if (currentKey == 208) {
            // Down arrow
            doty.moveZ(1);   
        } else if (currentKey == 203) {
            // Left arrow
            doty.moveX(-1);            
        } else if (currentKey == 205) {
            // Right arrow
            doty.moveX(1);         
        } 
    }
     
    /**
     * Check to see if doty has hit a wall.  It then tries to determine
     * if doty should be moved to the next room.
     */
    private void checkWall()
    {
        if (doty.getX() > currentRoom.getWidth()-doty.getScale()) {
            // Going east
            if (currentRoom.getEastExit() != null) {                
                currentRoom = currentRoom.getEastExit();
                env.setRoom(currentRoom);
                doty.setX(doty.getScale());                    
                env.addObject(doty);
            } else {
                // Doty has hit a wall
                doty.setX(currentRoom.getWidth()-doty.getScale());
            }
        } else if (doty.getX() < doty.getScale()) {
            // Going west
            if (currentRoom.getWestExit() != null) {
                currentRoom = currentRoom.getWestExit();
                env.setRoom(currentRoom);
                doty.setX(currentRoom.getWidth()-doty.getScale());                    
                env.addObject(doty);                    
            } else {
                // Doty has hit a wall
                doty.setX(doty.getScale());
            }
        }        
    }
     
    /**
     * Basic distance method.  It calculates the distance between 2 objects given
     * 2 sets of x, y, and z coordinates.
     */
    private double distance(double x1, double x2, double y1, double y2, double z1, double z2) {
        double xdiff, ydiff, zdiff;
        xdiff = x2 - x1;
        ydiff = y2 - y1;
        zdiff = z2 - z1;
        return (double) Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
    }
}