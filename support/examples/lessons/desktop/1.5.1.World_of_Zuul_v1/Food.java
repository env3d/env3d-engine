/**
 * Represents a food in the game
 */
public class Food
{
 
    private double x, y, z;
    private double scale;
     
    /**
     * Constructor for objects of class Food.  This
     * is flexible and the caller can position food anywhere
     */
    public Food(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        scale = 0.3;
    }
 
    /**
     * Get the x coordinate of this food
     */
    public double getX()
    {
        return x;
    }
 
    /**
     * Get the y coordinate of this food
     */
    public double getY()
    {
        return y;
    }
 
    /**
     * Get the z coordinate of this food
     */
    public double getZ()
    {
        return z;
    }
 
    /**
     * Get the scale of this food
     */
    public double getScale()
    {
        return scale;
    }
     
    /**
     * Make the food disappear.  It simply sets the food to be out of sight.
     */
    public void disappear()
    {
        x = 100;
    }
}