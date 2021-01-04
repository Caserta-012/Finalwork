package client.characters.camp.evilCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class frogSpirit extends Creature {
    public frogSpirit(double x,double y){
        setName("蟾蜍精");
        setLocation(x,y);
        setCamp(CAMP.EVIL);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/frogSpirit.PNG")));


        setMax_hp(80);
        setDamage(30);
        setDefence(15);
        setMovement(4);

    }

}
