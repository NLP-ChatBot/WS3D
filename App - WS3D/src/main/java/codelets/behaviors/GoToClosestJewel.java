package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import memory.CreatureInnerSense;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Thing;

public class GoToClosestJewel extends Codelet {
  private MemoryObject closestJewelMO;
  private MemoryObject selfInfoMO;
  private MemoryObject legsMO;

  private int creatureBasicSpeed;
  private double reachDistance;

  public GoToClosestJewel(int creatureBasicSpeed, int reachDistance) {
    this.creatureBasicSpeed = creatureBasicSpeed;
    this.reachDistance = reachDistance;
  }

  @Override
  public void accessMemoryObjects() {
    closestJewelMO = (MemoryObject) this.getInput("CLOSEST_JEWEL");
    selfInfoMO = (MemoryObject) this.getInput("INNER");
    legsMO = (MemoryObject) this.getOutput("GO_TO_JEWEL_LEGS_MO");
  }

  @Override
  public void proc() {
    Thing closestJewel = (Thing) closestJewelMO.getI();
    CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();

    if (closestJewel != null) {
      double jewelX = closestJewel.getX1();
      double jewelY = closestJewel.getY1();

      double selfX = cis.position.getX();
      double selfY = cis.position.getY();

      Point2D pJewel = new Point();
      pJewel.setLocation(jewelX, jewelY);

      Point2D pSelf = new Point();
      pSelf.setLocation(selfX, selfY);

      double distance = pSelf.distance(pJewel);
      JSONObject message = new JSONObject();
      try {
        if (distance > reachDistance) { //Go to it
          message.put("ACTION", "GOTO");
          message.put("X", (int) jewelX);
          message.put("Y", (int) jewelY);
          message.put("SPEED", creatureBasicSpeed);
        } else { //Stop
          message.put("ACTION", "GOTO");
          message.put("X", (int) jewelX);
          message.put("Y", (int) jewelY);
          message.put("SPEED", 0.0);
        }
        legsMO.setI(message.toString());
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    legsMO.setEvaluation(this.getActivation());
  } //end proc

  @Override
  public void calculateActivation() {
    try {
      Thing closestJewel = (Thing) closestJewelMO.getI();
      CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
      double distance = cis.position.distance(
        closestJewel.getX1(),
        closestJewel.getY1()
      );

      if (distance <= reachDistance && cis.fuel > 400) {
        this.setActivation(0.8d);
      } else {
        this.setActivation(0d);
      }
    } catch (CodeletActivationBoundsException ex) {
      Logger
        .getLogger(GoToClosestJewel.class.getName())
        .log(Level.SEVERE, null, ex);
    }
  }
}
