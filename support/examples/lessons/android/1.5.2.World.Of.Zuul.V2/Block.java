/**
 * A Block prevents the player from moving.
 * 
 */
public class Block
{
 
    private double x, y, z;
    private double scale, rotateY;
    private String desc;
    private String texture = "textures/earth.png";
     
    /**
     * Constructor for objects of class Block
     */
    public Block(double x, double y, double z, String desc)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        scale = 0.5;
        this.desc = desc;
    }
     
    /**
     * Make the block disappear
     */
    public void disappear()
    {
        x = 100;
    }
     
    /**
     * Accessor for the block's description
     */
    public String getDesc()    
    {
        return desc;
    }
 
    /**
     * Accessor for the block's location
     */
    public double getX()
    {
        return x;
    }
 
    /**
     * Accessor for the block's location
     */
    public double getY()
    {
        return y;
    }
 
    /**
     * Accessor for the block's location
     */
    public double getZ()
    {
        return z;
    }
     
    /**
     * Accessor for the block's size
     */
    public double getScale()
    {
        return scale;
    }    
    
    public void rotate()
    {
        rotateY += 1;
    }
}
