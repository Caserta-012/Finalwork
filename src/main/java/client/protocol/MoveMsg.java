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

public class MoveMsg implements Msg {
    private int msgType = Msg.MOVE_MSG;
    private PlayerClient pc;
    private GameHelper gameHelper;
    private int index;
    private int VX;
    private int VY;

    public MoveMsg(PlayerClient pc){this.pc = pc;}

    public MoveMsg(GameHelper gameHelper,int VX,int VY){
        this.gameHelper = gameHelper;
        this.index = gameHelper.getIndex();
        this.VX = VX;
        this.VY = VY;
    }


    @Override
    public void send(DatagramSocket ds, String ip, int UDP_PORT) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeInt(gameHelper.getPlayer().equals(GameHelper.PLAYER.CALABASH) ? 0 : 1);
            dos.writeInt(index);
            dos.writeInt(VX);
            dos.writeInt(VY);
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
            int VX = dis.readInt();
            int VY = dis.readInt();
            pc.getGameHelper().getFileHelper().addMove(playerNo,index,VX,VY);
            pc.getGameHelper().getMoveTimer().setActor(c);
            pc.getGameHelper().setMoveToX(VX);
            pc.getGameHelper().setMoveToY(VY);
            pc.getGameHelper().setStatus(GameHelper.GAME_STATUS.MOVE);
            pc.getGameHelper().getMoveTimer().start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
