package client.mapObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class ChooseRect extends BaseObject {
    private Image image;
    public ChooseRect(double x, double y, double width, double height, Image image){
        super(x,y,width,height);
        this.image = image;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(this.image,0,0,image.getWidth(),image.getHeight(),getX(),getY(),getWidth(),getHeight());
    }

    public void update() {
    }

}
