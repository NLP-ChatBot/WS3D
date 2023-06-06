package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import memory.CreatureInnerSense;
import ws3dproxy.model.Thing;

public class ClosestJewelDetector extends Codelet {
  private MemoryObject knownMO;
  private MemoryObject closestJewelMO;
  private MemoryObject innerSenseMO;

  private List<Thing> known;

  public ClosestJewelDetector() {}

  @Override
  public void accessMemoryObjects() {
    this.knownMO = (MemoryObject) this.getInput("KNOWN_JEWELS");
    this.innerSenseMO = (MemoryObject) this.getInput("INNER");
    this.closestJewelMO = (MemoryObject) this.getOutput("CLOSEST_JEWEL");
  }

  @Override
  public void proc() {
    Thing closest_jewel = null;
    known = Collections.synchronizedList((List<Thing>) knownMO.getI());
    CreatureInnerSense cis = (CreatureInnerSense) innerSenseMO.getI();
    synchronized (known) {
      if (known.size() != 0) {
        CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
        for (Thing t : myknown) {
          String objectName = t.getName();
          // Check if the thing is a jewel
          if (objectName.contains("Jewel")) {
            if (closest_jewel == null) {
              closest_jewel = t;
            } else {
              double Dnew = calculateDistance(
                t.getX1(),
                t.getY1(),
                cis.position.getX(),
                cis.position.getY()
              );
              double Dclosest = calculateDistance(
                closest_jewel.getX1(),
                closest_jewel.getY1(),
                cis.position.getX(),
                cis.position.getY()
              );
              if (Dnew < Dclosest) {
                closest_jewel = t;
              }
            }
          }
        }

        if (closest_jewel != null) {
          if (
            closestJewelMO.getI() == null ||
            !closestJewelMO.getI().equals(closest_jewel)
          ) {
            closestJewelMO.setI(closest_jewel);
          }
        } else {
          closest_jewel = null;
          closestJewelMO.setI(closest_jewel);
        }
      } else {
        closest_jewel = null;
        closestJewelMO.setI(closest_jewel);
      }
    }
  } //end proc

  @Override
  public void calculateActivation() {}

  private double calculateDistance(double x1, double y1, double x2, double y2) {
    return (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
  }
}
