public class Tux extends Creature
{   
    public Tux(double x, double y, double z)
    {        
        super(x, y, z);
          
        // Must use the mutator as the fields have private access
        // in the parent class        
        setTexture("models/tux/tux.png");
        setModel("models/tux/tux.obj");
    }  
}