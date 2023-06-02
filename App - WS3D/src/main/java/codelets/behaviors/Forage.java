
package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Thing;

/** 
 * 
 * @author klaus
 * 
 * 
 */

public class Forage extends Codelet {
    
        private MemoryObject knownMO;
        private List<Thing> known;
        private MemoryObject legsMO;
        


	/**
	 * Default constructor
	 */
	public Forage(){       
	}

	@Override
	public void proc() {
            known = (List<Thing>) knownMO.getI();
            if (known.size() == 0) {
		JSONObject message=new JSONObject();
			try {
				message.put("ACTION", "FORAGE");
				legsMO.setI(message.toString());
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
            }

            legsMO.setEvaluation(this.getActivation());
	}

	@Override
	public void accessMemoryObjects() {
            knownMO = (MemoryObject)this.getInput("KNOWN_APPLES");
            legsMO=(MemoryObject)this.getOutput("FORAGE_LEGS_MO");
	}
        
        @Override
        public void calculateActivation() {
            try {
                known = (List<Thing>) knownMO.getI();
                if (known.size() == 0) {
                    this.setActivation(0.66d);
                } else {
                    this.setActivation(0d);
                }
            } catch (CodeletActivationBoundsException ex) {
                Logger.getLogger(Forage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


}
