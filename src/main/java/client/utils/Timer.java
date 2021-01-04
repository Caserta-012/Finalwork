package client.utils;


import client.characters.Creature;
import client.mapObject.BaseObject;
import javafx.scene.canvas.GraphicsContext;

// 定时器类
public class Timer extends BaseObject {
    private OnTimerListener onTimerListener;
    private long timeUnit;
    private long lastTime;
    private boolean isRunning = false;
    private Creature actor = null;

    public void setActor(Creature actor) {
        this.actor = actor;
    }

    public Creature getActor() {
        return actor;
    }

    public Timer(long timeUnit, OnTimerListener onTimerListener ) {
        this.timeUnit = timeUnit;
        this.onTimerListener = onTimerListener;
    }

    public void start() {
        isRunning = true;
        lastTime = System.currentTimeMillis();
        this.setUpdate(true);
    }

    @Override
    public void draw(GraphicsContext gc) {
    }

    public void update() {
        if (isRunning) {
            long nowTime = System.currentTimeMillis();

            if (nowTime - lastTime >= timeUnit) {
                if (onTimerListener != null) {
                    onTimerListener.onTimerRunning(this);
                }
                lastTime = nowTime;
            }
        }
    }

    public void stop() {
        this.setUpdate(false);
        isRunning = false;
    }

    public interface OnTimerListener {
        void onTimerRunning(Timer mTimer);
    }
}