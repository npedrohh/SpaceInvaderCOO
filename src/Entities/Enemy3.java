package Entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import GameLibPackage.GameLib;

public class Enemy3 extends Enemy implements IEnemy3{

    private static double centerX; // Coordenada X do centro da órbita
    private static double centerY; // Coordenada Y do centro da órbita
    private double radiusMovement; // Raio da órbita
    private double orbitAngle; // Ângulo atual da órbita
    private double orbitSpeed; // Velocidade angular da órbita
    private long lastShotTime; // Tempo do último disparo
    private boolean shootNow; // Indica se o inimigo deve disparar agora

    // Construtor padrão
    public Enemy3() {
        super(ACTIVE, 0.0, 0.0, 0.0, 0.0, 15.0, 
        (double) System.currentTimeMillis(), (double) System.currentTimeMillis() + 500, 
              0.0, 0.0, 0.0, (long) (System.currentTimeMillis() + 2000 + Math.random() * 2000), 3);

        // Define a posição central de órbita do inimigo
        centerX = GameLib.WIDTH / 2;
        centerY = GameLib.HEIGHT / 2;
        radiusMovement = 100.0 + Math.random() * 50.0; // Raio da órbita
        orbitAngle = Math.random() * 2 * Math.PI; // Ângulo inicial da órbita
        orbitSpeed = 0.005; // Velocidade angular da órbita
        lastShotTime = System.currentTimeMillis();
        shootNow = false;
    }

    // Construtor que recebe uma lista de inimigos
    public Enemy3(ArrayList<IEnemy> lastEnemyArray) {
        super(ACTIVE, 0.0, 0.0, 0.0, 0.0, 15.0, 
        (double) System.currentTimeMillis(), (double) System.currentTimeMillis() + 500, 
              0.0, 0.0, 0.0, (long) (System.currentTimeMillis() + 2000 + Math.random() * 2000), 3);
        lastEnemyArray.add(2, this); // Adiciona este inimigo na posição 2 da lista

        // Define a posição central de órbita do inimigo
        centerX = GameLib.WIDTH / 2;
        centerY = GameLib.HEIGHT / 2;
        radiusMovement = 100.0 + Math.random() * 50.0; // Raio da órbita
        orbitAngle = Math.random() * 2 * Math.PI; // Ângulo inicial da órbita
        orbitSpeed = 0.005; // Velocidade angular da órbita
        lastShotTime = System.currentTimeMillis();
        shootNow = false;
    }

    @Override
    public void update(long delta) {
        System.out.println("Enemy3 update");
        // Atualiza o ângulo da órbita
        orbitAngle += orbitSpeed * delta;

        // Calcula a nova posição baseada na órbita
        this.setX(centerX + radiusMovement * Math.cos(orbitAngle));
        this.setY(centerY + radiusMovement * Math.sin(orbitAngle));

        // Verifica se está na hora de disparar
        if (System.currentTimeMillis() - lastShotTime > 1500) {
            this.shootNow = true;
        }
    }

    @Override
    public boolean canShoot(ICharacter player) {
        return this.shootNow; // Retorna se o inimigo deve disparar agora
    }

    @Override
    public IProjectile shot(int i){
        IProjectile bola = new EProjectile(i, i, i, i, i, i);
        return bola;
    }

    @Override
    public IProjectile shot(int i, ICharacter player) {
        // Calcula o ângulo para o jogador
        double angleToPlayer = Math.atan2(player.getY() - this.getY(), player.getX() - this.getX());
        // Cria um novo projétil
        IProjectile ep = new EProjectile(1, this.getX(), this.getY(), 
                                         Math.cos(angleToPlayer) * 0.3, 
                                         Math.sin(angleToPlayer) * 0.3, 2.0);

        // Atualiza o tempo do próximo disparo
        this.setNextShot(System.currentTimeMillis() + 500 + Math.random() * 500);
        this.shootNow = false;
        this.lastShotTime = System.currentTimeMillis();

        return ep; // Retorna o projétil criado
    }

    @Override
    public IEnemy spawn() {
        System.out.println("Enemy3 spawned");
        // Cria um novo inimigo do tipo Enemy3
        IEnemy e = new Enemy3();
        // Define o tempo para o próximo inimigo
        this.setNextEnemy((long) (System.currentTimeMillis() + 3000 + Math.random() * 3000));
        return e; // Retorna o novo inimigo
    }

    @Override
    public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray) {
        // Itera sobre a lista de inimigos
        Iterator<IEnemy> e_iterator = enemyList.iterator();
        while(e_iterator.hasNext()) {
            IEnemy e = e_iterator.next();
            // Atualiza a lista se encontrar um Enemy3
            if(e instanceof Enemy3)
                lastEnemyArray.set(1, e);
        }
    }

    @Override
    public boolean isInside() {
        // Verifica se o inimigo está dentro da tela
        return !(this.getX() < -10 || this.getX() > GameLib.WIDTH + 10 || this.getY() > GameLib.HEIGHT + 10);
    }

    @Override
    public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray){
        // Verifica se pode spawnar um novo inimigo
        return enemyList.isEmpty() || System.currentTimeMillis() > ((IEnemy) lastEnemyArray.get(1)).getNextEnemy();
    }

    @Override
    public void render() {
        // Desenha a explosão se o inimigo está explodindo
        System.out.println("Enemy3 update");
        if (this.getState() == EXPLODING) {
            double alpha = (System.currentTimeMillis() - this.getExplosionStart()) / 
                           (this.getExplosionEnd() - this.getExplosionStart());
            if(alpha > 1) alpha = 1;
            GameLib.drawExplosion(this.getX(), this.getY(), alpha);
        }

        // Desenha o inimigo se ele está ativo
        if (this.getState() == ACTIVE) {
            GameLib.setColor(Color.ORANGE);
            GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
        }
    }
}
