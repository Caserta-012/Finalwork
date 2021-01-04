package client.characters.camp.evilCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class snakeSpirit extends Creature {
    public snakeSpirit(double x,double y){
        setName("蛇精");
        setLocation(x,y);
        setCamp(CAMP.EVIL);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/snakeSpirit.PNG")));

        setMax_hp(60);
        setDamage(40);
        setDefence(10);
        setMovement(4);

    }
}
