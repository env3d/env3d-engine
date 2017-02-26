/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import javax.swing.JOptionPane;

/**h
 *
 * @author jmadar
 */
public class Sysutil {

    /**
     * Get a URL representation of a resource.
     * @param resource
     * @return the URL
     */
    public static URL getURL(String resource) {
        java.net.URL resourceURL;
        //image = (new javax.swing.ImageIcon(textureFile)).getImage();
        if (resource.startsWith("http")) {
            try {
                resourceURL = new java.net.URL(resource);
            } catch (Exception e) {
                e.printStackTrace();
                resourceURL = null;
            }
        } else {
            java.lang.ClassLoader cldr = Sysutil.class.getClassLoader();
            resourceURL = cldr.getResource(resource);
            if (resourceURL == null) {
                try {
                    resourceURL = (new File(resource)).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    resourceURL = null;
                }
            }
        }
        return resourceURL;
    }

    /**
     * Read the entire URL into a string
     * @param url
     * @return
     */
    public static String readUrl(String urlString) {
        try {
            java.net.URL url = new java.net.URL(urlString);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            // Create the BufferedReader based on the filename
            BufferedReader br = null;
            if (url != null) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            StringBuffer fileString = new StringBuffer();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                fileString.append(line + "\n");
            }

            return fileString.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void executeCommand(String command) {
        executeCommand(command, null);
    }
    
    public static void executeCommand(String command, File workdir) {
        // Execute the jar and sign
        String osName = System.getProperty("os.name");
        String[] cmdArray;
        if (osName.startsWith("Windows")) {
            cmdArray = new String[]{"cmd", "/C", command};
        } else {
            // Unix-like OS - need to invoke the shell
            cmdArray = new String[]{"bash", "-c", command};
        }
        Runtime r = Runtime.getRuntime();
        try {
            Process p = r.exec(cmdArray, null, workdir);
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
                System.out.flush();
            }

            while ((line = errReader.readLine()) != null) {
                System.out.println(line);
                System.out.flush();
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

    public static String getMacAddress() throws IOException {
        String command = null;
        String osName = System.getProperty("os.name");
        Pattern p = null;
        if (osName.startsWith("Windows")) {
            command = "ipconfig /all";
            p = Pattern.compile(".*Physical Address.*: (.*)");
        } else {
            command = "ifconfig";
            p = Pattern.compile(".*ether (.*)\\s*");
        }
        Process pid = Runtime.getRuntime().exec(command);
        BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
        if (p != null) {
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    return m.group(1).substring(0, 17);
                }
            }
        }
        // Fall back - if cannot get the hardware address, use
        // the local ip
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static void unCompressFile(String inputFile) {
        try {
            java.io.File inFile = new java.io.File(inputFile);
            String outFileString = inputFile.substring(0, inputFile.indexOf(".lzma"));
            java.io.File outFile = new java.io.File(outFileString);

            java.io.BufferedInputStream inStream = new java.io.BufferedInputStream(new java.io.FileInputStream(inFile));
            java.io.BufferedOutputStream outStream = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile));

            // This is taken from the code for lzma_alone
            int propertiesSize = 5;
            byte[] properties = new byte[propertiesSize];
            if (inStream.read(properties, 0, propertiesSize) != propertiesSize) {
                throw new Exception("input .lzma file is too short");
            }
            SevenZip.Compression.LZMA.Decoder decoder = new SevenZip.Compression.LZMA.Decoder();
            if (!decoder.SetDecoderProperties(properties)) {
                throw new Exception("Incorrect stream properties");
            }
            long outSize = 0;
            for (int i = 0; i < 8; i++) {
                int v = inStream.read();
                if (v < 0) {
                    throw new Exception("Can't read stream size");
                }
                outSize |= ((long) v) << (8 * i);
            }
            if (!decoder.Code(inStream, outStream, outSize)) {
                throw new Exception("Error in data stream");
            }

            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
            System.out.println("Error in creating lzma files " + e);
            e.printStackTrace();
        }
    }

