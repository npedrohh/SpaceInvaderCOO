package Entities;

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
