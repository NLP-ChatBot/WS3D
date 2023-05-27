package motorcontrol;

import model.Creature;

public class CarLikeCreatureKinematics implements CreatureKinematicsInterface{

    private Creature c;

    public CarLikeCreatureKinematics(Creature creature){
        this.c = creature;
    }

    public synchronized void updatePosition() {
        //dynamic model described in Gudwin's thesis (chapter 5)
                    double D, sena, dteta,vx,vy,cosp,senp;
                    double pRad = Math.toRadians(c.getPitch());
                    double wRad = Math.toRadians(c.getWheel());
                    //D = zoom * SIZE;
                    D = 1.0 * 50.0;
                    sena = Math.sin(wRad);
                    dteta = (1-c.getFriction()) * c.getSpeed() * sena / D;
                    if (sena == 0)
                      {vx = 0;
                       vy = (1-c.getFriction()) * c.getSpeed();
                      }
                    else
                      {vx = D * (1-Math.cos(dteta)) / sena;
                       vy = D * Math.sin(dteta) / sena;
                      }
                    //System.out.println("get friction= "+c.getFriction()+ " vx= "+vx+ " vy= "+vy);
                    cosp = Math.cos(pRad);
                    senp = Math.sin(pRad);
                    //x += vy * cosp - vx * senp;
                    //y += vy * senp + vx * cosp;
                    //pitch += Math.toDegrees(dteta);
                    //System.out.println("Before: x= "+c.getX()+ " and y= "+c.getY());
                    c.setX(c.getX() + (vy * cosp - vx * senp));
                    c.setY(c.getY() + (vy * senp + vx * cosp));
                    //System.out.println("After: x= "+c.getX()+ " and y= "+c.getY());
                    c.setPitch(c.getPitch() + Math.toDegrees(dteta));
    }

}
