import java.awt.Color;
import java.util.*;
import Entities.*;
import GameLibPackage.*;

public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	
	// sseria bom deletar essas constantes da main!!!
	
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

			if(player.getState()==INACTIVE){
				System.out.println("inativo");;

			}
			
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

			/* mostra tela de GameOver e termina o jogo após 5s */
			if (player.getState()==INACTIVE && currentTime > player.getExplosionEnd() + 5000){
				running = false;

			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			
			player.update(delta);
			
			if(player.getState() == ACTIVE){
				
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
