package Entities;

import java.util.ArrayList;
import java.util.LinkedList;

public interface ICollectable extends IEntity {
    public double getNextCollectable();
    public void setNextCollectable(double next_collectible);
    public void updateLastCollectable(LinkedList<ICollectable> powerUpList, ArrayList<ICollectable> lastPowerUpArray);
    public boolean isSpawnable(LinkedList<ICollectable> powerUpList, ArrayList<ICollectable> lastPowerUpArray);
    public ICollectable spawn();
    public void activate(ICharacter player);
    public void deactivate(ICharacter player);
    public long getStartTime();

    public void update(long delta);

    public boolean isExpired();
    public void explode();
}

