package client.characters.camp.evilCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class scorpionSpirit extends Creature {
    public scorpionSpirit(double x,double y){
        setName("蝎子精");
        setLocation(x,y);
        setCamp(CAMP.EVIL);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/scorpionSpirit.png")));


        setMax_hp(120);
        setDamage(40);
        setDefence(20);
        setMovement(2);

    }
}
