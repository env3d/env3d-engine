/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.scenecreator;

/**
 *
 * @author jmadar
 */
public class PythonSceneEditor {
    public static void main(String args[]) {
        UI ui = new UI(UI.Mode.PYTHON);
        //UI ui = new UI(Mode.SCENE_CREATOR);
        ui.setAssetDir(System.getProperty("user.dir")+"/");
        System.out.println("Using directory "+ui.getAssetDir());
        ui.start();
    }    
}
