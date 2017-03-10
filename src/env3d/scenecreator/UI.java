/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator;

import bluej.action.CreateModelAction;
import bluej.FileAlreadyExistsException;
import bluej.extensions.BlueJ;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.lwjgl.opengl.Display;
import org.lwjgl.openal.AL;
import env3d.util.Sysutil;

/**
 *
 * @author jmadar
 */
public class UI {
    
    private Game game;
    private Canvas canvas;
    private JComboBox modelList;
    private String assetDir;
    private JFrame frame;
    private BlueJ bluej;
    private Mode mode;
    private RSyntaxTextArea textArea;
    
    private ArrayList<SCTextEditor> editors = new ArrayList<SCTextEditor>();

    public UI(Mode m) {
        this.mode = m;
        frame = new JFrame();
        frame.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {
            }

            public void windowClosing(WindowEvent we) {
                exit();
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
        canvas.setPreferredSize(new Dimension(640, 480));
        // This is the magic!  The setParent method attaches the
        // opengl window to the awt canvas.

        // Construct the GUI as normal
        if (mode == Mode.SCENE_CREATOR || mode == Mode.PYTHON)  
            frame.add(createTopPanel(), BorderLayout.NORTH);
        
        // In Python mode, we embedd a text editor
        if (mode == Mode.PYTHON) {
            textArea = new RSyntaxTextArea();            
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
            RTextScrollPane pane = new RTextScrollPane(textArea);
            pane.setPreferredSize(new Dimension(640, 480));
            try {
                String worldText = FileUtils.readFileToString(new File(getAssetDir()+"World"));
                textArea.setText(worldText);
            } catch (IOException e) {

            } 
            frame.add(pane, BorderLayout.EAST);            
            
        }
        frame.add(canvas, BorderLayout.CENTER);

        frame.setVisible(true);
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
        }
           
        game = new Game(this);        
        // Only load the scene if the top panel is loaded
        if (mode == Mode.SCENE_CREATOR || mode == Mode.PYTHON) load();
        
        frame.pack();     
    }

