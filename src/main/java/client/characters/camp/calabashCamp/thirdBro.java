package client.characters.camp.calabashCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class thirdBro extends Creature {
    public thirdBro(double x,double y){
        setName("三娃");
        setLocation(x,y);
        setCamp(CAMP.CALABASH);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/thirdBro.png")));


        setMax_hp(110);
        setDamage(30);
        setDefence(20);
        setMovement(2);

    }
}
