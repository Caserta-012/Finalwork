package client.characters.camp.calabashCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class bigBro extends Creature {
    public bigBro(double x,double y){
        setName("大娃");
        setLocation(x,y);
        setCamp(CAMP.CALABASH);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/bigBro.png")));

        setMax_hp(120);
        setDamage(40);
        setDefence(10);
        setMovement(3);
    }
}
