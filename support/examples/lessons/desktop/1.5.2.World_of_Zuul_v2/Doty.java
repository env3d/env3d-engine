/**
 * Doty is the main character of the game
 * 
 */
public class Doty
{
    // Doty's location
    private double x, y, z;
     
    // Doty's previous location.  Useful in the revert method
    private double prev_x, prev_y, prev_z;
     
    private String texture;
    private double scale;    
    private double rotateY;
     
    // If doty remembers the room's dimension, doty will be able
    // to set itself to the right position when entering a new
    // room
    private double roomWidth, roomDepth;
     
     
    /**
     * Constructor for objects of class Doty
     */
    public Doty(double x, double y, double z)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.z = z;
         
        this.prev_x = x;
        this.prev_y = y;
        this.prev_z = z;
         
        texture = "textures/doty.png";
        scale = 1;
        rotateY = 0;
    }
     
    /**
     * Set the dimension of the room Doty is in
     */
    public void setRoomDim(double w, double d)
    {
        roomWidth = w;
        roomDepth = d;
    }
 
    /**
     * This method is called when Doty exits from a room.
     * If Doty exits from the east, then set Doty's position to the
     * west side of the new room.
     * 
     * @param dir The direction in which Doty exited from.
     */
    public void setExitFrom(String dir) 
    {
        if (dir.equals("east")) {
            setX(scale);
        } else if (dir.equals("west")) {
            setX(roomWidth-scale);
        }
    }
     
    /**
     * Move doty in the x direction.
     */
    public void moveX(double delta)
    {
        // Make Doty face the right direction
        if (delta > 0) {
            rotateY = 90;
        } else {
            rotateY = -90;
        }            
         
        // Remember the previous position
        prev_z = z;
        prev_x = x;    
         
        // Move doty
        x = x + delta;
    }
     
    /**
     * Move doty in the z direction
     */
    public void moveZ(double delta)
    {
        if (delta > 0) {
            rotateY = 0;
        } else {
            rotateY = 180;
        }
         
        // Remember the previous position
        prev_z = z;
        prev_x = x;
         
        // Move doty
        z = z + delta;
    }
     
    /**
     * Reverts doty back to the previous position
     */
    public void revert()
    {
        x = prev_x;
        z = prev_z;
    }
     
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
     
    public double getScale()
    {
        return scale;
    }
     
    public void setX(double x)
    {
        this.x = x;
    }
 
    public void setY(double y)
    {
        this.y = y;
    }
     
    public void setZ(double z)
    {
        this.z = z;
    }
}