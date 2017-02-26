package env3d.android;

import java.util.Collection;
import java.util.HashMap;

public class EnvRoom {
	private HashMap<String,EnvObject> wallMap = new HashMap<String, EnvObject>();	
	private double width = 10, height = 10, depth = 10;
	
	public EnvRoom() {
		// Default values
		wallMap.put("north", new EnvObject());
		wallMap.put("east", new EnvObject());
		wallMap.put("south", new EnvObject());
		wallMap.put("west", new EnvObject());
		wallMap.put("top", new EnvObject());
		wallMap.put("bottom", new EnvObject());
		
		for (String wall : wallMap.keySet()) {
			// only allow square rooms
			wallMap.get(wall).setScale(depth);
			wallMap.get(wall).setModel("models/square.obj");
			if (wall.equals("bottom")) 
				wallMap.get(wall).setTexture("textures/floor.png");
			else 
				wallMap.get(wall).setTexture("textures/fence0.png");
		}
		wallMap.get("north").setRotateX(90);
		wallMap.get("north").setY(wallMap.get("north").getScale());
		
		wallMap.get("east").setRotateX(90);
		wallMap.get("east").setRotateY(-90);
		wallMap.get("east").setX(wallMap.get("east").getScale());		
		wallMap.get("east").setY(wallMap.get("east").getScale());

		wallMap.get("west").setRotateX(90);
		wallMap.get("west").setRotateY(90);
		wallMap.get("west").setZ(wallMap.get("west").getScale());		
		wallMap.get("west").setY(wallMap.get("west").getScale());
		
	}
	
	public String getTextureNorth() {
		return wallMap.get("north").getTexture();
	}
	public void setTextureNorth(String textureNorth) {
		wallMap.put("north", new EnvObject());
		wallMap.get("north").setModel("models/square.obj");
		wallMap.get("north").setTexture(textureNorth);		
	}
	public String getTextureEast() {
		return wallMap.get("east").getTexture();
	}
	public void setTextureEast(String textureEast) {
		wallMap.put("east", new EnvObject());
		wallMap.get("east").setModel("models/square.obj");
		wallMap.get("east").setTexture(textureEast);		
	}
	public String getTextureSouth() {
		return wallMap.get("south").getTexture();
	}
	public void setTextureSouth(String textureSouth) {
		wallMap.put("south", new EnvObject());
		wallMap.get("south").setModel("models/square.obj");
		wallMap.get("south").setTexture(textureSouth);		
	}
	public String getTextureWest() {
		return wallMap.get("west").getTexture();
	}
	public void setTextureWest(String textureWest) {
		wallMap.put("west", new EnvObject());
		wallMap.get("west").setModel("models/square.obj");
		wallMap.get("west").setTexture(textureWest);		
	}
	public String getTextureTop() {
		return wallMap.get("top").getTexture();
	}
	public void setTextureTop(String textureTop) {
		wallMap.put("top", new EnvObject());
		wallMap.get("top").setModel("models/square.obj");
		wallMap.get("top").setTexture(textureTop);		
	}
	public String getTextureBottom() {
		return wallMap.get("bottom").getTexture();
	}
	public void setTextureBottom(String textureBottom) {
		wallMap.put("bottom", new EnvObject());
		wallMap.get("bottom").setModel("models/square.obj");
		wallMap.get("bottom").setTexture(textureBottom);		
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getDepth() {
		return depth;
	}
	public void setDepth(double depth) {
		this.depth = depth;
	}
	public Collection<EnvObject> getObjects() {			

		// Set properly scales and rotation
		for (String wall : wallMap.keySet()) {
			// only allow square rooms
			wallMap.get(wall).setScale(depth);
		}
		wallMap.get("north").setRotateX(90);
		wallMap.get("north").setY(wallMap.get("north").getScale());
		
		wallMap.get("east").setRotateX(90);
		wallMap.get("east").setRotateY(-90);
		wallMap.get("east").setX(wallMap.get("east").getScale());		
		wallMap.get("east").setY(wallMap.get("east").getScale());

		wallMap.get("west").setRotateX(90);
		wallMap.get("west").setRotateY(90);
		wallMap.get("west").setZ(wallMap.get("west").getScale());		
		wallMap.get("west").setY(wallMap.get("west").getScale());
		
		wallMap.get("south").setRotateX(-90);
		wallMap.get("south").setZ(wallMap.get("south").getScale());
		
		wallMap.get("top").setRotateX(180);
		wallMap.get("top").setZ(wallMap.get("top").getScale());
		wallMap.get("top").setY(wallMap.get("top").getScale());
		
		
		return wallMap.values();
	}
}
