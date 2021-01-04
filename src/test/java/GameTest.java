import client.client.PlayerClient;
import client.helper.GameHelper;
import client.mapObject.BlockObject;
import org.junit.Test;
import client.helper.DistanceHelper;

import java.util.ArrayList;

public class GameTest {
    @Test
    public void testDisHelper(){
    DistanceHelper helper = new DistanceHelper();
    BlockObject curPlace = new BlockObject(0,0,null);
    ArrayList<BlockObject> res = helper.getMovableDis(curPlace,8);
    int[][] map = new int[8][15];
    System.out.println(res.size());
    for (BlockObject b : res){
        map[b.getVY()][b.getVX()] = 1;
    }
    for (int i = 0;i < 8;i++){
        for (int j = 0 ;j < 15; j++){
            System.out.print(map[i][j] + " ");
        }
        System.out.println();
        }
    }

    @Test
    public void testFileHelper(){
        String filePath = "../src/main/resources/test.xml";
        new GameHelper().getAutoPlayer().setFilePath(filePath);
    }
}