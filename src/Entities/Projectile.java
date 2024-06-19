package Entities;

import java.awt.Color;

import GameLibPackage.GameLib;

public class Projectile extends Entity implements IProjectile {
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
