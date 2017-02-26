/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.model;

/**
 *
 * @author jmadar
 */
public class Doty {
    private double x=5, y=0, z=5;
    private double scale = 1;
    private double rotateX, rotateY, rotateZ;
    //private String model = "/Users/jmadar/Downloads/spider/Sphere.mesh.xml";
    //private String model = "/Users/jmadar/Documents/models/orca/Cube.mesh.xml";
    //private String texture = "/Users/jmadar/Documents/models/beast/beast1.png";
    //private String model = "/Users/jmadar/Documents/models/zombie/meHumanMale.001.mesh.xml";
    //private String model = "/Users/jmadar/Documents/models/beast/MilkShape3D Mesh.mesh.xml";
    //private String model = "/Users/jmadar/Downloads/hammer/Cube.003.mesh.xml";
    //private String model = "/Users/jmadar/Documents/models/orca/orca.blend";
//    private String model = "/Users/jmadar/Downloads/big_wine_barrel_0/bigwinebarrel.blend";
//    private String texture = "/Users/jmadar/Downloads/big_wine_barrel_0/big_wine_barrel.png";
//    private String model = "/Users/jmadar/Downloads/cimpletoon/horseman/horseman02.obj";
//    private String texture = "/Users/jmadar/Downloads/cimpletoon/horseman/ROUPA-DM.png";
    private String model = "/Users/jmadar/Documents/vandrico/soccer/models/girl/girl.blend";
    private String texture = "/Users/jmadar/Documents/vandrico/soccer/models/girl/textures/girl.png";
    //private String texture = "/Users/jmadar/Documents/models/Mech/Mech4camo.png";
   
    
    //private String model = "sphere";
    //private String action = "stand";
    //private String repeat = "none";
    
    private int t;

    public void stand() {
        //action = "walk";
        //repeat = "cycle";
    }
    
    public void walk() {
        //action = "bite1";
        //repeat = "loop";
    }

    public void spin() {
        rotateY++;
    }

    public void move() {
        rotateY = rotateY + 1;
        x =  Math.sin(Math.toRadians(t++))+5;
    }
}
