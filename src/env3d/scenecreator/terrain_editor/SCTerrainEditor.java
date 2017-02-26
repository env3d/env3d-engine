/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator.terrain_editor;

import env3d.scenecreator.Game;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import env3d.util.Sysutil;

/**
 *
 * @author jmadar
 */
public class SCTerrainEditor extends JDialog {

    private JSlider heightBar;
    private JSlider sizeBar;
    private int colorRGB;
    private HeightMapImage heightMap;
    private HeightMapImage roadMap;
    private Game game;
    
    public SCTerrainEditor(Game g) {
        this(g, g.getUI().getAssetDir());        
    }            
    
    public SCTerrainEditor(Game g, String baseDir) {      
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.game = g;
        setLayout(new BorderLayout());
        heightMap = new HeightMapImage(this, baseDir, "textures/terrain/flat.png");
        roadMap = new HeightMapImage(this, baseDir, "textures/terrain/alpha.png");
        
        heightBar = new JSlider(JSlider.VERTICAL);
        heightBar.setMinimum(0);
        heightBar.setMaximum(255);
        heightBar.setSize(30, 400);
        heightBar.setToolTipText("Adjust height");
        heightBar.setMajorTickSpacing(20);
        heightBar.setPaintLabels(true);
        
        sizeBar = new JSlider(JSlider.VERTICAL);
        sizeBar.setMinimum(1);
        sizeBar.setMaximum(100);
        sizeBar.setSize(30,80);
        sizeBar.setToolTipText("Adjust paint brush size");
        
        add(sizeBar, BorderLayout.EAST);

        JPanel heightMapPanel = new JPanel();
        heightMapPanel.setLayout(new BorderLayout());
        
        heightMapPanel.add(heightMap, BorderLayout.CENTER);
        heightMapPanel.add(heightBar, BorderLayout.EAST);
        
        JTabbedPane tabbedPane = new JTabbedPane();                        
        tabbedPane.add(heightMapPanel, "HeightMap");        
        
        JPanel roadMapPanel = new JPanel();
        roadMapPanel.setLayout(new BorderLayout());
        roadMapPanel.add(roadMap);                
        
        JPanel texturePanel = new JPanel();
        final JComboBox redChannel = new JComboBox(Sysutil.getFileStringArray(baseDir+"textures/floor", "<- red texture ->", new Sysutil.ImgFileFilter()));
        final JComboBox greenChannel = new JComboBox(Sysutil.getFileStringArray(baseDir+"textures/floor", "<- green texture ->", new Sysutil.ImgFileFilter()));
        final JComboBox blueChannel = new JComboBox(Sysutil.getFileStringArray(baseDir+"textures/floor", "<- blue texture ->", new Sysutil.ImgFileFilter()));
        ActionListener channelListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (game != null) {
                    String redTexture = redChannel.getSelectedIndex() > 0 ? "textures/floor/"+redChannel.getSelectedItem() : game.getEnvTerrain().getTexture();
                    String greenTexture = greenChannel.getSelectedIndex() > 0 ? "textures/floor/"+greenChannel.getSelectedItem() : game.getEnvTerrain().getTextureG();
                    String blueTexture = blueChannel.getSelectedIndex() > 0 ? "textures/floor/"+blueChannel.getSelectedItem() : game.getEnvTerrain().getTextureB();
                    game.setTerrainTexture(redTexture,greenTexture,blueTexture);
                    game.getUI().save(game.save());
                }
            }
        };
        redChannel.addActionListener(channelListener);
        greenChannel.addActionListener(channelListener);
        blueChannel.addActionListener(channelListener);
        
        texturePanel.add(redChannel);
        texturePanel.add(greenChannel);
        texturePanel.add(blueChannel);
        
        roadMapPanel.add(texturePanel, BorderLayout.SOUTH);        
        JRadioButton redButton = new JRadioButton("red", true);        
        redButton.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent ae) {
                colorRGB = 0;
            }
        });
        JRadioButton greenButton = new JRadioButton("Green");
        greenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                colorRGB = 1;
            }
        });
        JRadioButton blueButton = new JRadioButton("Blue");
        blueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                colorRGB = 2;
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(blueButton);
        group.add(redButton);
        group.add(greenButton);
        JPanel buttonPanel = new JPanel(new GridLayout(3,1));
        buttonPanel.add(redButton);
        buttonPanel.add(greenButton);
        buttonPanel.add(blueButton);
        
        roadMapPanel.add(buttonPanel, BorderLayout.EAST);
        
        tabbedPane.add(roadMapPanel, "TextureMap");
        
        add(tabbedPane, BorderLayout.CENTER);               

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                heightMap.save();
                roadMap.save();
                if (game != null) {  
                    game.getEnvTerrain().updateHeightMap("textures/terrain/flat.png");
                    game.getEnvTerrain().setTextureAlpha("textures/terrain/alpha.png");
                }
            }
        });
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(saveButton);
        add(topPanel, BorderLayout.NORTH);
                
        pack();
        
        setVisible(true);
    }
    
    public JSlider getHeightBar() {
        return heightBar;
    }
    
    public JSlider getSizeBar() {
        return sizeBar;
    }
    
    public int getColorRGB() {
        return colorRGB;
    }
    
    public static void main(String args[]) {
        new SCTerrainEditor(null, "");
    }        
}
