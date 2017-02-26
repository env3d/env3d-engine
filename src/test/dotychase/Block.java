package test.dotychase;


/**
 * A Block prevents the player from moving.
 * 
 */
public class Block
{

    private float x, y, z;
    private float scale;
    private String desc;
    // Testing the transparency field.
    private boolean transparent = true;
    //private String texture = "http://whalechat.com/models/orca/orca.png";
            //"http://whalechat.com/whalechat.png";
            //
    //
    //private String texture = "/Users/jmadar/Desktop/test.png";
    
    /**
     * Constructor for objects of class Block
     */
    public Block(float x, float y, float z, String desc)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        scale = 1f;
        this.desc = desc;
    }

    public Block(float x, float y, float z, float scale, String desc)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
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
    public float getX()
    {
        return x;
    }

    /**
     * Accessor for the block's location
     */
    public float getY()
    {
        return y;
    }

    /**
     * Accessor for the block's location
     */
    public float getZ()
    {
        return z;
    }
    
    /**
     * Accessor for the block's size
     */
    public float getScale()
    {
        return scale;
    }    

}
