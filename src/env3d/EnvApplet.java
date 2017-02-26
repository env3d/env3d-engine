/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
/**
 *
 * @author jmadar
 */
public class EnvApplet extends Applet {

    protected Canvas display_parent;
    protected Thread gameThread;
    private boolean running;
    protected Env env;

    public void destroy() {
        remove(display_parent);
        super.destroy();
        System.out.println("Clear up");
    }

    private void destroyLWJGL() {
        System.out.println("Destroying game thread");
        stopApplet();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            System.out.println("Error: "+e);
            e.printStackTrace();
        }
    }

    /**
     * @see java.applet.Applet#start()
     */
    @Override
    public void start() {
        
        gameThread = new Thread() {

            public void run() {

                setRunning(true);
                try {


                    Display.setParent(display_parent);

                    //env = getEnv();


                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
                play();
            }
        };
        gameThread.start();
//                setRunning(true);
//                try {
//
//
//                    Display.setParent(display_parent);
//
//                    //env = getEnv();
//
//
//                } catch (LWJGLException e) {
//                    e.printStackTrace();
//                }
//                play();
    }

    @Override
    public void stop() {
    }

    public void stopApplet() {
        setRunning(false);
    }

    @Override
    public void init() {
        setLayout(new BorderLayout());
        try {
            display_parent = new Canvas() {

                @Override
                public final void removeNotify() {
                    destroyLWJGL();
                    super.removeNotify();
                }
            };
            display_parent.setSize(getWidth(), getHeight());
            add(display_parent);            
            display_parent.setFocusable(true);
            display_parent.requestFocus();
            display_parent.setIgnoreRepaint(true);
            //setResizable(true);
            setVisible(true);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("Unable to create display");
        }
    }

    /**
     * Contains the game loop.
     **/
    public void play() {
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        this.running = running;
    }


}
