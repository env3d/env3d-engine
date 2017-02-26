package bluej;

/*
 * Preferences.java
 *
 * Created on October 28, 2007, 8:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import bluej.extensions.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import env3d.util.Sysutil;
import javax.swing.JOptionPane;

/**
 *
 * @author jmadar
 */
public class Preferences implements PreferenceGenerator {

    private JPanel myPanel;
    private JComboBox cameraControl;
    private JTextField x, y, z;
    private JTextField roomWidth, roomDepth, roomHeight;
    private JTextField serverUrl;
    private JTextField dictSize;
    private JTextField androidDir;
    private JTextField androidName;
    private JTextField androidIcon;
    private BlueJ bluej;
    public static final String PROFILE_CAMERA_CONTROL = "camera_control";
    public static final String PROFILE_CAMERA_X = "camera_x";
    public static final String PROFILE_CAMERA_Y = "camera_y";
    public static final String PROFILE_CAMERA_Z = "camera_z";
    public static final String PROFILE_ROOM_WIDTH = "room_width";
    public static final String PROFILE_ROOM_DEPTH = "room_depth";
    public static final String PROFILE_ROOM_HEIGHT = "room_height";
    public static final String PROFILE_SERVER_URL = "server_url";
    public static final String PROFILE_DICT_SIZE = "dict_size";
    public static final String PROFILE_ANDROID_DIR = "android_sdk_dir";
    public static final String PROFILE_ANDROID_NAME = "android_name";
    public static final String PROFILE_ANDROID_ICON = "android_icon";

