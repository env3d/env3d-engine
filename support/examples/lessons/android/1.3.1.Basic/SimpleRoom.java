/**
 * A very simple room with the south wall removed
 */
public class SimpleRoom {

    private double width;
    private double depth;
    private double height;
    
    // The background color of the entire scene can be controlled
    private double bgRed, bgGreen, bgBlue;

    // By setting the textureSouth field to null, the south wall is removed.
    // Other fields include textureNorth, textureEast, textureWest, textureTop, and textureBottom
    private String textureSouth = null;

    /**
     * The default constuctor, create a room of size 10x10x10
     */
    public SimpleRoom() {
        width = 10;
        depth = 10;
        height = 10;
    }

    /**
     * Allow the rooms of different sizes to be created
     */
    public SimpleRoom(double w, double d, double h) {
        width = w;
        depth = d;
        height = h;
    }
}
