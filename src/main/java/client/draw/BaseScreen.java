package client.draw;

import client.configuration.Configs;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import client.mapObject.BaseObject;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen extends Canvas {
    protected List<BaseObject> mObjects = new ArrayList<BaseObject>();
    private Timeline timeline;
    private static final int duration = 50;
    protected Configs configs = Configs.getInstance();

    public BaseScreen(double width, double height){
        super(width,height);
        initTimeLine();
    }

    protected abstract void onKeyPressed(KeyEvent keyEvent);

    public void initEvents(){
        getParent().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                onKeyPressed(event);
            }
        });

    }


    // 使用Timeline对象实现刷新
    private void initTimeLine(){
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // 循环刷新
                draw(getGraphicsContext2D());
                update();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
    }

    private void draw(GraphicsContext gc){
        gc.clearRect(0,0,getWidth(),getHeight());
        for (BaseObject o : mObjects){
            if (o.isVisible())
                o.draw(gc);
        }
    }

    private void update(){
        for (BaseObject o : mObjects){
            if (o.isUpdate()){
                o.update();
            }
        }
    }

    public void addObject(BaseObject o){
        this.mObjects.add(o);
    }

    public void removeObject(BaseObject o){
        this.mObjects.remove(o);
    }

    public void start(){
        timeline.play();
    }

    public void pause(){
        timeline.pause();
    }

    public void stop(){
        timeline.stop();
    }

}
