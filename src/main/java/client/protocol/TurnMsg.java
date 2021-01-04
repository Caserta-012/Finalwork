package client.protocol;

import client.client.PlayerClient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TurnMsg implements Msg{
    private int msgType  = Msg.TURN_MSG;
    private PlayerClient pc;
    private Boolean changeFlag = false;

    public TurnMsg(PlayerClient pc){this.pc = pc;}

    public TurnMsg(){
        this.changeFlag = true;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int UDP_PORT) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeBoolean(changeFlag);
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
            boolean changeFlag = dis.readBoolean();
            if (changeFlag){
                this.pc.getGameHelper().changeTurn();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
