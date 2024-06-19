import java.awt.Color;
import java.util.*;

interface IEntity {	
	public int getState();
	public double getX();
	public double getY();
	public double getVX();
	public double getVY();
	public double getRadius();
	public boolean isInside();
	public void render();
}

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

interface ICharacter extends IEntity {
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

abstract class Character extends Entity implements ICharacter {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	private double explosion_start;
	private double explosion_end;
	private double next_shot; 
	
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

class Player extends Character {
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

interface IEnemy extends ICharacter {
	public double getNextEnemy();
	public int getProjectileAmount();
	public void setNextEnemy(long next_enemy);
	public void update(long delta);
	public boolean canShoot(ICharacter player);
	public IProjectile shot(int i);
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray);
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray);
	public IEnemy spawn();
}

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

class Enemy1 extends Enemy {
	
	public Enemy1() {
		
		super(ACTIVE, Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 
			0.20 + Math.random() * 0.15, 9.0, 0.0, 0.0, System.currentTimeMillis() + 500, 
			3 * Math.PI / 2, 0.0, System.currentTimeMillis() + 500, 1);
	}
	
	public Enemy1(ArrayList<IEnemy> lastEnemyArray) {
		
		super(ACTIVE, Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 
			0.20 + Math.random() * 0.15, 9.0, 0.0, 0.0, System.currentTimeMillis() + 500, 
			3 * Math.PI / 2, 0.0, System.currentTimeMillis() + 500, 1);
		lastEnemyArray.add(0, this);
	}
	
	public void update(long delta) {
		
		this.setX(this.getX() + this.getVX() * Math.cos(this.getAngle()) * delta);
		this.setY(this.getY() + this.getVY() * Math.sin(this.getAngle()) * delta * (-1.0));
		this.setAngle(this.getAngle() + this.getRV() * delta);
	}
	
	public boolean canShoot(ICharacter player) {
		
		if(System.currentTimeMillis() > this.getNextShot() && this.getY() < player.getY()) return true;
		return false;
	}
	
	public IProjectile shot(int i) {
		
		IProjectile ep = new EProjectile(1, this.getX(), this.getY(), 
				Math.cos(this.getAngle()) * 0.45, 
				Math.sin(this.getAngle()) * 0.45 * (-1.0), 2.0);
		
		this.setNextShot(System.currentTimeMillis() + 200 + Math.random() * 500);
		
		return ep;
	}
	
	public IEnemy spawn (){
		
		IEnemy e = new Enemy1();
		this.setNextEnemy(System.currentTimeMillis() + 500);
		
		return e;
	 }
	
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray) {
		
		Iterator<IEnemy> e_iterator = enemyList.iterator();
		while(e_iterator.hasNext()) {
			
			IEnemy e = e_iterator.next();
			if(e instanceof Enemy1)
				lastEnemyArray.set(0, e);
		}
	}
	
	public boolean isInside() {
		
		if(this.getX() < -10 || this.getX() > GameLib.WIDTH + 10 || this.getY() > GameLib.HEIGHT + 10)
			return false;
		return true;
	}
	
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray){
		
		if (enemyList.isEmpty() || System.currentTimeMillis() > ((IEnemy) lastEnemyArray.get(0)).getNextEnemy()) return true;
		return false;
	 }
	
	public void render() {
		
		if(this.getState() == EXPLODING){
			
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / 
					(this.getExplosionEnd() - this.getExplosionStart());
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
		if(this.getState() == ACTIVE){
	
			GameLib.setColor(Color.CYAN);
			GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
		}
	}
}

class Enemy2 extends Enemy {
	
	private static double spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
	private static int count = 0;
	private boolean shootNow;
	
	public Enemy2() {
		
		super(ACTIVE, spawnX, -10.0, 0.42, 0.42, 12.0, 
				(double) System.currentTimeMillis(), (double) (System.currentTimeMillis() + 500), 
			0.0, (3 * Math.PI) / 2, 0.0, (long) (System.currentTimeMillis() + 3000 + Math.random() * 3000), 3);
		count++;
		this.shootNow = false;
	}
	
