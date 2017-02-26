public class Room
{
    private double width, depth, height;
    private String textureTop,  textureBottom, textureEast, textureWest, textureSouth, textureNorth;
      
    /**
     * A room with just marble floor and no walls.
     */
    public Room()
    {
        width = 50;
        depth = 50;
        height = 50;
        textureBottom = "textures/marble.png";
    }
}