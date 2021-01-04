package client.protocol;

import java.io.DataInputStream;
import java.net.DatagramSocket;

// 应用层协议接口
public interface Msg {
    static final int TURN_MSG = 0; // 通知回合改变的信息
    static final int MOVE_MSG = 1;
    static final int ATTACK_MSG = 2;
    static final int ABILITY_MSG = 3;
    static final int DEATH_MSG = 4;
    static final int STOP_MSG = 5;
    static final int WIN_MSG = 6;

    public void send(DatagramSocket ds, String ip, int UDP_PORT);
    public void parse(DataInputStream dis);
}
