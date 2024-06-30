package Entities;

import java.util.ArrayList;
import java.util.LinkedList;

import GameLibPackage.GameLib;

abstract class Collectable extends Entity implements ICollectable {

    private double explosion_start;
	private double explosion_end;

    private int duration;
    private long startTime;

    private double next_collectible;

    public Collectable(int state, double X, double Y, double VX, double VY, double radius, int duration) {
        super(state, X, Y, VX, VY, radius);
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        return elapsedTime >= duration * 100;
    }

    public double getNextCollectable() {

        return next_collectible;

    }

    public void setNextCollectable(double next_collectible) {

        this.next_collectible = next_collectible;

    }

    public double getExplosionStart() {
		return explosion_start;
	}

	public double getExplosionEnd() {
		return explosion_end;
	}

    public boolean isSpawnable() {
        return this.getState() == INACTIVE;
    }

    public boolean isSpawnable(LinkedList<ICollectable> powerUpList, ArrayList<ICollectable> lastPowerUpArray){
		
		if (powerUpList.isEmpty() || System.currentTimeMillis() > ((ICollectable) lastPowerUpArray.get(0)).getNextCollectable()) return true;
		return false;
	 }

     public boolean isInside() {
    
        if (this.getX() < -10 || this.getX() > GameLib.WIDTH + 10 || this.getY() > GameLib.HEIGHT + 10) {
            return false;
        }
        
        return true;
    }
    
    public int getDuration() {
        return duration;
    }


    public void resetTimer() {
        startTime = System.currentTimeMillis();
    }

    public void explode() {
        this.setState(EXPLODING);
    }
}
