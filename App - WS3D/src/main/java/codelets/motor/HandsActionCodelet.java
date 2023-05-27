
package codelets.motor;


import org.json.JSONException;
import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Random;
import java.util.logging.Logger;
import ws3dproxy.model.Creature;

/**
 *  Hands Action Codelet monitors working storage for instructions and acts on the World accordingly.
 *  
 * @author klaus
 *
 */


public class HandsActionCodelet extends Codelet{

	private MemoryObject handsMO;
	private String previousHandsAction="";
        private Creature c;
        private Random r = new Random();
        static Logger log = Logger.getLogger(HandsActionCodelet.class.getCanonicalName());

	public HandsActionCodelet(Creature nc) {
                c = nc;
	}
	
        @Override
	public void accessMemoryObjects() {
		handsMO=(MemoryObject)this.getInput("HANDS");
	}
	public void proc() {
            
                String command = (String) handsMO.getI();

		if(!command.equals("") && (!command.equals(previousHandsAction))){
			JSONObject jsonAction;
			try {
				jsonAction = new JSONObject(command);
				if(jsonAction.has("ACTION") && jsonAction.has("OBJECT")){
					String action=jsonAction.getString("ACTION");
					String objectName=jsonAction.getString("OBJECT");
					if(action.equals("PICKUP")){
                                                try {
                                                 c.putInSack(objectName);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } 
						log.info("Sending Put In Sack command to agent:****** "+objectName+"**********");							
					}
					if(action.equals("EATIT")){
                                                try {
                                                 c.eatIt(objectName);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
						log.info("Sending Eat command to agent:****** "+objectName+"**********");							
					}
					if(action.equals("BURY")){
                                                try {
                                                 c.hideIt(objectName);
                                                } catch (Exception e) {
                                                    
                                                }
						log.info("Sending Bury command to agent:****** "+objectName+"**********");							
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		previousHandsAction = (String) handsMO.getI();
	}//end proc

    @Override
    public void calculateActivation() {
        
    }


}
