package client.client;

import client.helper.GameHelper;
import client.protocol.*;
import server.Server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class NetClient {
    private PlayerClient pc;
    private int UDP_PORT;
    private String serverIP;
    private int severUDP_PORT;
    private DatagramSocket ds = null;

    public NetClient(PlayerClient pc){
        this.pc = pc;
        try{
            this.UDP_PORT = getRandomPORT();
        }catch (Exception e){
            System.exit(0);
        }
    }

    public void send(Msg msg){msg.send(ds,serverIP,severUDP_PORT);}

    public void connect(String ip){
        serverIP = ip;
        Socket s = null;
        try{
            ds = new DatagramSocket(UDP_PORT);
            try {
                s = new Socket(ip, Server.getTcpPort());
            }catch (Exception e){
                e.printStackTrace();
            }
            assert s != null;
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(UDP_PORT);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            int playerId = dis.readInt(); // id 号
            this.severUDP_PORT = dis.readInt();
            this.pc.getGameHelper().setPlayer(playerId == 1 ? GameHelper.PLAYER.CALABASH : GameHelper.PLAYER.EVIL);
            System.out.println("connected to sever successfully.");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (s != null)s.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        new Thread(new UDPThread()).start(); // 开启客户端UDP线程

    }

    public class UDPThread implements Runnable{
        byte[] buf = new byte[1024];

        @Override
        public void run() {
            while (ds != null){
                DatagramPacket dp = new DatagramPacket(buf,buf.length);
                try {
                    ds.receive(dp);
                    parse(dp);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        private void parse(DatagramPacket dp){
            ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,dp.getLength());
            DataInputStream dis = new DataInputStream(bais);
            int msgType = -1;
            try {
                msgType = dis.readInt();
            }catch (IOException e){
                e.printStackTrace();
            }

            Msg msg = null;
            switch (msgType){
                case Msg.TURN_MSG:
                    msg = new TurnMsg(pc);
                    msg.parse(dis);
                    break;
                case Msg.MOVE_MSG:
                    msg = new MoveMsg(pc);
                    msg.parse(dis);
                    break;
                case Msg.ATTACK_MSG:
                    msg = new AttackMsg(pc);
                    msg.parse(dis);
                    break;
                case Msg.ABILITY_MSG:
                    msg = new AbilityMsg(pc);
                    msg.parse(dis);
                    break;
                case Msg.DEATH_MSG:
                    msg = new DeathMsg(pc);
                    msg.parse(dis);
                    break;
                case Msg.STOP_MSG:
                    msg = new StopMsg(pc);
                    msg.parse(dis);
                    break;
                case Msg.WIN_MSG:
                    msg = new WinMsg(pc);
                    msg.parse(dis);
                    break;
                default:
                    System.err.println("No such message.");
                    break;
            }
        }
    }

    public void setUDP_PORT(int UDP_PORT){this.UDP_PORT = UDP_PORT;}

    private int getRandomPORT(){ return 51000 + (int)(Math.random() * 9000);}

}
