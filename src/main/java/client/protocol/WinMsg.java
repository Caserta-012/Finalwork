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

public class WinMsg implements Msg {
    private int msgType = Msg.WIN_MSG;
    private PlayerClient pc;
    private GameHelper gameHelper;
    private int camp;

    public WinMsg(PlayerClient pc){this.pc = pc;}

    public WinMsg(GameHelper gameHelper,int camp){
        this.gameHelper = gameHelper;
        this.camp = camp;
    }


    @Override
    public void send(DatagramSocket ds, String ip, int UDP_PORT) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeInt(camp);
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
            int camp = dis.readInt();
            pc.getGameHelper().getFileHelper().addWin(camp);
            pc.getGameHelper().winEvent(camp);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
