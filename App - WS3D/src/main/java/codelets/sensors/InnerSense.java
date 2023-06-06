package codelets.sensors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import memory.CreatureInnerSense;
import ws3dproxy.model.Creature;

public class InnerSense extends Codelet {
  private MemoryObject innerSenseMO;
  private Creature c;
  private CreatureInnerSense cis;

  public InnerSense(Creature nc) {
    c = nc;
  }

  @Override
  public void accessMemoryObjects() {
    innerSenseMO = (MemoryObject) this.getOutput("INNER");
    cis = (CreatureInnerSense) innerSenseMO.getI();
  }

  public void proc() {
    cis.position = c.getPosition();
    cis.pitch = c.getPitch();
    cis.fuel = c.getFuel();
    cis.FOV = c.getFOV();
  }

  @Override
  public void calculateActivation() {}
}
