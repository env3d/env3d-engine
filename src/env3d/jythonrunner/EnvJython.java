/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env3d.jythonrunner;

import env3d.advanced.EnvAdvanced;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author jmadar
 */
public class EnvJython extends EnvAdvanced {
    
    private ArrayList<GameObject> objectsList = new ArrayList<GameObject>();
    
    @Override
    public void addObject(Object obj) {
        if (obj instanceof GameObject) {
            ((GameObject)obj).setEnv(this);
        }
        super.addObject(obj);
    }

    public Collection<GameObject> getObjects(String className) {
        objectsList.clear();
        for (GameObject o : this.getObjects(GameObject.class)) {
            if (o.getClassName().equals(className)) objectsList.add(o);
        }
        return objectsList;
    }
    
    /**
     * Returns a single object with a particular class name
     * @param className
     * @return 
     */
    public GameObject getObject(String className) {
        for (GameObject o : this.getObjects(GameObject.class)) {
            if (o.getClassName().equals(className)) return (GameObject) o;
        }
        return null;
    }
        
}
