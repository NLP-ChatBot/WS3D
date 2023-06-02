
package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import memory.CreatureInnerSense;
import ws3dproxy.model.Thing;

public class GoToClosestApple extends Codelet {

	private MemoryObject closestAppleMO;
	private MemoryObject selfInfoMO;
	private MemoryObject legsMO;
        
	private int creatureBasicSpeed;
	private double reachDistance;

	public GoToClosestApple(int creatureBasicSpeed, int reachDistance) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
                
                
		closestAppleMO=(MemoryObject)this.getInput("CLOSEST_APPLE");
		selfInfoMO=(MemoryObject)this.getInput("INNER");
		legsMO=(MemoryObject)this.getOutput("GO_TO_APPLE_LEGS_MO");
	}

	@Override
	public void proc() {
		// Find distance between creature and closest apple
		//If far, go towards it
		//If close, stops

                Thing closestApple = (Thing) closestAppleMO.getI();
                CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();

		if(closestApple != null)
		{
			double appleX=0;
			double appleY=0;
			try {
                                appleX = closestApple.getX1();
                                appleY = closestApple.getY1();

			} catch (Exception e) {
				e.printStackTrace();
			}

			double selfX=cis.position.getX();
			double selfY=cis.position.getY();

			Point2D pApple = new Point();
			pApple.setLocation(appleX, appleY);

			Point2D pSelf = new Point();
			pSelf.setLocation(selfX, selfY);

			double distance = pSelf.distance(pApple);
			JSONObject message=new JSONObject();
			try {
				if(distance>reachDistance){ //Go to it
                                        message.put("ACTION", "GOTO");
					message.put("X", (int)appleX);
					message.put("Y", (int)appleY);
                                        message.put("SPEED", creatureBasicSpeed);	

				}else{//Stop
					message.put("ACTION", "GOTO");
					message.put("X", (int)appleX);
					message.put("Y", (int)appleY);
                                        message.put("SPEED", 0.0);	
				}
				legsMO.setsI(message.toString());
                                
                            
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}
                
                legsMO.setEvaluation(this.getActivation());
	}//end proc
        
        @Override
        public void calculateActivation() {
            try {
                CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
                
                this.setActivation(1 - cis.fuel/1000);
//                if(cis.fuel < 700)
//                    this.setActivation(0.999d);
//                else
//                    this.setActivation(0);
            } catch (CodeletActivationBoundsException ex) {
                Logger.getLogger(GoToClosestApple.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
        }

}
