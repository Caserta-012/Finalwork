package client.client;

import client.applications.BaseApplication;
import client.draw.MapScreen;
import client.helper.GameHelper;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.Server;

import java.io.File;


public class PlayerClient extends BaseApplication{
    private NetClient nc = new NetClient(this);
    private GameHelper gameHelper = new GameHelper(this);

    public NetClient getNc() {
        return nc;
    }

    public static void main(String[] args) {
        launch(args);
    }


    protected void loadBefore() {
        stage.setTitle("Huluwa Game");
    }

    protected void loadAfter() {
        ImageView bg = new ImageView(new Image(getClass().getResourceAsStream("/background.png")));
        bg.setFitWidth(configs.getSCENE_WIDTH());
        bg.setFitHeight(configs.getSCENE_HEIGHT());
        getGroup().getChildren().add(bg);

        Image netmode = new Image(getClass().getResourceAsStream("/twoPlayer.png"));
        Image stress = new Image(getClass().getResourceAsStream("/twoPlayerS.png"));
        ImageView playTitle = new ImageView(netmode);
        playTitle.setFitWidth(configs.getTITLE_WIDTH());
        playTitle.setFitHeight(configs.getTITLE_HEIGHT());
        playTitle.setX(configs.getSCENE_WIDTH() / 2.0 - configs.getTITLE_WIDTH() / 2.0);
        playTitle.setY(-10);
        playTitle.setOnMouseMoved(event -> playTitle.setImage(stress));
        playTitle.setOnMouseExited(event -> playTitle.setImage(netmode));
        playTitle.setOnMouseClicked(event -> {
            netMode();
        });
        getGroup().getChildren().add(playTitle);

        Image readfile = new Image(getClass().getResourceAsStream("/readFile.png"));
        Image readS = new Image(getClass().getResourceAsStream("/readFileS.png"));
        ImageView readTitle = new ImageView(readfile);
        readTitle.setFitWidth(configs.getTITLE_WIDTH());
        readTitle.setFitHeight(configs.getTITLE_HEIGHT());
        readTitle.setX(configs.getSCENE_WIDTH() / 2.0 - configs.getTITLE_WIDTH() / 2.0);
        readTitle.setY(170);
        readTitle.setOnMouseMoved(event -> readTitle.setImage(readS));
        readTitle.setOnMouseExited(event -> readTitle.setImage(readfile));
        readTitle.setOnMouseClicked(event -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(getStage());
            if (file!=null){
                autoMode(file.getPath());
            }
        });
        getGroup().getChildren().add(readTitle);


    }

    @Override
    protected void showStage(Stage stage) {
        super.showStage(stage);
    }

    public GameHelper getGameHelper() {
        return gameHelper;
    }

    public Stage getStage(){return this.stage;}

    private void autoMode(String filePath){
        this.gameHelper.setAUTO_MODE(true);
        this.gameHelper.getAutoPlayer().setFilePath(filePath);
        MapScreen mapScreen = new MapScreen(configs.getSCENE_WIDTH(),configs.getSCENE_HEIGHT(),this.gameHelper);
        getGroup().getChildren().add(mapScreen);
        mapScreen.start();
        mapScreen.initEvents();
        new Thread(gameHelper.getAutoPlayer()).start();
    }

    private void netMode(){
        ImageView saveimg = new ImageView(new Image(getClass().getResourceAsStream("/save.PNG")));
        saveimg.setFitWidth(configs.getTILE_WIDTH()/2.0);
        saveimg.setFitHeight(configs.getTILE_HEIGHT()/2.0);
        Label save = new Label("",saveimg);
        save.setLayoutX(configs.getSCENE_WIDTH() - saveimg.getFitWidth());
        save.setOnMouseClicked((MouseEvent)->{
            FileChooser chooser = new FileChooser();
            File file = chooser.showSaveDialog(getStage());
            if (file != null) {
                this.gameHelper.getFileHelper().saveFile(file.getPath() + ".xml");
            }
        });
        MapScreen mapScreen = new MapScreen(configs.getSCENE_WIDTH(),configs.getSCENE_HEIGHT(),this.gameHelper);
        getGroup().getChildren().add(mapScreen);
        getGroup().getChildren().add(save);
        mapScreen.start();
        mapScreen.initEvents();
        this.nc.connect("127.0.0.1");
    }

}
