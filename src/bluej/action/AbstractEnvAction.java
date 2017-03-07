/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluej.action;

import bluej.Preferences;
import bluej.extensions.BlueJ;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.AbstractAction;

/**
 *
 * @author jmadar
 */
public abstract class AbstractEnvAction extends AbstractAction {
    
    protected static BlueJ bluej;
    protected static Preferences prefs;        
    
    /**
     * @return the bluej
     */
    public static BlueJ getBluej() {
        return bluej;
    }

    /**
     * @param aBluej the bluej to set
     */
    public static void setBluej(BlueJ aBluej) {
        bluej = aBluej;
    }

    /**
     * @return the prefs
     */
    public static Preferences getPrefs() {
        return prefs;
    }

    /**
     * @param prefs the prefs to set
     */
    public static void setPrefs(Preferences myPrefs) {
        prefs = myPrefs;
    }
    
    public static void executeCommand(String command, File execDir) {
        // Execute the jar and sign
        String osName = System.getProperty("os.name");
        String [] cmdArray;
        if (osName.startsWith("Windows")) {
            cmdArray = new String[] {"cmd", "/C", command};
        } else {
            // Unix-like OS - need to invoke the shell
            ArrayList<String> commands = new ArrayList<String>();
            commands.add("bash");   
            commands.add("-l");
            for (String c : command.split(" ")) {
                commands.add(c);
            }
            System.out.print(">");
            for (String c: commands) {
                System.out.print(c+" ");
            }
            System.out.println();
            cmdArray = commands.toArray(new String[0]);
        }
//        Runtime r = Runtime.getRuntime();
        try {
//            Process p = r.exec(cmdArray, null, execDir);
            ProcessBuilder pb = new ProcessBuilder(cmdArray); 
//            String path = pb.environment().get("PATH");
//            pb.environment().put("PATH", path+":./node/bin:~/.jsweet-node_modules");
//            
//            for(String key : pb.environment().keySet()) {
//                System.out.println(key+" "+pb.environment().get(key));
//            }
            pb.directory(execDir);
            Process p = pb.start();
            InputStream in = p.getInputStream();
            InputStream err = p.getErrorStream();

            BufferedInputStream buf = new BufferedInputStream(in);
            InputStreamReader inread = new InputStreamReader(buf);
            BufferedReader bufferedreader = new BufferedReader(inread);

            BufferedReader errReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(err)));
            // Read the ls output
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                System.out.println(line);
            }

            while ((line = errReader.readLine()) != null) {
                System.out.println(line);
            }
            try {
                if (p.waitFor() != 0) {
                    System.out.println("exit value = " + p.exitValue());
                }
            } catch (InterruptedException e) {
                System.out.println(e);
            } finally {
                // Close the InputStream
                bufferedreader.close();
                inread.close();
                buf.close();
                in.close();
                errReader.close();
            }
        } catch (Exception e) {
            System.out.println("Error :" + e);
        }
    }
    
}
