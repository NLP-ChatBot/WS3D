import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import codelets.behaviors.EatClosestApple;
import codelets.behaviors.Forage;
import codelets.behaviors.GoToClosestApple;
import codelets.behaviors.GoToClosestJewel;
import codelets.behaviors.PickupClosestJewel;
import codelets.motor.HandsActionCodelet;
import codelets.motor.LegsActionCodelet;
import codelets.perception.AppleDetector;
import codelets.perception.ClosestAppleDetector;
import codelets.perception.JewelDetector;
import codelets.perception.ClosestJewelDetector;
import codelets.sensors.InnerSense;
import codelets.sensors.Vision;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import memory.CreatureInnerSense;
import support.MindView;
import ws3dproxy.model.Thing;

public class AgentMind extends Mind {
  private static int creatureBasicSpeed = 3;
  private static int reachDistance = 50;

  public AgentMind(Environment env) {
    super();
    // Declare Memory Objects
    MemoryObject visionMO;
    MemoryObject innerSenseMO;
    MemoryObject closestAppleMO;
    MemoryObject knownApplesMO;
    MemoryObject knownJewelsMO;
    MemoryObject closestJewelMO;
    MemoryObject goToAppleLegsMO;
    MemoryObject goToJewelLegsMO;
    MemoryObject pickupJewelHandsMO;
    MemoryObject eatAppleHandsMO;
    MemoryObject forageMO;
    MemoryContainer legsMC;
    MemoryContainer handsMC;

    //Initialize Memory Objects
    List<Thing> vision_list = Collections.synchronizedList(
      new ArrayList<Thing>()
    );
    visionMO = createMemoryObject("VISION", vision_list);
    CreatureInnerSense cis = new CreatureInnerSense();
    innerSenseMO = createMemoryObject("INNER", cis);
    Thing closestApple = null;
    closestAppleMO = createMemoryObject("CLOSEST_APPLE", closestApple);
    List<Thing> knownApples = Collections.synchronizedList(
      new ArrayList<Thing>()
    );
    knownApplesMO = createMemoryObject("KNOWN_APPLES", knownApples);

    forageMO = createMemoryObject("FORAGE_LEGS_MO");
    goToAppleLegsMO = createMemoryObject("GO_TO_APPLE_LEGS_MO");

    // Initialize new MemoryObjects
    List<Thing> knownJewels = Collections.synchronizedList(new ArrayList<Thing>());
    knownJewelsMO = createMemoryObject("KNOWN_JEWELS", knownJewels);

    Thing closestJewel = null;
    closestJewelMO = createMemoryObject("CLOSEST_JEWEL", closestJewel);

    goToJewelLegsMO = createMemoryObject("GO_TO_JEWEL_LEGS_MO");
    
    pickupJewelHandsMO = createMemoryObject("PICKUP_JEWEL_HANDS_MO");
    eatAppleHandsMO = createMemoryObject("EAT_APPLE_HANDS_MO");

    legsMC = createMemoryContainer("LEGS_MC");
    handsMC = createMemoryContainer("HANDS_MC");

    legsMC.add(goToAppleLegsMO);
    legsMC.add(forageMO);
    legsMC.add(goToJewelLegsMO);

    handsMC.add(pickupJewelHandsMO);
    handsMC.add(eatAppleHandsMO);

    // Create and Populate MindViewer
    MindView mv = new MindView("MindView");
    mv.addMO(knownApplesMO);
    mv.addMO(visionMO);
    mv.addMO(closestAppleMO);
    mv.addMO(innerSenseMO);
    //mv.addMO(handsMC);
    //mv.addMO(legsMO);
    mv.addMO(knownJewelsMO);
    mv.addMO(closestJewelMO);
    mv.StartTimer();
    mv.setVisible(true);

    // Create Sensor Codelets
    Codelet vision = new Vision(env.c);
    vision.addOutput(visionMO);
    insertCodelet(vision); //Creates a vision sensor

    Codelet innerSense = new InnerSense(env.c);
    innerSense.addOutput(innerSenseMO);
    insertCodelet(innerSense); //A sensor for the inner state of the creature

    // Create Actuator Codelets
    Codelet legs = new LegsActionCodelet(env.c);
    legs.addInput(legsMC);
    insertCodelet(legs);

    Codelet hands = new HandsActionCodelet(env.c);
    hands.addInput(handsMC);
    insertCodelet(hands);

    // Create Perception Codelets
    Codelet ad = new AppleDetector();
    ad.addInput(visionMO);
    ad.addOutput(knownApplesMO);
    insertCodelet(ad);

    Codelet closestAppleDetector = new ClosestAppleDetector();
    closestAppleDetector.addInput(knownApplesMO);
    closestAppleDetector.addInput(innerSenseMO);
    closestAppleDetector.addOutput(closestAppleMO);
    insertCodelet(closestAppleDetector);

    // Create Perception Codelets for jewels
    Codelet jd = new JewelDetector();
    jd.addInput(visionMO);
    jd.addOutput(knownJewelsMO);
    insertCodelet(jd);

    Codelet closestJewelDetector = new ClosestJewelDetector();
    closestJewelDetector.addInput(knownJewelsMO);
    closestJewelDetector.addInput(innerSenseMO);
    closestJewelDetector.addOutput(closestJewelMO);
    insertCodelet(closestJewelDetector);

    // Create Behavior Codelets
    Codelet goToClosestApple = new GoToClosestApple(
      creatureBasicSpeed,
      reachDistance
    );
    goToClosestApple.addInput(closestAppleMO);
    goToClosestApple.addInput(innerSenseMO);
    goToClosestApple.addOutput(goToAppleLegsMO);
    insertCodelet(goToClosestApple);

    Codelet eatApple = new EatClosestApple(reachDistance);
    eatApple.addInput(closestAppleMO);
    eatApple.addInput(innerSenseMO);
    eatApple.addOutput(eatAppleHandsMO);
    eatApple.addOutput(knownApplesMO);
    insertCodelet(eatApple);

    Codelet forage = new Forage();
    forage.addInput(knownApplesMO);
    insertCodelet(forage);

    // Create Behavior Codelets for jewels
    Codelet goToClosestJewel = new GoToClosestJewel(creatureBasicSpeed, reachDistance);
    goToClosestJewel.addInput(closestJewelMO);
    goToClosestJewel.addInput(innerSenseMO);
    goToClosestJewel.addOutput(goToJewelLegsMO);
    insertCodelet(goToClosestJewel);

    Codelet pickupJewel = new PickupClosestJewel(reachDistance);
    pickupJewel.addInput(closestJewelMO);
    pickupJewel.addInput(innerSenseMO);
    pickupJewel.addOutput(knownJewelsMO);
    pickupJewel.addOutput(pickupJewelHandsMO);
    insertCodelet(pickupJewel);

    // sets a time step for running the codelets to avoid heating too much your machine
    for (Codelet c : this.getCodeRack().getAllCodelets()) c.setTimeStep(200);

    // Start Cognitive Cycle
    start();
  }
}