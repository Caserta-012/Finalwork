package client.mapObject;

import client.characters.Creature;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;


public class PropertyMenu extends BaseObject {
    private TextObject[] textContainer;
    private int spaceLine = 6;
    private Paint bgColor = configs.getFONT_BG_COLOR();

    public PropertyMenu(int width,int height){
        setWidth(width);
        setHeight(height);
        setX(0);
        setY(0);
        textContainer = new TextObject[6];
        for (int i = 0;i < textContainer.length;i++)
            textContainer[i] = new TextObject();
    }


    public void initCreature(Creature creature){
        textContainer[0].setText("姓名:" + creature.getName());
        textContainer[1].setText("HP:" + creature.getHp() + "/" + creature.getMax_hp());
        textContainer[2].setText("攻击力:" + creature.getDamage());
        textContainer[3].setText("守备力:" + creature.getDefence());
        textContainer[4].setText("移动力:" + creature.getMovement());
        String canAbility = creature.getCanUseAbility() ? "是" : "否";
        textContainer[5].setText("技能可用:" + canAbility);
    }


    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(bgColor);
        gc.setGlobalAlpha(0.7f);
        gc.fillRoundRect(getX(),getY(),getWidth(),getHeight(),20,20);
        if (textContainer != null) {
            for (int i = 0; i < textContainer.length; i++) {
                textContainer[i].setX((getWidth() - textContainer[i].getWidth()) / 2 + getX());  // 居中
                textContainer[i].setY(getY() + spaceLine * (i + 1) + textContainer[i].getHeight() * (i + 1));
                textContainer[i].draw(gc);
            }
        }
        gc.restore();
    }

    public void update() {

    }
}