	public Enemy2(ArrayList<IEnemy> lastEnemyArray) {
		
		super(ACTIVE, spawnX, -10.0, 0.42, 0.42, 12.0, 
				(double) System.currentTimeMillis(), (double) (System.currentTimeMillis() + 500), 
				0.0, (3 * Math.PI) / 2, 0.0, (long) (System.currentTimeMillis() + 3000 + Math.random() * 3000), 3);
		lastEnemyArray.add(1, this);
		count++;
		this.shootNow = false;
	}
	
	public double getSpawnX() {
		
		return spawnX;
	}
	
	public int getCount() {
		
		return count;
	}
	
	public void setSpawnX(double new_spawnX) {
		
		spawnX = new_spawnX;
	}
	
	public void resetCount() {
		
		count = 0;
	}
	
	public void update(long delta) {
		
		double previousY = this.getY();
		
		this.setX(this.getVX() * Math.cos(this.getAngle()) * delta);
		this.setY(this.getVY() * Math.sin(this.getAngle()) * delta * (-1.0));
		this.setAngle(this.getRV() * delta);
		
		double threshold = GameLib.HEIGHT * 0.30;	
		
		System.out.println(this.getY());
		
		if(previousY < threshold && this.getY() >= threshold) {
			
			if(this.getX() < GameLib.WIDTH / 2) this.setRV(0.003);
			else this.setRV(-0.003);
		}
		
		if(this.getRV() > 0 && Math.abs(this.getAngle() - 3 * Math.PI) < 0.05){
			
			this.setRV(0.0);
			this.setAngle(3 * Math.PI);
			this.shootNow = true;
		}
		
		if(this.getRV() < 0 && Math.abs(this.getAngle()) < 0.05){
			
			this.setRV(0.0);
			this.setAngle(0.0);
			this.shootNow = true;
		}
	}
	
	public boolean canShoot(ICharacter player) {
		
		if(this.shootNow) return true;
		return false;
	}
	
	public IProjectile shot(int i) {
			
		double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
		
		double a = angles[i] + Math.random() * Math.PI/6 - Math.PI/12;

			IProjectile ep = new EProjectile(1, this.getX(), this.getY(), 
					Math.cos(a) * 0.30, Math.sin(a) * 0.30, 2.0);
			
		return ep;
	}
	
	public IEnemy spawn (){
		
		IEnemy e = new Enemy2();
		
		if(this.getCount() < 10){
			
			this.setNextEnemy(System.currentTimeMillis() + 120);
		}
		else {
			
			this.resetCount();
			
			this.setSpawnX(Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8);
			this.setNextEnemy((long) (System.currentTimeMillis() + 3000 + Math.random() * 3000));
		}
		
		return e;
	 }
	
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray) {
		
		Iterator<IEnemy> e_iterator = enemyList.iterator();
		while(e_iterator.hasNext()) {
			
			IEnemy e = e_iterator.next();
			if(e instanceof Enemy2)
				lastEnemyArray.set(1, e);
		}
	}
	 
	public boolean isInside() {
		
		if(this.getX() < -10 || this.getX() > GameLib.WIDTH + 10) return false;
		return true;
	}
	
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray){
		
		if (enemyList.isEmpty() || System.currentTimeMillis() > ((IEnemy) lastEnemyArray.get(1)).getNextEnemy()) return true;
		return true;
	 }
	
	public void render() {
		
		if(this.getState() == EXPLODING){
			
			double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / 
					(this.getExplosionEnd() - this.getExplosionStart());
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
		if(this.getState() == ACTIVE){
	
			GameLib.setColor(Color.MAGENTA);
			GameLib.drawDiamond(this.getX(), this.getY(), this.getRadius());
		}
	}
}

interface IProjectile extends IEntity {
	public void update(long delta);
}

