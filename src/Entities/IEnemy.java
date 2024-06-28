package Entities;

import java.util.ArrayList;
import java.util.LinkedList;

public interface IEnemy extends ICharacter {
	public double getNextEnemy();
	public int getProjectileAmount();
	public void setNextEnemy(long next_enemy);
	public boolean canShoot(ICharacter player);
	public IProjectile shot(int i);
	public void updateLastEnemy(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray);
	public boolean isSpawnable(LinkedList<IEnemy> enemyList, ArrayList<IEnemy> lastEnemyArray);
	public IEnemy spawn();
}
