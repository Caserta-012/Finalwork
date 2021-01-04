package client.characters.camp.calabashCamp;

import client.characters.Creature;
import javafx.scene.image.Image;

public class fifthBro extends Creature {
    public fifthBro(double x,double y){
        setName("五娃");
        setLocation(x,y);
        setCamp(CAMP.CALABASH);
        setImage(new Image(getClass().getResourceAsStream("/characterImages/fifthBro.png")));


        setMax_hp(100);
        setDamage(30);
        setDefence(20);
        setMovement(3);

    }
}
