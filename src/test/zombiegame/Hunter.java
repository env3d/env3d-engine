package test.zombiegame;

import java.util.HashMap;

public class Hunter
{
    //The various states of this model.  Each animation is a state
    public static final int FLINCH = 0;
    public static final int MELEE = 1;
    public static final int IDLE = 2;
    public static final int JUMP = 3;
    public static final int RUN = 4;
    public static final int RANGED = 5;
    public static final int DIE = 6;
    public static final int BACKWARDS = 7;
    private HashMap<Integer, String[]> modelsMap = new HashMap<Integer, String[]>();

    //Fields for state management
    private int state = IDLE;
    private int frame;

    // Required env3d fields
    private double x, y, z, rotateX, rotateY, rotateZ, scale = 1;
    private String texture, model;

    public Hunter(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        texture = "models/hunter/player.png";
        init();
    }

    public void walk(double direction, double speed) 
    {        
        rotateY = direction;
        x = x + speed*Math.sin(Math.toRadians(direction));
        z = z + speed*Math.cos(Math.toRadians(direction));
        if (speed < 0) {
            setState(BACKWARDS);
        } else {
            setState(RUN);
        }
    }
    
    public void strife(double direction, double speed) 
    {
        x = x + speed*Math.sin(Math.toRadians(direction));
        z = z + speed*Math.cos(Math.toRadians(direction));
        setState(RUN);        
    }
    /** 
     * Basic implementation. Simply animate the model
     * based on the state 
     */ 
    public void move() 
    {
        model = modelsMap.get(state)[frame];
        frame = (frame+1) % modelsMap.get(state).length;
        if (frame == 0) {
            if (state != IDLE) {
                state = IDLE;
            }
        }
    }

    public int getFrame()
    {
        return frame;
    }
    
    public int getState() 
    {
        return state;
    }            
    
