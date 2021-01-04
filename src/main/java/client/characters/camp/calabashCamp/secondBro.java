package client.characters.camp.calabashCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class secondBro extends Creature {
    public secondBro(double x,double y){
        setName("二娃");
        setLocation(x,y);
        setCamp(CAMP.CALABASH);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/secondBro.png")));


        setMax_hp(100);
        setDamage(30);
        setDefence(10);
        setMovement(4);

    }
}
