package Entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import GameLibPackage.GameLib;

public class Background {
	
	private List<Double> background_X = new ArrayList<>(20);
	private List<Double> background_Y = new ArrayList<>(20);
	private double speed;
	private double count;
	
	public Background(double Speed) {
		
		background_X = new ArrayList<>(20);
		background_Y = new ArrayList<>(20);
		speed = Speed;
		count = 0.0;
	}
	
	public void initialize() {
		
        for (int i = 0; i < 20; i++) {
            
        	background_X.add(Math.random() * GameLib.WIDTH);
            background_Y.add(Math.random() * GameLib.HEIGHT);
        }
	}
	
	public void render(Color color, long delta, int mod) {
		
		GameLib.setColor(color);
		count += speed * delta;
		
		for(int i = 0; i < background_X.size(); i++){
			
			GameLib.fillRect(background_X.get(i), (background_Y.get(i) + count) % GameLib.HEIGHT, mod, mod);
		}
	}
	
}
