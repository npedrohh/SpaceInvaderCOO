package Entities;

import java.awt.Color;

import GameLibPackage.GameLib;

public class Player extends Character {
	
	private int vidas; //atributo especial do player, o sistema de vidas//
	public int widthLife;
	public int heightLife;
	
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

		this.morreu();
		this.setState(EXPLODING);
		this.setExplosionStart(System.currentTimeMillis());
		this.setExplosionEnd(System.currentTimeMillis() + 2000);
	}
	
	public void render() {

		this.sistemaVidas();

		if(this.getState() == EXPLODING){
			
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / (this.getExplosionEnd() - this.getExplosionStart());
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		else{
			
			GameLib.setColor(Color.BLUE);
			GameLib.drawPlayer(this.getX(), this.getY(), this.getRadius());
		}
	}
	
	public void update(long delta) {
		
		if(this.getState() == ACTIVE){
			
			if(GameLib.iskeyPressed(GameLib.KEY_UP)) this.setY(this.getY() - delta * this.getVY());
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.setY(this.getY() + delta * this.getVY());
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.setX(this.getX() - delta * this.getVX());
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.setX(this.getX() + delta * this.getVX());
		}
	}

	//sistema de vidas//

	private void setVidas(int vidas){
		this.vidas = vidas;
	}

	public int getVidas(){
		return this.vidas;
	}

	public void iniciaVidas(){
		this.vidas = 3;
	}

	public void morreu(){
		this.setVidas(getVidas()-1);
	}

	public void setWidthLife(int widthLife) {
		this.widthLife = widthLife;
	}

	public void setHeightLife(int heightLife) {
		this.heightLife = heightLife;
	}

	public int getWidthLife() {
		return widthLife;
	}

	public int getHeightLife() {
		return heightLife;
	}

	//cria a interface gráfica do sistema de vidas//

public void sistemaVidas(){

	if(this.getVidas()==3){
		GameLib.drawHeart(9, 9);
		GameLib.drawHeart(50, 50);
		GameLib.drawHeart(70, 70);
	}else if(this.getVidas()==2){
		GameLib.drawHeart(50, 50);
		GameLib.drawHeart(70, 70);
	}else if(this.getVidas()==1){
		GameLib.drawHeart(70, 70);
	}

}

}
