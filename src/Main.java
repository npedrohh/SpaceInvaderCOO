/*********************************************************************/
/**   ACH2003 - Computação Orientada a Objetos                      **/
/**   EACH-USP - Primeiro Semestre de 2024                          **/
/**   Turma 04 - Prof. Flávio Coutinho                              **/
/**                                                                 **/
/**   Exercicio-Programa                                            **/
/**                                                                 **/
/**   Pedro Henrique Resnitzky Barbedo            14657691          **/
/**   Aline Crispim de Moraes                     14567051          **/
/**   Daniela Yumi Takara                         13659997          **/
/**   Giovanna Almeida Albuquerque                13657515          **/
/**                                                                 **/
/*********************************************************************/

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
		
		/* variáveis dos coletáveis */

		/* PowerUp1 */

		LinkedList<ICollectable> powerUp1List = new LinkedList<>();
		ArrayList<ICollectable> lastPowerUp1Array = new ArrayList<>();
		ICollectable apu1 = new PowerUp1();
		final int MAX_POWERUPS1 = 1;

		// LinkedList<ICollectable> powerUp2List = new LinkedList<>();

		/* variáveis dos inimigos tipo 2 */
		

		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		LinkedList<IProjectile> e_projectileList = new LinkedList<>();
		
		/* estrelas que formam o fundo de primeiro plano */
		
		Background background1 = new Background(0.070);
		
		/* estrelas que formam o fundo de segundo plano */
		
		Background background2 = new Background(0.045);
		
		/* inicializações */
		
		background1.initialize();
		background2.initialize();
						
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
					
					if(player.isColliding(p)) {player.explode(); player.setBuff(0); apu1.deactivate(player);}
				}
			
				/* colisões player - inimigos */
					
				for(IEnemy e : enemyList) {
					
					if(player.isColliding(e)) {player.explode(); player.setBuff(0); apu1.deactivate(player);}
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
			
			/* power-ups (PowerUp1) */

			Iterator<ICollectable> pu1_iterator = powerUp1List.iterator();

			while (pu1_iterator.hasNext()) {
				
    			ICollectable pu1 = pu1_iterator.next();
    
				if (pu1.getState() == ACTIVE){

					if (player.getBuff() == 1 && apu1.isExpired() == true){
						player.setBuff(0);
						System.out.println("deactivated1");
						apu1.deactivate(player);	
					}

					if (player.isColliding(pu1) && player.getBuff() != 1){
						apu1 = pu1;
						apu1.activate(player);
						player.setBuff(1);

						pu1_iterator.remove();
					}else{

						pu1.update(delta/5);

						if(!pu1.isInside()) {
							
							pu1_iterator.remove();
						}
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

			if (lastPowerUp1Array.isEmpty()) {

				ICollectable pu1 = new PowerUp1(lastPowerUp1Array);

				pu1.setNextCollectable(currentTime + 5000);

			}
			Iterator<IEnemy> le_iterator = lastEnemyArray.iterator();
			
			while(le_iterator.hasNext()) {
			
			IEnemy le = le_iterator.next();	
						
			le.updateLastEnemy(enemyList, lastEnemyArray);
			
				if(le.isSpawnable(enemyList, lastEnemyArray)) {
					
					enemyList.add(le.spawn());
				}
			}

			Iterator<ICollectable> lpu1_iterator = lastPowerUp1Array.iterator();

			if(lpu1_iterator.hasNext() && powerUp1List.size() < MAX_POWERUPS1){
			
				ICollectable lpu1 = lpu1_iterator.next();	
						
				lpu1.updateLastCollectable(powerUp1List, lastPowerUp1Array);
			
				if(lpu1.isSpawnable(powerUp1List, lastPowerUp1Array)) {
					
					powerUp1List.add(lpu1.spawn());
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
			
			
			background2.render(Color.DARK_GRAY, delta, 2);
			
			/* desenhando plano de fundo próximo */
			
			background1.render(Color.GRAY, delta, 3);
			
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

			/* desenhando powerUp1 */

			for (ICollectable pu1 : powerUp1List) {
				pu1.render();
			}

			
			/* chamama a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 5 ms. */
			
			busyWait(currentTime + 5);
		}
		
		System.exit(0);
	}
}
