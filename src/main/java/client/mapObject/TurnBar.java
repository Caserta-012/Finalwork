package client.mapObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TurnBar extends BaseObject {
    private TextObject turnMsg;

    public TurnBar(){
        super();
        setX(10);
        setY(configs.getSCENE_HEIGHT() - 40);
        setHeight(30);
        setWidth(120);
    }

    public void setCamp(int camp){
        if (camp == 0){
            this.turnMsg = new TextObject("葫芦娃回合");
        }
        else {
            this.turnMsg = new TextObject("妖精回合");
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(0.5f);
        gc.fillRect(getX(),getY(),getWidth(),getHeight());
        turnMsg.setX((getWidth() - turnMsg.getWidth()) / 2 + getX());  // 居中
        turnMsg.setY(getY() +2 + turnMsg.getHeight());
        turnMsg.draw(gc);
        gc.restore();
    }

    @Override
    public void update() {

    }
}
