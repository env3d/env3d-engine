/**
 * Doty is the main character of our game.  For
 * now, it can only move around.
 * 
 */
public class Doty
{
 
    private double x;
    private double y;
    private double z;
    private String texture;
    private double scale;    
    private double rotateY;
     
     
    /**
     * Constructor for objects of class Doty.  The
     * caller can create a doty in a flexible location
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Doty(double x, double y, double z)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.z = z;
        texture = "textures/doty.png";
        scale = 1f;
        rotateY = 0;
    }
     
    /**
     * Moves doty in the x direction
     * 
     * @param dir the direction. 
     * 1 means positive (to the right), 
     * -1 means negative (to the left), 
     * 0 means don't move
     */
    public void moveX(int dir)
    {
        // Make Doty face the right direction
        if (dir > 0) {
            rotateY = 90;
        } else {
            rotateY = -90;
        }            
             
        x = x + scale*dir;
    }
     
    /**
     * Moves doty in the z direction
     * 
     * @param dir the direction. 
     * 1 means positive (towards the screen), 
     * -1 means negative (away from the screen), 
     * 0 means don't move
     */
    public void moveZ(int dir)
    {
        if (dir > 0) {
            rotateY = 0;
        } else {
            rotateY = 180;
        }
        z = z + scale*dir;
    }
     
    /**
     * Returns the x coordinate
     */
    public double getX()
    {
        return x;
    }
 
    /**
     * Returns the y coordinate
     */
    public double getY()
    {
        return y;
    }
 
    /**
     * Returns the z coordinate
     */
    public double getZ()
    {
        return z;
    }
     
    /**
     * Returns the current scale of doty
     */
    public double getScale()
    {
        return scale;
    }
     
    /**
     * Sets the x coordinate
     * 
     * @param x the x coordinate
     */
    public void setX(double x)
    {
        this.x = x;
    }
 
    /**
     * Sets the y coordinate
     * 
     * @param y the y coordinate
     */
    public void setY(double y)
    {
        this.y = y;
    }
     
    /**
     * Sets the z coordinate
     * 
     * @param z the z coordinate
     */
    public void setZ(double z)
    {
        this.z = z;
    }
     
    /**
     * Grow doty by delta amount
     * 
     * @param delta the amount by which doty will grow
     */
    public void grow(double delta)
    {
        scale = scale + delta;
    }
     
    /**
     * Switch doty's texture to a simily face
     */
    public void smile()
    {
        texture = "textures/doty_happy.png";
    }
}