    public void load() {
        try {
            game.clearNodes();
            File saveFile = new File(generateAssetDir() + "world.txt");
            BufferedReader br = null;
            if (saveFile.isFile()) {
                br = new BufferedReader(new FileReader(saveFile));
            } else {
                // No world file present, nothing to load
                game.getEnv().setDisplayStr("Start by choosing background...");
                return;
            }
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[0].equalsIgnoreCase("skybox")) {
                    // The background
                    game.setRoom(fields[1]);
                } else if (fields[0].equalsIgnoreCase("camera")) {
                    // The camera
                    game.getEnv().setCameraXYZ(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]));
                    game.getEnv().setCameraYaw(Double.parseDouble(fields[4]));
                    game.getEnv().setCameraPitch(Double.parseDouble(fields[5]));
                } else if (fields[0].equalsIgnoreCase("terrain")) {                        
                    game.setTerrainTexture(fields[3], fields[4], fields[5]);
                } else if (fields[0].equalsIgnoreCase("object")) {                    
                    SceneNode n = new SceneNode();
                    n.setX(Double.parseDouble(fields[1]));
                    n.setY(Double.parseDouble(fields[2]));
                    n.setZ(Double.parseDouble(fields[3]));
                    n.setScale(Double.parseDouble(fields[4]));
                    n.setRotateX(Double.parseDouble(fields[5]));
                    n.setRotateY(Double.parseDouble(fields[6]));
                    n.setRotateZ(Double.parseDouble(fields[7]));
                    n.setModel(fields[8]);
                    n.setTexture(fields[9]);
                    n.setName(fields[10]);
                    game.addObject(n);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void start() {
        frame.setAlwaysOnTop(true);
        game.play();
        System.out.println("Destroying stuff");
        game.getEnv().destroy();
        Display.destroy();
        AL.destroy();
        frame.dispose();
    }

    private JPanel createTopPanel() {
        assetDir = generateAssetDir();

        JPanel panel = new JPanel();


        JComboBox skyList = new JComboBox(Sysutil.getFileStringArray(assetDir + "textures/skybox", "<- background ->", new Sysutil.DirOnlyFilter()));
        skyList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if ( ((JComboBox) ae.getSource()).getSelectedIndex() == 0) return;
                game.setRoom("textures/skybox/"
                        + ((JComboBox) ae.getSource()).getSelectedItem());
                game.getEnv().setDisplayStr(null);
                canvas.requestFocus();
            }
        });

        JComboBox textureList = new JComboBox(Sysutil.getFileStringArray(assetDir + "textures/floor", "<- ground ->", new Sysutil.ImgFileFilter()));
        textureList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if ( ((JComboBox) ae.getSource()).getSelectedIndex() == 0) {
                    game.setTerrainTexture(null);
                } else {
                    game.setTerrainTexture("textures/floor/" + ((JComboBox) ae.getSource()).getSelectedItem());
                }
            }
        });

        modelList = new JComboBox(Sysutil.getFileStringArray(assetDir + "models", "<- model ->", new Sysutil.DirOnlyFilter()));
        modelList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if ( ((JComboBox) ae.getSource()).getSelectedIndex() == 0) return;
                SceneNode node = createSceneNode();
                game.addAndPlaceObject(node);
                canvas.requestFocus();
                game.setEditMode(true);
            }
        });

        
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                save(game.save());
            }
        });

        JButton runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                save(game.save());
                Iterator<SCTextEditor> it = editors.iterator();
                while (it.hasNext()) {
                    SCTextEditor editor = it.next();
                    if (editor.isVisible()) {
                        editor.save();
                    } else {
                        it.remove();
                    }
                }
                // Save the visible text editor
                if (mode == Mode.PYTHON) {
                    try {
                        FileUtils.writeStringToFile(new File(getAssetDir()+"World"), textArea.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }                    
                }
                String javabin = System.getProperty("java.home")+"/bin/java ";
                String classpath = System.getProperty("java.class.path");
                System.out.println(javabin + " -cp "+classpath + " env3d.jythonrunner.Game");
                Sysutil.executeCommand(javabin + " -cp "+classpath + " env3d.jythonrunner.Game");
            }
        });
        
        panel.add(skyList);
        panel.add(textureList);
        panel.add(modelList);
        if (mode == Mode.SCENE_CREATOR) {
            panel.add(saveButton);
        } else if (mode == Mode.PYTHON) {
            panel.add(runButton);
        }

        return panel;
    }

    private String generateAssetDir() {
        String assetDir = System.getProperty("org.lwjgl.librarypath");
        if (assetDir == null) {
            assetDir = "";
        } else {
            assetDir = assetDir + "/";
        }
        return assetDir;
    }


    public void save(String gameString) {
        FileWriter writer = null;
        try {
            File saveFile = new File(assetDir + "world.txt");
            writer = new FileWriter(saveFile,false);
            writer.write(gameString);
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        // return if we are running in python mode -- don't bother creating any
        // class files.
        if (mode == Mode.PYTHON) return;
        
        // Create the controller
        createGameClass();
        
        String[] lines = gameString.split("\n");
        System.out.println(gameString);
        // Look at every object line and create a class
        for (int i = 0; i < lines.length; i++) {
            
            if (!lines[i].startsWith("object")) continue;
            
            String [] objectFields = lines[i].split(",");
            // The 9th field is the model name
            String [] path = objectFields[8].split("/");
            String modelName = path[path.length-2];
            try {
                CreateModelAction.createModel(modelName, true);
            } catch (FileAlreadyExistsException fe) {
                System.out.println(fe);                        
            } catch (Exception ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // class name is recorded in the field 11
            System.out.println("Class used: "+objectFields[10]);
            //classNames.remove(objectFields[10]);
        }
                
    }
    
    /**
     * Doesn't work, BlueJ hangs when trying to invoke the Game.play() method
     */
    public void run() {
        save(game.save());
        try {
//            BClass gameClass = Bluejenv.bluej.getCurrentPackage().getBClass("Game");
//            System.out.println("Compiling Game class "+gameClass.isCompiled());
//            if (!gameClass.isCompiled()) {
//                gameClass.compile(true);
//            }
            
            //Bluejenv.bluej.getCurrentPackage().compileAll(false);
            // Run using an external process
            // (somehow invoking the method from BlueJ doesn't work
            
                                  
            String javaHome = System.getProperty("java.home");            
            // Compile using javac
            String compileCommand = javaHome+File.separator+"bin/javac "+" -classpath \""+assetDir+"+libs/*:"+assetDir+"\" "+assetDir+"*.java";
            System.out.println(compileCommand);
            Sysutil.executeCommand(compileCommand);
            // Run it
            String command = javaHome+File.separator+"bin/java -Djava.library.path=\""+assetDir +"\" -classpath \""+assetDir+"+libs/*:"+assetDir+"\" Game";
            System.out.println(command);
            Sysutil.executeCommand(command);
//            //Bluejenv.bluej.getCurrentPackage().compileAll(false);            
//            Class [] params = {String[].class};
//            BMethod mainMethod = gameClass.getDeclaredMethod("main", params);
//            Object [] actualParams = new Object[1];
//            String [] argList = {};
//            actualParams[0] = argList;
//            
//            BObject o = (BObject) mainMethod.invoke(null, actualParams);
//            System.out.println(o);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void exit() {
        if (isSceneCreatorMode()) save(game.save());
        game.exit();
    }

    /**
     * Creates an EnvNode from the currently selected item
     * @return a new EnvNode
     */
    public SceneNode createSceneNode() {
        if (modelList == null || modelList.getSelectedIndex() == 0) return null;
        SceneNode node = new SceneNode();
        node.setName(modelList.getSelectedItem().toString());
        // Find the obj file and the texture
        String modelDir = "models/" + modelList.getSelectedItem();
                
        File[] textureFile = Sysutil.fileList(assetDir + modelDir, new Sysutil.ImgFileFilter());
        if (textureFile.length > 0) {
            node.setTexture(modelDir + "/" + textureFile[0].getName());
        }

        File[] mtlFile = Sysutil.fileList(assetDir + modelDir, new Sysutil.MtlFileFilter());
        if (mtlFile.length > 0) {
            node.setTexture(null);
        }
        
        File[] objFile = Sysutil.fileList(assetDir + modelDir, new Sysutil.ObjFileFilter());
        if (objFile.length > 0) {
            node.setModel(modelDir + "/" + objFile[0].getName());
        }

        double xv = game.getEnv().getCameraX() - 10 * Math.sin(Math.toRadians(game.getEnv().getCameraYaw())) * Math.cos(Math.toRadians(game.getEnv().getCameraPitch()));
        double yv = game.getEnv().getCameraY() + 10 * Math.sin(Math.toRadians(game.getEnv().getCameraPitch()));
        double zv = game.getEnv().getCameraZ() - 10 * Math.cos(Math.toRadians(game.getEnv().getCameraYaw())) * Math.cos(Math.toRadians(game.getEnv().getCameraPitch()));

        node.setX(xv);
        node.setY(yv);
        node.setZ(zv);
        node.setScale(1);        
        
        return node;
    }

    public Game getGame() {
        return game;
    }
    
    /**
     * Returns true if topPanel is visible (full scene creator mode).
     * @return 
     */
    public boolean isSceneCreatorMode() {
        return (modelList != null);
    }

    /**
     * @return the bluej
     */
    public BlueJ getBluej() {
        return bluej;
    }

    /**
     * @param bluej the bluej to set
     */
    public void setBluej(BlueJ bluej) {
        this.bluej = bluej;
    }
    
    
    public JFrame getFrame() {
        return frame;
    }
    
    public String getAssetDir() {
        return assetDir;        
    }
    
    public void setAssetDir(String dir) {
        assetDir = dir;
    }
    
    public Mode getMode() {
        return mode;
    }
    
    public ArrayList<SCTextEditor> getEditors() {
        return editors;
    }
    
    public static enum Mode {
        BLUEJ, SCENE_CREATOR, PYTHON
    }
    
    public static void main(String args[]) {        
        UI ui = new UI(Mode.SCENE_CREATOR);
        ui.assetDir = System.getProperty("user.dir")+"/";
        System.out.println("Using directory "+ui.assetDir);
        ui.start();
    }
    
    /**
     * Utility method for creating the Game class.  Put here because I'm 
     * not sure where is the best place to put this.
     */
    private void createGameClass() {
        try {
            String packageDir, srcDir, platform = "desktop";
            if (bluej != null) {
                packageDir = bluej.getCurrentPackage().getDir().getCanonicalPath()+"/";
                srcDir = packageDir;
            } else {
                packageDir = "";
                srcDir = "src/";
            }
            
            File gameJavaFileDest = new File(srcDir+"Game.java");
            // If we don't have the Game class already, let the user choose which platform to use            
            
            // Deprecating the Android platform, since we can now deploy desktop to webgl
//            if (!gameJavaFileDest.isFile()) {
//                String[] platforms = {"desktop", "android"};
//                platform = (String) JOptionPane.showInputDialog(this.frame, 
//                        "Choose a platform", "Platforms", JOptionPane.INFORMATION_MESSAGE, 
//                        null, platforms, platforms[0]);
//            }
            File gameObjectJavaFileDest = new File(srcDir+"GameObject.java");

            if (platform == null) platform = "desktop";            
            File gameJavaFile = new File(packageDir+"examples/scenecreator/"+platform+"/Game.java");            
            File gameObjectJavaFile = new File(packageDir+"examples/scenecreator/"+platform+"/GameObject.java");
            

            if (!gameJavaFileDest.isFile()) {
                FileUtils.copyFile(gameJavaFile, gameJavaFileDest);
                //Sysutil.copyFile(gameJavaFile, gameJavaFileDest);
                if (bluej != null) bluej.getCurrentPackage().newClass("Game");
            }

            if (!gameObjectJavaFileDest.isFile()) {
                FileUtils.copyFile(gameObjectJavaFile, gameObjectJavaFileDest);
                //Sysutil.copyFile(gameObjectJavaFile, gameObjectJavaFileDest);
                if (bluej != null) bluej.getCurrentPackage().newClass("GameObject");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
