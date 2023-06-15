package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import memory.CreatureInnerSense;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Thing;

public class PickupClosestJewel extends Codelet {
  private MemoryObject closestJewelMO;
  private MemoryObject innerSenseMO;
  private MemoryObject knownMO;
  private int reachDistance;
  private MemoryObject handsMO;
  Thing closestJewel;
  CreatureInnerSense cis;
  List<Thing> known;

  public PickupClosestJewel(int reachDistance) {
    setTimeStep(50);
    this.reachDistance = reachDistance;
  }

  @Override
  public void accessMemoryObjects() {
    closestJewelMO = (MemoryObject) this.getInput("CLOSEST_JEWEL");
    innerSenseMO = (MemoryObject) this.getInput("INNER");
    handsMO = (MemoryObject) this.getOutput("PICKUP_JEWEL_HANDS_MO");
    knownMO = (MemoryObject) this.getOutput("KNOWN_JEWELS");
  }

  @Override
  public void proc() {
    String jewelName = "";
    closestJewel = (Thing) closestJewelMO.getI();
    cis = (CreatureInnerSense) innerSenseMO.getI();
    known = (List<Thing>) knownMO.getI();

    if (closestJewel != null) {
      double jewelX = closestJewel.getX1();
      double jewelY = closestJewel.getY1();
      jewelName = closestJewel.getName();

      double selfX = cis.position.getX();
      double selfY = cis.position.getY();

      Point2D pJewel = new Point();
      pJewel.setLocation(jewelX, jewelY);

      Point2D pSelf = new Point();
      pSelf.setLocation(selfX, selfY);

      double distance = pSelf.distance(pJewel);
      JSONObject message = new JSONObject();
      try {
        if (distance < reachDistance) { // Pickup it
          message.put("OBJECT", jewelName);
          message.put("ACTION", "SACKIT");
          handsMO.setI(message.toString());
          DestroyClosestJewel(); // Consumir a jóia
        } else {
          handsMO.setI(""); // nothing
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } else {
      handsMO.setI(""); // nothing
    }
  }

  public void DestroyClosestJewel() {
    int r = -1;
    int i = 0;
    synchronized (known) {
      CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
      for (Thing t : myknown) {
        if (
          closestJewel != null && t.getName().equals(closestJewel.getName())
        ) {
          r = i;
        }
        i++;
      }
      if (r != -1) {
        known.remove(r);
        closestJewel = null;
        closestJewelMO.setI(closestJewel);
      }
    }
  }

  @Override
  public void calculateActivation() {}
}
