/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author jmadar
 */
public class Staging {

    /**
     * Create a staging directory for models, textures, and sound
     * @param packageDir the directory to operate on
     * @param bluej is it being run within BlueJ?
     * @throws Exception 
     */
    public static void makeStagingDir(File packageDir, boolean bluej) throws Exception {
        HashSet<String> assetsSet = new HashSet<String>();
        
        // Make sure to include world.txt     
        assetsSet.add("world.txt");
        // Put in the default textures
        assetsSet.add("textures/earth.png");
        assetsSet.add("textures/fence0.png"); 
        assetsSet.add("textures/fence1.png");
        assetsSet.add("textures/floor.png");
        assetsSet.add("textures/red.png");
        assetsSet.add("textures/terrain/alpha.png");
        assetsSet.add("textures/particle/flare1.png");
        assetsSet.add("textures/controller/buttons.png");
        assetsSet.add("textures/controller/touched.png");
        assetsSet.add("textures/controller/layout.txt");
        
        // Read from world.txt and extract all models and textures
        try {
            List<String> lines = FileUtils.readLines(new File(packageDir.getCanonicalPath()+"/world.txt"));
            for (String line: lines) {
                // This is a csv file
                String[] fields = line.split(",");
                for (String field : fields) {
                    Pattern p = Pattern.compile("(models|textures|sound).*");
                    Matcher matcher = p.matcher(field);
                    if (matcher.find()) {
                        //System.out.println(field);
                        assetsSet.add(field);
                    }                
                }
            }
        } catch (IOException e) {
            // File not found, don't worry about it            
            System.out.println("Error: "+e);
        }
        
        // Loop over all the .java files
        String srcDir;
        if (bluej) {
            srcDir = packageDir.getCanonicalPath();
        } else {
            srcDir = "src/";
        }
        Collection<File> javaFiles = FileUtils.listFiles(new File(srcDir), new String[]{"java"}, true);
        for (File f : javaFiles) {
            // Skip files from the "examples" directory if we are running in BlueJ
            if (f.getAbsolutePath().startsWith(srcDir+"/examples") && bluej)
                continue;
            
            System.out.println("Scanning "+f.getAbsolutePath());
            List<String> lines = FileUtils.readLines(f);
            for (String line : lines) {
                Pattern p = Pattern.compile(".*\"(models|textures|sounds)(.*?)\".*");
                Matcher matcher = p.matcher(line);
                if (matcher.find()) {
                    String fileName = matcher.group(1)+matcher.group(2);
                    assetsSet.add(fileName);
                }
            }
        }
        
        
        // Now create the staging directory
        File stagingDir = new File(packageDir.getCanonicalPath()+"/staging");
        if (stagingDir.isDirectory()) {
            FileUtils.cleanDirectory(stagingDir);
        }
        for (String asset : assetsSet) {
            System.out.println("Copying "+asset+" to staging directory");
            File src = new File(packageDir.getCanonicalPath()+"/"+asset);
            File dest = new File(stagingDir.getCanonicalPath()+"/"+asset);
            if (src.isDirectory()) {
                FileUtils.copyDirectory(src, dest);
            } else {
                if (src.isFile()) FileUtils.copyFile(src, dest);
            }
        }
    }    
    
    public static void main(String args[]) {
        
        File dir = new File(args[0]);
        if (dir.isDirectory()) {
            try {
                makeStagingDir(dir, false);
            } catch (Exception ex) {
                Logger.getLogger(Staging.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Must be called with a directory as argument!");
        }
    }
}