class Projectile extends Entity implements IProjectile {
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

class EProjectile extends Projectile {
	public EProjectile(int state, double X, double Y, double VX, double VY, double radius) {
		
		super(state, X, Y, VX, VY, radius);
	}
	
	public void render() {
		
		GameLib.setColor(Color.RED);
		GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
	}
}

public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	

	/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
	/* Método principal */
	
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();

		/* variáveis do player - OO*/
		
		ICharacter player = new Player(ACTIVE, GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 
				12.0, 0, 0, currentTime);

		/* variáveis dos projéteis disparados pelo player */
		
		LinkedList<IProjectile> projectileList = new LinkedList<>();
		
		/* variáveis dos inimigos tipo 1 */
		
		LinkedList<IEnemy> enemyList = new LinkedList<>();
		ArrayList<IEnemy> lastEnemyArray = new ArrayList<>();
		
		/* variáveis dos inimigos tipo 2 */
		

		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		LinkedList<IProjectile> e_projectileList = new LinkedList<>();
		
		/* estrelas que formam o fundo de primeiro plano */
		
		double [] background1_X = new double[20];
		double [] background1_Y = new double[20];
		double background1_speed = 0.070;
		double background1_count = 0.0;
		
		/* estrelas que formam o fundo de segundo plano */
		
		double [] background2_X = new double[50];
		double [] background2_Y = new double[50];
		double background2_speed = 0.045;
		double background2_count = 0.0;
		
		/* inicializações */
		
		for(int i = 0; i < background1_X.length; i++){
			
			background1_X[i] = Math.random() * GameLib.WIDTH;
			background1_Y[i] = Math.random() * GameLib.HEIGHT;
		}
		
		for(int i = 0; i < background2_X.length; i++){
			
			background2_X[i] = Math.random() * GameLib.WIDTH;
			background2_Y[i] = Math.random() * GameLib.HEIGHT;
		}
						
		/* iniciado interface gráfica */
		
		GameLib.initGraphics();
		
		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/*                                                                                               */
		/* O main loop do jogo possui executa as seguintes operações:                                    */
		/*                                                                                               */
		/* 1) Verifica se há colisões e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu desde a última atualização     */
		/*    e no timestamp atual: posição e orientação, execução de disparos de projéteis, etc.        */
		/*                                                                                               */
		/* 3) Processa entrada do usuário (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um período de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/
		
		while(running){
		
			/* Usada para atualizar o estado dos elementos do jogo    */
			/* (player, projéteis e inimigos) "delta" indica quantos  */
			/* ms se passaram desde a última atualização.             */
			
			delta = System.currentTimeMillis() - currentTime;
			
			/* Já a variável "currentTime" nos dá o timestamp atual.  */
			
			currentTime = System.currentTimeMillis();
			
			/***************************/
			/* Verificação de colisões */
			/***************************/
					
