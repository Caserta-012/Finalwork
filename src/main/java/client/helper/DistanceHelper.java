package client.helper;

import client.configuration.Configs;
import client.mapObject.BlockObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class DistanceHelper {
    private Configs configs = Configs.getInstance();
    private int mapWidth = configs.getSCENE_WIDTH() / configs.getTILE_WIDTH();
    private int mapHeight = configs.getSCENE_HEIGHT() / configs.getTILE_HEIGHT();
    private BlockObject[][] map = new BlockObject[mapWidth][mapHeight];
    private ArrayList<BlockObject> res;
    private Queue<BlockObject> queue;

    public DistanceHelper(){
        for (int i = 0 ;i < this.mapWidth;i++){
            for (int j = 0;j < this.mapHeight;j++){
                map[i][j] = new BlockObject(i * configs.getTILE_WIDTH(),j * configs.getTILE_HEIGHT(),null);
            }
        }
    }

    public ArrayList<BlockObject> getMovableDis (BlockObject curPlace, int range){
        queue = new LinkedList<>();
        queue.offer(curPlace);
        res = new ArrayList<>();
        findDis(0,range);
        return res;
    }

    // find distance by BFS
    private void findDis (int nowRange, int maxRange){
        if (queue.isEmpty()) return;
        BlockObject thisBlock = queue.poll();
        int VX = thisBlock.getVX();
        int VY = thisBlock.getVY();
        if (!res.contains(map[VX][VY]))res.add(map[VX][VY]);

        if (VX + 1 < mapWidth  && nowRange + 1 <= maxRange)
        {
            queue.add(map[VX + 1][VY]);
            findDis(nowRange + 1,maxRange);
        }
        if (VX - 1 >= 0  && nowRange + 1 <= maxRange){
            queue.add(map[VX - 1][VY]);
            findDis(nowRange + 1,maxRange);
        }
        if (VY + 1 < mapHeight  && nowRange + 1 <= maxRange)
        {
            queue.add(map[VX][VY + 1]);
            findDis(nowRange + 1,maxRange);
        }
        if (VY - 1 >= 0 && nowRange + 1 <= maxRange){
            queue.add(map[VX][VY - 1]);
            findDis(nowRange + 1,maxRange);
        }

    }

}
