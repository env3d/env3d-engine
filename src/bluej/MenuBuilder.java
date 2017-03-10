package bluej;

import bluej.env3d.BEnv;
import bluej.action.StartEnvAction;
import bluej.action.OnlineExampleAction;
import bluej.action.ExtractAssetsAction;
import bluej.action.CreateModelAction;
import bluej.action.CreateWebAppAction;
import bluej.extensions.*;
import com.sun.javafx.stage.StageHelper;
import java.io.File;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import env3d.util.Sysutil;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class MenuBuilder extends MenuGenerator {

    private BPackage curPackage;
    private BClass curClass;
    private BObject curObject;
    private BEnv env;
    private JMenuItem startEnvMenu;
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
        addExamples(exampleMenu, "desktop");

        final JMenu modelsMenu = new JMenu("Models");
        File[] directories;
        try {
            directories = Sysutil.dirList(curPackage.getDir().getCanonicalFile() + "/models");
            // The number of models has changed, repopulate                     
            for (File dir : directories) {
                modelsMenu.add(new JMenuItem(new CreateModelAction(dir.getName(), dir.getName())));
            }
        } catch (Exception ex) {
            Logger.getLogger(MenuBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        env3dMenu.add(modelsMenu);

        env3dMenu.add(new StartEnvAction("Env3D Scene Creator", "Env3D Scene Creator"));

        try {
            File additionalModels = new File(curPackage.getDir().getCanonicalFile() + File.separator + "env3d_characterpack.jar.lzma");
            if (additionalModels.isFile()) {
                env3dMenu.add(new JPopupMenu.Separator());
                env3dMenu.add(new ExtractAssetsAction("Extract additional models"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Bluejenv.bluej.getCurrentFrame(), e, "", JOptionPane.INFORMATION_MESSAGE);
        }

        return env3dMenu;
        //return startEnvMenu;
    }

    public JMenuItem getClassMenuItem(BClass aClass) {

        try {
            System.out.println(aClass.getJavaClass().getSuperclass());
            System.out.println("Class menu invoked " + aClass.getName());
            JMenu menu = new JMenu("Env3D");
            if (aClass.getName().equals("Game")) {

                if (aClass.getMethod("setup", null) != null
                        && aClass.getMethod("loop", null) != null
                        && aClass.getMethod("play", null) != null) {

                    final BMethod method = aClass.getMethod("play", null);

                    menu.add(new JMenuItem(new AbstractAction("Execute App") {
                                                     
                        boolean started = false;
                        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("Executing main");
                            
                            Thread en = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        BConstructor con = aClass.getConstructor(null);
                                        BObject o = con.newInstance(null);
                                        o.addToBench("game1");
                                        method.invoke(o, null);
                                    } catch (Exception ex) {
                                        Logger.getLogger(MenuBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            };
                            en.start();

                            final Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        StageHelper.getStages().stream().forEach((stg) -> {
                                            if (!stg.getTitle().contains("Terminal Window")) {
                                                stg.toBack();
                                            }
                                        });
                                    } catch (Exception ex) {
                                        Logger.getLogger(MenuBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            };

                            Thread t = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(2000);
                                        Platform.runLater(r);
                                    } catch (Exception ex) {
                                        Logger.getLogger(MenuBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            };
                            t.start();

                        }

                    }));

                    menu.add(new JMenuItem(new CreateWebAppAction()));
                }

                if (menu.getItemCount() > 0) {
                    return menu;
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Error in getClassMenuItem: " + e);
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
        String[] examples = {};
        try {
            //examples = Sysutil.readUrl(prefs.getServerUrl()+"/examples/").split("\n");

            File examplesDir = new File(curPackage.getDir() + "/examples/lessons/" + path + "/");
            if (examplesDir.isDirectory()) {
                examples = examplesDir.list();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        for (String example : examples) {
            // Get all examples from env3d.org
            JMenuItem exampleItem = new JMenuItem(new OnlineExampleAction(example, path + "/" + example));
            menu.add(exampleItem);
        }

    }
}
