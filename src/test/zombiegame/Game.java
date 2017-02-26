package test.zombiegame;

import env3d.Env;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvTerrain;
import env3d.advanced.EnvSkyRoom;
import org.lwjgl.input.Keyboard;
 
public class Game
{
 
    public Game()
    { 
    }
 
 
    public void play()
    {
        int ammo = 10;
        
        // A variable to determine if the game is finished                
        boolean finished = false;
         
        // Create the env object so that we can manipulate
        // the 3D environment.
        Env env = new EnvAdvanced();        
        env.setDefaultControl(false);                
        env.setMouseGrab(true);
        env.soundLoop("/Users/jmadar/Documents/sounds/zombie main music.ogg");
        
        env.setRoom(new EnvSkyRoom("textures/skybox/default"));
        
        EnvTerrain terrain = new EnvTerrain("textures/terrain/termap1.png");
        terrain.setTexture("textures/mud.png");
        env.addObject(terrain);
        terrain.setScale(1, 0, 1);
         
        Hunter player = new Hunter(5, 1, 25);       
        env.addObject(player);       
        
        Zombie [] zombies = new Zombie[10];
        for (int i = 0; i < 10; i++) {
            zombies[i] = new Zombie(5, 1, 5);
            env.addObject(zombies[i]);
        }
        

        while (!finished)
        {
            String ammoString = "Ammo: ";
            for (int i = 0; i < ammo; i++) {
                ammoString = ammoString+" | ";
            }
            env.setDisplayStr(ammoString, 25, 25);
            // Gather the user input
            int key = env.getKey();            
 
            // Terminate program when user press escape
            if (key == 1) {
                finished = true;
            }             
            
            player.setRotateY(player.getRotateY()-env.getMouseDX()/5);
            env.setCameraPitch(env.getCameraPitch()+env.getMouseDY()/5);
            
            if (env.getKeyDown(Keyboard.KEY_S)) {
                player.walk(player.getRotateY(), -0.2);
            }             
            if (env.getKeyDown(Keyboard.KEY_W)) {
                player.walk(player.getRotateY(), 0.2);
            }            
            if (env.getKeyDown(Keyboard.KEY_A)) {
                player.strife(player.getRotateY()+90, 0.2);
            }
            if (env.getKeyDown(Keyboard.KEY_D)) {
                player.strife(player.getRotateY()-90, 0.2);
            }
            
            
            if (env.getMouseButtonDown(0) && player.getState() != Hunter.MELEE && player.getState() != Hunter.RANGED) {
                if (ammo > 0) {
                    player.setState(Hunter.RANGED);
                } else {
                    player.setState(Hunter.MELEE);
                }
                for (Zombie z : zombies) {
                    if (!z.isDead() && distance(player.getX(), z.getX(), player.getY(), z.getY(), player.getZ(), z.getZ()) < 2) {    
                        player.setState(Hunter.MELEE);
                        break;
                    }
                }
            }
            
            
            if (player.getState() == Hunter.MELEE && player.getFrame() == 15) {               
                for (Zombie z : zombies) {
                    if (!z.isDead() && distance(player.getX(), z.getX(), player.getY(), z.getY(), player.getZ(), z.getZ()) < 2) {
                        env.soundPlay("/Users/jmadar/Documents/sounds/male-death-sounds/15.wav");
                        z.setState(Zombie.DIE);                          
                        break;
                    }
                }
            }
            
            if (player.getState() == Hunter.RANGED && player.getFrame() == 15) {
                //System.out.println("Shot!");
                env.soundPlay("/Users/jmadar/Documents/sounds/gun/10 Guage Shotgun-SoundBible.com-74120584.wav");
                ammo--;
                double bX = player.getX();
                double bY = player.getY();
                double bZ = player.getZ();
                double bR = player.getRotateY();                        
                // check 100 times, 100 here is the range of the weapon
                boolean hit = false;
                for (int i = 0; i < 20; i++) {
                    bX = bX + 0.5*Math.sin(Math.toRadians(bR));
                    bZ = bZ + 0.5*Math.cos(Math.toRadians(bR));
                    for (Zombie z : zombies) {
                        if (!z.isDead() && distance(bX, z.getX(), bY, z.getY(), bZ, z.getZ()) < 0.5) {
                            // turn zombie to face player
                            if (Math.random() < 0.3) {
                                double angle = Math.toDegrees(Math.atan2(player.getX()-z.getX(),player.getZ()-z.getZ()));
                                z.setRotateY(angle);
                                z.setState(Zombie.BLOWNED);
                                env.soundPlay("/Users/jmadar/Documents/sounds/oh_yeah_wav_cut.wav");
                            } else {
                                if (Math.random() > 0.5) {
                                    z.setState(Zombie.ATTACKED1);
                                } else {
                                    z.setState(Zombie.ATTACKED2);
                                }
                            }
                            hit = true;
                            break;
                        }
                    }
                    if (hit) break;
                }             
            }

            player.move();
            for (Zombie z : zombies) {
                z.move();
                // Attack if player is in front on the zombie
                double vecX = Math.sin(Math.toRadians(z.getRotateY()));
                double vecZ = Math.cos(Math.toRadians(z.getRotateY()));
                double zX = z.getX() + vecX;
                double zY = z.getY();
                double zZ = z.getZ() + vecZ;
                if (distance(player.getX(), zX, player.getY(), zY, player.getZ(), zZ) < 1) {
                    if (z.getState() == Zombie.WALK1 || z.getState() == Zombie.WALK2) {
                        z.setState(Zombie.PUNCH);
                    }
                    if (z.getState() == Zombie.PUNCH && Math.random() > 0.5) {                                                
                        player.setState(Hunter.FLINCH);                    
                    }
                } else {
                    if (z.getState() == Zombie.PUNCH) {
                        z.setState(Zombie.WALK1);
                    }
                }
                
                if (z.getState() == Zombie.WALK1 || z.getState() == Zombie.WALK2) {
                
                    if (distance(player.getX(), z.getX() + 3*vecX, player.getY(), zY, player.getZ(), z.getZ() + 3*vecZ) < 3) {
                    // turn and attack
                        double angle = Math.toDegrees(Math.atan2(player.getX()-z.getX(),player.getZ()-z.getZ()));                    
                        z.setRotateY(angle);
                        z.setState(Zombie.WALK2);
                    } else {
                        z.setState(Zombie.WALK1);
                    }
                }
            }                        
            
            
            env.setCameraXYZ(player.getX() - 2 * Math.sin(Math.toRadians(player.getRotateY())),
            player.getY() + 1 - 2*Math.toRadians(env.getCameraPitch()),
            player.getZ() - 2 * Math.cos(Math.toRadians(player.getRotateY())));
            

            env.setCameraYaw(player.getRotateY() - 180);    
            
            // Update the environment
            env.advanceOneFrame(30);
        }
         
 
        // Exit the program cleanly
        env.exit();
    }
    
    private double distance(double x1, double x2, double y1, double y2, double z1, double z2) {
        double xdiff, ydiff, zdiff;
        xdiff = x2 - x1;
        ydiff = y2 - y1;
        zdiff = z2 - z1;
        return Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
    }    
    
    public static void main(String[] args) {
        (new Game()).play();
    }
    
}
