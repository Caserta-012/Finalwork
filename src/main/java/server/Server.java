package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

// 服务器类
public class Server {
    private static final int TCP_PORT = 50001; // TCP端口号
    private static final int UDP_PORT = 60001; // 转发客户端数据的UDP端口号
    private List<Client> clients = new ArrayList<>();
    private static Server serverInstance = new Server();
    private boolean isRunning = false;
    private final int playNum = 1;

    public static int getTcpPort() {
        return TCP_PORT;
    }

    public Boolean isRunning(){return this.isRunning;}

    private Server(){
    }

    public static Server getInstance(){
        return serverInstance;
    }

    public static void main(String[] ags){
        getInstance().run();
    }

    public void run(){
        new Thread(new UDPThread()).start();
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(TCP_PORT);
            System.out.println("Server has started.");
        }catch (IOException e){
            e.printStackTrace();
        }

        isRunning = true;
        int players = 0;
        while (true){
            Socket s = null;
            try {
                assert ss != null;
                s = ss.accept();
                System.out.println("client:" + (++players) + " has connected." );
                DataInputStream dis = new DataInputStream(s.getInputStream());
                int UDP_PORT_NO = dis.readInt();
                Client client = new Client(s.getInetAddress().getHostAddress(),UDP_PORT_NO,players);
                clients.add(client);

                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(players);
                dos.writeInt(this.UDP_PORT);
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    if (s != null)s.close();
                }catch (IOException ef){
                    ef.printStackTrace();
                }
            }
        }

    }

    private class UDPThread implements Runnable{
        byte[] buf = new byte[1024];


        @Override
        public void run() {
            DatagramSocket ds = null;
            try{
                ds = new DatagramSocket(UDP_PORT);
            }catch (SocketException e){
                e.printStackTrace();
            }

            while (ds != null) // 连接上转发服务器
            {
                DatagramPacket dp = new DatagramPacket(buf,buf.length);
                try {
                    ds.receive(dp);
                    for (Client c: clients){
                        dp.setSocketAddress(new InetSocketAddress(c.IP, c.UDP_PORT));
                        ds.send(dp);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public class Client{
        String IP;
        int UDP_PORT;
        int id;

        public Client(String ipAddr, int UDP_PORT,int id){
            this.IP = ipAddr;
            this.UDP_PORT = UDP_PORT;
            this.id = id;
        }
    }

}
