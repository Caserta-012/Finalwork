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

public class StopMsg implements Msg {
    private int msgType = Msg.STOP_MSG;
    private PlayerClient pc;
    private GameHelper gameHelper;
    private int index;

    public StopMsg(PlayerClient pc){this.pc = pc;}

    public StopMsg(GameHelper gameHelper,int index){
        this.gameHelper = gameHelper;
        this.index = gameHelper.getIndex();
    }


    @Override
    public void send(DatagramSocket ds, String ip, int UDP_PORT) {
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
            Creature c = playerNo == 0 ? pc.getGameHelper().getCalabash(index) : pc.getGameHelper().getEvil(index);
            pc.getGameHelper().getFileHelper().addStop(playerNo,index);
            pc.getGameHelper().setStatus(GameHelper.GAME_STATUS.STOP);
            pc.getGameHelper().stopEvent(c);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
