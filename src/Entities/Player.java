package Entities;

import java.awt.Color;

import GameLibPackage.GameLib;

public class Player extends Character {
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
