public class Ball
{
 
    private double x, y, z, scale;
    private double dir;
     
    /**
     * Constructor for objects of class Ball
     */
    public Ball(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        scale = 0.2f;
    }
 
    public void setXYZ(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
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
     
    /**
     * Set the direction of the ball
     */
    public void setDir(double angle) 
    {
        dir = angle;
    }
     
    /**
     * Move the ball based on it's direction
     */
    public void move()
    {
        if (dir == -90) {
            x -= 0.1;
        } else if (dir == 90) {
            x += 0.1;
        } else if (dir == 180) {
            z -= 0.1;
        } else if (dir == 0) {
            z += 0.1;
        }
    }   
}