    // Construct the panel, and initialise it from any stored values
    public Preferences(BlueJ bluej) {
        this.bluej = bluej;
        //myPanel = new JPanel(new GridLayout(0,1,0,6));
        myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

//        JPanel firstLine = new JPanel(new FlowLayout(FlowLayout.LEFT,1,1));
//        firstLine.add(new JLabel("Camera Control"));
//        cameraControl = new JComboBox();
//        cameraControl.addItem(false);
//        cameraControl.addItem(true);
//        firstLine.add(cameraControl);
//        firstLine.add(new JLabel(""));
//        firstLine.add(new JLabel(""));

        JPanel secondLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        secondLine.add(new JLabel("Camera X, Y, Z"));
        x = new JTextField(5);
        y = new JTextField(5);
        z = new JTextField(5);
        secondLine.add(x);
        secondLine.add(y);
        secondLine.add(z);

//        JPanel thirdLine = new JPanel(new FlowLayout(FlowLayout.LEFT,1,1));
//        thirdLine.add(new JLabel("Room width, height, depth"));
//        roomWidth = new JTextField(5);
//        roomDepth = new JTextField(5);
//        roomHeight = new JTextField(5);
//        thirdLine.add(roomWidth);
//        thirdLine.add(roomDepth);
//        thirdLine.add(roomHeight);

        JPanel fourthLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        fourthLine.add(new JLabel("Env3D Server URL"));
        serverUrl = new JTextField(25);
        fourthLine.add(serverUrl);

        JPanel fifthLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        fifthLine.add(new JLabel("Compression Dictionary Size"));
        dictSize = new JTextField(5);
        fifthLine.add(dictSize);

        JPanel sixthLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        sixthLine.add(new JLabel("Android SDK directory"));
        JPanel androidPanel = new JPanel();
        androidPanel.setLayout(new BoxLayout(androidPanel, BoxLayout.Y_AXIS));
        sixthLine.add(androidPanel);
        androidDir = new JTextField(50);
        androidPanel.add(androidDir);

        JPanel androidButtonPanel = new JPanel();
        androidPanel.add(androidButtonPanel);
        androidButtonPanel.add(new JButton("Pick Dir") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        String defaultDir = null;
                        if (androidDir.getText().length() > 0) {
                            if (androidDir.getText().endsWith(System.getProperty("file.separator"))) {
                                defaultDir = androidDir.getText().substring(0, androidDir.getText().length() - 1);
                            } else {
                                defaultDir = androidDir.getText();
                            }
                            defaultDir = defaultDir.substring(0, defaultDir.lastIndexOf(System.getProperty("file.separator")));
                        }
                        final JFileChooser fc = new JFileChooser(defaultDir);
                        fc.setFileFilter(new FileFilter() {

                            @Override
                            public boolean accept(File file) {
                                if (file.isDirectory()) {
                                    for (File f : file.listFiles()) {
                                        if (f.isDirectory() || f.getName().contains("platform-tools")) {
                                            return true;
                                        }
                                    }
                                }
                                return false;
                            }

                            @Override
                            public String getDescription() {
                                return "Android sdk directory file filter";
                            }
                        });
                        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                        if (fc.showOpenDialog(myPanel) == JFileChooser.APPROVE_OPTION) {
                            try {
                                androidDir.setText(fc.getSelectedFile().getCanonicalPath() + System.getProperty("file.separator"));
                            } catch (IOException ex) {
                                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                });
            }
        });

        androidButtonPanel.add(new JButton("Run android config") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        Sysutil.executeCommand(androidDir.getText() + "tools" + System.getProperty("file.separator") + "android");
                    }
                });
            }
        });

        JPanel androidNameLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        androidNameLine.add(new JLabel("Android Name"));
        androidName = new JTextField(30);
        androidNameLine.add(androidName);


        JPanel androidIconLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        androidIconLine.add(new JLabel("Android Icon (png, jpg, or gif)"));
        androidIcon = new JTextField(30);
        androidIcon.setEnabled(false);
        androidIconLine.add(androidIcon);
        final BlueJ _bluej = bluej;
        androidIconLine.add(new JButton("Browse") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        try {
                            final JFileChooser fc = new JFileChooser(_bluej.getCurrentPackage().getDir());
                            fc.setFileFilter(new FileFilter() {

                                @Override
                                public boolean accept(File file) {
                                    return (file.getName().endsWith("png")
                                            || file.getName().endsWith("gif")
                                            || file.getName().endsWith("jpg"));
                                }

                                @Override
                                public String getDescription() {
                                    return "android accepted image icon";
                                }
                            });
                            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                            if (fc.showOpenDialog(myPanel) == JFileChooser.APPROVE_OPTION) {
                                try {
                                    // create relative path if within the current project
                                    String selectedFilePath = fc.getSelectedFile().getAbsolutePath();
                                    if (selectedFilePath.startsWith(_bluej.getCurrentPackage().getDir().getAbsolutePath())) {
                                        int i = _bluej.getCurrentPackage().getDir().getAbsolutePath().length();
                                        selectedFilePath = selectedFilePath.substring(i+1);                                        
                                        androidIcon.setText(selectedFilePath);
                                    } else {
                                        // Must keep file within the project directory
                                        JOptionPane.showMessageDialog(_bluej.getCurrentFrame(), "File must stay inside the project directory", "Alert", JOptionPane.ERROR_MESSAGE); 
                                    }
                                    
                                } catch (Exception ex) {
                                    Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (ProjectNotOpenException ex) {
                            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (PackageNotFoundException ex) {
                            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });
            }
        });

        //myPanel.add(firstLine);
        myPanel.add(secondLine);
        //myPanel.add(thirdLine);
        myPanel.add(fourthLine);
        myPanel.add(fifthLine);
        myPanel.add(sixthLine);
        myPanel.add(androidNameLine);
        myPanel.add(androidIconLine);

        // Load the default value
        loadValues();
    }

    public JPanel getPanel() {
        return myPanel;
    }

    public void saveValues() {
        // Save the preference value in the BlueJ properties file
        //bluej.setExtensionPropertyString(PROFILE_CAMERA_CONTROL, cameraControl.getSelectedItem().toString());
        bluej.setExtensionPropertyString(PROFILE_CAMERA_X, x.getText());
        bluej.setExtensionPropertyString(PROFILE_CAMERA_Y, y.getText());
        bluej.setExtensionPropertyString(PROFILE_CAMERA_Z, z.getText());

//        bluej.setExtensionPropertyString(PROFILE_ROOM_WIDTH, roomWidth.getText());
//        bluej.setExtensionPropertyString(PROFILE_ROOM_DEPTH, roomDepth.getText());
//        bluej.setExtensionPropertyString(PROFILE_ROOM_HEIGHT, roomHeight.getText());

        bluej.setExtensionPropertyString(PROFILE_SERVER_URL, serverUrl.getText());
        bluej.setExtensionPropertyString(PROFILE_DICT_SIZE, dictSize.getText());
        // be sure to add a trailing /
        if (!androidDir.getText().endsWith(System.getProperty("file.separator"))) {
            androidDir.setText(androidDir.getText() + System.getProperty("file.separator"));
        }
        bluej.setExtensionPropertyString(PROFILE_ANDROID_DIR, androidDir.getText());

        bluej.setExtensionPropertyString(PROFILE_ANDROID_NAME, androidName.getText());
        bluej.setExtensionPropertyString(PROFILE_ANDROID_ICON, androidIcon.getText());
    }

    public void loadValues() {
        // Load the property value from the BlueJ proerties file        
//        cameraControl.setSelectedItem(new Boolean(bluej.getExtensionPropertyString(PROFILE_CAMERA_CONTROL,"false")));
        x.setText(bluej.getExtensionPropertyString(PROFILE_CAMERA_X, "5"));
        y.setText(bluej.getExtensionPropertyString(PROFILE_CAMERA_Y, "5"));
        z.setText(bluej.getExtensionPropertyString(PROFILE_CAMERA_Z, "18"));

//        roomWidth.setText(bluej.getExtensionPropertyString(PROFILE_ROOM_WIDTH,"10"));
//        roomDepth.setText(bluej.getExtensionPropertyString(PROFILE_ROOM_DEPTH,"10"));
//        roomHeight.setText(bluej.getExtensionPropertyString(PROFILE_ROOM_HEIGHT,"10"));

        serverUrl.setText(bluej.getExtensionPropertyString(PROFILE_SERVER_URL, "http://env3d.org"));
        dictSize.setText(bluej.getExtensionPropertyString(PROFILE_DICT_SIZE, "23"));
        androidDir.setText(bluej.getExtensionPropertyString(PROFILE_ANDROID_DIR, ""));
        androidName.setText(bluej.getExtensionPropertyString(PROFILE_ANDROID_NAME, "Env3D"));
        androidIcon.setText(bluej.getExtensionPropertyString(PROFILE_ANDROID_ICON, "textures/doty.png"));
    }

    public float getCameraX() {
        return new Float(x.getText());
    }

    public float getCameraY() {
        return new Float(y.getText());
    }

    public float getCameraZ() {
        return new Float(z.getText());
    }

    public boolean getCameraControl() {
        return (Boolean) cameraControl.getSelectedItem();
    }

    public float getRoomWidth() {
        return new Float(roomWidth.getText());
    }

    public float getRoomDepth() {
        return new Float(roomDepth.getText());
    }

    public float getRoomHeight() {
        return new Float(roomHeight.getText());
    }

    public String getServerUrl() {
        return serverUrl.getText();
    }

    public int getDictSize() {
        return new Integer(dictSize.getText());
    }

    public String getAndroidDir() {
        return androidDir.getText();
    }

    public String getAndroidName() {
        return androidName.getText();
    }

    public String getAndroidIcon() {
        return androidIcon.getText();
    }
}
