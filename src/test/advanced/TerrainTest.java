/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.advanced;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvTerrain;
import env3d.advanced.EnvSkyRoom;
import env3d.Env;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class TerrainTest {


    public void play() {
        Env env = new EnvAdvanced();

        // All the textures required by the skybox
        //String textureDir = "/Users/jmadar/Downloads/sky13/";
//        String textureDir = "/Users/jmadar/Downloads/sky7/";

//        String textureDir = "textures/skybox/";
//        String north = textureDir+"north.png";
//        String east = textureDir+"east.png";
//        String south = textureDir+"south.png";
//        String west = textureDir+"west.png";
//        String top = textureDir+"top.png";
//        String bottom = textureDir+"bottom.png";

        // Create a the sky box.  The first 3 parameters specifies how far
        // the sky box extends in the x, y, and z direction.  This is for both
        // positive and negative direction on each axis.  The next 6 parameters
        // specifies the texture for each direction.
        //
        // Here, we create a skybox of size 256x256x256.  By default, the skybox's
        // center point is located at 0, 0, 0.
//        EnvSkyRoom skyroom = new EnvSkyRoom(north, east, south, west, top, bottom);
        EnvSkyRoom skyroom = new EnvSkyRoom("http://compscistu.capilanou.ca/~jmadar/skybox/sky20");

        // Move the skybox to the middle of the room
        skyroom.setXYZ(128, 128, 128);

        // EnvSkyRoom is a room, so we use env.setRoom() to set make it visible in our
        // environment.
        env.setRoom(skyroom);

        // Use an image to provide height data, this image is 256x256
        EnvTerrain terrain = new EnvTerrain("textures/terrain/termap1.png");
        // Terrain needs a texture
        //terrain.setTexture("textures/mud.png");
        terrain.setTexture("textures/mud.png");

        // Flatten the hills and valleys
        terrain.setScale(1, 0.2, 1);

        // Add the terrain object to the environemnt.
        env.addObject(terrain);

        // Start in the middle of the terrain
        env.setCameraXYZ(60, 0, 60);
        double height = terrain.getHeight(env.getCameraX(), env.getCameraZ());
        env.setCameraXYZ(env.getCameraX(), height+2, env.getCameraZ());
        // Exit when the escape key is pressed
        while (env.getKey() != 1) {
            // Get the height of the camera
            height = terrain.getHeight(env.getCameraX(), env.getCameraZ());
            System.out.println(terrain.getNormal(env.getCameraX(), env.getCameraZ()));
            // Make sure that the height is valid (within the terrain boundary)
            if (!Double.isNaN(height)) {
                // Make the y value a little higher than the terrain, to create the
                // illusion that we are "walking" on the surface.
                //env.setCameraXYZ(env.getCameraX(), height+2, env.getCameraZ());
            }
            if (env.getKey() == Keyboard.KEY_0) {
                terrain.setTexture("textures/stone.png");
            }

            env.advanceOneFrame();
        }
        System.exit(0);
    }

    public static void main(String [] args) {
        (new TerrainTest()).play();
    }

}
