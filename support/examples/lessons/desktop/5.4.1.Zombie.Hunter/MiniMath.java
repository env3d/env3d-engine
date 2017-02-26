import env3d.Env;
import org.lwjgl.input.Keyboard;

/**
 * A mini math game
 * 
 */
public class MiniMath
{
    // Question mode
    public static final int ADD = 0;
    public static final int SUBTRACT = 1;
    
    public static int DIFFICULTY = 1;
    
    private Env env;
        
    private int op1, op2;
    private int answer;        
    private int timer, timeRemain;
    private String answerStr;

    private int mode = ADD;
    
    /**
     * MiniMath needs a env object for input/output
     */
    public MiniMath(Env env)
    {
        this.env = env;
        init();
    }
    
    /**
     * Initialize the math game
     */
    public void init()
    {   
        // Set op1 and op2 to be random numbers
        op1 = (int) (Math.random()*DIFFICULTY*10);
        op2 = (int) (Math.random()*DIFFICULTY*10);
        if (mode == SUBTRACT) {
            // In subtraction mode, always want the smaller number to be op2.
            if (op1 < op2) {
                int tmp = op1;
                op1 = op2;
                op2 = tmp;
            }
        }
        // How long the user has to respond 
        timer = 150;

        answerStr = "";
        answer = 99999;
    }

    /**
     * retruns true if we need to keep playing, false if game is finished
     */ 
    public void play()
    {        
        String question;
        if (mode == ADD) {
            question = op1+" + "+op2+" = ";
        } else {
            question = op1+" - "+op2+" = ";
        }

        while (timer > 0) {
            if (answerStr.length() < 9) {
                if (getEnv().getKey() == Keyboard.KEY_0 || getEnv().getKey() == Keyboard.KEY_NUMPAD0) {
                    answerStr += 0;
                } else if (getEnv().getKey() == Keyboard.KEY_1 || getEnv().getKey() == Keyboard.KEY_NUMPAD1) {
                    answerStr += 1;
                } else if (getEnv().getKey() == Keyboard.KEY_2 || getEnv().getKey() == Keyboard.KEY_NUMPAD2) {
                    answerStr += 2;
                } else if (getEnv().getKey() == Keyboard.KEY_3 || getEnv().getKey() == Keyboard.KEY_NUMPAD3) {
                    answerStr += 3;
                } else if (getEnv().getKey() == Keyboard.KEY_4 || getEnv().getKey() == Keyboard.KEY_NUMPAD4) {
                    answerStr += 4;
                } else if (getEnv().getKey() == Keyboard.KEY_5 || getEnv().getKey() == Keyboard.KEY_NUMPAD5) {
                    answerStr += 5;
                } else if (getEnv().getKey() == Keyboard.KEY_6 || getEnv().getKey() == Keyboard.KEY_NUMPAD6) {
                    answerStr += 6;
                } else if (getEnv().getKey() == Keyboard.KEY_7 || getEnv().getKey() == Keyboard.KEY_NUMPAD7) {
                    answerStr += 7;
                } else if (getEnv().getKey() == Keyboard.KEY_8 || getEnv().getKey() == Keyboard.KEY_NUMPAD8) {
                    answerStr += 8;
                } else if (getEnv().getKey() == Keyboard.KEY_9 || getEnv().getKey() == Keyboard.KEY_NUMPAD9) {
                    answerStr += 9;
                }
                

                if (answerStr.length() > 0) {
                    answer = Integer.parseInt(answerStr);
                }
            }
            if (getEnv().getKey() == Keyboard.KEY_DELETE || getEnv().getKey() == Keyboard.KEY_BACK) {
                if (answerStr.length() > 0) {
                    answerStr = answerStr.substring(0, answerStr.length()-1);
                }
            }
            getEnv().setDisplayStr(question+answerStr, 200, 260, 3, 1, 1, 1, 1);
            
            if (getEnv().getMouseButtonClicked() == 0 || getEnv().getKey() == Keyboard.KEY_ESCAPE) timer = 0;        
            
            boolean gotAnswer = false;
            if (mode == ADD) {
                if (op1 + op2 == answer) gotAnswer = true;
            } else {
                if (op1 - op2 == answer) gotAnswer = true;
            }
            // Got the right answer
            if (gotAnswer) {
                timeRemain = timer;
                timer = 0;
            }
            timer--;
            env.advanceOneFrame(30);
        }       
    }
    
    public void reset()
    {
        getEnv().setDisplayStr(null, 200, 260, 3, 1, 1, 1, 1);
        init();        
    }
    
    public boolean clearQuestion()
    {
        getEnv().setDisplayStr(null, 200, 260);
        return true;
    }
    
    public int getTimeRemain()
    {
        return timeRemain;
    }
    
    public boolean isCorrect() 
    {
        return (mode == ADD ? (op1 + op2 == answer) : (op1 - op2 == answer));
    }
    
    public void setMode(int mode) {
        this.mode = mode;
    }
    
    public Env getEnv()
    {
        return env;
    }
    
    public void test() 
    {
        env = new Env();
        env.setDefaultControl(false);
        setMode(SUBTRACT);
        reset();
        int delay;
        
        while (env.getKey() != 1) {
            play();
            if (isCorrect()) {
                System.out.println("Awesome! "+getTimeRemain());
            } else {
                System.out.println("Doh!");
            }      
            if (clearQuestion()) {
                init();
            }
        }
        env.exit();
    }
}
