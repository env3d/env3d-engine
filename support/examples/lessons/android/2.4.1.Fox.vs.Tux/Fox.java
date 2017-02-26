public class Fox extends Creature
{
    public Fox(double x, double y, double z)
    {
        super(x, y, z);
          
        // Must use the mutator as the fields have private access
        // in the parent class
        setTexture("models/fox/fox.png");
        setModel("models/fox/fox.obj");
    }    
}