package client.mapObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;

public class WinBar extends BaseObject {
    private TextObject winText;

    public WinBar(int camp){
        super();
        if (camp == 0){
            winText = new TextObject("葫芦娃胜利");
        }
        else winText = new TextObject("妖精胜利");
        setY(configs.getSCENE_HEIGHT() / 2.0 - 50);
        setHeight(100);
        setX(configs.getSCENE_WIDTH() / 2.0 - 200);
        setWidth(400);
        winText.setFont(Font.font("FangSong",60));
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(Color.GRAY);
        gc.setGlobalAlpha(0.8f);
        gc.fillRect(getX(),getY(),getWidth(),getHeight());
        winText.setX((getWidth() - winText.getWidth()) / 2 + getX());  // 居中
        winText.setY(getY() + 5 + winText.getHeight());
        winText.draw(gc);
        gc.restore();

    }

    @Override
    public void update() {

    }
}
