import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;

public class Environment {
  public String host = "localhost";
  public int port = 4011;
  public String robotID = "r0";
  public Creature c = null;

  public Environment() {
    WS3DProxy proxy = new WS3DProxy();
    try {
      World w = World.getInstance();
      w.reset();
      World.createJewel(0, 350, 75);
      World.createJewel(0, 100, 220);
      World.createJewel(0, 250, 210);
      c = proxy.createCreature(100, 450, 0);
      c.start();
    } catch (CommandExecException e) {}
    System.out.println("Robot " + c.getName() + " is ready to go.");
  }
}
