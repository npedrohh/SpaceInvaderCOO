package Entities;

import java.awt.Color;

import GameLibPackage.GameLib;

public class EProjectile extends Projectile {
	public EProjectile(int state, double X, double Y, double VX, double VY, double radius) {
		
		super(state, X, Y, VX, VY, radius);
	}
	
	public void render() {
		
		GameLib.setColor(Color.RED);
		GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
	}
}
