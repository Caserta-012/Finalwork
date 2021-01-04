package client.characters.camp.evilCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

// 蜈蚣精
public class chilopodSpirit extends Creature {
    public chilopodSpirit(double x,double y){
        setName("蜈蚣精");
        setLocation(x,y);
        setCamp(CAMP.EVIL);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/chilopodSpirit.png")));


        setMax_hp(110);
        setDamage(30);
        setDefence(20);
        setMovement(3);

    }
}
