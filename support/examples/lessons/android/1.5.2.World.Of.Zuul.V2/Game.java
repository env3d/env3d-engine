import env3d.android.EnvMobileGame;
 
/**
 * 
 * The Game class.  This class is the entry point
 * of the program and contains a controller loop.
 * 
 */
public class Game extends EnvMobileGame
{
    private Doty doty1;
    private Room roomLeft, roomRight;
    private Room currentRoom;
     
     
    /**
     * The constructor.  It sets up all the rooms and necessary 
     * objects in each room.
     * 
     */
    public void setup()
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
        env.setShowController(true);
    }
     
     
    /**
     * The loop method is called by the android Env system
     * every frame
     */
    public void loop()
    {
        // Process the various objects
        checkWall();            
        checkCollision();
        env.setDisplayStr("Touched? "+env.isTouchEvent());
        env.setDisplayStr(env.getKeyDown("up")+" "                
                +env.getKeyDown("down")+" "
                +env.getKeyDown("left")+" "
                +env.getKeyDown("right")+" "
                +env.getKeyDown("a")+" "
                +env.getKeyDown("b")+" "
                , 100, 100);
        Object b = env.getPick((int)env.getTouchX(), (int)env.getTouchY());
        if (b != null) {
            if (b instanceof Block) {
                ((Block) b).rotate();
            }
        }             
        
        if (env.getKeyDown("up")) {
            doty1.moveZ(-0.1);
        }
        if (env.getKeyDown("down")) {
            doty1.moveZ(0.1);
        }
        if (env.getKeyDown("left")) {
            doty1.moveX(-0.1);            
        }
        if (env.getKeyDown("right")) {
            doty1.moveX(0.1);
        }        
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
        Game g = new Game();        
        g.start();
    }
}
