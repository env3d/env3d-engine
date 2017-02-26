package env3d.jythonrunner;

import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import env3d.util.Sysutil;

import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.Display;
import org.python.core.PyCode;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class Game {

    private EnvJython env;
    private EnvTerrain terrain;
    private PythonInterpreter pythonInterpreter;
    private PyCode script;
    private PyObject dict = new PyDictionary();
    private PyObject setupMethod, moveMethod, startMethod;

    
    private void setupSwing() {
        Canvas canvas;
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {
            }

            public void windowClosing(WindowEvent we) {
                env.exit();
            }

            public void windowClosed(WindowEvent we) {
            }

            public void windowIconified(WindowEvent we) {
            }

            public void windowDeiconified(WindowEvent we) {
            }

            public void windowActivated(WindowEvent we) {
            }

            public void windowDeactivated(WindowEvent we) {
            }
        });
        
        // Create a new canvas and set its size.
        canvas = new Canvas();
        // Must be 640*480 to match the size of an Env3D window
        canvas.setSize(640, 480);
        // This is the magic!  The setParent method attaches the
        // opengl window to the awt canvas.
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
        }

        frame.add(canvas, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);        
    }
            
    private void setup() {
        setupSwing();
        env = new EnvJython();
        pythonInterpreter = new PythonInterpreter(dict);
        pythonInterpreter.set("env", env);

        // Retrieve the script
        try {
            script = pythonInterpreter.compile(FileUtils.readFileToString(new File("World")));
            // execute it once to create locals
//            pythonInterpreter.exec(script);
//            setupMethod = pythonInterpreter.get("setup");
//            moveMethod = pythonInterpreter.get("move");
//            startMethod = pythonInterpreter.get("start");
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            script = null;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        // Load the game objects
        try {
            URL url = Sysutil.getURL("world.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[0].equalsIgnoreCase("skybox")) {
                    // The background
                    env.setRoom(new EnvSkyRoom(fields[1]));
                } else if (fields[0].equalsIgnoreCase("camera")) {
                    // The camera
                    env.setCameraXYZ(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]));
                    env.setCameraYaw(Double.parseDouble(fields[4]));
                    env.setCameraPitch(Double.parseDouble(fields[5]));
                } else if (fields[0].equalsIgnoreCase("terrain")) {
                    terrain = new EnvTerrain(fields[1]);
                    terrain.setTextureAlpha(fields[2]);
                    terrain.setTexture(fields[3], 1);
                    terrain.setTexture(fields[4], 2);
                    terrain.setTexture(fields[5], 3);
                    env.addObject(terrain);
                } else if (fields[0].equalsIgnoreCase("object")) {
                    GameObject n = new GameObject(fields[10]);
                    n.setX(Double.parseDouble(fields[1]));
                    n.setY(Double.parseDouble(fields[2]));
                    n.setZ(Double.parseDouble(fields[3]));
                    n.setScale(Double.parseDouble(fields[4]));
                    n.setRotateX(Double.parseDouble(fields[5]));
                    n.setRotateY(Double.parseDouble(fields[6]));
                    n.setRotateZ(Double.parseDouble(fields[7]));
                    n.setTexture(fields[9]);
                    n.setModel(fields[8]);
                    env.addObject(n);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void play() {
        setup();

        // Disable the default env3d control
        env.setDefaultControl(false);
        // Do not hide the mouse
        env.setMouseGrab(false);

        if (setupMethod != null) {
            setupMethod.__call__();
        }


        Thread t = new Thread() {

            @Override
            public void run() {
                if (startMethod != null) {
                    try {
                        startMethod.__call__();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        System.exit(1);
                    }
                }
            }
        };

        //t.start();
        if (script != null) {
            try {
                pythonInterpreter.exec(script);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                System.exit(1);
            }
        }

//        while (env.getKey() != Keyboard.KEY_ESCAPE) {
//
//            if (moveMethod != null) {
//                try {
//                    moveMethod.__call__();
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, e);
//                    System.exit(1);
//                }
//            }
//
//            // move every object, which runs a separate script
//            for (GameObject o : env.getObjects(GameObject.class)) {
//                o.move();
//            }
//
//            env.advanceOneFrame(30);
//        }
        env.exit();
    }

    public static void main(String[] args) {
        (new Game()).play();
    }
}
