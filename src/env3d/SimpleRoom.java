/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package env3d;

/**
 *
 * @author jmadar
 */
public class SimpleRoom {
    private double width = 10;
    private double depth = 10;
    private double height = 10;

    private String textureSouth = null;
//    private String textureNorth = null;
//    private String textureEast = null;
//    private String textureWest = null;
//    private String textureTop = null;
//    private String textureBottom = null;

    public SimpleRoom() {

    }

    public SimpleRoom(double w, double d, double h) {
        width = w;
        depth = d;
        height = h;
    }
}