			if(player.getState() == ACTIVE) {
			
				/* colisões player - projeteis (inimigo) */
				
				for(IProjectile p : e_projectileList) {
					
					if(player.isColliding(p)) player.explode();
				}
			
				/* colisões player - inimigos */
					
				for(IEnemy e : enemyList) {
					
					if(player.isColliding(e)) player.explode();
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(IProjectile p : projectileList) {
				
				for(IEnemy e : enemyList) {
					
					if(e.isColliding(p)) e.explode();
				}
			}
			
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis (player) */
			
			Iterator<IProjectile> p_iterator = projectileList.iterator();

			while (p_iterator.hasNext()) {
				
			    IProjectile p = p_iterator.next();
			    
			    if (p.isInside()) {
			    	
			        p.update(delta);
			    } else {
			    	
			        p_iterator.remove();
			    }
			}
			
			/* projeteis (inimigos) */
			
			Iterator<IProjectile> ep_iterator = e_projectileList.iterator();

			while (ep_iterator.hasNext()) {
				
			    IProjectile ep = ep_iterator.next();
			    
			    if (ep.isInside()) {
			    	
			        ep.update(delta);
			    } else {
			    	
			        ep_iterator.remove();
			    }
			}
			
			/* inimigos */
			
			Iterator<IEnemy> e_iterator = enemyList.iterator();
			
			while(e_iterator.hasNext()) {
				
				IEnemy e = e_iterator.next();
				
				if(e.getState() == EXPLODING){
					
					if(currentTime > e.getExplosionEnd()){
						
						e_iterator.remove();
					}
				}
				
				if(e.getState() == ACTIVE) {
					
					if(e.isInside()) {
					
						e.update(delta);
						
						if(e.canShoot(player)) {
							
							for(int i = 0; i < e.getProjectileAmount(); i++)
								e_projectileList.add(e.shot(i));
						}
					}
					else {
						
						e_iterator.remove();
					}
				}
				
			}
			
			/* verificando se novos inimigos devem ser "lançados" */
			
			if(lastEnemyArray.isEmpty()) {
				
				IEnemy e1 = new Enemy1(lastEnemyArray);
				IEnemy e2 = new Enemy2(lastEnemyArray);
				
				e1.setNextEnemy(currentTime+500);
				e2.setNextEnemy((long) (System.currentTimeMillis() + 3000 + Math.random() * 3000));
			}
			
			Iterator<IEnemy> le_iterator = lastEnemyArray.iterator();
			
			while(le_iterator.hasNext()) {
			
			IEnemy le = le_iterator.next();	
						
			le.updateLastEnemy(enemyList, lastEnemyArray);
			
				if(le.isSpawnable(enemyList, lastEnemyArray)) {
					
					enemyList.add(le.spawn());
				}
			}
					
			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			if(player.getState() == EXPLODING){
				
				if(currentTime > player.getExplosionEnd()){
					
					player.setState(ACTIVE);
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			
			if(player.getState() == ACTIVE){
				
				if(GameLib.iskeyPressed(GameLib.KEY_UP)) player.setY(player.getY() - delta * player.getVY());
				if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player.setY(player.getY() + delta * player.getVY());
				if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player.setX(player.getX() - delta * player.getVX());
				if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player.setX(player.getX() + delta * player.getVX());
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(currentTime > player.getNextShot()) {
						
						IProjectile p = new Projectile(1, player.getX(), player.getY() - 2* player.getRadius(), 0.0, -1.0, -1.0);
						
						projectileList.add(p);
						
						player.setNextShot(currentTime + 100);
					}
				}
			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */
			
			player.isInside();

			/*******************/
			/* Desenho da cena */
			/*******************/
		
			/* desenhando plano fundo distante */
			
			GameLib.setColor(Color.DARK_GRAY);
			background2_count += background2_speed * delta;
			
			for(int i = 0; i < background2_X.length; i++){
				
				GameLib.fillRect(background2_X[i], (background2_Y[i] + background2_count) % GameLib.HEIGHT, 2, 2);
			}
			
			/* desenhando plano de fundo próximo */
			
			GameLib.setColor(Color.GRAY);
			background1_count += background1_speed * delta;
			
			for(int i = 0; i < background1_X.length; i++){
				
				GameLib.fillRect(background1_X[i], (background1_Y[i] + background1_count) % GameLib.HEIGHT, 3, 3);
			}	
			
			/* desenhando player */
			
			player.render();
			
			/* deenhando projeteis (player) */
			
			for(IProjectile p : projectileList) {
				
				p.render();
			}
			
			for(IProjectile ep : e_projectileList) {
				
				ep.render();
			}
			
			/* desenhando inimigo 1*/
			
			if(enemyList.get(0) instanceof Enemy2) System.out.println(enemyList.get(0).getY());
			
			for(IEnemy e : enemyList) {
				
				e.render();
			}
			
			/* chamama a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 5 ms. */
			
			busyWait(currentTime + 5);
		}
		
		System.exit(0);
	}
}
