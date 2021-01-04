package client.mapObject;

import client.characters.Creature;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class ActionMenu extends BaseObject {
    public enum ACTION{
        MOVE,
        ATTACK,
        ABILITY,
        STOP
    }
    private ArrayList<TextObject> textContainer;
    private int spaceLine = 6;
    private Paint bgColor = configs.getFONT_BG_COLOR();
    private int index = 0;

    public ActionMenu(){
        super();
        setWidth(60);
    }

    public void initIndex(Creature creature){
        this.index = 0;
        setLocation(creature.getX(),creature.getY());
        textContainer = new ArrayList<>();
        if (creature.getCanMove())
            textContainer.add(new TextObject("移动"));
        if (creature.getCanAttack())
            textContainer.add(new TextObject("攻击"));
        if (creature.getCanUseAbility())
            textContainer.add(new TextObject("技能"));
        textContainer.add(new TextObject("待机"));
        setHeight(textContainer.size() * 29);
    }

    public void onKeyPressed(KeyEvent keyEvent){
        switch (keyEvent.getCode()){
            case W:
                index = (index - 1 + textContainer.size()) % textContainer.size() ;
                break;
            case S:
                index = (index + 1) % textContainer.size();
                break;
        }
    }

    public void onMouseClicked(MouseEvent mouseEvent){
        for (TextObject text : textContainer){
            if (text.isCollisionWith(mouseEvent.getX(),mouseEvent.getY())){
                index = textContainer.indexOf(text);
            }
        }
    }

   public void setLocation(double x,double y){
        if (x + getWidth() + configs.getTILE_WIDTH() > configs.getSCENE_WIDTH()){
            x = x - getWidth();
        }
        else x = x + configs.getTILE_WIDTH();

        if (y + getHeight() > configs.getSCENE_HEIGHT()){
            y = y - getHeight();
        }
        setX(x);
        setY(y);
   }


    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(bgColor);
        gc.setGlobalAlpha(0.7f);
        gc.fillRect(getX(),getY(),getWidth(),getHeight());
        if (textContainer != null) {
            for (int i = 0; i < textContainer.size(); i++) {
                TextObject text = textContainer.get(i);
                if (index == i)text.setFontColor(Color.RED);
                else text.setFontColor(configs.getFONT_COLOR());
                text.setX((getWidth() - text.getWidth()) / 2 + getX());  // 居中
                text.setY(getY() + spaceLine * (i + 1) + text.getHeight() * (i + 1));
                text.draw(gc);
            }
        }
        gc.restore();
    }

    public void update() {

    }

    public ACTION getChoose(){
        switch (textContainer.get(index).getText()){
            case "移动":
                return ACTION.MOVE;
            case "攻击":
                return ACTION.ATTACK;
            case "技能":
                return ACTION.ABILITY;
            case "待机":
                return ACTION.STOP;
            default:
                return null;
        }
    }
}
