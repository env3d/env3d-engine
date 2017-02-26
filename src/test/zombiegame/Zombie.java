package test.zombiegame;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

public class Zombie
{
    //The various states of this model.  Each animation is a state
    public static final int TWITCH = 0;
    public static final int PUNCH = 1;
    public static final int KICK = 2;
    public static final int WALK2 = 3;
    public static final int WALK1 = 4;
    public static final int HEADBUTT = 5;
    public static final int DIE = 6;
    public static final int ATTACKED2 = 7;
    public static final int ATTACKED1 = 8;
    public static final int IDLE1 = 9;
    public static final int BLOWNED = 10;
    public static final int IDLE2 = 11;
    
    private HashMap<Integer, String[]> modelsMap = new HashMap<Integer, String[]>();

    
    //Fields for state management
    private int state;
    private double frame;
    
    // Required env3d fields
    private double x, y, z, rotateX, rotateY, rotateZ, scale = 1;
    private String texture, model;

    public Zombie(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        texture = "models/zombie/Zombie.png";
        model = "models/zombie/zombie.obj";
        state = IDLE1;
        init();
    }

    public void walk(double direction) 
    {
        if (state != TWITCH && state != BLOWNED && state != DIE) {
            rotateY = direction;
            x = x + 0.02*Math.sin(Math.toRadians(direction));
            z = z + 0.02*Math.cos(Math.toRadians(direction));            
        }
    }
    
    private int step = 0;    
    private double randAngle;
    public void randomWalk()
    {        
        if (step++ % 60 == 0) {
            randAngle += Math.random()*180 - 90;
        }
        walk(randAngle);
        setState(WALK1);

    }
    
    /** 
     * Basic implementation. Simply animate the model
     * based on the state 
     */ 
    public void move() 
    {
        if (state == WALK1) {
            randomWalk();        
        } else if (state == WALK2) {
            walk(rotateY);
        }
        model = modelsMap.get(state)[(int)frame];        
        if (state == IDLE1 || state == IDLE2) {        
            frame = (frame+0.5) % modelsMap.get(state).length;
        } else {
            frame = (frame+1) % modelsMap.get(state).length;
        }
        // Check to see if we need to switch to a new state
        if (frame < 0.2) {
            if (state == BLOWNED) {
                state = TWITCH;
            } 
            else if (state == TWITCH) {
                state = TWITCH;
            }
            else if (state == DIE) {
                state = DIE;
                frame = modelsMap.get(state).length-1;
            }
            else if (state == ATTACKED1 || state == ATTACKED2) {
                state = IDLE1;
            }             
            else if (state == IDLE1 || state == IDLE2) {
                state = WALK1;
            }
        }
    }

    public void setState(int newState)
    {
        if (state != newState) {
            frame = 0;
            state = newState;
        }
    }            
    
    public int getState() {
        return state;
    }
    
