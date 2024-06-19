package Entities;

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