/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator;

import env3d.scenecreator.terrain_editor.SCTerrainEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author jmadar
 */
public class ContextMenu extends JPopupMenu {

    private Game game;
    
    public ContextMenu(Game g) {      
        game = g;
        
        final ContextMenu thisMenu = this;
        
        //setLocation(getMousePosition());
        add(new JMenuItem("Edit world.txt") {
            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        thisMenu.setVisible(false);
                        SCTextEditor editor = new SCTextEditor(game.getUI().getFrame(), game.getUI().getAssetDir(), "world.txt", true);
                        //game.getUI().save(editor.getContent());
                        editor.save();
                        game.getUI().load();
                    }
                });
            }           
            
        });
        
        add(new JMenuItem("Edit terrain") {
            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        // Load up the terrain editor
                        thisMenu.setVisible(false);                        
                        SCTerrainEditor editor = new SCTerrainEditor(game);
                        //game.getUI().save(game.save());
                        //game.getUI().load();
                    }
                });
            }
        });
    }
    
}
