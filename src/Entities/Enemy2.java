package Entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import GameLibPackage.GameLib;

public class Enemy2 extends Enemy {
	
	private static double spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
	private static int count = 0;
	private boolean shootNow;
	
	public Enemy2() {
		
		super(ACTIVE, spawnX, -10.0, 0.42, 0.42, 12.0, 
				(double) System.currentTimeMillis(), (double) (System.currentTimeMillis() + 500), 
			0.0, (3 * Math.PI) / 2, 0.0, (long) (System.currentTimeMillis() + 3000 + Math.random() * 3000), 3);
		count++;
		this.shootNow = false;
	}
	
	public Enemy2(ArrayList<IEnemy> lastEnemyArray) {
		
		super(ACTIVE, spawnX, -10.0, 0.42, 0.42, 12.0, 
				(double) System.currentTimeMillis(), (double) (System.currentTimeMillis() + 500), 
				0.0, (3 * Math.PI) / 2, 0.0, (long) (System.currentTimeMillis() + 3000 + Math.random() * 3000), 3);
		lastEnemyArray.add(1, this);
		count++;
		this.shootNow = false;
	}
	
	public double getSpawnX() {
		
		return spawnX;
	}
	
	public int getCount() {
		
		return count;
	}
	
	public void setSpawnX(double new_spawnX) {
		
		spawnX = new_spawnX;
	}
	
	public void resetCount() {
		
		count = 0;
	}
	
	public void update(long delta) {
		
		double previousY = this.getY();
		
		this.setX(this.getVX() * Math.cos(this.getAngle()) * delta);
		this.setY(this.getVY() * Math.sin(this.getAngle()) * delta * (-1.0));
		this.setAngle(this.getRV() * delta);
		
		double threshold = GameLib.HEIGHT * 0.30;	
		
		if(previousY < threshold && this.getY() >= threshold) {
			
			if(this.getX() < GameLib.WIDTH / 2) this.setRV(0.003);
			else this.setRV(-0.003);
		}
		
		if(this.getRV() > 0 && Math.abs(this.getAngle() - 3 * Math.PI) < 0.05){
			
			this.setRV(0.0);
			this.setAngle(3 * Math.PI);
			this.shootNow = true;
		}
		
		if(this.getRV() < 0 && Math.abs(this.getAngle()) < 0.05){
			
			this.setRV(0.0);
			this.setAngle(0.0);
			this.shootNow = true;
		}
	}
	
	public boolean canShoot(ICharacter player) {
		
		if(this.shootNow) return true;
		return false;
	}
	
	public IProjectile shot(int i) {
			
		double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
		
		double a = angles[i] + Math.random() * Math.PI/6 - Math.PI/12;

			IProjectile ep = new EProjectile(1, this.getX(), this.getY(), 
					Math.cos(a) * 0.30, Math.sin(a) * 0.30, 2.0);
			
		return ep;
	}
	
	public IEnemy spawn (){
		
		IEnemy e = new Enemy2();
		
		if(this.getCount() < 10){
			
			this.setNextEnemy(System.currentTimeMillis() + 120);
			
		}
		else {
			
			this.resetCount();
			
			this.setSpawnX(Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8);
			this.setNextEnemy((long) (System.currentTimeMillis() + 3000 + Math.random() * 3000));
		}
		
		return e;
	 }
	
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray) {
		
		Iterator<IEnemy> e_iterator = enemyList.iterator();
		while(e_iterator.hasNext()) {
			
			IEnemy e = e_iterator.next();
			if(e instanceof Enemy2)
				lastEnemyArray.set(1, e);
		}
	}
	 
	public boolean isInside() {
		
		if(this.getX() < -10 || this.getX() > GameLib.WIDTH + 10) return false;
		return true;
	}
	
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray){
		
		if (enemyList.isEmpty() || System.currentTimeMillis() > ((IEnemy) lastEnemyArray.get(1)).getNextEnemy()) return true;
		return true;
	 }
	
	public void render() {
		
		if(this.getState() == EXPLODING){
			
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / 
					(this.getExplosionEnd() - this.getExplosionStart());
			if(alpha > 1) alpha = 1;
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
		if(this.getState() == ACTIVE){
	
			GameLib.setColor(Color.MAGENTA);
			GameLib.drawDiamond(this.getX(), this.getY(), this.getRadius());
		}
	}
}