    public void setState(int newState)
    {
        if (state != newState) {
            frame = 0;
            state = newState;
        }
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
        String[] Flinch = new String[10];
        Flinch[0] = "models/hunter/Flinch/playerflinch00.obj";
        Flinch[1] = "models/hunter/Flinch/playerflinch01.obj";
        Flinch[2] = "models/hunter/Flinch/playerflinch02.obj";
        Flinch[3] = "models/hunter/Flinch/playerflinch03.obj";
        Flinch[4] = "models/hunter/Flinch/playerflinch04.obj";
        Flinch[5] = "models/hunter/Flinch/playerflinch05.obj";
        Flinch[6] = "models/hunter/Flinch/playerflinch06.obj";
        Flinch[7] = "models/hunter/Flinch/playerflinch07.obj";
        Flinch[8] = "models/hunter/Flinch/playerflinch08.obj";
        Flinch[9] = "models/hunter/Flinch/playerflinch09.obj";
        modelsMap.put(FLINCH,Flinch);
        String[] Melee = new String[30];
        Melee[0] = "models/hunter/Melee/playermelee00.obj";
        Melee[1] = "models/hunter/Melee/playermelee01.obj";
        Melee[2] = "models/hunter/Melee/playermelee02.obj";
        Melee[3] = "models/hunter/Melee/playermelee03.obj";
        Melee[4] = "models/hunter/Melee/playermelee04.obj";
        Melee[5] = "models/hunter/Melee/playermelee05.obj";
        Melee[6] = "models/hunter/Melee/playermelee06.obj";
        Melee[7] = "models/hunter/Melee/playermelee07.obj";
        Melee[8] = "models/hunter/Melee/playermelee08.obj";
        Melee[9] = "models/hunter/Melee/playermelee09.obj";
        Melee[10] = "models/hunter/Melee/playermelee10.obj";
        Melee[11] = "models/hunter/Melee/playermelee11.obj";
        Melee[12] = "models/hunter/Melee/playermelee12.obj";
        Melee[13] = "models/hunter/Melee/playermelee13.obj";
        Melee[14] = "models/hunter/Melee/playermelee14.obj";
        Melee[15] = "models/hunter/Melee/playermelee15.obj";
        Melee[16] = "models/hunter/Melee/playermelee16.obj";
        Melee[17] = "models/hunter/Melee/playermelee17.obj";
        Melee[18] = "models/hunter/Melee/playermelee18.obj";
        Melee[19] = "models/hunter/Melee/playermelee19.obj";
        Melee[20] = "models/hunter/Melee/playermelee20.obj";
        Melee[21] = "models/hunter/Melee/playermelee21.obj";
        Melee[22] = "models/hunter/Melee/playermelee22.obj";
        Melee[23] = "models/hunter/Melee/playermelee23.obj";
        Melee[24] = "models/hunter/Melee/playermelee24.obj";
        Melee[25] = "models/hunter/Melee/playermelee25.obj";
        Melee[26] = "models/hunter/Melee/playermelee26.obj";
        Melee[27] = "models/hunter/Melee/playermelee27.obj";
        Melee[28] = "models/hunter/Melee/playermelee28.obj";
        Melee[29] = "models/hunter/Melee/playermelee29.obj";
        modelsMap.put(MELEE,Melee);
        String[] Idle = new String[20];
        Idle[0] = "models/hunter/Idle/playeridle00.obj";
        Idle[1] = "models/hunter/Idle/playeridle01.obj";
        Idle[2] = "models/hunter/Idle/playeridle02.obj";
        Idle[3] = "models/hunter/Idle/playeridle03.obj";
        Idle[4] = "models/hunter/Idle/playeridle04.obj";
        Idle[5] = "models/hunter/Idle/playeridle05.obj";
        Idle[6] = "models/hunter/Idle/playeridle06.obj";
        Idle[7] = "models/hunter/Idle/playeridle07.obj";
        Idle[8] = "models/hunter/Idle/playeridle08.obj";
        Idle[9] = "models/hunter/Idle/playeridle09.obj";
        Idle[10] = "models/hunter/Idle/playeridle10.obj";
        Idle[11] = "models/hunter/Idle/playeridle11.obj";
        Idle[12] = "models/hunter/Idle/playeridle12.obj";
        Idle[13] = "models/hunter/Idle/playeridle13.obj";
        Idle[14] = "models/hunter/Idle/playeridle14.obj";
        Idle[15] = "models/hunter/Idle/playeridle15.obj";
        Idle[16] = "models/hunter/Idle/playeridle16.obj";
        Idle[17] = "models/hunter/Idle/playeridle17.obj";
        Idle[18] = "models/hunter/Idle/playeridle18.obj";
        Idle[19] = "models/hunter/Idle/playeridle19.obj";
        modelsMap.put(IDLE,Idle);
        String[] Jump = new String[30];
        Jump[0] = "models/hunter/Jump/playerjump00.obj";
        Jump[1] = "models/hunter/Jump/playerjump01.obj";
        Jump[2] = "models/hunter/Jump/playerjump02.obj";
        Jump[3] = "models/hunter/Jump/playerjump03.obj";
        Jump[4] = "models/hunter/Jump/playerjump04.obj";
        Jump[5] = "models/hunter/Jump/playerjump05.obj";
        Jump[6] = "models/hunter/Jump/playerjump06.obj";
        Jump[7] = "models/hunter/Jump/playerjump07.obj";
        Jump[8] = "models/hunter/Jump/playerjump08.obj";
        Jump[9] = "models/hunter/Jump/playerjump09.obj";
        Jump[10] = "models/hunter/Jump/playerjump10.obj";
        Jump[11] = "models/hunter/Jump/playerjump11.obj";
        Jump[12] = "models/hunter/Jump/playerjump12.obj";
        Jump[13] = "models/hunter/Jump/playerjump13.obj";
        Jump[14] = "models/hunter/Jump/playerjump14.obj";
        Jump[15] = "models/hunter/Jump/playerjump15.obj";
        Jump[16] = "models/hunter/Jump/playerjump16.obj";
        Jump[17] = "models/hunter/Jump/playerjump17.obj";
        Jump[18] = "models/hunter/Jump/playerjump18.obj";
        Jump[19] = "models/hunter/Jump/playerjump19.obj";
        Jump[20] = "models/hunter/Jump/playerjump20.obj";
        Jump[21] = "models/hunter/Jump/playerjump21.obj";
        Jump[22] = "models/hunter/Jump/playerjump22.obj";
        Jump[23] = "models/hunter/Jump/playerjump23.obj";
        Jump[24] = "models/hunter/Jump/playerjump24.obj";
        Jump[25] = "models/hunter/Jump/playerjump25.obj";
        Jump[26] = "models/hunter/Jump/playerjump26.obj";
        Jump[27] = "models/hunter/Jump/playerjump27.obj";
        Jump[28] = "models/hunter/Jump/playerjump28.obj";
        Jump[29] = "models/hunter/Jump/playerjump29.obj";
        modelsMap.put(JUMP,Jump);
        String[] Run = new String[20];
        Run[0] = "models/hunter/Run/playerrun00.obj";
        Run[1] = "models/hunter/Run/playerrun01.obj";
        Run[2] = "models/hunter/Run/playerrun02.obj";
        Run[3] = "models/hunter/Run/playerrun03.obj";
        Run[4] = "models/hunter/Run/playerrun04.obj";
        Run[5] = "models/hunter/Run/playerrun05.obj";
        Run[6] = "models/hunter/Run/playerrun06.obj";
        Run[7] = "models/hunter/Run/playerrun07.obj";
        Run[8] = "models/hunter/Run/playerrun08.obj";
        Run[9] = "models/hunter/Run/playerrun09.obj";
        Run[10] = "models/hunter/Run/playerrun10.obj";
        Run[11] = "models/hunter/Run/playerrun11.obj";
        Run[12] = "models/hunter/Run/playerrun12.obj";
        Run[13] = "models/hunter/Run/playerrun13.obj";
        Run[14] = "models/hunter/Run/playerrun14.obj";
        Run[15] = "models/hunter/Run/playerrun15.obj";
        Run[16] = "models/hunter/Run/playerrun16.obj";
        Run[17] = "models/hunter/Run/playerrun17.obj";
        Run[18] = "models/hunter/Run/playerrun18.obj";
        Run[19] = "models/hunter/Run/playerrun19.obj";
        modelsMap.put(RUN,Run);
        String[] Ranged = new String[38];
        Ranged[0] = "models/hunter/Ranged/playerranged00.obj";
        Ranged[1] = "models/hunter/Ranged/playerranged01.obj";
        Ranged[2] = "models/hunter/Ranged/playerranged02.obj";
        Ranged[3] = "models/hunter/Ranged/playerranged03.obj";
        Ranged[4] = "models/hunter/Ranged/playerranged04.obj";
        Ranged[5] = "models/hunter/Ranged/playerranged05.obj";
        Ranged[6] = "models/hunter/Ranged/playerranged06.obj";
        Ranged[7] = "models/hunter/Ranged/playerranged07.obj";
        Ranged[8] = "models/hunter/Ranged/playerranged08.obj";
        Ranged[9] = "models/hunter/Ranged/playerranged09.obj";
        Ranged[10] = "models/hunter/Ranged/playerranged10.obj";
        Ranged[11] = "models/hunter/Ranged/playerranged11.obj";
        Ranged[12] = "models/hunter/Ranged/playerranged12.obj";
        Ranged[13] = "models/hunter/Ranged/playerranged13.obj";
        Ranged[14] = "models/hunter/Ranged/playerranged14.obj";
        Ranged[15] = "models/hunter/Ranged/playerranged15.obj";
        Ranged[16] = "models/hunter/Ranged/playerranged16.obj";
        Ranged[17] = "models/hunter/Ranged/playerranged17.obj";
        Ranged[18] = "models/hunter/Ranged/playerranged18.obj";
        Ranged[19] = "models/hunter/Ranged/playerranged19.obj";
        Ranged[20] = "models/hunter/Ranged/playerranged20.obj";
        Ranged[21] = "models/hunter/Ranged/playerranged21.obj";
        Ranged[22] = "models/hunter/Ranged/playerranged22.obj";
        Ranged[23] = "models/hunter/Ranged/playerranged23.obj";
        Ranged[24] = "models/hunter/Ranged/playerranged24.obj";
        Ranged[25] = "models/hunter/Ranged/playerranged25.obj";
        Ranged[26] = "models/hunter/Ranged/playerranged26.obj";
        Ranged[27] = "models/hunter/Ranged/playerranged27.obj";
        Ranged[28] = "models/hunter/Ranged/playerranged28.obj";
        Ranged[29] = "models/hunter/Ranged/playerranged29.obj";
        Ranged[30] = "models/hunter/Ranged/playerranged30.obj";
        Ranged[31] = "models/hunter/Ranged/playerranged31.obj";
        Ranged[32] = "models/hunter/Ranged/playerranged32.obj";
        Ranged[33] = "models/hunter/Ranged/playerranged33.obj";
        Ranged[34] = "models/hunter/Ranged/playerranged34.obj";
        Ranged[35] = "models/hunter/Ranged/playerranged35.obj";
        Ranged[36] = "models/hunter/Ranged/playerranged36.obj";
        Ranged[37] = "models/hunter/Ranged/playerranged37.obj";
        modelsMap.put(RANGED,Ranged);
        String[] Die = new String[22];
        Die[0] = "models/hunter/Die/playerdie00.obj";
        Die[1] = "models/hunter/Die/playerdie01.obj";
        Die[2] = "models/hunter/Die/playerdie02.obj";
        Die[3] = "models/hunter/Die/playerdie03.obj";
        Die[4] = "models/hunter/Die/playerdie04.obj";
        Die[5] = "models/hunter/Die/playerdie05.obj";
        Die[6] = "models/hunter/Die/playerdie06.obj";
        Die[7] = "models/hunter/Die/playerdie07.obj";
        Die[8] = "models/hunter/Die/playerdie08.obj";
        Die[9] = "models/hunter/Die/playerdie09.obj";
        Die[10] = "models/hunter/Die/playerdie10.obj";
        Die[11] = "models/hunter/Die/playerdie11.obj";
        Die[12] = "models/hunter/Die/playerdie12.obj";
        Die[13] = "models/hunter/Die/playerdie13.obj";
        Die[14] = "models/hunter/Die/playerdie14.obj";
        Die[15] = "models/hunter/Die/playerdie15.obj";
        Die[16] = "models/hunter/Die/playerdie16.obj";
        Die[17] = "models/hunter/Die/playerdie17.obj";
        Die[18] = "models/hunter/Die/playerdie18.obj";
        Die[19] = "models/hunter/Die/playerdie19.obj";
        Die[20] = "models/hunter/Die/playerdie20.obj";
        Die[21] = "models/hunter/Die/playerdie21.obj";
        modelsMap.put(DIE,Die);
        String[] Backwards = new String[20];
        Backwards[0] = "models/hunter/Backwards/playerbackwards00.obj";
        Backwards[1] = "models/hunter/Backwards/playerbackwards01.obj";
        Backwards[2] = "models/hunter/Backwards/playerbackwards02.obj";
        Backwards[3] = "models/hunter/Backwards/playerbackwards03.obj";
        Backwards[4] = "models/hunter/Backwards/playerbackwards04.obj";
        Backwards[5] = "models/hunter/Backwards/playerbackwards05.obj";
        Backwards[6] = "models/hunter/Backwards/playerbackwards06.obj";
        Backwards[7] = "models/hunter/Backwards/playerbackwards07.obj";
        Backwards[8] = "models/hunter/Backwards/playerbackwards08.obj";
        Backwards[9] = "models/hunter/Backwards/playerbackwards09.obj";
        Backwards[10] = "models/hunter/Backwards/playerbackwards10.obj";
        Backwards[11] = "models/hunter/Backwards/playerbackwards11.obj";
        Backwards[12] = "models/hunter/Backwards/playerbackwards12.obj";
        Backwards[13] = "models/hunter/Backwards/playerbackwards13.obj";
        Backwards[14] = "models/hunter/Backwards/playerbackwards14.obj";
        Backwards[15] = "models/hunter/Backwards/playerbackwards15.obj";
        Backwards[16] = "models/hunter/Backwards/playerbackwards16.obj";
        Backwards[17] = "models/hunter/Backwards/playerbackwards17.obj";
        Backwards[18] = "models/hunter/Backwards/playerbackwards18.obj";
        Backwards[19] = "models/hunter/Backwards/playerbackwards19.obj";
        modelsMap.put(BACKWARDS,Backwards);
    }

}
