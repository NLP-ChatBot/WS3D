

package model;

import motorcontrol.CarLikeCreatureKinematics;
import motorcontrol.CreatureKinematicsInterface;
import motorcontrol.TwoWheeledRobotKinematics;
import java.io.IOException;
import java.util.logging.Logger;

public class RobotCreature extends Creature {
    
    static Logger log = Logger.getLogger(RobotCreature.class.getCanonicalName());

    public RobotCreature(double initialX, double initialY, double iPitch, Environment env, int motorSys) {

        super(initialX, initialY, iPitch, env, motorSys);

        switch (motorSys){
            case 1:  kinematics = new CarLikeCreatureKinematics(this);  break;

            case 2:  kinematics = new TwoWheeledRobotKinematics(this); break;

            default: kinematics = new TwoWheeledRobotKinematics(this);
        }
        
        try {
             sf = new ThingShapeFactory("images/robo.3ds", this);

        } catch (IOException ex) {
            log.severe("!!!!!Creature: Erro ! ");
            ex.printStackTrace();
        }

    }

    public void setSpeedExternally(){
        ((TwoWheeledRobotKinematics)this.kinematics).setDeriveSpeed(false);
    }

   @Override
    public void updateMyPosition() {
        this.kinematics.updatePosition();
    }

    @Override
    public Knapsack putMeInKnapsack(Knapsack sack) {
        throw new UnsupportedOperationException("Makes no sense for my type of Thing!!!");
    }

}
