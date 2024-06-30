package Entities;

abstract class Character extends Entity implements ICharacter {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	private double explosion_start;
	private double explosion_end;
	private double next_shot; 

	private int buff;
	
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

	public void setBuff(int buff) {
		this.buff = buff;
	}

	public int getBuff() {
		return buff;
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