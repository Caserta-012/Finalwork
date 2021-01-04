package client.characters.actions;

import client.characters.Creature;

public interface Action {
    void attack(Creature c);
    void die();
    void moveTo(double dx,double dy);
    void moveX(double dx);
    void moveY(double dy);
    void useAbility(Creature c);
}
