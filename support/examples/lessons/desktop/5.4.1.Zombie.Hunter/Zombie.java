import java.util.HashMap;
import java.util.ArrayList;
import env3d.advanced.EnvNode;

/**
 * The Zombie class
 */
public class Zombie extends Actor
{
    // The various states of this model.  Each animation is a state
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
    // The main data structure for animated models, a HashMap of ArrayList
    private HashMap<Integer, ArrayList<String>> modelsMap = new HashMap<Integer, ArrayList<String>>();

    //Fields for state management
    private int state = WALK1;
    private int frame;
    
    // zombie's speed
    public double speed = 0.03;
    // the range where zombie can "see" the hunter
    public double seekRange = 5;
    public double attackRange = 0.5;
    
    public EnvNode seekRangeNode, attackRangeNode;
    
    private MiniMath miniMath;
    private int health = 0;
    
    /**
     * Zero-argument constructor that places the object in location 0, 0, 0
     */
    public Zombie()
    {
        this(0,0,0);
    }

    /**
     * Parameterized constructor - allows arbitary of object
     */
    public Zombie(double x, double y, double z)
    {
        setX(x);
        setY(y);
        setZ(z);
        setScale(1);
        setTexture("models/zombie/Zombie.png");
        setModel("models/zombie/zombie.obj");
        // initialize the animation HashMap
        init();
    }

    /**
     * Performs setup
     */
    private void setup()
    {
        // each zombie has its own miniMath game
        if (miniMath == null) {
            miniMath = new MiniMath(getEnv());
            miniMath.setMode(MiniMath.SUBTRACT);
            miniMath.reset();
        }        
    }
    
    /** 
     * Basic implementation. Simply animate the model
     * based on the state 
     */ 
    public void move()     
    {   
        setup();
        
        // Move the zombie if not dead
        if (!isDead() && getState() != ATTACKED1) {
            ai();
        }
        
        // Animate the zombie
        setModel(modelsMap.get(state).get(frame));
        frame = (frame+1) % modelsMap.get(state).size();                      
        
        if (frame == 0) {
            if (state == DIE) {
                frame = modelsMap.get(state).size()-1;
            } else if (state == BLOWNED) {
                setState(TWITCH);
            }
            
            // If neither DIE, BLOWNED, or TWITCH, set back to IDLE
            if (!isDead())  setState(IDLE1);
        }
                
    }
    
    public boolean isDead() 
    {
        return (getState() == DIE || getState() == TWITCH || getState() == BLOWNED);
    }
    
    private void ai()
    {
        // either attack or seek the hunter
        Hunter hunter = getEnv().getObject(Hunter.class);
        
        // detect if hunter is in "front" of this zombie, vecX and vecZ are 
        // vector components that extends in front of the zombie
        double vecX =  Math.sin(Math.toRadians(this.getRotateY()));
        double vecZ =  Math.cos(Math.toRadians(this.getRotateY()));
        if (attackRangeNode == null) {
            attackRangeNode = new EnvNode();
            attackRangeNode.setScale(attackRange);
            //getEnv().addObject(attackRangeNode);
        }
        attackRangeNode.setX(getX()+attackRange*vecX);
        attackRangeNode.setY(getY());
        attackRangeNode.setZ(getZ()+attackRange*vecZ);

        if (seekRangeNode == null) {
            seekRangeNode = new EnvNode();
            seekRangeNode.setScale(seekRange);
            //getEnv().addObject(seekRangeNode);
        }
        seekRangeNode.setX(getX()+seekRange*vecX);
        seekRangeNode.setY(getY());
        seekRangeNode.setZ(getZ()+seekRange*vecZ);
        
        if (hunter.distance(attackRangeNode) < attackRangeNode.getScale() + hunter.getScale()) { 
            // Play the attack animation
            if (Math.random() < 0.5) {
                setState(PUNCH);
            } else {
                setState(KICK);
            }
            
            // Don't bother attacking if the hunter's shield is on, is dead or hurt
            if (hunter.getShield() != null || hunter.getState() == Hunter.MELEE || 
                hunter.getState() == Hunter.DIE || hunter.getState() == Hunter.FLINCH) return;
            
            if (Math.random() < 0.1) {   
                // chance of attacking the hunter.
                env.setDisplayStr("Under Attack!", 200, 350, 3, 1, 0, 0, 1);
                miniMath.play();
                env.setDisplayStr(null, 200, 350);
                if (miniMath.isCorrect()) {
                    hunter.turnToFace(this);
                    hunter.setState(Hunter.MELEE);
                    this.setState(IDLE1);
                } else {
                    hunter.setState(Hunter.FLINCH);
                }
                miniMath.reset();
            }

        } else if (hunter.distance(seekRangeNode) < seekRangeNode.getScale() + hunter.getScale()){
            // Only seek hunter if it is in "front" of zombie
            seekHunter();
        } else {
            // random wolk
            randomWalk();           
        }
    }
    
