package Entities;

public interface ICharacter extends IEntity {
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
