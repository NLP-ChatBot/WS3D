
package memory;

import java.awt.Polygon;
import ws3dproxy.model.WorldPoint;

public class CreatureInnerSense {
    public WorldPoint position;
    public double pitch;
    public double fuel;
    public Polygon FOV;
    
    public String toString() {
        if (position != null)
            return("Position: ("+(int)position.getX()+","+(int)position.getY()+") Pitch: "+(int)pitch+" Fuel: "+fuel);
        else 
            return("Position: null,null"+" Pitch: "+pitch+" Fuel: "+fuel);
    }
}

