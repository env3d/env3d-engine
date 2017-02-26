package bluej;

import bluej.env3d.BEnv;
import bluej.action.StartEnvAction;
import bluej.action.OnlineExampleAction;
import bluej.action.ExtractAssetsAction;
import bluej.action.DeployAndroidAction;
import bluej.action.CreateModelAction;
import bluej.action.CreateFatJarAction;
import bluej.action.CreateAppletAction;
import bluej.extensions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import java.util.Collection;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import env3d.util.Sysutil;

public class MenuBuilder extends MenuGenerator {

    private BPackage curPackage;
    private BClass curClass;
    private BObject curObject;
    private BEnv env;
    JMenuItem startEnvMenu;
    private Collection objectCollection;
    private Preferences prefs;

    public MenuBuilder(Preferences p) {
        prefs = p;
    }

    public JMenuItem getToolsMenuItem(BPackage aPackage) {

        startEnvMenu = new JMenuItem(new StartEnvAction("Start Env3D", "Start Env3D"));
        //System.out.println("Call getToolsMenuItem: "+aPackage);
        curPackage = aPackage;

        JMenu env3dMenu = new JMenu("Env3D");

        env3dMenu.add(startEnvMenu);

        JMenu exampleMenu = new JMenu("Examples");
        env3dMenu.add(exampleMenu);
        
        
        // Look into the lessons directory and create menu items
        try {
            File lessonsDir = new File(curPackage.getDir()+"/examples/lessons");
            if (lessonsDir.isDirectory()) {
                for (String item : lessonsDir.list()) {
                    JMenu lessonMenu = new JMenu(item);
                    exampleMenu.add(lessonMenu);
                    addExamples(lessonMenu, item);
                }
            }
        } catch (Exception e) {
            // no lessons directory found!
        }
        
        
        JMenu modelsMenu = new JMenu("Models");
        // Get all the models from the models directory
        modelsMenu.addMenuListener(new MenuListener() {

            // A few to hold the models directories;
            private File[] directories = {};
            public void menuSelected(MenuEvent me) {
                // Refresh the menu every time it is selected
                JMenu modelsMenu = (JMenu) me.getSource();
                try {                    
                    File[] newDirectories = Sysutil.dirList(curPackage.getDir().getCanonicalFile()+"/models");
                    // The number of models has changed, repopulate                     
                    if (directories.length != newDirectories.length) {
                        modelsMenu.removeAll();
                        directories = newDirectories;
                        for (File dir : directories) {
                            modelsMenu.add(new JMenuItem(new CreateModelAction(dir.getName(), dir.getName())));
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error "+ex);
                }                
            }

            public void menuDeselected(MenuEvent me) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void menuCanceled(MenuEvent me) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        env3dMenu.add(modelsMenu);
        
        env3dMenu.add(new StartEnvAction("Env3D Scene Creator", "Env3D Scene Creator"));

        try {
            File additionalModels = new File(curPackage.getDir().getCanonicalFile()+File.separator+"env3d_characterpack.jar.lzma");
            if (additionalModels.isFile()) {
                env3dMenu.add(new JPopupMenu.Separator());
                env3dMenu.add(new ExtractAssetsAction("Extract additional models"));
            } 
        } catch (Exception e) {         
            JOptionPane.showMessageDialog(Bluejenv.bluej.getCurrentFrame(), e, "", JOptionPane.INFORMATION_MESSAGE);
        }

        // If this is an empty project, allow users to choose from online examples
//        try {
//            if (curPackage.getClasses().length == 0) {
//                env3dMenu.add(new JPopupMenu.Separator());
//                String [] examples = Sysutil.readUrl(prefs.getServerUrl()+"/examples/").split("\n");
//
//                for (String example : examples) {
//                    // Get all examples from env3d.org
//                    JMenuItem exampleItem = new JMenuItem(new OnlineExampleAction(example, example));
//                    env3dMenu.add(exampleItem);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return env3dMenu;
        //return startEnvMenu;
    }

    public JMenuItem getClassMenuItem(BClass aClass) {
        
        try {
            System.out.println(aClass.getJavaClass().getSuperclass());
            System.out.println("Class menu invoked "+aClass.getName());
            JMenu menu = new JMenu("Env3D Deploy Menu");
            if (aClass.getName().equals("Game")) {
                if (aClass.getJavaClass().getSuperclass().getName().contains("EnvApplet")) {
                    menu.add(new JMenuItem(new CreateAppletAction("Create Env3D Applet", "Run Env3D Applet")));
                    menu.add(new JMenuItem(new CreateAppletAction("Upload Applet to Env3D.org", "Upload Applet to Env3D.org")));
                } else if (aClass.getJavaClass().getSuperclass().getName().contains("EnvMobileGame")) {
                    menu.add(new JMenuItem(new DeployAndroidAction("Create Android Package (debug)", "Create Android Package (debug)")));
                    menu.add(new JMenuItem(new DeployAndroidAction("Create Android Package (release)", "Create Android Package (release)")));
                }
                Class<?>[] params = {String[].class};
                if (aClass.getMethod("main", params) != null) {
                    menu.add(new JMenuItem(new CreateFatJarAction("Create distribution jar file","Create distribution jar file")));
                    menu.add(new JMenuItem(new CreateAppletAction("Upload Java WebStart to Env3D.org","Upload Java WebStart to Env3D.org")));
                }
                if (menu.getItemCount() > 0) {
                    return menu;
                } else {
                    return null;
                }
            } else {
                return null;
            }
//            if (EnvApplet.class.isInstance(aClass.getJavaClass())) {
//                return new JMenuItem(new CreateAppletAction("Run Env3D Applet", "Run Env3D Applet"));
//            } else {
//                return null;
//            }
        } catch (Exception e) {
            System.out.println("Error in getClassMenuItem: "+e);
            return null;
        }
    }

    public void notifyPostObjectMenu(BObject bo, JMenuItem jmi) {
        curPackage = null;
        curClass = null;
        curObject = bo;
    }

    public void notifyPostToolsMenu(BPackage bp, JMenuItem jmi) {

        curPackage = bp;
        curClass = null;
        curObject = null;
        if (env != null) {
            startEnvMenu.setEnabled(false);
        }
    }

    // A utility method which pops up a dialog detailing the objects                
    // involved in the current (SimpleAction) menu invocation.
    private void showCurrentStatus(String header) {
        try {
            if (curObject != null) {
                curClass = curObject.getBClass();
            }
            if (curClass != null) {
                curPackage = curClass.getPackage();
            }

            String msg = header;
            if (curPackage != null) {
                msg += "\nCurrent Package = " + curPackage;
            }
            if (curClass != null) {
                msg += "\nCurrent Class = " + curClass;
            }
            if (curObject != null) {
                msg += "\nCurrent Object = " + curObject;
            }
            JOptionPane.showMessageDialog(null, msg);
        } catch (Exception exc) {
        }
    }
    
    private void addExamples(JMenu menu, String path) {
        String [] examples = {};
        try {
            //examples = Sysutil.readUrl(prefs.getServerUrl()+"/examples/").split("\n");
            
            File examplesDir = new File(curPackage.getDir()+"/examples/lessons/"+path+"/");
            if (examplesDir.isDirectory()) {
                examples = examplesDir.list();
            }
        } catch (Exception e) {
            System.out.println("Error: "+e);
        }

        for (String example : examples) {
            // Get all examples from env3d.org
            JMenuItem exampleItem = new JMenuItem(new OnlineExampleAction(example, path+"/"+example));
            menu.add(exampleItem);
        }
        
    }
}
