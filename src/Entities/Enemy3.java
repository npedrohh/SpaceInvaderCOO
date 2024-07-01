package Entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import GameLibPackage.GameLib;

public class Enemy3 extends Enemy {
	
	private static double spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
	private static int count = 0;
	private boolean shootNow;
	private static final double OFFSET = 30.0; // Offset for wyrm-like formation
	private static final int MAX_SHOTS = 3; // Maximum shots per enemy
	private int shotsFired = 0; // Shots fired by this enemy
	
	public Enemy3() {
		super(ACTIVE, spawnX, -10.0, 0.42, 0.42, 12.0, 
				(double) System.currentTimeMillis(), (double) (System.currentTimeMillis() + 500), 
				0.0, (3 * Math.PI) / 2, 0.0, (long) (System.currentTimeMillis() + 3000 + Math.random() * 3000), 3);
		count++;
		this.shootNow = false;
	}
	
	public Enemy3(ArrayList<IEnemy> lastEnemyArray) {
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
	
		// Update position based on velocity and angle
		this.setX(this.getX() + this.getVX() * Math.cos(this.getAngle()) * delta);
		this.setY(this.getY() + this.getVY() * Math.sin(this.getAngle()) * delta * (-1.0));
		this.setAngle(this.getAngle() + this.getRV() * delta);
	
		double threshold = GameLib.HEIGHT * 0.30;    
	
		if (previousY < threshold && this.getY() >= threshold) {
			if (this.getX() < GameLib.WIDTH / 2) {
				this.setRV(0.003);
			} else {
				this.setRV(-0.003);
			}
		}
	
		if (this.getRV() > 0 && Math.abs(this.getAngle() - 3 * Math.PI) < 0.05) {
			this.setRV(0.0);
			this.setAngle(3 * Math.PI);
			this.shootNow = true;
		}
	
		if (this.getRV() < 0 && Math.abs(this.getAngle()) < 0.05) {
			this.setRV(0.0);
			this.setAngle(0.0);
			this.shootNow = true;
		}
	}
	
	public boolean canShoot(ICharacter player) {
		return this.shootNow && this.shotsFired < MAX_SHOTS;
	}
	
	public IProjectile shot(int i) {
		if (this.shotsFired >= MAX_SHOTS) return null;
		
		double[] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
		double a = angles[i] + Math.random() * Math.PI/6 - Math.PI/12;
		IProjectile ep = new EProjectile(1, this.getX(), this.getY(), 
				Math.cos(a) * 0.30, Math.sin(a) * 0.30, 2.0);
		this.shotsFired++;
		return ep;
	}
	
	public IEnemy spawn() {
		if (this.getCount() < 100) {
			this.setNextEnemy(System.currentTimeMillis() + 120);
			
			// Set the spawn position offset for wyrm-like formation
			double offsetX = this.getCount() * OFFSET;
			double offsetY = -this.getCount() * OFFSET;
			Enemy3 e = new Enemy3();
			e.setX(this.getSpawnX() + offsetX);
			e.setY(offsetY);
			return e;
		}
		
		if (this.getCount() == 100) {
			this.resetCount();
			this.setSpawnX(Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8);
			this.setNextEnemy((long) (System.currentTimeMillis() + 3000 + Math.random() * 3000));
		}
		
		return null;
	}

	
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray) {
		Iterator<IEnemy> e_iterator = enemyList.iterator();
		while (e_iterator.hasNext()) {
			IEnemy e = e_iterator.next();
			if (e instanceof Enemy3) lastEnemyArray.set(1, e);
		}
	}
	
	public boolean isInside() {
		return this.getX() >= -10 && this.getX() <= GameLib.WIDTH + 10;
	}
	
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray) {
		return enemyList.isEmpty() || (System.currentTimeMillis() > ((IEnemy) lastEnemyArray.get(1)).getNextEnemy() && this.getCount() < 10);
	}
	
	public void render() {
		if (this.getState() == EXPLODING) {
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / 
					(this.getExplosionEnd() - this.getExplosionStart());
			if (alpha > 1) alpha = 1;
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
		if (this.getState() == ACTIVE) {
			GameLib.setColor(Color.PINK);
			GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
		}
	}
}
