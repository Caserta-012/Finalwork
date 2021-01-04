package client.characters.camp.calabashCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class forthBro extends Creature {
    public forthBro(double x,double y){
        setName("四娃");
        setLocation(x,y);
        setCamp(CAMP.CALABASH);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/forthBro.png")));

        setMax_hp(80);
        setDamage(50);
        setDefence(10);
        setMovement(3);

    }
}
