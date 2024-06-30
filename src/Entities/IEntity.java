package Entities;

public interface IEntity {	
	public int getState();
	public double getX();
	public double getY();
	public double getVX();
	public double getVY();
	public double getRadius();
	public void setVX(double VX);
	public void setVY(double VY);
	public boolean isInside();
	public void render();
}