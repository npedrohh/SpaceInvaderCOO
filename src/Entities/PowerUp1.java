package Entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import GameLibPackage.GameLib;

public class PowerUp1 extends Collectable {

    public PowerUp1() {
        super(ACTIVE , Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 1.0 + Math.random() * 0.15, 10.0, 100);
    }

    public PowerUp1(ArrayList<ICollectable> lastPowerUpArray) {
        super(ACTIVE , Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 1.0 + Math.random() * 0.15, 10.0, 100);
        lastPowerUpArray.add(this);
    }

    public void activate(ICharacter player) {

        player.setVX(player.getVX() * 1.5);
        player.setVY(player.getVY() * 1.5);
        this.resetTimer();
    }
    public void deactivate(ICharacter player) {

        player.setVX(player.getVX() / 1.5);
        player.setVY(player.getVY() / 1.5);
    }
    public void updateLastCollectable(LinkedList<ICollectable> powerUpList, ArrayList<ICollectable> lastPowerUpArray) {
		
		Iterator<ICollectable> pu_iterator = powerUpList.iterator();
		while(pu_iterator.hasNext()) {
			
			ICollectable pu = pu_iterator.next();
			if(pu instanceof PowerUp1)
				lastPowerUpArray.set(0, pu);
		}
	}

    public ICollectable spawn(){
		
		ICollectable pu = new PowerUp1();
		this.setNextCollectable(System.currentTimeMillis() + 500);
		
		return pu;
    }

    public void update(long delta) {
        if (this.getState() == ACTIVE) {
            double newY = this.getY() + this.getVY() * delta;
            this.setY(newY);
        }
    }

    @Override
    public void render() {

        if (this.getState() == ACTIVE) {
            GameLib.setColor(Color.ORANGE);
            GameLib.drawDiamond(this.getX(), this.getY(), this.getRadius());
        }
    }
}