    public boolean isDead() 
    {
        return state == BLOWNED || state == TWITCH || state == DIE;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getRotateX() {
        return rotateX;
    }

    public void setRotateX(double rotateX) {
        this.rotateX = rotateX;
    }

    public double getRotateY() {
        return rotateY;
    }

    public void setRotateY(double rotateY) {
        this.rotateY = rotateY;
    }

    public double getRotateZ() {
        return rotateZ;
    }

    public void setRotateZ(double rotateZ) {
        this.rotateZ = rotateZ;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    //Initialize the modelsMap hash for animation purpose
    private void init() {
        String[] twitch = new String[11];
        twitch[0] = "models/zombie/twitch/zombie_000078.obj";
        twitch[1] = "models/zombie/twitch/zombie_000079.obj";
        twitch[2] = "models/zombie/twitch/zombie_000080.obj";
        twitch[3] = "models/zombie/twitch/zombie_000081.obj";
        twitch[4] = "models/zombie/twitch/zombie_000082.obj";
        twitch[5] = "models/zombie/twitch/zombie_000083.obj";
        twitch[6] = "models/zombie/twitch/zombie_000084.obj";
        twitch[7] = "models/zombie/twitch/zombie_000085.obj";
        twitch[8] = "models/zombie/twitch/zombie_000086.obj";
        twitch[9] = "models/zombie/twitch/zombie_000087.obj";
        twitch[10] = "models/zombie/twitch/zombie_000088.obj";
        modelsMap.put(TWITCH,twitch);
        String[] punch = new String[12];
        punch[0] = "models/zombie/punch/zombie_000117.obj";
        punch[1] = "models/zombie/punch/zombie_000118.obj";
        punch[2] = "models/zombie/punch/zombie_000119.obj";
        punch[3] = "models/zombie/punch/zombie_000120.obj";
        punch[4] = "models/zombie/punch/zombie_000121.obj";
        punch[5] = "models/zombie/punch/zombie_000122.obj";
        punch[6] = "models/zombie/punch/zombie_000123.obj";
        punch[7] = "models/zombie/punch/zombie_000124.obj";
        punch[8] = "models/zombie/punch/zombie_000125.obj";
        punch[9] = "models/zombie/punch/zombie_000126.obj";
        punch[10] = "models/zombie/punch/zombie_000127.obj";
        punch[11] = "models/zombie/punch/zombie_000128.obj";
        modelsMap.put(PUNCH,punch);
        String[] kick = new String[10];
        kick[0] = "models/zombie/kick/zombie_000106.obj";
        kick[1] = "models/zombie/kick/zombie_000107.obj";
        kick[2] = "models/zombie/kick/zombie_000108.obj";
        kick[3] = "models/zombie/kick/zombie_000109.obj";
        kick[4] = "models/zombie/kick/zombie_000110.obj";
        kick[5] = "models/zombie/kick/zombie_000111.obj";
        kick[6] = "models/zombie/kick/zombie_000112.obj";
        kick[7] = "models/zombie/kick/zombie_000113.obj";
        kick[8] = "models/zombie/kick/zombie_000114.obj";
        kick[9] = "models/zombie/kick/zombie_000115.obj";
        modelsMap.put(KICK,kick);
        String[] walk2 = new String[15];
        walk2[0] = "models/zombie/walk2/zombie_000022.obj";
        walk2[1] = "models/zombie/walk2/zombie_000023.obj";
        walk2[2] = "models/zombie/walk2/zombie_000024.obj";
        walk2[3] = "models/zombie/walk2/zombie_000025.obj";
        walk2[4] = "models/zombie/walk2/zombie_000026.obj";
        walk2[5] = "models/zombie/walk2/zombie_000027.obj";
        walk2[6] = "models/zombie/walk2/zombie_000028.obj";
        walk2[7] = "models/zombie/walk2/zombie_000029.obj";
        walk2[8] = "models/zombie/walk2/zombie_000030.obj";
        walk2[9] = "models/zombie/walk2/zombie_000031.obj";
        walk2[10] = "models/zombie/walk2/zombie_000032.obj";
        walk2[11] = "models/zombie/walk2/zombie_000033.obj";
        walk2[12] = "models/zombie/walk2/zombie_000034.obj";
        walk2[13] = "models/zombie/walk2/zombie_000035.obj";
        walk2[14] = "models/zombie/walk2/zombie_000036.obj";
        modelsMap.put(WALK2,walk2);
        String[] walk1 = new String[19];
        walk1[0] = "models/zombie/walk1/zombie_000002.obj";
        walk1[1] = "models/zombie/walk1/zombie_000003.obj";
        walk1[2] = "models/zombie/walk1/zombie_000004.obj";
        walk1[3] = "models/zombie/walk1/zombie_000005.obj";
        walk1[4] = "models/zombie/walk1/zombie_000006.obj";
        walk1[5] = "models/zombie/walk1/zombie_000007.obj";
        walk1[6] = "models/zombie/walk1/zombie_000008.obj";
        walk1[7] = "models/zombie/walk1/zombie_000009.obj";
        walk1[8] = "models/zombie/walk1/zombie_000010.obj";
        walk1[9] = "models/zombie/walk1/zombie_000011.obj";
        walk1[10] = "models/zombie/walk1/zombie_000012.obj";
        walk1[11] = "models/zombie/walk1/zombie_000013.obj";
        walk1[12] = "models/zombie/walk1/zombie_000014.obj";
        walk1[13] = "models/zombie/walk1/zombie_000015.obj";
        walk1[14] = "models/zombie/walk1/zombie_000016.obj";
        walk1[15] = "models/zombie/walk1/zombie_000017.obj";
        walk1[16] = "models/zombie/walk1/zombie_000018.obj";
        walk1[17] = "models/zombie/walk1/zombie_000019.obj";
        walk1[18] = "models/zombie/walk1/zombie_000020.obj";
        modelsMap.put(WALK1,walk1);
        String[] headbutt = new String[8];
        headbutt[0] = "models/zombie/headbutt/zombie_000129.obj";
        headbutt[1] = "models/zombie/headbutt/zombie_000130.obj";
        headbutt[2] = "models/zombie/headbutt/zombie_000131.obj";
        headbutt[3] = "models/zombie/headbutt/zombie_000132.obj";
        headbutt[4] = "models/zombie/headbutt/zombie_000133.obj";
        headbutt[5] = "models/zombie/headbutt/zombie_000134.obj";
        headbutt[6] = "models/zombie/headbutt/zombie_000135.obj";
        headbutt[7] = "models/zombie/headbutt/zombie_000136.obj";
        modelsMap.put(HEADBUTT,headbutt);
        String[] die = new String[13];
        die[0] = "models/zombie/die/zombie_000091.obj";
        die[1] = "models/zombie/die/zombie_000092.obj";
        die[2] = "models/zombie/die/zombie_000093.obj";
        die[3] = "models/zombie/die/zombie_000094.obj";
        die[4] = "models/zombie/die/zombie_000095.obj";
        die[5] = "models/zombie/die/zombie_000096.obj";
        die[6] = "models/zombie/die/zombie_000097.obj";
        die[7] = "models/zombie/die/zombie_000098.obj";
        die[8] = "models/zombie/die/zombie_000099.obj";
        die[9] = "models/zombie/die/zombie_000100.obj";
        die[10] = "models/zombie/die/zombie_000101.obj";
        die[11] = "models/zombie/die/zombie_000102.obj";
        die[12] = "models/zombie/die/zombie_000103.obj";
        modelsMap.put(DIE,die);
        String[] attacked2 = new String[10];
        attacked2[0] = "models/zombie/attacked2/zombie_000048.obj";
        attacked2[1] = "models/zombie/attacked2/zombie_000049.obj";
        attacked2[2] = "models/zombie/attacked2/zombie_000050.obj";
        attacked2[3] = "models/zombie/attacked2/zombie_000051.obj";
        attacked2[4] = "models/zombie/attacked2/zombie_000052.obj";
        attacked2[5] = "models/zombie/attacked2/zombie_000053.obj";
        attacked2[6] = "models/zombie/attacked2/zombie_000054.obj";
        attacked2[7] = "models/zombie/attacked2/zombie_000055.obj";
        attacked2[8] = "models/zombie/attacked2/zombie_000056.obj";
        attacked2[9] = "models/zombie/attacked2/zombie_000057.obj";
        modelsMap.put(ATTACKED2,attacked2);
        String[] attacked1 = new String[10];
        attacked1[0] = "models/zombie/attacked1/zombie_000038.obj";
        attacked1[1] = "models/zombie/attacked1/zombie_000039.obj";
        attacked1[2] = "models/zombie/attacked1/zombie_000040.obj";
        attacked1[3] = "models/zombie/attacked1/zombie_000041.obj";
        attacked1[4] = "models/zombie/attacked1/zombie_000042.obj";
        attacked1[5] = "models/zombie/attacked1/zombie_000043.obj";
        attacked1[6] = "models/zombie/attacked1/zombie_000044.obj";
        attacked1[7] = "models/zombie/attacked1/zombie_000045.obj";
        attacked1[8] = "models/zombie/attacked1/zombie_000046.obj";
        attacked1[9] = "models/zombie/attacked1/zombie_000047.obj";
        modelsMap.put(ATTACKED1,attacked1);
        String[] idle1 = new String[33];
        idle1[0] = "models/zombie/idle1/zombie_000137.obj";
        idle1[1] = "models/zombie/idle1/zombie_000138.obj";
        idle1[2] = "models/zombie/idle1/zombie_000139.obj";
        idle1[3] = "models/zombie/idle1/zombie_000140.obj";
        idle1[4] = "models/zombie/idle1/zombie_000141.obj";
        idle1[5] = "models/zombie/idle1/zombie_000142.obj";
        idle1[6] = "models/zombie/idle1/zombie_000143.obj";
        idle1[7] = "models/zombie/idle1/zombie_000144.obj";
        idle1[8] = "models/zombie/idle1/zombie_000145.obj";
        idle1[9] = "models/zombie/idle1/zombie_000146.obj";
        idle1[10] = "models/zombie/idle1/zombie_000147.obj";
        idle1[11] = "models/zombie/idle1/zombie_000148.obj";
        idle1[12] = "models/zombie/idle1/zombie_000149.obj";
        idle1[13] = "models/zombie/idle1/zombie_000150.obj";
        idle1[14] = "models/zombie/idle1/zombie_000151.obj";
        idle1[15] = "models/zombie/idle1/zombie_000152.obj";
        idle1[16] = "models/zombie/idle1/zombie_000153.obj";
        idle1[17] = "models/zombie/idle1/zombie_000154.obj";
        idle1[18] = "models/zombie/idle1/zombie_000155.obj";
        idle1[19] = "models/zombie/idle1/zombie_000156.obj";
        idle1[20] = "models/zombie/idle1/zombie_000157.obj";
        idle1[21] = "models/zombie/idle1/zombie_000158.obj";
        idle1[22] = "models/zombie/idle1/zombie_000159.obj";
        idle1[23] = "models/zombie/idle1/zombie_000160.obj";
        idle1[24] = "models/zombie/idle1/zombie_000161.obj";
        idle1[25] = "models/zombie/idle1/zombie_000162.obj";
        idle1[26] = "models/zombie/idle1/zombie_000163.obj";
        idle1[27] = "models/zombie/idle1/zombie_000164.obj";
        idle1[28] = "models/zombie/idle1/zombie_000165.obj";
        idle1[29] = "models/zombie/idle1/zombie_000166.obj";
        idle1[30] = "models/zombie/idle1/zombie_000167.obj";
        idle1[31] = "models/zombie/idle1/zombie_000168.obj";
        idle1[32] = "models/zombie/idle1/zombie_000169.obj";
        modelsMap.put(IDLE1,idle1);
        String[] blowned = new String[17];
        blowned[0] = "models/zombie/blowned/zombie_000059.obj";
        blowned[1] = "models/zombie/blowned/zombie_000060.obj";
        blowned[2] = "models/zombie/blowned/zombie_000061.obj";
        blowned[3] = "models/zombie/blowned/zombie_000062.obj";
        blowned[4] = "models/zombie/blowned/zombie_000063.obj";
        blowned[5] = "models/zombie/blowned/zombie_000064.obj";
        blowned[6] = "models/zombie/blowned/zombie_000065.obj";
        blowned[7] = "models/zombie/blowned/zombie_000066.obj";
        blowned[8] = "models/zombie/blowned/zombie_000067.obj";
        blowned[9] = "models/zombie/blowned/zombie_000068.obj";
        blowned[10] = "models/zombie/blowned/zombie_000069.obj";
        blowned[11] = "models/zombie/blowned/zombie_000070.obj";
        blowned[12] = "models/zombie/blowned/zombie_000071.obj";
        blowned[13] = "models/zombie/blowned/zombie_000072.obj";
        blowned[14] = "models/zombie/blowned/zombie_000073.obj";
        blowned[15] = "models/zombie/blowned/zombie_000074.obj";
        blowned[16] = "models/zombie/blowned/zombie_000075.obj";
        modelsMap.put(BLOWNED,blowned);
        String[] idle2 = new String[31];
        idle2[0] = "models/zombie/idle2/zombie_000170.obj";
        idle2[1] = "models/zombie/idle2/zombie_000171.obj";
        idle2[2] = "models/zombie/idle2/zombie_000172.obj";
        idle2[3] = "models/zombie/idle2/zombie_000173.obj";
        idle2[4] = "models/zombie/idle2/zombie_000174.obj";
        idle2[5] = "models/zombie/idle2/zombie_000175.obj";
        idle2[6] = "models/zombie/idle2/zombie_000176.obj";
        idle2[7] = "models/zombie/idle2/zombie_000177.obj";
        idle2[8] = "models/zombie/idle2/zombie_000178.obj";
        idle2[9] = "models/zombie/idle2/zombie_000179.obj";
        idle2[10] = "models/zombie/idle2/zombie_000180.obj";
        idle2[11] = "models/zombie/idle2/zombie_000181.obj";
        idle2[12] = "models/zombie/idle2/zombie_000182.obj";
        idle2[13] = "models/zombie/idle2/zombie_000183.obj";
        idle2[14] = "models/zombie/idle2/zombie_000184.obj";
        idle2[15] = "models/zombie/idle2/zombie_000185.obj";
        idle2[16] = "models/zombie/idle2/zombie_000186.obj";
        idle2[17] = "models/zombie/idle2/zombie_000187.obj";
        idle2[18] = "models/zombie/idle2/zombie_000188.obj";
        idle2[19] = "models/zombie/idle2/zombie_000189.obj";
        idle2[20] = "models/zombie/idle2/zombie_000190.obj";
        idle2[21] = "models/zombie/idle2/zombie_000191.obj";
        idle2[22] = "models/zombie/idle2/zombie_000192.obj";
        idle2[23] = "models/zombie/idle2/zombie_000193.obj";
        idle2[24] = "models/zombie/idle2/zombie_000194.obj";
        idle2[25] = "models/zombie/idle2/zombie_000195.obj";
        idle2[26] = "models/zombie/idle2/zombie_000196.obj";
        idle2[27] = "models/zombie/idle2/zombie_000197.obj";
        idle2[28] = "models/zombie/idle2/zombie_000198.obj";
        idle2[29] = "models/zombie/idle2/zombie_000199.obj";
        idle2[30] = "models/zombie/idle2/zombie_000200.obj";
        modelsMap.put(IDLE2,idle2);
    }

}
