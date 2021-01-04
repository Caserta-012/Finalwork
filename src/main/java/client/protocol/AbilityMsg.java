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

public class AbilityMsg implements Msg {
    private int msgType = Msg.ABILITY_MSG;
    private PlayerClient pc;
    private GameHelper gameHelper;
    private int userIndex;
    private int toAttackIndex;

    public AbilityMsg(PlayerClient pc){this.pc = pc;}

    public AbilityMsg(GameHelper gameHelper,int toAttackIndex){
        this.gameHelper = gameHelper;
        this.userIndex = gameHelper.getIndex();
        this.toAttackIndex = toAttackIndex;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int UDP_PORT) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeInt(gameHelper.getPlayer().equals(GameHelper.PLAYER.CALABASH) ? 0 : 1);
            dos.writeInt(userIndex);
            dos.writeInt(toAttackIndex);
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
            int userIndex = dis.readInt();
            Creature user = playerNo == 0 ? pc.getGameHelper().getCalabash(userIndex) : pc.getGameHelper().getEvil(userIndex);
            int toAttackIndex = dis.readInt();
            Creature toAttacker = playerNo == 1 ? pc.getGameHelper().getCalabash(toAttackIndex) : pc.getGameHelper().getEvil(toAttackIndex);
            pc.getGameHelper().getFileHelper().addAbi(playerNo,userIndex,toAttackIndex);
            pc.getGameHelper().setStatus(GameHelper.GAME_STATUS.ABILITY);
            pc.getGameHelper().abilityEvent(user,toAttacker);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
