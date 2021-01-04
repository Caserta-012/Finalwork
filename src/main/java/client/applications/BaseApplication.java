package client.applications;

import client.configuration.Configs;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseApplication extends Application {
    private Group mGroup;
    private Scene mScene;
    protected Configs configs = Configs.getInstance();
    protected Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        loadBefore();
        mGroup = new Group();
        mScene = new Scene(mGroup,configs.getSCENE_WIDTH(),configs.getSCENE_HEIGHT());
        loadAfter();
        showStage(primaryStage);
    }

    protected abstract void loadBefore();

    protected abstract void loadAfter();

    protected Scene getScene(){
        return mScene;
    }

    public Group getGroup() {
        return mGroup;
    }

    protected void showStage(Stage stage){
        stage.setScene(this.mScene);
        stage.show();
    }
}
