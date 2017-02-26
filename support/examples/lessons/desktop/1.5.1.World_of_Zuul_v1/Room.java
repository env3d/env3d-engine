import java.util.ArrayList;
 
/**
 * The Room class represents a generic room.  
 * The dimensions of the room is flexible
 * 
 */
public class Room
{
    // instance variables - replace the example below with your own
    private double width, height, depth;
    private String textureNorth, textureSouth, textureEast, textureWest;
    private Room exitEast, exitWest;
         
     
    /**
     * Constructor for objects of class Room.  Allows the 
     * caller to create rooms of different dimensions.
     */
    public Room(double w, double h, double d)
    {
        width = w;
        height = h;
        depth = d;
         
        textureNorth = "textures/fence0.png"; 
        textureEast = "textures/fence0.png"; 
        textureSouth = "textures/fence0.png"; 
        textureWest =  "textures/fence0.png"; 
         
    }
     
    /**
     * Sets the north texture of the room
     * 
     * @param fileName name of the texture file
     */
    public void setTextureNorth(String fileName)
    {
        textureNorth = fileName;
    }
 
    /**
     * Sets the east texture of the room
     * 
     * @param fileName name of the texture file
     */
    public void setTextureEast(String fileName)
    {
        textureEast = fileName;
    }
 
    /**
     * Sets the south texture of the room
     * @param fileName name of the texture file
     */
    public void setTextureSouth(String fileName)
    {
        textureSouth = fileName;
    }
 
    /**
     * Sets the west texture of the room
     * @param fileName name of the texture file
     */
    public void setTextureWest(String fileName)
    {
        textureWest = fileName;
    }
     
    /**
     * Every object of the room class can have 2 exits.
     * Each exit is actually a pointer to another room.
     * We can essentially create a long corridor.
     * 
     * @param west The west exit 
     * @param east The east exit
     */
    public void setExits(Room west, Room east)
    {
        exitEast = east;
        exitWest = west;
    }
     
    /**
     * Returns the east exit room to the caller
     */
    public Room getEastExit()
    {
        return exitEast;
    }
     
    /**
     * Returns the west exit room to the caller
     */
    public Room getWestExit()
    {
        return exitWest;        
    }
 
    /**
     * Returns the width of the room
     */
    public double getWidth()
    {
        return width;
    }
     
    /**
     * Returns the depth of the room
     */
    public double getDepth()
    {
        return depth;
    }   
}