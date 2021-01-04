package client.mapObject;

import client.configuration.Configs;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;

public abstract class BaseObject {
    protected DoubleProperty widthProperty = new SimpleDoubleProperty(0);
    protected DoubleProperty heightProperty = new SimpleDoubleProperty(0);
    protected DoubleProperty xProperty = new SimpleDoubleProperty(0);
    protected DoubleProperty yProperty = new SimpleDoubleProperty(0);
    protected BooleanProperty visibleProperty = new SimpleBooleanProperty(true);
    protected BooleanProperty updateProperty = new SimpleBooleanProperty(true);

    protected Configs configs = Configs.getInstance();

    public BaseObject(){}

    public BaseObject(double x, double y, double width,double height){
        this.setWidth(width);
        this.setHeight(height);
        this.setX(x);
        this.setY(y);
    }


    public abstract void draw(GraphicsContext gc);

    public abstract void update();

    public double getWidth() {
        return widthProperty.get();
    }

    public void setWidth(double widthProperty) {
        this.widthProperty.set(widthProperty);
    }

    public double getHeight() {
        return heightProperty.get();
    }

    public void setHeight(double heightProperty) {
        this.heightProperty.set(heightProperty);
    }

    public double getX() {
        return xProperty.get();
    }

    public void setX(double xProperty) {
        this.xProperty.set(xProperty);
    }

    public double getY() {
        return yProperty.get();
    }

    public void setY(double yProperty) {
        this.yProperty.set(yProperty);
    }

    public boolean isVisible() {
        return visibleProperty.get();
    }

    public void setVisible(boolean visibleProperty) {
        this.visibleProperty.set(visibleProperty);
    }

    public boolean isUpdate() {
        return updateProperty.get();
    }

    public void setUpdate(boolean updateProperty) {
        this.updateProperty.set(updateProperty);
    }

    public int getVX(){
        return (int)(getX() / getWidth());
    }

    public int getVY(){
        return (int)(getY() / getHeight());
    }

    public void move(double x,double y){
        double px = Math.max(this.getX() + x, 0.0d);
        double py = Math.max(this.getY() + y, 0.0d);
        px = Math.min(px, configs.getSCENE_WIDTH() - configs.getTILE_WIDTH());
        py = Math.min(py, configs.getSCENE_HEIGHT() - configs.getTILE_HEIGHT());

        this.setX(px);
        this.setY(py);
    }

    public void moveTo(double dx,double dy){
        this.setX(dx);
        this.setY(dy);
    }


    // handle collision
    public boolean isCollisionWith(BaseObject o){
        double x = getX();
        double y = getY();
        double width = getWidth();
        double height = getHeight();
        return x == o.getX() && y == o.getY() && width == o.getWidth() && height == o.getHeight();
    }
}
