/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator.terrain_editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author jmadar
 */
public class HeightMapImage extends Component {

    private String imageFile;
    private BufferedImage image;
    private Graphics g;
    private SCTerrainEditor editor;
    private int mouseX, mouseY;
    private boolean hideCursor = false;
    private String baseDir;

    public HeightMapImage(SCTerrainEditor _etr, String baseDir, String imageFile) {
        this.editor = _etr;
        this.baseDir = baseDir;
        this.imageFile = imageFile;

        try {
            image = ImageIO.read(new File(baseDir + imageFile));
        } catch (IOException e) {
            image = null;
        }


        setSize(image.getWidth(), image.getHeight());
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

        final Graphics2D _scratchGraphics = image.createGraphics();

        final HeightMapImage _image = this;

        addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent me) {
            }

            public void mousePressed(MouseEvent me) {
                int height = editor.getHeightBar().getValue();
                
                int size = editor.getSizeBar().getValue();
                if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
                    _scratchGraphics.setColor(new Color(height, height, height));
                } else {
                    int r = 0, g = 0, b = 0;
                    switch (editor.getColorRGB()) {
                        case 0: 
                            r = 255;
                            break;
                        case 1:
                            g = 255;
                            break;
                        case 2:
                            b = 255;
                            break;                            
                    }
                    _scratchGraphics.setColor(new Color(r, g, b));
                }
                _scratchGraphics.fillOval(me.getX()-size/2, me.getY()-size/2, size, size);
                _image.repaint();
                hideCursor = true;
            }

            public void mouseReleased(MouseEvent me) {
                hideCursor = false;
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent me) {
                int height = editor.getHeightBar().getValue();
                int size = editor.getSizeBar().getValue();
                if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
                    _scratchGraphics.setColor(new Color(height, height, height));
                } else {
                    int r = 0, g = 0, b = 0;
                    switch (editor.getColorRGB()) {
                        case 0: 
                            r = 255;
                            break;
                        case 1:
                            g = 255;
                            break;
                        case 2:
                            b = 255;
                            break;                            
                    }
                    _scratchGraphics.setColor(new Color(r, g, b));
                }
                _scratchGraphics.fillOval(me.getX()-size/2, me.getY()-size/2, size, size);
                _image.repaint();
            }

            public void mouseMoved(MouseEvent me) {
                mouseX = me.getX();
                mouseY = me.getY();
                _image.repaint();
                //image.getGraphics().drawOval(me.getX(), me.getY(), 10, 10);        
                //_image.repaint();                                
            }
        });
    }

    @Override
    public void paint(Graphics grphcs) {
        //super.paint(grphcs);
        // Draw the image
        grphcs.drawImage(image, 0, 0, null);
        // Draw the cursor
        //grphcs.drawImage(scratchImage, 0, 0, null);
        if (!hideCursor) {
            int size = editor.getSizeBar().getValue();
            grphcs.setColor(Color.white);
            grphcs.drawOval(mouseX-size/2, mouseY-size/2, size, size);
        }
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }
    
    public BufferedImage getImage() {
        return image;
    }

    // Save the image
    public void save() {
        try {
            ImageIO.write(image, "png", new File(baseDir + imageFile));
        } catch (IOException ex) {
            Logger.getLogger(HeightMapImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
