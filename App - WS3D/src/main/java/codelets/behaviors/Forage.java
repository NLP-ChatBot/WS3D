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

public class Forage extends Codelet {
<<<<<<< Updated upstream
    
        private MemoryObject knownMO;
        private List<Thing> known;
        private MemoryObject legsMO;
        
=======
  private MemoryObject knownMO;
  private List<Thing> known;
  private MemoryObject legsMO;
>>>>>>> Stashed changes

  /**
   * Default constructor
   */
  public Forage() {}

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

  @Override
  public void accessMemoryObjects() {
    knownMO = (MemoryObject) this.getInput("KNOWN_APPLES");
    legsMO = (MemoryObject) this.getOutput("LEGS");
  }

  @Override
  public void calculateActivation() {}
}