    public void setSeekRange(double dist) 
    {
        seekRangeNode.setScale(dist);
    }
       
    // part of the AI -- seek out the hunter
    private void seekHunter()
    {
        setState(WALK1);
        turnToFace(getEnv().getObject(Hunter.class));
        moveForward(speed*1.3);
        if (seekRangeNode.getScale() > seekRange) {
            seekRangeNode.setScale(seekRangeNode.getScale()-0.01);
        }        
    }
    
    private void randomWalk()
    {
        setState(WALK2);
        if (frame == 0) {
            setRotateY(getRotateY()+Math.random()*90-45);
        }
        moveForward(speed);
    }
    
    public void hit()
    {
        health--;
        if (health < 0) {
            setState(DIE);
        } else {
            setState(ATTACKED1);
        }
    }
    
    /**
     * Returns the current animation state
     */
    public int getState()
    {
        return state;
    }

    /**
     * Sets the current animation state.  Resets frame counter to 0
     * Note: it only sets the state if new state is different than
     * the current state
     */
    public void setState(int newState)
    {
        if (state != newState) {
            frame = 0;
            state = newState;
        }
    }

    // Initialize the modelsMap hash for animation purpose
    private void init() {
        ArrayList<String> twitch = new ArrayList<String>();
        twitch.add("models/zombie/twitch/zombie_000078.obj");
        twitch.add("models/zombie/twitch/zombie_000079.obj");
        twitch.add("models/zombie/twitch/zombie_000080.obj");
        twitch.add("models/zombie/twitch/zombie_000081.obj");
        twitch.add("models/zombie/twitch/zombie_000082.obj");
        twitch.add("models/zombie/twitch/zombie_000083.obj");
        twitch.add("models/zombie/twitch/zombie_000084.obj");
        twitch.add("models/zombie/twitch/zombie_000085.obj");
        twitch.add("models/zombie/twitch/zombie_000086.obj");
        twitch.add("models/zombie/twitch/zombie_000087.obj");
        twitch.add("models/zombie/twitch/zombie_000088.obj");
        modelsMap.put(TWITCH,twitch);
        ArrayList<String> punch = new ArrayList<String>();
        punch.add("models/zombie/punch/zombie_000117.obj");
        punch.add("models/zombie/punch/zombie_000118.obj");
        punch.add("models/zombie/punch/zombie_000119.obj");
        punch.add("models/zombie/punch/zombie_000120.obj");
        punch.add("models/zombie/punch/zombie_000121.obj");
        punch.add("models/zombie/punch/zombie_000122.obj");
        punch.add("models/zombie/punch/zombie_000123.obj");
        punch.add("models/zombie/punch/zombie_000124.obj");
        punch.add("models/zombie/punch/zombie_000125.obj");
        punch.add("models/zombie/punch/zombie_000126.obj");
        punch.add("models/zombie/punch/zombie_000127.obj");
        punch.add("models/zombie/punch/zombie_000128.obj");
        modelsMap.put(PUNCH,punch);
        ArrayList<String> kick = new ArrayList<String>();
        kick.add("models/zombie/kick/zombie_000106.obj");
        kick.add("models/zombie/kick/zombie_000107.obj");
        kick.add("models/zombie/kick/zombie_000108.obj");
        kick.add("models/zombie/kick/zombie_000109.obj");
        kick.add("models/zombie/kick/zombie_000110.obj");
        kick.add("models/zombie/kick/zombie_000111.obj");
        kick.add("models/zombie/kick/zombie_000112.obj");
        kick.add("models/zombie/kick/zombie_000113.obj");
        kick.add("models/zombie/kick/zombie_000114.obj");
        kick.add("models/zombie/kick/zombie_000115.obj");
        modelsMap.put(KICK,kick);
        ArrayList<String> walk2 = new ArrayList<String>();
        walk2.add("models/zombie/walk2/zombie_000022.obj");
        walk2.add("models/zombie/walk2/zombie_000023.obj");
        walk2.add("models/zombie/walk2/zombie_000024.obj");
        walk2.add("models/zombie/walk2/zombie_000025.obj");
        walk2.add("models/zombie/walk2/zombie_000026.obj");
        walk2.add("models/zombie/walk2/zombie_000027.obj");
        walk2.add("models/zombie/walk2/zombie_000028.obj");
        walk2.add("models/zombie/walk2/zombie_000029.obj");
        walk2.add("models/zombie/walk2/zombie_000030.obj");
        walk2.add("models/zombie/walk2/zombie_000031.obj");
        walk2.add("models/zombie/walk2/zombie_000032.obj");
        walk2.add("models/zombie/walk2/zombie_000033.obj");
        walk2.add("models/zombie/walk2/zombie_000034.obj");
        walk2.add("models/zombie/walk2/zombie_000035.obj");
        walk2.add("models/zombie/walk2/zombie_000036.obj");
        modelsMap.put(WALK2,walk2);
        ArrayList<String> walk1 = new ArrayList<String>();
        walk1.add("models/zombie/walk1/zombie_000002.obj");
        walk1.add("models/zombie/walk1/zombie_000003.obj");
        walk1.add("models/zombie/walk1/zombie_000004.obj");
        walk1.add("models/zombie/walk1/zombie_000005.obj");
        walk1.add("models/zombie/walk1/zombie_000006.obj");
        walk1.add("models/zombie/walk1/zombie_000007.obj");
        walk1.add("models/zombie/walk1/zombie_000008.obj");
        walk1.add("models/zombie/walk1/zombie_000009.obj");
        walk1.add("models/zombie/walk1/zombie_000010.obj");
        walk1.add("models/zombie/walk1/zombie_000011.obj");
        walk1.add("models/zombie/walk1/zombie_000012.obj");
        walk1.add("models/zombie/walk1/zombie_000013.obj");
        walk1.add("models/zombie/walk1/zombie_000014.obj");
        walk1.add("models/zombie/walk1/zombie_000015.obj");
        walk1.add("models/zombie/walk1/zombie_000016.obj");
        walk1.add("models/zombie/walk1/zombie_000017.obj");
        walk1.add("models/zombie/walk1/zombie_000018.obj");
        walk1.add("models/zombie/walk1/zombie_000019.obj");
        walk1.add("models/zombie/walk1/zombie_000020.obj");
        modelsMap.put(WALK1,walk1);
        ArrayList<String> headbutt = new ArrayList<String>();
        headbutt.add("models/zombie/headbutt/zombie_000129.obj");
        headbutt.add("models/zombie/headbutt/zombie_000130.obj");
        headbutt.add("models/zombie/headbutt/zombie_000131.obj");
        headbutt.add("models/zombie/headbutt/zombie_000132.obj");
        headbutt.add("models/zombie/headbutt/zombie_000133.obj");
        headbutt.add("models/zombie/headbutt/zombie_000134.obj");
        headbutt.add("models/zombie/headbutt/zombie_000135.obj");
        headbutt.add("models/zombie/headbutt/zombie_000136.obj");
        modelsMap.put(HEADBUTT,headbutt);
        ArrayList<String> die = new ArrayList<String>();
        die.add("models/zombie/die/zombie_000091.obj");
        die.add("models/zombie/die/zombie_000092.obj");
        die.add("models/zombie/die/zombie_000093.obj");
        die.add("models/zombie/die/zombie_000094.obj");
        die.add("models/zombie/die/zombie_000095.obj");
        die.add("models/zombie/die/zombie_000096.obj");
        die.add("models/zombie/die/zombie_000097.obj");
        die.add("models/zombie/die/zombie_000098.obj");
        die.add("models/zombie/die/zombie_000099.obj");
        die.add("models/zombie/die/zombie_000100.obj");
        die.add("models/zombie/die/zombie_000101.obj");
        die.add("models/zombie/die/zombie_000102.obj");
        die.add("models/zombie/die/zombie_000103.obj");
        modelsMap.put(DIE,die);
        ArrayList<String> attacked2 = new ArrayList<String>();
        attacked2.add("models/zombie/attacked2/zombie_000048.obj");
        attacked2.add("models/zombie/attacked2/zombie_000049.obj");
        attacked2.add("models/zombie/attacked2/zombie_000050.obj");
        attacked2.add("models/zombie/attacked2/zombie_000051.obj");
        attacked2.add("models/zombie/attacked2/zombie_000052.obj");
        attacked2.add("models/zombie/attacked2/zombie_000053.obj");
        attacked2.add("models/zombie/attacked2/zombie_000054.obj");
        attacked2.add("models/zombie/attacked2/zombie_000055.obj");
        attacked2.add("models/zombie/attacked2/zombie_000056.obj");
        attacked2.add("models/zombie/attacked2/zombie_000057.obj");
        modelsMap.put(ATTACKED2,attacked2);
        ArrayList<String> attacked1 = new ArrayList<String>();
        attacked1.add("models/zombie/attacked1/zombie_000038.obj");
        attacked1.add("models/zombie/attacked1/zombie_000038.obj");
        attacked1.add("models/zombie/attacked1/zombie_000038.obj");
        attacked1.add("models/zombie/attacked1/zombie_000039.obj");
        attacked1.add("models/zombie/attacked1/zombie_000039.obj");
        attacked1.add("models/zombie/attacked1/zombie_000039.obj");
        attacked1.add("models/zombie/attacked1/zombie_000040.obj");
        attacked1.add("models/zombie/attacked1/zombie_000040.obj");
        attacked1.add("models/zombie/attacked1/zombie_000040.obj");
        attacked1.add("models/zombie/attacked1/zombie_000041.obj");
        attacked1.add("models/zombie/attacked1/zombie_000041.obj");
        attacked1.add("models/zombie/attacked1/zombie_000041.obj");
        attacked1.add("models/zombie/attacked1/zombie_000042.obj");
        attacked1.add("models/zombie/attacked1/zombie_000042.obj");
        attacked1.add("models/zombie/attacked1/zombie_000042.obj");
        attacked1.add("models/zombie/attacked1/zombie_000043.obj");
        attacked1.add("models/zombie/attacked1/zombie_000043.obj");
        attacked1.add("models/zombie/attacked1/zombie_000043.obj");
        attacked1.add("models/zombie/attacked1/zombie_000044.obj");
        attacked1.add("models/zombie/attacked1/zombie_000044.obj");
        attacked1.add("models/zombie/attacked1/zombie_000044.obj");
        attacked1.add("models/zombie/attacked1/zombie_000045.obj");
        attacked1.add("models/zombie/attacked1/zombie_000045.obj");
        attacked1.add("models/zombie/attacked1/zombie_000045.obj");
        attacked1.add("models/zombie/attacked1/zombie_000046.obj");
        attacked1.add("models/zombie/attacked1/zombie_000046.obj");
        attacked1.add("models/zombie/attacked1/zombie_000046.obj");
        attacked1.add("models/zombie/attacked1/zombie_000047.obj");
        attacked1.add("models/zombie/attacked1/zombie_000047.obj");
        attacked1.add("models/zombie/attacked1/zombie_000047.obj");
        modelsMap.put(ATTACKED1,attacked1);
        ArrayList<String> idle1 = new ArrayList<String>();
        idle1.add("models/zombie/idle1/zombie_000137.obj");
        idle1.add("models/zombie/idle1/zombie_000138.obj");
        idle1.add("models/zombie/idle1/zombie_000139.obj");
        idle1.add("models/zombie/idle1/zombie_000140.obj");
        idle1.add("models/zombie/idle1/zombie_000141.obj");
        idle1.add("models/zombie/idle1/zombie_000142.obj");
        idle1.add("models/zombie/idle1/zombie_000143.obj");
        idle1.add("models/zombie/idle1/zombie_000144.obj");
        idle1.add("models/zombie/idle1/zombie_000145.obj");
        idle1.add("models/zombie/idle1/zombie_000146.obj");
        idle1.add("models/zombie/idle1/zombie_000147.obj");
        idle1.add("models/zombie/idle1/zombie_000148.obj");
        idle1.add("models/zombie/idle1/zombie_000149.obj");
        idle1.add("models/zombie/idle1/zombie_000150.obj");
        idle1.add("models/zombie/idle1/zombie_000151.obj");
        idle1.add("models/zombie/idle1/zombie_000152.obj");
        idle1.add("models/zombie/idle1/zombie_000153.obj");
        idle1.add("models/zombie/idle1/zombie_000154.obj");
        idle1.add("models/zombie/idle1/zombie_000155.obj");
        idle1.add("models/zombie/idle1/zombie_000156.obj");
        idle1.add("models/zombie/idle1/zombie_000157.obj");
        idle1.add("models/zombie/idle1/zombie_000158.obj");
        idle1.add("models/zombie/idle1/zombie_000159.obj");
        idle1.add("models/zombie/idle1/zombie_000160.obj");
        idle1.add("models/zombie/idle1/zombie_000161.obj");
        idle1.add("models/zombie/idle1/zombie_000162.obj");
        idle1.add("models/zombie/idle1/zombie_000163.obj");
        idle1.add("models/zombie/idle1/zombie_000164.obj");
        idle1.add("models/zombie/idle1/zombie_000165.obj");
        idle1.add("models/zombie/idle1/zombie_000166.obj");
        idle1.add("models/zombie/idle1/zombie_000167.obj");
        idle1.add("models/zombie/idle1/zombie_000168.obj");
        idle1.add("models/zombie/idle1/zombie_000169.obj");
        modelsMap.put(IDLE1,idle1);
        ArrayList<String> blowned = new ArrayList<String>();
        blowned.add("models/zombie/blowned/zombie_000059.obj");
        blowned.add("models/zombie/blowned/zombie_000060.obj");
        blowned.add("models/zombie/blowned/zombie_000061.obj");
        blowned.add("models/zombie/blowned/zombie_000062.obj");
        blowned.add("models/zombie/blowned/zombie_000063.obj");
        blowned.add("models/zombie/blowned/zombie_000064.obj");
        blowned.add("models/zombie/blowned/zombie_000065.obj");
        blowned.add("models/zombie/blowned/zombie_000066.obj");
        blowned.add("models/zombie/blowned/zombie_000067.obj");
        blowned.add("models/zombie/blowned/zombie_000068.obj");
        blowned.add("models/zombie/blowned/zombie_000069.obj");
        blowned.add("models/zombie/blowned/zombie_000070.obj");
        blowned.add("models/zombie/blowned/zombie_000071.obj");
        blowned.add("models/zombie/blowned/zombie_000072.obj");
        blowned.add("models/zombie/blowned/zombie_000073.obj");
        blowned.add("models/zombie/blowned/zombie_000074.obj");
        blowned.add("models/zombie/blowned/zombie_000075.obj");
        modelsMap.put(BLOWNED,blowned);
        ArrayList<String> idle2 = new ArrayList<String>();
        idle2.add("models/zombie/idle2/zombie_000170.obj");
        idle2.add("models/zombie/idle2/zombie_000171.obj");
        idle2.add("models/zombie/idle2/zombie_000172.obj");
        idle2.add("models/zombie/idle2/zombie_000173.obj");
        idle2.add("models/zombie/idle2/zombie_000174.obj");
        idle2.add("models/zombie/idle2/zombie_000175.obj");
        idle2.add("models/zombie/idle2/zombie_000176.obj");
        idle2.add("models/zombie/idle2/zombie_000177.obj");
        idle2.add("models/zombie/idle2/zombie_000178.obj");
        idle2.add("models/zombie/idle2/zombie_000179.obj");
        idle2.add("models/zombie/idle2/zombie_000180.obj");
        idle2.add("models/zombie/idle2/zombie_000181.obj");
        idle2.add("models/zombie/idle2/zombie_000182.obj");
        idle2.add("models/zombie/idle2/zombie_000183.obj");
        idle2.add("models/zombie/idle2/zombie_000184.obj");
        idle2.add("models/zombie/idle2/zombie_000185.obj");
        idle2.add("models/zombie/idle2/zombie_000186.obj");
        idle2.add("models/zombie/idle2/zombie_000187.obj");
        idle2.add("models/zombie/idle2/zombie_000188.obj");
        idle2.add("models/zombie/idle2/zombie_000189.obj");
        idle2.add("models/zombie/idle2/zombie_000190.obj");
        idle2.add("models/zombie/idle2/zombie_000191.obj");
        idle2.add("models/zombie/idle2/zombie_000192.obj");
        idle2.add("models/zombie/idle2/zombie_000193.obj");
        idle2.add("models/zombie/idle2/zombie_000194.obj");
        idle2.add("models/zombie/idle2/zombie_000195.obj");
        idle2.add("models/zombie/idle2/zombie_000196.obj");
        idle2.add("models/zombie/idle2/zombie_000197.obj");
        idle2.add("models/zombie/idle2/zombie_000198.obj");
        idle2.add("models/zombie/idle2/zombie_000199.obj");
        idle2.add("models/zombie/idle2/zombie_000200.obj");
        modelsMap.put(IDLE2,idle2);
    }

}
