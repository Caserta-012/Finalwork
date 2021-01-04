package client.helper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class FileHelper {
    private Document docs;
    private Element root;
    private Element round;

    static final int SAVE_MOVE = 0;
    static final int SAVE_ATTACK = 1;
    static final int SAVE_ABILITY = 2;
    static final int SAVE_DEATH = 3;
    static final int SAVE_STOP = 4;
    static final int SAVE_WIN = 5;

    private boolean newround = true;

    public FileHelper(){
        init();
    }

    //创建并打开xml文件
    private void init(){
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            docs = db.newDocument();
            root = docs.createElement("game");
            String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            Element ctime = docs.createElement("ctime");
            Text m = docs.createTextNode(time);
            ctime.appendChild(m);
            root.appendChild(ctime);
            docs.appendChild(root);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 添加一次移动信息
     * @param camp 角色阵营
     * @param index 阵营序列角色所在下标
     * @param dx 目的地横坐标
     * @param dy 目的地纵坐标
     */
    public void addMove(int camp,int index,int dx,int dy){
        if(newround) {
            if(round!=null)
                root.appendChild(round);
            round = docs.createElement("round");
            newround = false;
        }
        Element move = docs.createElement("move");
        move.setAttribute("Camp",String.valueOf(camp));
        move.setAttribute("Id",String.valueOf(index));
        move.setAttribute("Dx",String.valueOf(dx));
        move.setAttribute("Dy",String.valueOf(dy));
        Text m = docs.createTextNode(String.valueOf(SAVE_MOVE));
        move.appendChild(m);
        round.appendChild(move);
    }

    /**
     * 添加一次攻击信息
     * @param camp 攻击者阵营
     * @param attacker 攻击者所在阵营序列下标
     * @param attacked 被攻击者所在阵营序列下标
     */
    public void addAttack(int camp,int attacker,int attacked){
        if(newround) {
            if(round!=null)
                root.appendChild(round);
            round = docs.createElement("round");
            newround = false;
        }
        Element attack = docs.createElement("attack");
        attack.setAttribute("Camp",String.valueOf(camp));
        attack.setAttribute("AttackerId",String.valueOf(attacker));
        attack.setAttribute("AttackedId",String.valueOf(attacked));
        Text m = docs.createTextNode(String.valueOf(SAVE_ATTACK));
        attack.appendChild(m);
        round.appendChild(attack);
    }

    /**
     * 添加一次使用技能信息
     * @param camp 攻击者阵营
     * @param attacker 攻击者所在阵营序列下标
     * @param attacked 被攻击者所在阵营序列下标
     */
    public void addAbi(int camp,int attacker,int attacked){
        if(newround) {
            if(round!=null)
                root.appendChild(round);
            round = docs.createElement("round");
            newround = false;
        }
        Element ability = docs.createElement("ability");
        ability.setAttribute("Camp",String.valueOf(camp));
        ability.setAttribute("AttackerId",String.valueOf(attacker));
        ability.setAttribute("AttackedId",String.valueOf(attacked));
        Text m = docs.createTextNode(String.valueOf(SAVE_ABILITY));
        ability.appendChild(m);
        round.appendChild(ability);
    }

    /**
     * 添加一次死亡消息
     * @param camp 死者所在阵营
     * @param index 下标
     */
    public void addDeath(int camp,int index){
        if(newround) {
            if(round!=null)
                root.appendChild(round);
            round = docs.createElement("round");
            newround = false;
        }
        Element death = docs.createElement("death");
        death.setAttribute("Camp",String.valueOf(camp));
        death.setAttribute("Id",String.valueOf(index));
        Text m = docs.createTextNode(String.valueOf(SAVE_DEATH));
        death.appendChild(m);
        round.appendChild(death);
    }

    /**
     * 添加一次待机消息
     * @param camp 阵营
     * @param index 下标
     */
    public void addStop(int camp,int index){
        if(newround) {
            if(round!=null)
                root.appendChild(round);
            round = docs.createElement("round");
            newround = false;
        }
        Element stop = docs.createElement("stop");
        stop.setAttribute("Camp",String.valueOf(camp));
        stop.setAttribute("Id",String.valueOf(index));
        Text m = docs.createTextNode(String.valueOf(SAVE_STOP));
        stop.appendChild(m);
        round.appendChild(stop);
    }

    /**
     * 添加胜利信息
     * @param camp 胜利者阵营
     */
    public void addWin(int camp){
        Element win = docs.createElement("win");
        win.setAttribute("Camp",String.valueOf(camp));
        Text m = docs.createTextNode(String.valueOf(SAVE_WIN));
        win.appendChild(m);
        round.appendChild(win);
    }


    // 写入文件
    public void saveFile(String filePath){
        if (filePath == null || round == null)return;
        root.appendChild(round);
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(docs);

            Properties p = new Properties();
            p.setProperty(OutputKeys.ENCODING,"UTF-8");
            transformer.setOutputProperties(p);
            File f = new File(filePath);
            StreamResult streamResult = new StreamResult(new FileOutputStream(f));
            transformer.transform(domSource,streamResult);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
