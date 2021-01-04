package client.draw;

import client.configuration.Configs;
import client.helper.GameHelper;
import client.mapObject.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;


public class MapScreen extends BaseScreen {
    Configs configs = Configs.getInstance();
    private GraphicsContext gc = getGraphicsContext2D();
    private GameHelper gameHelper;


    public MapScreen(int width, int height, GameHelper gameHelper){
        super(width,height);
        gameHelper.setmObject(this.mObjects);
        this.gameHelper = gameHelper;
        drawMap();

    }


    private void drawMap(){
        Image image = new Image(getClass().getResourceAsStream("/grassLand.png"));
        for (int i = 0 ;i < configs.getMAP_WIDTH();i++){
            for (int j = 0 ;j < configs.getMAP_HEIGHT();j++){
                this.mObjects.add(new BlockObject(i * configs.getTILE_WIDTH(),j * configs.getTILE_HEIGHT(),image));
            }
        }

        image = new Image(getClass().getResourceAsStream("/chooseRect.png"));
        ChooseRect rect = new ChooseRect(0,0,configs.getTILE_WIDTH(),configs.getTILE_HEIGHT(),image);
        mObjects.add(rect);
        this.gameHelper.setChooseRect(rect);

        initCreature();

        ActionMenu actionMenu = new ActionMenu();
        actionMenu.setVisible(false);
        gameHelper.setActionMenu(actionMenu);
        mObjects.add(actionMenu);

        PropertyMenu propertyMenu = new PropertyMenu(configs.getPROPERTY_WIDTH(),configs.getPROPERTY_HEIGHT());
        propertyMenu.setVisible(false);
        gameHelper.setPropertyMenu(propertyMenu);
        mObjects.add(propertyMenu);

        TurnBar turnBar = new TurnBar();
        gameHelper.setTurnBar(turnBar);
        mObjects.add(turnBar);

        mObjects.add(gameHelper.getMoveTimer());

    }

    protected void onKeyPressed(KeyEvent keyEvent) {
        gameHelper.onKeyPressed(keyEvent);
    }



    private void initCreature(){
        gameHelper.initCreature();
        mObjects.addAll(gameHelper.getCalabashCamp());
        mObjects.addAll(gameHelper.getEvilCamp());
    }
}
