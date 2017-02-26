/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.basic;

/**
 *
 * @author jmadar
 */
public class Doty {
    public double x=0, y=1, z=5;
    private double rotateX, rotateY, rotateZ;
    //private String model = "sphere";
   // private String texture = "textures/doty.png";
    private String model = "models/tux/tux.obj";
    private String texture = "textures/red.png";
    private boolean transparent = true;
//    private String texture = "models/tux/tux.png";
    private int t;

    public void spin() {
        rotateY++;
    }

    public void move() {
        x =  Math.sin(Math.toRadians(t++))+5;
//        if (t > 120) {
//            texture = "textures/doty_happy.png";
//        }
    }
}
