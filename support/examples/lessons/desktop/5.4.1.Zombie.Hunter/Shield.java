import env3d.advanced.EnvNode;
import env3d.Env;

public class Shield extends EnvNode
{
    private int duration = 300;
    private Env env;
    
    public Shield(Env env)
    {
        this.env = env;
        // a transparent red ball
        env.setDisplayStr("Gone berzerk! Slash those zombies!!!", 120, 330, 1.5, 1, 0, 0, 1); 
        setTexture("textures/red.png");
        setScale(1.2);
        setTransparent(true);
    }

    private void flash()
    {
        if (getScale() <= 0) {
            setScale(1.2);
        } else {
            setScale(0);
        }
    }
    
    public boolean active() 
    {
        if (duration > 0) {
            duration--;
            if (duration < 100) {
                if (duration % 15 == 0) {
                    flash();
                }
            }
            return true;
        } else {
            env.setDisplayStr(null, 120, 330);
            return false;
        }
    }
}
