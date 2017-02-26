package bluej.action;

import static bluej.action.AbstractEnvAction.bluej;
import static bluej.action.AbstractEnvAction.prefs;
import bluej.messageconsole.EnvConsole;
import env3d.util.Staging;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.AbstractAction;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import env3d.util.Sysutil;
import java.awt.Frame;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Echo;

/**
 * Experimental. The idea is to package up the project as a jar file that can be
 * included inside the env3d android application.
 *
 * @author jmadar
 */
public class DeployAndroidAction extends AbstractEnvAction {

    private String msgHeader;

    public DeployAndroidAction(String menuName, String msg) {

        putValue(AbstractAction.NAME, menuName);
        msgHeader = msg;
    }
    
    public static void deployAndroid(Frame topFrame, String command, File projectDir, String androidSDKPath, String appName, String icon, String splash) {
        
        boolean firstInvoked = false;
        try {
            // Show a console
            final EnvConsole mc = new EnvConsole(topFrame, "Deploying Android");


            // execute the android tool

            // First, check to see if the android directory is there.  If not, 
            // create it using the android tool
            final File androidDir = new File(projectDir.getCanonicalPath() + "/android/");
            if (!androidDir.isDirectory()) {
                firstInvoked = true;
                String name = " --name " + appName + " ";
                String target = " --target 1 ";
                String path = " --path " + androidDir;
                String apackage = " --package com.mycompany.mygame ";
                String activity = " --activity MainActivity ";

                String commandLine = androidSDKPath + "tools/android create project " + name + target + path + apackage + activity;
                System.out.println("Creating Android Project: " + commandLine);
                Sysutil.executeCommand(commandLine);
            }

            // Check if the android directory is created properly
            if (!androidDir.isDirectory()) {
                throw new Exception("Android SDK path is not set properly! Please set it under Preferences->Extensions");
            }

            if (firstInvoked) {
                // copy the AndroidManifest and related files
                FileUtils.copyFile(new File(projectDir.getCanonicalPath() + "/examples/android/AndroidManifest.xml"),
                        new File(androidDir.getCanonicalPath() + "/AndroidManifest.xml"));
                FileUtils.copyFile(new File(projectDir.getCanonicalPath() + "/examples/android/ant.properties"),
                        new File(androidDir.getCanonicalPath() + "/ant.properties"));
                FileUtils.copyFile(new File(projectDir.getCanonicalPath() + "/" + icon),
                        new File(androidDir.getCanonicalPath() + "/res/drawable/icon.png"));
                FileUtils.copyFile(new File(projectDir.getCanonicalPath() + "/" + splash),
                        new File(androidDir.getCanonicalPath() + "/res/drawable/splash.png"));
            }

            // Set the application name, have to modify the appname in strings.xml
            try {
                File stringsXML = new File(projectDir.getCanonicalPath() + "/android/res/values/strings.xml");
                List<String> lines = FileUtils.readLines(stringsXML);

                StringBuffer replacementString = new StringBuffer();
                for (String line : lines) {
                    String pattern = "(.*<string name=\"app_name\">)(.+)(</string>)";
                    replacementString.append(line.replaceAll(pattern, "$1" + appName + "$3"));
                    replacementString.append("\n");
                }
                FileUtils.writeStringToFile(stringsXML, replacementString.toString(), false);
            } catch (IOException e) {
                // File not found, don't worry about it            
                System.out.println("Error: " + e);
            }


            File fromDir = new File(projectDir.getCanonicalPath() + "/examples/android/jme3/com");
            File toDir = new File(androidDir.getCanonicalPath() + "/src");

            FileUtils.deleteQuietly(toDir);            
            // copy from the current directory all the .java files to the android src directory
            if (bluej != null) {
                for (File javaFile : FileUtils.listFiles(projectDir, new String[]{"java"}, false)) {
                    FileUtils.copyFileToDirectory(javaFile, toDir);
                }
            } else {
                FileUtils.copyDirectoryToDirectory(new File(projectDir.getCanonicalFile()+"/src/"), toDir);
            }
            // copy the harness            
            FileUtils.copyDirectoryToDirectory(fromDir, toDir);

            // Make the staging directory copy to the assets directory
            //CreateAppletAction.setBluej(bluej);
            //CreateAppletAction.makeStagingDir();
            Staging.makeStagingDir(projectDir, bluej != null ? true : false);    

            File stagingDir = new File(projectDir.getCanonicalPath() + "/staging");
            File assetDir = new File(androidDir + "/assets");
            FileUtils.deleteQuietly(assetDir);
            FileUtils.copyDirectory(stagingDir, assetDir);

            if (firstInvoked) {
                File libsDir = new File(projectDir.getCanonicalPath() + "/+libs");
                File androidLibs = new File(projectDir.getCanonicalPath() + "/android/libs");
                FileUtils.copyDirectory(libsDir, androidLibs);

                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/lwjgl.jar"));
                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/jME3-lwjgl.jar"));
                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/jME3-desktop.jar"));
                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/bluejext.jar"));
                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/rsyntaxtextarea.jar"));
                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/jinput.jar"));
                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/jython.jar"));
                FileUtils.deleteQuietly(new File(projectDir.getCanonicalPath() + "/android/libs/ant.jar"));

                // Need to replace the jME3 desktop renderer with the android one.
                FileUtils.copyFile(new File(projectDir.getCanonicalPath() + "/extras/jME3-android.jar"),
                        new File(projectDir.getCanonicalPath() + "/android/libs/jME3-android.jar"));
                // Include google admob
                FileUtils.copyFile(new File(projectDir.getCanonicalPath() + "/extras/admob/GoogleAdMobAdsSdk-6.3.1.jar"),
                        new File(projectDir.getCanonicalPath() + "/android/libs/GoogleAdMobAdsSdk-6.3.1.jar"));
            }

            final String _appName = appName;
            final String _command = command;
            // Exceute the ant task for debug install.
            // Need to run this on a separate thread so the console
            // will be updated properly.
            Thread t = new Thread() {
                public void run() {
                    try {
                        //Sysutil.executeCommand("ant debug install", androidDir);
                        Project project = new Project();
                        DefaultLogger consoleLogger = new DefaultLogger();
                        consoleLogger.setErrorPrintStream(System.err);
                        consoleLogger.setOutputPrintStream(System.out);
                        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
                        project.addBuildListener(consoleLogger);

                        project.init();
                        File buildFile = new File(androidDir, "build.xml");
                        ProjectHelper.configureProject(project, buildFile);
                        Vector<String> targets = new Vector<String>();
                        if (_command.contains("release")) {
                            // Create a new target to echo a message to the user
                            Target t = new Target();
                            t.setName("env3d-release");
                            t.setDepends("release");

                            // Add a user friendly message at the end
                            Echo msg = new Echo();
                            msg.setProject(project);
                            Echo.EchoLevel level = new Echo.EchoLevel();
                            level.setValue("info");
                            msg.setLevel(level);
                            msg.setTaskName("echo");
                            msg.addText("APK created successfully\n");
                            msg.addText("Look for the " + _appName + "-release.apk file in the dist/ directory");

                            t.addTask(msg);
                            // add this new custom task to the project
                            project.addTarget(t);
                            t.setProject(project);


                            // execute the target
                            targets.add("env3d-release");

                        } else {
                            targets.add("debug");
                            targets.add("install");
                        }
                        project.executeTargets(targets);
                        mc.reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            mc.setThread(t);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            deployAndroid(bluej.getCurrentFrame(), 
                    ae.getActionCommand().toLowerCase(),
                    bluej.getCurrentPackage().getDir(),
                    prefs.getAndroidDir(),
                    prefs.getAndroidName(),
                    prefs.getAndroidIcon(),
                    "textures/black.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
                
    }
    
    public static void main(String args[]) {
        try {
            String command = args[0];
            File projectDir = new File(args[1]);
            
            // Read from property file
            Properties prop = new Properties();
            prop.load(new FileReader(projectDir.getCanonicalPath()+"/env3d.properties"));
            String sdkDir = prop.getProperty("android.sdk");
            String appName = prop.getProperty("android.appname");
            String icon = prop.getProperty("android.icon");
            String splash = prop.getProperty("android.splash");
            deployAndroid(null, command, projectDir, sdkDir, appName, icon, splash);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error "+ex);
            Logger.getLogger(DeployAndroidAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
