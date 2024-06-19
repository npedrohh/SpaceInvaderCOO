import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

interface IEntity {	
	public int getState();
	public double getX();
	public double getY();
	public double getVX();
	public double getVY();
	public double getRadius();
	public boolean isInside();
	public void render();
}

abstract class Entity implements IEntity {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	private int state;
	private double X;
	private double Y;
	private double VX;
	private double VY;
	private double radius;
	
	public Entity(int state, double X, double Y, double VX, double VY, double radius) {
		
		this.state = state;
		this.X = X;
		this.Y = Y;
		this.VX = VX;
		this.VY = VY;
		this.radius = radius;
	}
	
	public int getState() {
		
		return state;
	}

	public double getX() {
		
		return X;
	}

	public double getY() {
		
		return Y;
	}

	public double getVX() {
		
		return VX;
	}

	public double getVY() {
		
		return VY;
	}

	public double getRadius() {
		return radius;
	}
	
	public void setState(int state) {
		
		this.state = state;
	}
	
	public void setX(double X) {
		
		this.X = X;
	}
	
	public void setY(double Y) {
		
		this.Y = Y;
	}
}

interface ICharacter extends IEntity {
	public double getExplosionStart();
	public double getExplosionEnd();
	public double getNextShot();
	public void setState(int state);
	public void setX(double X);
	public void setY(double Y);
	public void setExplosionStart(double explosion_start);
	public void setExplosionEnd(double explosion_end);
	public void setNextShot(double next_shot);
	public boolean isColliding(IEntity e);
	public void explode();
}

abstract class Character extends Entity implements ICharacter {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	private double explosion_start;
	private double explosion_end;
	private double next_shot; 
	
	public Character(int state, double X, double Y, double VX, double VY, double radius, 
			double explosion_start, double explosion_end, double next_shot) {
		
		super(state, X, Y, VX, VY, radius);
		this.explosion_start = explosion_start;
		this.explosion_end = explosion_end;
		this.next_shot = next_shot;
	}

	public double getExplosionStart() {
		return explosion_start;
	}

	public double getExplosionEnd() {
		return explosion_end;
	}

	public double getNextShot() {
		return next_shot;
	}
	
	public void setExplosionStart(double explosion_start) {
		
		this.explosion_start = explosion_start;
	}
	
	public void setExplosionEnd(double explosion_end) {
		
		this.explosion_end = explosion_end;
	}
	
	public void setNextShot(double next_shot) {
		
		this.next_shot = next_shot;
	}

	public boolean isColliding(IEntity e) {
		
		if(this.getState() == ACTIVE){
			
			double dx = this.getX() - e.getX();
			double dy = this.getY() - e.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			if(dist < (this.getRadius() + e.getRadius()) * 0.8){
				
				return true;
			}
			return false;
		}
		return false;
	}
	
	public void explode() {
		
		this.setState(EXPLODING);
		this.setExplosionStart(System.currentTimeMillis());
		this.setExplosionEnd(System.currentTimeMillis() + 500);
	}
}

class Player extends Character {
	public Player(int state, double X, double Y, double VX, double VY, double radius, double explosion_start, 
			double explosion_end, double next_shot) {
		
		super(state, X, Y, VX, VY, radius, explosion_start, explosion_end, next_shot);
	}
	
	public boolean isInside() {
		
		if(this.getX() < 0.0) this.setX(0.0);
		if(this.getX() >= GameLib.WIDTH) this.setX(GameLib.WIDTH - 1);
		if(this.getY() < 25.0) this.setY(25.0);
		if(this.getY() >= GameLib.HEIGHT) this.setY(GameLib.HEIGHT - 1);
		return true;
	}	
	
	public void explode() {
		
		this.setState(EXPLODING);
		this.setExplosionStart(System.currentTimeMillis());
		this.setExplosionEnd(System.currentTimeMillis() + 2000);
	}
	
	public void render() {
		
		if(this.getState() == EXPLODING){
			
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / (this.getExplosionEnd() - this.getExplosionStart());
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		else{
			
			GameLib.setColor(Color.BLUE);
			GameLib.drawPlayer(this.getX(), this.getY(), this.getRadius());
		}
	}
}

interface IEnemy extends ICharacter {
	public double getNextEnemy();
	public int getProjectileAmount();
	public void setNextEnemy(long next_enemy);
	public void update(long delta);
	public boolean canShoot(ICharacter player);
	public IProjectile shot(int i);
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray);
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray);
	public IEnemy spawn();
}

abstract class Enemy extends Character implements IEnemy {
	private double angle;
	private double RV;
	private long next_enemy;
	private int projectile_amount;
	
	public Enemy(int state, double X, double Y, double VX, double VY, double radius, double explosion_start,
			double explosion_end, double next_shot, double angle, double RV, long next_enemy, int projectile_amount) {
		
		super(state, X, Y, VX, VY, radius, explosion_start, explosion_end, next_shot);
		this.angle = angle;
		this.RV = RV;
		this.next_enemy = next_enemy;
		this.projectile_amount = projectile_amount;
	}
	
	public double getAngle() {
		
		return angle;
	}
	
	public double getRV() {
		
		return RV;
	}
	
	public double getNextEnemy() {
		
		return next_enemy;
	}
	
	public int getProjectileAmount() {
		
		return projectile_amount;
	}
	
	public void setAngle(double angle) {
		
		this.angle = angle;
	}
	
	public void setRV(double RV) {
		
		this.RV = RV;
	}
	
	public void setNextEnemy(long next_enemy) {
		
		this.next_enemy = next_enemy;
	}
	
	public boolean isColliding(IEntity e) {
		
		if(this.getState() == ACTIVE){
			
			double dx = this.getX() - e.getX();
			double dy = this.getY() - e.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			if(dist < this.getRadius()){
				
				return true;
			}
			return false;
		}
		return false;
	}
}

class Enemy1 extends Enemy {
	
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
			
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / 
					(this.getExplosionEnd() - this.getExplosionStart());
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
		if(this.getState() == ACTIVE){
	
			GameLib.setColor(Color.CYAN);
			GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
		}
	}
}

class Enemy2 extends Enemy {
	
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
		
		System.out.println(this.getY());
		
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
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
		if(this.getState() == ACTIVE){
	
			GameLib.setColor(Color.MAGENTA);
			GameLib.drawDiamond(this.getX(), this.getY(), this.getRadius());
		}
	}
}

interface IProjectile extends IEntity {
	public void update(long delta);
}

class Projectile extends Entity implements IProjectile {
	public Projectile(int state, double X, double Y, double VX, double VY, double radius) {
		
		super(state, X, Y, VX, VY, radius);
	}

	public void render() {
		
		GameLib.setColor(Color.GREEN);
		GameLib.drawLine(this.getX(), this.getY() - 5, this.getX(), this.getY() + 5);
		GameLib.drawLine(this.getX() - 1, this.getY() - 3, this.getX() - 1, this.getY() + 3);
		GameLib.drawLine(this.getX() + 1, this.getY() - 3, this.getX() + 1, this.getY() + 3);
	}

	public boolean isInside() {
		
		if(this.getY() < 0) {
			
			return false;
		}
		
		return true;
	}
	
	public void update(long delta) {
		

		this.setX(this.getX() + this.getVX() * delta);
		this.setY(this.getY() + this.getVY() * delta);
	}
}

class EProjectile extends Projectile {
	public EProjectile(int state, double X, double Y, double VX, double VY, double radius) {
		
		super(state, X, Y, VX, VY, radius);
	}
	
	public void render() {
		
		GameLib.setColor(Color.RED);
		GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
	}
}