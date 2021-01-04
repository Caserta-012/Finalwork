package client.mapObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

public class BlockObject extends BaseObject {
    public enum B_TYPE {
        MOVE,
        ATTACK,
        ABILITY,
        BACKGROUND
    }

    private B_TYPE blockType = B_TYPE.MOVE;
    private Paint moveColor = configs.getMOVE_RANGE_COLOR();
    private Paint attackColor = configs.getATTACK_RANGE_COLOR();
    private Paint abilityColor = configs.getABILITY_RANGE_COLOR();
    private Image image = null;

    public BlockObject(double x, double y, Image image){
        super();
        setX(x);
        setY(y);
        setWidth(configs.getTILE_WIDTH());
        setHeight(configs.getTILE_HEIGHT());
        if (image != null){
            this.image = image;
            setBlockType(B_TYPE.BACKGROUND);
        }
    }



    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        switch (getBlockType()){
            case MOVE:
                gc.setFill(this.moveColor);
                break;
            case ATTACK:
                gc.setFill(this.attackColor);
                break;
            case ABILITY:
                gc.setFill(this.abilityColor);
                break;
            default:
                break;
        }
        if (!getBlockType().equals(B_TYPE.BACKGROUND)) {
            gc.setGlobalAlpha(0.4f);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
        }
        else {
            gc.drawImage(this.image,0,0,image.getWidth(),image.getHeight(),getX(),getY(),getWidth(),getHeight());
        }
        gc.restore();
    }

    @Override
    public void update() {

    }

    public B_TYPE getBlockType() {
        return blockType;
    }

    public void setBlockType(B_TYPE blockType) {
        this.blockType = blockType;
    }
}
