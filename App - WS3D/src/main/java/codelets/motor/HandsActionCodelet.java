package codelets.motor;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Creature;

public class HandsActionCodelet extends Codelet {
  private Memory handsMC;
  private String previousHandsAction = "";
  private Creature c;
  static Logger log = Logger.getLogger(
    HandsActionCodelet.class.getCanonicalName()
  );

  public HandsActionCodelet(Creature nc) {
    c = nc;
  }

  @Override
  public void accessMemoryObjects() {
    handsMC = this.getInput("HANDS_MC");
  }

  public void proc() {
    String command = (String) handsMC.getI();

    if (!command.equals("") && (!command.equals(previousHandsAction))) {
      JSONObject jsonAction;
      try {
        jsonAction = new JSONObject(command);
        if (jsonAction.has("ACTION") && jsonAction.has("OBJECT")) {
          String action = jsonAction.getString("ACTION");
          String objectName = jsonAction.getString("OBJECT");
          if (action.equals("PICKUP")) {
            try {
              c.putInSack(objectName);
            } catch (Exception e) {
              e.printStackTrace();
            }
            log.info(
              "Sending Put In Sack command to agent:****** " +
              objectName +
              "**********"
            );
          }
          if (action.equals("EATIT")) {
            try {
              c.eatIt(objectName);
            } catch (Exception e) {
              e.printStackTrace();
            }
            log.info(
              "Sending Eat command to agent:****** " + objectName + "**********"
            );
          }
          if (action.equals("BURY")) {
            try {
              c.hideIt(objectName);
            } catch (Exception e) {}
            log.info(
              "Sending Bury command to agent:****** " +
              objectName +
              "**********"
            );
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    previousHandsAction = (String) handsMC.getI();
  } //end proc

  @Override
  public void calculateActivation() {}
}
