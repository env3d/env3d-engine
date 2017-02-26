/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluej.action;

import bluej.action.CreateAppletAction;
import bluej.action.AbstractEnvAction;
import bluej.messageconsole.EnvConsole;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.AbstractAction;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

/**
 *
 * @author jmadar
 */
public class CreateFatJarAction extends AbstractEnvAction {

    public CreateFatJarAction(String menuName, String message) {
        putValue(AbstractAction.NAME, menuName);
    }

    public void actionPerformed(ActionEvent ae) {
        // Create a fat jar by calling the appropriate ant task
        final EnvConsole mc = new EnvConsole(bluej.getCurrentFrame(), "Creating executable jar file");

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    // Create staging directory first
                    CreateAppletAction.makeStagingDir();

                    // Now being handled in the ant task
//                    String os = System.getProperty("os.name");
//                    String ver = System.getProperty("os.version");
//
//                    System.out.println("Host OS: " + os + " Ver:" + ver);
//
//                    // A little hack for os specific setup        
//                    if (os.toLowerCase().contains("os x") && ver.toLowerCase().contains("10.8")) {                        
//                        System.out.println("OS X 10.8 Detected.  Replacing launch4j executables.");
//                        // for osx 10.8, use different launch4j binary
//                        String dir = bluej.getCurrentPackage().getProject().getDir() + "/extras/launch4j/";
//                        FileUtils.forceDelete(new File(dir + "bin/windres"));
//                        FileUtils.forceDelete(new File(dir + "bin/ld"));
//                        FileUtils.copyFile(new File(dir + "bin-osx-10.8/windres"), new File(dir + "bin/windres"));
//                        FileUtils.copyFile(new File(dir + "bin-osx-10.8/ld"), new File(dir + "bin/ld"));
//                    }

                    // execute the ant task
                    Project project = new Project();
                    DefaultLogger consoleLogger = new DefaultLogger();
                    consoleLogger.setErrorPrintStream(System.err);
                    consoleLogger.setOutputPrintStream(System.out);
                    consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
                    project.addBuildListener(consoleLogger);

                    project.init();
                    String projectName = bluej.getCurrentPackage().getProject().getName();
                    project.setProperty("application.title", projectName);
                    File buildFile = new File(bluej.getCurrentPackage().getDir(), "build.xml");
                    ProjectHelper.configureProject(project, buildFile);
                    Vector<String> targets = new Vector<String>();
                    targets.add("fat-jar");
                    targets.add("exe");
                    targets.add("mac-app");
                    project.executeTargets(targets);

                    mc.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }
}
