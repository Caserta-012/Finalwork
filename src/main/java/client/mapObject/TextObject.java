package client.mapObject;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;


public class TextObject extends BaseObject {
    private Font font = configs.getDEFAULT_FONT();
    private double fontSize = configs.getFONT_SIZE();
    private Paint fontColor = configs.getFONT_COLOR();
    private String text;

    public TextObject(){

    }

    public TextObject(String str){
        this.setText(str);
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFont(this.font);
        gc.setFill(this.fontColor);
        if (text != null){
            gc.fillText(text,getX(),getY());
        }
        gc.restore();
    }

    public void update() {

    }

    public double getWidth(){
        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(this.font);
        return fm.computeStringWidth(text);
    }

    public double getHeight(){
        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(this.font);
        return  fm.getLineHeight();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCollisionWith(double x,double y){
        if(x > getX() && y > getY() - getHeight() && x < getX() + getWidth() && y < getY()){
            return true;
        }
        return false;
    }

    public Paint getFontColor() {
        return fontColor;
    }

    public void setFontColor(Paint fontColor) {
        this.fontColor = fontColor;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
