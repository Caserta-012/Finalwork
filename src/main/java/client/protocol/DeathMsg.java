package client.protocol;

import client.characters.Creature;
import client.client.PlayerClient;
import client.helper.GameHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class DeathMsg implements Msg{
    private int msgType = Msg.DEATH_MSG;
    private PlayerClient pc;
    private GameHelper gameHelper;
    private int index = -1;

    public DeathMsg(PlayerClient pc){this.pc = pc;}

    // 默认死亡只发生在当前玩家回合（即当前回合只会发生敌方角色死亡）
    public DeathMsg(GameHelper gameHelper,int index){
        this.gameHelper = gameHelper;
        this.index = index;
    }


    @Override
    public void send(DatagramSocket ds, String ip, int UDP_PORT) {
        if (index == -1)return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeInt(gameHelper.getPlayer().equals(GameHelper.PLAYER.CALABASH) ? 0 : 1);
            dos.writeInt(index);
        }catch (IOException e){
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket dp = new DatagramPacket(buf, buf.length,new InetSocketAddress(ip, UDP_PORT));
            ds.send(dp);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parse(DataInputStream dis) {
        try {
            int playerNo = dis.readInt();
            int index = dis.readInt();
            Creature c = playerNo == 1 ? pc.getGameHelper().getCalabash(index) : pc.getGameHelper().getEvil(index);
            pc.getGameHelper().getFileHelper().addDeath(playerNo == 0 ? 1 : 0,index);
            pc.getGameHelper().deathEvent(c);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
