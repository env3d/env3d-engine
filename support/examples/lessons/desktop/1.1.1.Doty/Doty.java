/**
 * A simple class to demonstrate Env3D 
 */
public class Doty
{
    private double x;
    private double y;
    private double z;
    private double rotateX;
    private double rotateY;
    private double rotateZ;
    private double scale;
    private String texture;
     
    /**
     * Constructor 
     */
    public Doty()
    {
        // initialise instance variables
        x = 5;
        y = 2;
        z = 5;
        rotateX = 0;
        rotateY = 0;
        rotateZ = 0;
        scale = 1;
        texture = "textures/doty.png";
    }
     
    public void setXYZ(double x, double y, double z)
    {
        this.z = z;
        this.y = y;
        this.x = x;
    }    
     
    public void talk() 
    {
        texture = "textures/doty_talk.png";
    }
         
    public void happy() 
    {
        texture = "textures/doty_happy.png";
    }
    public void angry() 
    {
        texture = "textures/doty_angry.png";
    }    
 
    public void surprise() 
    {
        texture = "textures/doty_surprise.png";
    }
     
    public void sad() 
    {
        texture = "textures/doty_sad.png";
    }
      
    public void setRotateX(double ang) {
        rotateX = ang;
    }
 
    public void setRotateY(double ang) {
        rotateY = ang;
    }
 
    public void setRotateZ(double ang) {
        rotateZ = ang;
    }
     
    public String toString() {
        return "Doty position: "+x+" "+y+" "+z;   
    }
     
    public double getVolume() {
        double radius = scale/2;
        return (4/3f*3.1416f*radius*radius*radius);
    }
}