package test.dotychase;


/**
 * Doty is the main character of the game
 * 
 */
public class Doty
{
    // Doty's location
    private float x, y, z;
    
    // Doty's previous location.  Useful in the revert method
    private float prev_x, prev_y, prev_z;

    // Base y level.  Used for jumping.
    private float base_y;
    
    private String texture;
    private float scale;    
    private float rotateY;
    private float rotateX;
    private float gravity;
    private float jump_speed;
    private float speed;
    private boolean jumping;
    private int jump_frame, jump_max;
    private boolean stopped;    
    
    // If doty remembers the room's dimension, doty will be able
    // to set itself to the right position when entering a new
    // room
    private float roomWidth, roomDepth;
    
    
    /**
     * Constructor for objects of class Doty
     */
    public Doty(float x, float y, float z)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.prev_x = x;
        this.prev_y = y;
        this.prev_z = z;
        
        texture = "textures/doty.png";
        scale = 0.5f;
        rotateY = 0;
        rotateX = 0;
        speed = 0.02f;
        gravity = 0.25f;
        jump_speed = 0f;

        jumping = false;
        jump_max = 120;
        
        stopped = false;
    }
    
    /**
     * Set the dimension of the room Doty is in
     */
    public void setRoomDim(float w, float d)
    {
        roomWidth = w;
        roomDepth = d;
    }

    /**
     * This method is called when Doty exits from a room.
     * If Doty exits from the east, then set Doty's position to the
     * west side of the new room.
     * 
     * @param dir The direction in which Doty exited from.
     */
    public void setExitFrom(String dir) 
    {
        if (dir.equals("east")) {
            setX(scale);
        } else if (dir.equals("west")) {
            setX(roomWidth-scale);
        }
    }
    
    /**
     * Move doty in the x direction.
     */
    public void moveX(float delta)
    {
        // Make Doty face the right direction
//        if (delta > 0) {
//            rotateY = 90;
//        } else {
//            rotateY = -90;
//        }            
//        rotateX += Math.abs(delta/(2*Math.PI*scale)*180);
        
        // Remember the previous position
        prev_z = z;
        prev_x = x;    
        
        // Move doty
        x = x + delta;
    }
    
    /**
     * Move doty in the z direction
     */
    public void moveZ(float delta)
    {
//        if (delta > 0) {
//            rotateY = 0;
//        } else {
//            rotateY = 180;
//        }
//        rotateX += Math.abs(delta/(2*Math.PI*scale)*180);        
        
        // Remember the previous position
        prev_z = z;
        prev_x = x;
        
        // Move doty
        z = z + delta;
    }

    /**
     * A generic move method that can be called every frame
     */
    public void move() 
    {
        rotateX += Math.abs(speed/(2*Math.PI*scale)*180);

        if (x != prev_x || z != prev_z) {
            stopped = false;
        }
        
        prev_x = x;
        prev_y = y;
        prev_z = z;

        x += ((float)(speed*Math.sin(Math.PI*rotateY/180)));
        z += ((float)(speed*Math.cos(Math.PI*rotateY/180)));
        
        // we are on higher groud, attempt to lower

        if (jumping || y > 1) {
            float g_speed = ((float)jump_frame++/60 * gravity);
            y -= g_speed;
            y += jump_speed;
            if (jumping && g_speed >= jump_speed) {
                // We are now free falling
                jump_frame = 0;
                jump_speed = 0;
                jumping = false;
            }
        }
        if (y <= 1) {
            y = 1f;
       }

//        }

//        if (jumping) {
//            y = base_y + (2f * (float) Math.sin(jump_frame++/(double)jump_max * Math.PI));
//            if ( jump_frame > jump_max ) jumping = false;
//        } else if (y > 1) {
//            y -= 0.1f;
//        }
    }
    
    public void jump() 
    {
        if (!jumping && (stopped || y <= 1)) {
            jumping = true;
            jump_frame = 0;
            base_y = y;
            jump_speed = 0.12f;
        }
    }
    
    /*
     * Have doty face the proper direction
     */
    public void rotateY(float delta)
    {
        if (!jumping) {
            this.rotateY += delta;
        }
    }
    
    /**
     * Reverts doty back to the previous position
     */
    public void revert()
    {
        jump_frame = 0;
        jumping = false;
        jump_speed = 0;
        stopped = true;
        x = prev_x;
        y = prev_y;
        z = prev_z;
    }
    
    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }
    
    public float getScale()
    {
        return scale;
    }
    
    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }
    
    public void setZ(float z)
    {
        this.z = z;
    }
    
    public void setSpeed(float s)
    {
        speed = s;
    }
    
    public float getSpeed()
    {
        return speed;
    }
    
    public float getRotateY()
    {
        return rotateY;
    }
    
    public boolean isStopped()
    {
        return stopped;
    }
    
    public String toString() 
    {
        return "Doty rotate Y: "+rotateY;
    }
}