    public static void unjar(File inFile, File dest) throws IOException {
        if (!dest.exists()) {
            dest.mkdirs();
        }
        if (!dest.isDirectory()) {
            throw new IOException("Destination must be a directory.");
        }
        JarInputStream jin = new JarInputStream(new FileInputStream(inFile));
        byte[] buffer = new byte[1024];

        ZipEntry entry = jin.getNextEntry();
        while (entry != null) {
            String fileName = entry.getName();
            if (fileName.charAt(fileName.length() - 1) == '/') {
                fileName = fileName.substring(0, fileName.length() - 1);
            }
            if (fileName.charAt(0) == '/') {
                fileName = fileName.substring(1);
            }
            if (File.separatorChar != '/') {
                fileName = fileName.replace('/', File.separatorChar);
            }
            File file = new File(dest, fileName);
            if (entry.isDirectory()) {
                // make sure the directory exists
                file.mkdirs();
                jin.closeEntry();
            } else {
                // make sure the directory exists
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }

                // dump the file
                OutputStream out = new FileOutputStream(file);
                int len = 0;
                while ((len = jin.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                jin.closeEntry();
                file.setLastModified(entry.getTime());
            }
            entry = jin.getNextEntry();
        }
        jin.close();
    }

    /**
     * returns a hashmap of all of the OBJ files in a directory.
     */
    public static HashMap<String, String[]> createModelsHashMap(String modelDir) {
        // First, create a list of subdirectories under modelDir
        File[] actionDirectories = fileList(modelDir, new DirOnlyFilter());

        FileFilter objFileFilter = new ObjFileFilter();
        HashMap<String, String[]> animationMap = new HashMap<String, String[]>();
        if (actionDirectories == null || actionDirectories.length == 0) {
            return animationMap;
        }
        for (File action : actionDirectories) {
            // Get the list of obj files in each directory
            File[] objFiles = fileList(action.getAbsolutePath(), objFileFilter);
//            for (File f : objFiles) {
//                System.out.println(f.getName());
//            }
            Pattern p = Pattern.compile("(.*?)(\\d+).obj");
            Matcher matcher = p.matcher(objFiles[0].getAbsoluteFile().getName());
            matcher.matches();
            int startNum = Integer.parseInt(matcher.group(2));
            matcher = p.matcher(objFiles[objFiles.length - 1].getAbsoluteFile().getName());
            matcher.matches();
            int endNum = Integer.parseInt(matcher.group(2));

            String[] objArray = new String[endNum - startNum + 1];
            int objFileIndex = 0;
            for (int i = 0; i < objArray.length; i++) {
                matcher = p.matcher(objFiles[objFileIndex].getAbsoluteFile().getName());
                matcher.matches();
                int currentFrame = Integer.parseInt(matcher.group(2)) - startNum;
                if (i > currentFrame) {
                    objFileIndex++;
                }
                objArray[i] = objFiles[objFileIndex].getAbsolutePath();
            }
            animationMap.put(action.getName(), objArray);
        }
        return animationMap;
    }

    /**
     * Get all the subdirectories from dirName
     * @param dirName
     * @return 
     */
    public static File[] dirList(String dirName) {
        FileFilter filter = new DirOnlyFilter();
        return fileList(dirName, filter);
    }

    public static File[] fileList(String dirName, FileFilter filter) {
        File dir = new File(dirName);

        return dir.listFiles(filter);
    }

    public static class DirOnlyFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return file.isDirectory() && !file.getName().startsWith(".");
        }
    }

    public static class ObjFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return file.getName().toLowerCase().endsWith(".obj");
        }
    }

    public static class MtlFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return file.getName().toLowerCase().endsWith(".mtl");
        }
    }

    public static class ImgFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            String name = file.getName().toLowerCase();
            return (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith("tga"));
        }
    }

    public static void copyFile(File inFile, File outFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            //bw.write("package env3d.android.game;");
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                bw.write(line + "\n");
            }

            br.close();
            bw.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
            e.printStackTrace();
        }
    }
    
    public static String[] getFileStringArray(String dir, String firstItem, FileFilter filter) {
        File[] fileArray = Sysutil.fileList(dir, filter);
        String[] stringArray = new String[fileArray.length+1];
        stringArray[0] = firstItem;
        for (int i = 1; i <= fileArray.length; i++) {
            stringArray[i] = fileArray[i-1].getName();
        }

        return stringArray;
    }    
}
