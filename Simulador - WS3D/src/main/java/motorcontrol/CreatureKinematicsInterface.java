package motorcontrol;

/**
 * This interface defines the creature motor system. Currently, it can be a car-like
 * motor system or two-wheeled differential steering robot.
 *
 * @author eccastro
 */
public interface CreatureKinematicsInterface {


    public void updatePosition(); //synchronized
    
}
