package bluej.action;

import bluej.action.AbstractEnvAction;
import SevenZip.LzmaAlone;
import bluej.env3d.BEnv;
import bluej.ProgressBar;
import bluej.extensions.BlueJ;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import env3d.util.ClientHttpRequest;
import env3d.util.Staging;
import env3d.util.Sysutil;

/**
 * Creates the jars necessary for env3d applet. Sign the applet as well.
 *
 * @author jmadar
 */
public class CreateAppletAction extends AbstractEnvAction {

    private BEnv env;
    private String msgHeader;
    private static File packageDir;
    private static String env3dUrl;

    public CreateAppletAction(String menuName, String msg) {

        putValue(AbstractAction.NAME, menuName);
        msgHeader = msg;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (packageDir == null) {
                packageDir = bluej.getCurrentPackage().getDir();
            }
            createApplet(bluej.getCurrentFrame(), packageDir.getPath(), ae.getActionCommand().toLowerCase());
        } catch (Exception e) {
            System.out.println("Error: "+e);
            e.printStackTrace();            
        }
    }    
    
    public static void createApplet(Frame topFrame, String packageDir, String eventString) {        
        if (CreateAppletAction.packageDir == null) CreateAppletAction.packageDir = new File(packageDir);
        ProgressBar progress = new ProgressBar(topFrame, "Creating Env3D Applet");
        try {
            makeStagingDir();
            System.out.println("Creating Env3d Applet...");
            String javaBin = "";
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Windows")) {
                javaBin = System.getProperty("java.home")+"bin\\";
            }
            javaBin = javaBin.replaceAll("jre", "");
            javaBin = javaBin.replaceAll("(Program Files.*)\\\\", "\"$1\"\\\\");
            System.out.println("Java bin: "+javaBin);
            // Sign and pack game4.jar and game_support.jar
            progress.setProgress(0, "Deploying Env3D");
            // Loading the use of topFrame parameter.  If topFrame is null, we assume it is called
            // from standalone and not create the game4.jar file.
            if (topFrame != null) {                
                executeCommand(javaBin+"jar cf applet/game4.jar *.class", CreateAppletAction.packageDir);
            }
            //executeCommand(javaBin+"jar cf applet/game_support.jar models/** textures/** sounds/**");
            executeCommand(javaBin+"jar cf applet/game_support.jar -C staging/ .", CreateAppletAction.packageDir);
            progress.setProgress(20, "Packaging...");
            executeCommand(javaBin+"pack200 --repack applet/game4.jar", CreateAppletAction.packageDir);
            executeCommand(javaBin+"jarsigner -keystore env3dkey -storepass 123456 applet/game4.jar mykey", CreateAppletAction.packageDir);
            executeCommand(javaBin+"pack200 -g applet/game4.jar.pack applet/game4.jar", CreateAppletAction.packageDir);
            executeCommand(javaBin+"pack200 --repack applet/game_support.jar", CreateAppletAction.packageDir);
            executeCommand(javaBin+"jarsigner -keystore env3dkey -storepass 123456 applet/game_support.jar mykey", CreateAppletAction.packageDir);
            executeCommand(javaBin+"pack200 -g applet/game_support.jar.pack applet/game_support.jar", CreateAppletAction.packageDir);
            // Compress game4.jar.pack and game4.jar.pack.lzma
            System.out.println("Compressing jars");
            progress.setProgress(50, "Compressing...");
            // Create LZMA files
            String filepath = packageDir.replaceAll("\\\\", "/");
            compressFile(filepath+"/applet/game4.jar.pack");
            compressFile(filepath+"/applet/game_support.jar.pack");
            // Create gz files
            executeCommand(javaBin+"pack200 --gzip applet/game4.jar.pack.gz applet/game4.jar", CreateAppletAction.packageDir);
            executeCommand(javaBin+"pack200 --gzip applet/game_support.jar.pack.gz applet/game_support.jar", CreateAppletAction.packageDir);

            progress.setProgress(80, "Clean up...");
            deleteFile(filepath+"/applet/game4.jar");
            deleteFile(filepath+"/applet/game_support.jar");
            deleteFile(filepath+"/applet/game4.jar.pack");
            deleteFile(filepath+"/applet/game_support.jar.pack");


            if (eventString.contains("upload")) {
                // Get the env3d url location
                if (prefs == null) {
                    env3dUrl = "http://env3d.org";
                } else {
                    env3dUrl = prefs.getServerUrl();
                }
                progress.setProgress(90, "Uploading...");
                try {                    
                    String appid = uploadFile(null);
                    String path = env3dUrl+"/?appid="+appid;
                    if (eventString.contains("webstart")) {
                        uploadFile(filepath+"/applet/game4.jar.pack.gz");
                        uploadFile(filepath+"/applet/game_support.jar.pack.gz");
                        path += "&webstart";
                    } else {
                        uploadFile(filepath+"/applet/game4.jar.pack.lzma");
                        uploadFile(filepath+"/applet/game_support.jar.pack.lzma");                        
                    }
                    progress.setProgress(100, null);

                    java.awt.Desktop.getDesktop().browse(new URL(path).toURI());
                } catch (Exception e) {
                    progress.setProgress(100, null);
                    JOptionPane.showMessageDialog(null,"Error uploading to server!\n"+e);
                    e.printStackTrace();
                }
            } else {
                String path = filepath+"/applet/appletloader.html";
                progress.setProgress(100, null);
                java.awt.Desktop.getDesktop().browse(new File(path).toURI());
            }

        } catch (Exception e) {
            System.out.println("Error: "+e);
            e.printStackTrace();            
        }
    }
    
    
    public static void compressFile(String inputFile) {
        try {
            java.io.File inFile = new java.io.File(inputFile);
            java.io.File outFile = new java.io.File(inputFile+".lzma");

            java.io.BufferedInputStream inStream  = new java.io.BufferedInputStream(new java.io.FileInputStream(inFile));
            java.io.BufferedOutputStream outStream = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile));
            SevenZip.Compression.LZMA.Encoder encoder = new SevenZip.Compression.LZMA.Encoder();
            LzmaAlone.CommandLine params = new LzmaAlone.CommandLine();
            try {
                params.DictionarySize = 1 << prefs.getDictSize();
            } catch (NullPointerException e) {
                System.out.println("Using safe dictionary size");
                params.DictionarySize = 1 << 20;
            }
            if (!encoder.SetAlgorithm(params.Algorithm))
                    throw new Exception("Incorrect compression mode");
            if (!encoder.SetDictionarySize(params.DictionarySize))
                    throw new Exception("Incorrect dictionary size");
            if (!encoder.SetNumFastBytes(params.Fb))
                    throw new Exception("Incorrect -fb value");
            if (!encoder.SetMatchFinder(params.MatchFinder))
                    throw new Exception("Incorrect -mf value");
            if (!encoder.SetLcLpPb(params.Lc, params.Lp, params.Pb))
                    throw new Exception("Incorrect -lc or -lp or -pb value");
            encoder.SetEndMarkerMode(params.Eos);
            encoder.WriteCoderProperties(outStream);

            long fileSize = inFile.length();
            for (int i = 0; i < 8; i++)
                    outStream.write((int)(fileSize >>> (8 * i)) & 0xFF);
            encoder.Code(inStream, outStream, -1, -1, null);

            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
            System.out.println("Error in creating lzma files "+e);
            e.printStackTrace();
        }

    }

    public static void deleteFile(String fileName) {
        File f = new File(fileName);
        f.delete();
    }


    /**
     * Upload a file to the env3d website and return the application id
     * @param filename
     * @return
     * @throws IOException
     */
    public static String uploadFile(String filename) throws IOException {
        ClientHttpRequest request = new ClientHttpRequest(env3dUrl+"/upload.php");
        request.postCookies();
        if (filename != null) {
            File uploadFile = new File(filename);
            if (uploadFile.length() > 20*1024*1024) {
                // File is too large
                throw new IOException("File too large! Must be under 20Mb");
            }
            request.setParameter("uploaded_file", uploadFile);
        }        
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.post()));
        while(true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
        request.printCookies();
        return request.cookies.get("PHPSESSID").toString().split(";")[0];
    }
    
    /**
     * Create a staging directory for models, textures, and sound
     * @throws Exception 
     */
    public static void makeStagingDir() throws Exception {
        
        boolean bluejMode = bluej != null ? true : false;
        
        if (packageDir == null && bluejMode) {
            packageDir = bluej.getCurrentPackage().getDir();
        }        
        
        // Delegate to static utility method
        Staging.makeStagingDir(packageDir, bluejMode);        
    }
    
    public static void main(String args[]) {                
        createApplet(null, args[0], args[1]);
    }
}
