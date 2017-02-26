import env3d.EnvObject;
import java.util.ArrayList;
  
abstract public class Creature extends EnvObject
{
      
    /**
     * Constructor for objects of class Creature
     */
    public Creature(double x, double y, double z)
    {
        setX(x);
        setY(y);
        setZ(z);
        setScale(1);
    }
      
    public void move(ArrayList<Creature> creatures, ArrayList<Creature> dead_creatures)
    {                
        double rand = Math.random();
        if (rand < 0.25) {
            setX(getX()+getScale());
        } else if (rand < 0.5) {
            setX(getX()-getScale());
        } else if (rand < 0.75) {
            setZ(getZ()+getScale());
        } else if (rand < 1) {
            setZ(getZ()-getScale());
        }                
          
        if (getX() < getScale()) setX(getScale());
        if (getX() > 50-getScale()) setX(50 - getScale());
        if (getZ() < getScale()) setZ(getScale());
        if (getZ() > 50-getScale()) setZ(50 - getScale());
   
        // The move method now handles collision detection
        if (this instanceof Fox) {
            for (Creature c : creatures) {
                if (c.distance(this) < c.getScale()+this.getScale() && c instanceof Tux) {
                    dead_creatures.add(c);
                }
            }
        }
    }        
}