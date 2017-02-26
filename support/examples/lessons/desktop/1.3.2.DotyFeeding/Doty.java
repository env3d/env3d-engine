public class Doty
{
 
    private double x;
    private double y;
    private double z;
    private String texture;
     
    public Doty(double x, double y, double z)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.z = z;
        texture = "textures/doty.png";
    }
     
    public void moveX(double delta)
    {
        x = x + delta;
    }
     
    public void moveZ(double delta)
    {
        z = z + delta;
    }    
}