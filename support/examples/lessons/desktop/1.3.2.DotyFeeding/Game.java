import env3d.Env;
 
public class Game
{
    private Doty doty1;
     
    public Game()
    {
        doty1 = new Doty(5,1,9);        
    }
     
    public void play()
    {
        boolean finished;
        finished = false;
         
        Env env;
        env = new Env();
         
        // Adding doty to the environment
        env.addObject(doty1);
 
        // Disable the default keyboard and mouse control
        env.setDefaultControl(false); 
 
        // Fix the camera location
        env.setCameraXYZ(5,9,13);
        env.setCameraPitch(-45);
 
         
        // The game loop
        while (finished == false)
        {
            int currentKey = env.getKey();
             
            if (currentKey == 1) {
                // escape button
                finished = true;
            } else if (currentKey == 200) {
                // Up arrow
                doty1.moveZ(-0.3);
            } else if (currentKey == 208) {
                // Down arrow
                doty1.moveZ(0.3);
            } else if (currentKey == 203) {
                // Left arrow
                doty1.moveX(-0.3);                
            } else if (currentKey == 205) {
                // Right arrow
                doty1.moveX(0.3);
            }
           
            env.advanceOneFrame();
        }
         
        env.exit();
    }
     
    private double distance(double x1, double x2, double y1, double y2, double z1, double z2) {
        double xdiff, ydiff, zdiff;
        xdiff = x2 - x1;
        ydiff = y2 - y1;
        zdiff = z2 - z1;
        return Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
    }
}