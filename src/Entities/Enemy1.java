package Entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import GameLibPackage.GameLib;

public class Enemy1 extends Enemy {
	
	public Enemy1() {
		
		super(ACTIVE, Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 
			0.20 + Math.random() * 0.15, 9.0, 0.0, 0.0, System.currentTimeMillis() + 500, 
			3 * Math.PI / 2, 0.0, System.currentTimeMillis() + 500, 1);
	}
	
	public Enemy1(ArrayList<IEnemy> lastEnemyArray) {
		
		super(ACTIVE, Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 
			0.20 + Math.random() * 0.15, 9.0, 0.0, 0.0, System.currentTimeMillis() + 500, 
			3 * Math.PI / 2, 0.0, System.currentTimeMillis() + 500, 1);
		lastEnemyArray.add(0, this);
	}
	
	public void update(long delta) {
		
		this.setX(this.getX() + this.getVX() * Math.cos(this.getAngle()) * delta);
		this.setY(this.getY() + this.getVY() * Math.sin(this.getAngle()) * delta * (-1.0));
		this.setAngle(this.getAngle() + this.getRV() * delta);
	}
	
	public boolean canShoot(ICharacter player) {
		
		if(System.currentTimeMillis() > this.getNextShot() && this.getY() < player.getY()) return true;
		return false;
	}
	
	public IProjectile shot(int i) {
		
		IProjectile ep = new EProjectile(1, this.getX(), this.getY(), 
				Math.cos(this.getAngle()) * 0.45, 
				Math.sin(this.getAngle()) * 0.45 * (-1.0), 2.0);
		
		this.setNextShot(System.currentTimeMillis() + 200 + Math.random() * 500);
		
		return ep;
	}
	
	public IEnemy spawn (){
		
		IEnemy e = new Enemy1();
		this.setNextEnemy(System.currentTimeMillis() + 500);
		
		return e;
	 }
	
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray) {
		
		Iterator<IEnemy> e_iterator = enemyList.iterator();
		while(e_iterator.hasNext()) {
			
			IEnemy e = e_iterator.next();
			if(e instanceof Enemy1)
				lastEnemyArray.set(0, e);
		}
	}
	
	public boolean isInside() {
		
		if(this.getX() < -10 || this.getX() > GameLib.WIDTH + 10 || this.getY() > GameLib.HEIGHT + 10)
			return false;
		return true;
	}
	
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray){
		
		if (enemyList.isEmpty() || System.currentTimeMillis() > ((IEnemy) lastEnemyArray.get(0)).getNextEnemy()) return true;
		return false;
	 }
	
	public void render() {
		
		if(this.getState() == EXPLODING){
			
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / (this.getExplosionEnd() - this.getExplosionStart());
			if(alpha > 1) alpha = 1;
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
		if(this.getState() == ACTIVE){
	
			GameLib.setColor(Color.CYAN);
			GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
		}
	}
}
