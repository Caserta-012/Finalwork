package client.configuration;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;


// 属性类，服从单例模式设计
public class Configs {
    final int WINDOW_WIDTH = 680;
    final int WINDOW_HEIGHT = 480;
    final int SCENE_WIDTH = 1500;
    final int SCENE_HEIGHT = 800;
    final int TILE_WIDTH = 100;
    final int TILE_HEIGHT = 100;
    final int MAP_WIDTH = SCENE_WIDTH / TILE_WIDTH;
    final int MAP_HEIGHT = SCENE_HEIGHT / TILE_HEIGHT;
    final Font DEFAULT_FONT = Font.font("FangSong",17);
    final double FONT_SIZE = 16;
    final Paint FONT_BG_COLOR = Color.BLACK;
    final Paint FONT_COLOR = Color.WHITE;
    final long TIME_UNIT = 80;
    final Paint MOVE_RANGE_COLOR = Color.BLUE;
    final Paint ATTACK_RANGE_COLOR = Color.RED;
    final Paint ABILITY_RANGE_COLOR = Color.YELLOW;
    final int PROPERTY_WIDTH = TILE_WIDTH;
    final int PROPERTY_HEIGHT  = 2 * TILE_HEIGHT - 30;
    final int FLASH_NUM = 10;
    final int TITLE_WIDTH = 600;
    final int TITLE_HEIGHT = 250;
    private static Configs configs = new Configs();

    // 单例模式设计
    private Configs(){
    }

    public static Configs getInstance(){
        return configs;
    }

    public int getSCENE_WIDTH() {
        return SCENE_WIDTH;
    }

    public int getSCENE_HEIGHT() {
        return SCENE_HEIGHT;
    }

    public int getWINDOW_WIDTH() {
        return WINDOW_WIDTH;
    }

    public int getWINDOW_HEIGHT() {
        return WINDOW_HEIGHT;
    }

    public int getTILE_WIDTH() {
        return TILE_WIDTH;
    }

    public int getTILE_HEIGHT() {
        return TILE_HEIGHT;
    }

    public Font getDEFAULT_FONT() {
        return DEFAULT_FONT;
    }

    public double getFONT_SIZE() {
        return FONT_SIZE;
    }

    public Paint getFONT_BG_COLOR() {
        return FONT_BG_COLOR;
    }

    public Paint getFONT_COLOR() {
        return FONT_COLOR;
    }

    public long getTIME_UNIT() {
        return TIME_UNIT;
    }

    public Paint getMOVE_RANGE_COLOR() {
        return MOVE_RANGE_COLOR;
    }

    public Paint getATTACK_RANGE_COLOR() {
        return ATTACK_RANGE_COLOR;
    }

    public Paint getABILITY_RANGE_COLOR() {
        return ABILITY_RANGE_COLOR;
    }

    public int getMAP_WIDTH() {
        return MAP_WIDTH;
    }

    public int getMAP_HEIGHT() {
        return MAP_HEIGHT;
    }

    public int getPROPERTY_WIDTH() {
        return PROPERTY_WIDTH;
    }

    public int getPROPERTY_HEIGHT() {
        return PROPERTY_HEIGHT;
    }

    public int getFLASH_NUM() {
        return FLASH_NUM;
    }

    public int getTITLE_WIDTH() {
        return TITLE_WIDTH;
    }

    public int getTITLE_HEIGHT() {
        return TITLE_HEIGHT;
    }
}
