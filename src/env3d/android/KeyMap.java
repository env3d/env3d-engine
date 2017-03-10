package env3d.android;

/**
 * This class is only used within this package.
 * 
 * Encapsulates a single key->location mapping of the on-screen keyboard
 * 
 * @author jmadar
 * @deprecated 
 */
class KeyMap {

    public String keyCode;
    public double minX, maxX, minY, maxY;
    public boolean down;
    public int pointerId = -1;

    public KeyMap(String key, double x1, double y1, double x2, double y2) {
        keyCode = key;
        minX = x1;
        maxX = x2;
        // Flip the coordinates because of jME's coordinate system
        minY = 1-y2;
        maxY = 1-y1;
    }

    /**
     * Returns true if the coordinate is within the button
     * @param touchX
     * @param touchY
     * @return 
     */
    public boolean pressed(double x, double y) {
        return (x > minX && x < maxX && y > minY && y < maxY);
    }
}
