package client.helper;

import client.characters.Creature;
import client.characters.camp.calabashCamp.*;
import client.characters.camp.evilCamp.chilopodSpirit;
import client.characters.camp.evilCamp.frogSpirit;
import client.characters.camp.evilCamp.scorpionSpirit;
import client.characters.camp.evilCamp.snakeSpirit;
import client.client.PlayerClient;
import client.configuration.Configs;
import client.mapObject.*;
import client.protocol.*;
import client.utils.Timer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class GameHelper {
    public enum GAME_STATUS{
        NONE,
        SHOW_PROPERTY,
        SHOW_MENU,
        PREPARE_MOVE,
        MOVE,
        PREPARE_ATTACK,
        ATTACK,
        PREPARE_ABILITY,
        ABILITY,
        DEATH,
        STOP,
        CALABASH_WIN,
        EVIL_WIN
    }
    public enum PLAYER{
        CALABASH,
        EVIL
    }
    public enum GAME_TURN{
        CALABASH,
        EVIL
    }
    private boolean AUTO_MODE = false;
    private PLAYER player = PLAYER.CALABASH;
    private GAME_STATUS status = GAME_STATUS.NONE;
    private GAME_TURN turn = GAME_TURN.CALABASH;
    private Configs configs = Configs.getInstance();
    private Creature nowControl;
    private int moveToX;
    private int moveToY;

    private ChooseRect chooseRect;
    private List<Creature> calabashCamp = new ArrayList<>();
    private List<Creature> evilCamp = new ArrayList<>();
    private List<BaseObject> mObject;
    private List<BlockObject> movableRange;
    private List<BlockObject> attackableRange;
    private List<BlockObject> abilityRange;

    private PropertyMenu propertyMenu;// 属性菜单
    private ActionMenu actionMenu;//行动菜单
    private TurnBar turnBar;

    private FileHelper fileHelper = new FileHelper();
    private DistanceHelper distanceHelper = new DistanceHelper();
    private KeyAdapter keyAdapter = new KeyAdapter();

    private PlayerClient pc;
    private AutoPlayer autoPlayer = new AutoPlayer(this);

    public GameHelper(){}

    public GameHelper(PlayerClient pc){
        this.pc = pc;
    }

    public void setmObject(List<BaseObject> mObject){
        this.mObject = mObject;
    }

    // 移动定时器，实现一格格移动
    Timer moveTimer = new Timer(configs.getTIME_UNIT(), new Timer.OnTimerListener() {
        @Override
        public void onTimerRunning(Timer mTimer) {
            Creature actor = moveTimer.getActor();
            if (status == GAME_STATUS.MOVE && actor != null){
                int PVX = actor.getVX();
                int PVY = actor.getVY();
                if (PVX != moveToX){
                    actor.moveX(PVX > moveToX ? -configs.getTILE_WIDTH() : configs.getTILE_WIDTH());
                }
                else if (PVY != moveToY){
                    actor.moveY(PVY > moveToY ? -configs.getTILE_HEIGHT() : configs.getTILE_HEIGHT());
                }
                else {
                    moveTimer.stop();
                    actor.setCanMove(false);
                    setStatus(GAME_STATUS.NONE);
                }
            }
        }
    });

    public AutoPlayer getAutoPlayer() {
        return autoPlayer;
    }

    public DistanceHelper getDistanceHelper() {
        return distanceHelper;
    }

    public void setRect(double dx,double dy){
        this.chooseRect.setX(dx);
        this.chooseRect.setY(dy);
    }
    public Timer getMoveTimer() {
        return moveTimer;
    }

    public GAME_STATUS getStatus() {
        return status;
    }

    public void setAUTO_MODE(boolean AUTO_MODE) {
        this.AUTO_MODE = AUTO_MODE;
    }

    public void setStatus(GAME_STATUS status) {
        this.status = status;
    }

    public PLAYER getPlayer() {
        return player;
    }

    public int getIndex(){
        switch (player){
            case CALABASH:
                return calabashCamp.indexOf(nowControl);
            case EVIL:
                return evilCamp.indexOf(nowControl);
            default:
                break;
        }
        return -1;
    }

    public FileHelper getFileHelper() {
        return fileHelper;
    }

    public Creature getCalabash(int index){
        return calabashCamp.get(index);
    }

    public Creature getEvil(int index){
        return evilCamp.get(index);
    }

    private int getEnemyIndex(Creature creature){
        return player.equals(PLAYER.CALABASH) ? getEvilCamp().indexOf(creature) : getCalabashCamp().indexOf(creature);
    }

    public void changeTurn(){
        if (this.turn.equals(GAME_TURN.CALABASH))
            this.turn = GAME_TURN.EVIL;
        else this.turn = GAME_TURN.CALABASH;
        if (!AUTO_MODE) {
            if (this.turn.toString().equals(this.player.toString())) {
                resetCreature();
            } else {
                resetEnemy();
            }
        }
        else {
            if (this.turn.equals(GAME_TURN.CALABASH)){
                for (Creature calabash : getCalabashCamp()){
                    calabash.reset();
                }
            }
            else {
                for (Creature evil : getEvilCamp()){
                    evil.reset();
                }
            }
        }
        turnBar.setCamp(this.turn.equals(GAME_TURN.CALABASH) ? 0 : 1);
    }

    private void resetCreature(){
        switch (player){
            case CALABASH:
                for (Creature calabash : getCalabashCamp()){
                    calabash.reset();
                }
                break;
            case EVIL:
                for (Creature evil : getEvilCamp()){
                    evil.reset();
                }
                break;
        }
    }

    private void resetEnemy(){
        switch (player){
            case EVIL:
                for (Creature calabash : getCalabashCamp()){
                    calabash.reset();
                }
                break;
            case CALABASH:
                for (Creature evil : getEvilCamp()){
                    evil.reset();
                }
                break;
        }
    }

    public void setPlayer(PLAYER player) {
        this.player = player;
    }

    public List<Creature> getCalabashCamp() {
        return calabashCamp;
    }

    public List<Creature> getEvilCamp() {
        return evilCamp;
    }

    public void setActionMenu(ActionMenu actionMenu) {
        this.actionMenu = actionMenu;
    }

    public void setPropertyMenu(PropertyMenu propertyMenu) {
        this.propertyMenu = propertyMenu;
    }

    public void setMoveToX(int moveToX) {
        this.moveToX = moveToX;
    }

    public void setMoveToY(int moveToY) {
        this.moveToY = moveToY;
    }

    public void setChooseRect(ChooseRect chooseRect) {
        this.chooseRect = chooseRect;
    }

    public void setTurnBar(TurnBar turnBar){
        turnBar.setCamp(this.turn.equals(GAME_TURN.CALABASH) ? 0 : 1);
        this.turnBar = turnBar;
    }

    public void initCreature(){
        this.calabashCamp.add(new bigBro(100,100));
        this.calabashCamp.add(new secondBro(100,200));
        this.calabashCamp.add(new thirdBro(100,300));
        this.calabashCamp.add(new forthBro(100,400));
        this.calabashCamp.add(new fifthBro(100,500));

        this.evilCamp.add(new scorpionSpirit(configs.getSCENE_WIDTH() - 200 ,100));
        this.evilCamp.add(new snakeSpirit(configs.getSCENE_WIDTH() - 200,200));
        this.evilCamp.add(new frogSpirit(configs.getSCENE_WIDTH() - 200,300));
        this.evilCamp.add(new chilopodSpirit(configs.getSCENE_WIDTH() - 200,400));
    }


    public void attackEvent(Creature attacker,Creature attacked){
        if (getStatus().equals(GAME_STATUS.ATTACK)){
            attacker.attack(attacked);
            attacker.setCanAttack(false);
            if (attacked.getHp() == 0){
                if(!AUTO_MODE)sendDeathMsg(attacked);
                setStatus(GAME_STATUS.DEATH);
            }
            else
                setStatus(GAME_STATUS.NONE);
        }
    }

    public void deathEvent(Creature death){
        if (getStatus().equals(GAME_STATUS.DEATH)){
            death.die();
            setStatus(GAME_STATUS.NONE);
            try {
                Thread.sleep(510);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            mObject.remove(death);
            if (getEvilCamp().contains(death)) {
                getEvilCamp().remove(death);
            } else {
                getCalabashCamp().remove(death);
            }
            if (getEvilCamp().isEmpty()){
                if(!AUTO_MODE)sendWinMsg(0);
            }
            else if (getCalabashCamp().isEmpty()){
                if(!AUTO_MODE)sendWinMsg(1);
            }
        }
    }

    public void abilityEvent(Creature user,Creature attacked){
        if (getStatus().equals(GAME_STATUS.ABILITY)){
            user.useAbility(attacked);
            user.setCanUseAbility(false);
            if (attacked.getHp() == 0){
                if(!AUTO_MODE)sendDeathMsg(attacked);
                setStatus(GAME_STATUS.DEATH);
            }
            else
                setStatus(GAME_STATUS.NONE);
        }
    }

    public void stopEvent(Creature stoper){
        if (getStatus().equals(GAME_STATUS.STOP)){
            stoper.setCanAct(false);
        }
        if (checkTurnChange()){
            if (!AUTO_MODE)sendTurnChangeMsg();
            else {
                changeTurn();
            }
        }
        setStatus(GAME_STATUS.NONE);
    }

    public void winEvent(int camp){
        WinBar winBar = null;
        switch (camp){
            case 0:
                setStatus(GAME_STATUS.CALABASH_WIN);
                winBar = new WinBar(camp);
                mObject.add(winBar);
                break;
            case 1:
                setStatus(GAME_STATUS.EVIL_WIN);
                winBar = new WinBar(camp);
                mObject.add(winBar);
                break;
        }
    }

    public void onKeyPressed(KeyEvent keyEvent){
        if(!AUTO_MODE)this.keyAdapter.handle(keyEvent);
    }


    private void sendWinMsg(int camp){
        Msg winMsg = new WinMsg(this,camp);
        this.pc.getNc().send(winMsg);
    }

    private void sendMoveMsg(int VX,int VY){
        Msg moveMsg = new MoveMsg(this,VX,VY);
        this.pc.getNc().send(moveMsg);
    }

    private void sendAttackMsg(Creature enemy){
        Msg attackMsg = new AttackMsg(this,getEnemyIndex(enemy));
        this.pc.getNc().send(attackMsg);
    }

    private void sendAbilityMsg(Creature enemy){
        Msg abiMsg = new AbilityMsg(this,getEnemyIndex(enemy));
        this.pc.getNc().send(abiMsg);
    }

    private void sendDeathMsg(Creature death){
        Msg deathMsg = new DeathMsg(this,getEnemyIndex(death));
        this.pc.getNc().send(deathMsg);
    }


    private void sendStopMsg(){
        Msg stopMsg = new StopMsg(this,getIndex());
        this.pc.getNc().send(stopMsg);
    }

    private void sendTurnChangeMsg(){
        Msg turnMsg = new TurnMsg();
        this.pc.getNc().send(turnMsg);
    }


    private boolean checkTurnChange(){
        if (!AUTO_MODE) {
            if (turn.toString().equals(player.toString())) {
                switch (player) {
                    case CALABASH:
                        for (Creature calabash : getCalabashCamp()) {
                            if (calabash.getCanAct())
                                return false;
                        }
                        break;
                    case EVIL:
                        for (Creature evil : getEvilCamp()) {
                            if (evil.getCanAct()) {
                                return false;
                            }
                        }
                        break;
                }
                return true;
            } else return false;
        }
        else {
            switch (turn){
                case CALABASH:
                    for (Creature calabash : getCalabashCamp()){
                        if (calabash.getCanAct())
                            return false;
                    }
                    return true;
                case EVIL:
                    for (Creature evil : getEvilCamp()){
                        if (evil.getCanAct()){
                            return false;
                        }
                    }
                    return true;
            }
        }
        return false;
    }

    public boolean hasCreature(double x,double y){
        for (Creature calabash : getCalabashCamp()){
            if (calabash.getX() == x && calabash.getY() == y)
                return true;
        }

        for (Creature evil : getEvilCamp()){
            if (evil.getX() == x && evil.getY() == y)
                return true;
        }

        return false;
    }


    private class KeyAdapter{
        KeyAdapter(){
        }

        void handle(KeyEvent keyEvent){
            switch (status){
                case NONE:
                    nowControl = null;
                    moveRect(keyEvent);
                    break;
                case SHOW_PROPERTY:
                    if (keyEvent.getCode().equals(KeyCode.K)) {
                        propertyMenu.setVisible(false);
                        setStatus(GAME_STATUS.NONE);
                    }
                    break;
                case SHOW_MENU:
                    menuAction(keyEvent);
                    break;
                case PREPARE_MOVE:
                    handleMoveAct(keyEvent);
                    break;
                case PREPARE_ATTACK:
                    handleAttAct(keyEvent);
                    break;
                case PREPARE_ABILITY:
                    handleAbiAct(keyEvent);
                    break;
            }
        }

        private void handleMoveAct(KeyEvent keyEvent){
            switch (keyEvent.getCode()){
                case K:
                    actionMenu.setVisible(true);
                    propertyMenu.setVisible(true);
                    mObject.removeAll(movableRange);
                    setStatus(GAME_STATUS.SHOW_MENU);
                    break;
                case W:
                case S:
                case A:
                case D:
                    moveRect(keyEvent);
                    break;
                case J:
                    if (checkIfInRange(movableRange)){
                        mObject.removeAll(movableRange);
                        sendMoveMsg(chooseRect.getVX(),chooseRect.getVY());
                    }

            }
        }

        private void handleAttAct(KeyEvent keyEvent){
            switch (keyEvent.getCode()){
                case K:
                    actionMenu.setVisible(true);
                    propertyMenu.setVisible(true);
                    mObject.removeAll(attackableRange);
                    setStatus(GAME_STATUS.SHOW_MENU);
                    break;
                case W:
                case S:
                case A:
                case D:
                    moveRect(keyEvent);
                    break;
                case J:
                    Creature enemy = getEnemy();
                    if (enemy != null && checkIfInRange(attackableRange)){
                        mObject.removeAll(attackableRange);
                        sendAttackMsg(enemy);
                    }
            }
        }

        private void handleAbiAct(KeyEvent keyEvent){
            switch (keyEvent.getCode()){
                case K:
                    actionMenu.setVisible(true);
                    propertyMenu.setVisible(true);
                    mObject.removeAll(abilityRange);
                    setStatus(GAME_STATUS.SHOW_MENU);
                    break;
                case W:
                case S:
                case A:
                case D:
                    moveRect(keyEvent);
                    break;
                case J:
                    Creature enemy = getEnemy();
                    if (enemy != null && checkIfInRange(abilityRange)){
                        mObject.removeAll(abilityRange);
                        sendAbilityMsg(enemy);
                    }
            }

        }


        private void menuAction(KeyEvent keyEvent){
            switch (keyEvent.getCode()){
                case K:
                    propertyMenu.setVisible(false);
                    actionMenu.setVisible(false);
                    setStatus(GAME_STATUS.NONE);
                    break;
                case S:
                case W:
                    actionMenu.onKeyPressed(keyEvent);
                    break;
                case J:
                    switch (actionMenu.getChoose()){
                        case MOVE:
                            showMoveRange();
                            break;
                        case ATTACK:
                            showAttackRange();
                            break;
                        case ABILITY:
                            showAbilityRange();
                            break;
                        case STOP:
                            sendStopMsg();
                            actionMenu.setVisible(false);
                            propertyMenu.setVisible(false);
                            break;
                    }
            }
        }


        private void moveRect(KeyEvent keyEvent){
            switch (keyEvent.getCode()){
                case W:
                    chooseRect.move(0,-configs.getTILE_HEIGHT());
                    break;
                case D:
                    chooseRect.move(configs.getTILE_WIDTH(),0);
                    break;
                case A:
                    chooseRect.move(-configs.getTILE_WIDTH(),0);
                    break;
                case S:
                    chooseRect.move(0,configs.getTILE_HEIGHT());
                    break;
                case J:
                    checkCreature();
                    break;
            }
        }

        private void checkCreature(){
            for (Creature calabash : getCalabashCamp()){
                if (calabash.isCollisionWith(chooseRect)){
                    if (player.equals(PLAYER.CALABASH) && turn.equals(GAME_TURN.CALABASH) && calabash.getCanAct()){
                        nowControl = calabash;
                        actionMenu.initIndex(nowControl);
                        propertyMenu.initCreature(calabash);
                        actionMenu.setVisible(true);
                        propertyMenu.setVisible(true);
                        setStatus(GAME_STATUS.SHOW_MENU);
                        return;
                    }
                    //显示属性
                    else{
                        propertyMenu.initCreature(calabash);
                        propertyMenu.setVisible(true);
                        setStatus(GAME_STATUS.SHOW_PROPERTY);
                    }
                }
            }

            for (Creature evil : getEvilCamp()){
                if (evil.isCollisionWith(chooseRect)){
                    if (player.equals(PLAYER.EVIL) && turn.equals(GAME_TURN.EVIL) && evil.getCanAct()){
                        nowControl = evil;
                        actionMenu.initIndex(nowControl);
                        propertyMenu.initCreature(evil);
                        actionMenu.setVisible(true);
                        propertyMenu.setVisible(true);
                        setStatus(GAME_STATUS.SHOW_MENU);
                        return;
                    }
                    //显示属性
                    else{
                        propertyMenu.initCreature(evil);
                        propertyMenu.setVisible(true);
                        setStatus(GAME_STATUS.SHOW_PROPERTY);
                    }
                }
            }
        }

        public void showMoveRange(){
            movableRange = distanceHelper.getMovableDis(new BlockObject(nowControl.getX(),nowControl.getY(),null),nowControl.getMovement());
            movableRange.removeIf(b -> hasCreature(b.getX(), b.getY()));
            for (BlockObject b : movableRange)b.setBlockType(BlockObject.B_TYPE.MOVE);
            mObject.addAll(movableRange);
            actionMenu.setVisible(false);
            propertyMenu.setVisible(false);
            setStatus(GAME_STATUS.PREPARE_MOVE);
        }

        public void showAttackRange(){
            attackableRange = distanceHelper.getMovableDis(new BlockObject(nowControl.getX(),nowControl.getY(),null),nowControl.getAttackRange());
            attackableRange.removeIf(b -> b.isCollisionWith(nowControl));
            for (BlockObject b : attackableRange)b.setBlockType(BlockObject.B_TYPE.ATTACK);
            mObject.addAll(attackableRange);
            actionMenu.setVisible(false);
            propertyMenu.setVisible(false);
            setStatus(GAME_STATUS.PREPARE_ATTACK);
        }

        public void showAbilityRange(){
            abilityRange = distanceHelper.getMovableDis(new BlockObject(nowControl.getX(),nowControl.getY(),null),nowControl.getAbilityRange());
            abilityRange.removeIf(b -> b.isCollisionWith(nowControl));
            for (BlockObject b : abilityRange)b.setBlockType(BlockObject.B_TYPE.ABILITY);
            mObject.addAll(abilityRange);
            actionMenu.setVisible(false);
            propertyMenu.setVisible(false);
            setStatus(GAME_STATUS.PREPARE_ABILITY);
        }

        private Creature getEnemy(){
            switch (player){
                case CALABASH:
                    for (Creature evil : getEvilCamp()){
                        if (evil.isCollisionWith(chooseRect)) return evil;
                    }
                case EVIL:
                    for (Creature calabash : getCalabashCamp()){
                        if (calabash.isCollisionWith(chooseRect))return calabash;
                    }
            }
            return null;
        }

        private boolean checkIfInRange(List<BlockObject> rangeBlocks){
            for (BlockObject b : rangeBlocks){
                if (b.isCollisionWith(chooseRect))
                    return true;
            }
            return false;
        }

    }

    public class AutoPlayer implements Runnable{
        private String filePath;
        private Document docs;
        private Element root;
        private GameHelper gameHelper;

        AutoPlayer(GameHelper gameHelper){
            this.gameHelper = gameHelper;
        }

        public void setFilePath(String filePath){this.filePath = filePath;}

        public void init(){
            if (filePath == null)return;
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            try {
                db = dbf.newDocumentBuilder();
                docs = db.parse(filePath);
                root=docs.getDocumentElement();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            init();
            if (docs == null)return;

            NodeList game = root.getChildNodes();
            NodeList rounds = game.item(1).getChildNodes();
            for (int i = 0;i < rounds.getLength();i++){
                Node action = rounds.item(i);

                int actionType = Integer.parseInt(action.getFirstChild().getNodeValue());
                switch (actionType){
                    case FileHelper.SAVE_MOVE:
                        int camp = Integer.parseInt(action.getAttributes().getNamedItem("Camp").getNodeValue());
                        int index = Integer.parseInt(action.getAttributes().getNamedItem("Id").getNodeValue());
                        int dx = Integer.parseInt(action.getAttributes().getNamedItem("Dx").getNodeValue());
                        int dy = Integer.parseInt(action.getAttributes().getNamedItem("Dy").getNodeValue());
                        Creature  c = camp == 0 ? gameHelper.getCalabash(index) : gameHelper.getEvil(index);
                        gameHelper.setRect(c.getX(),c.getY());
                        try {
                            Thread.sleep(100);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        int duration = Math.abs(dx - c.getVX()) + Math.abs(dy - c.getVY());
                        gameHelper.movableRange = gameHelper.getDistanceHelper().getMovableDis(new BlockObject(c.getX(),c.getY(),null),c.getMovement());
                        gameHelper.movableRange.removeIf(b -> hasCreature(b.getX(), b.getY()));
                        for (BlockObject b : movableRange)b.setBlockType(BlockObject.B_TYPE.MOVE);
                        mObject.addAll(movableRange);
                        gameHelper.getMoveTimer().setActor(c);
                        gameHelper.setMoveToX(dx);
                        gameHelper.setMoveToY(dy);
                        gameHelper.setRect(dx * configs.getTILE_WIDTH(),dy * configs.getTILE_HEIGHT());
                        gameHelper.setStatus(GameHelper.GAME_STATUS.MOVE);
                        gameHelper.getMoveTimer().start();
                        try {
                            Thread.sleep(duration * 60);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        mObject.removeAll(movableRange);
                        break;
                    case FileHelper.SAVE_ATTACK:
                        camp = Integer.parseInt(action.getAttributes().getNamedItem("Camp").getNodeValue());
                        int attackerIndex = Integer.parseInt(action.getAttributes().getNamedItem("AttackerId").getNodeValue());
                        int attackedIndex = Integer.parseInt(action.getAttributes().getNamedItem("AttackedId").getNodeValue());
                        Creature attacker = camp == 0 ? gameHelper.getCalabash(attackerIndex) : gameHelper.getEvil(attackerIndex);
                        Creature toAttacker = camp == 1 ? gameHelper.getCalabash(attackedIndex) : gameHelper.getEvil(attackedIndex);
                        gameHelper.attackableRange = gameHelper.getDistanceHelper().getMovableDis(new BlockObject(attacker.getX(),attacker.getY(),null),attacker.getAttackRange());
                        for (BlockObject b : attackableRange)b.setBlockType(BlockObject.B_TYPE.ATTACK);
                        attackableRange.removeIf(b -> b.isCollisionWith(attacker));
                        mObject.addAll(attackableRange);
                        gameHelper.setStatus(GameHelper.GAME_STATUS.ATTACK);
                        setRect(attacker.getX(),attacker.getY());
                        try {
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        setRect(toAttacker.getX(),toAttacker.getY());
                        gameHelper.attackEvent(attacker,toAttacker);
                        mObject.removeAll(attackableRange);
                        break;
                    case FileHelper.SAVE_ABILITY:
                        camp = Integer.parseInt(action.getAttributes().getNamedItem("Camp").getNodeValue());
                        int userIndex = Integer.parseInt(action.getAttributes().getNamedItem("AttackerId").getNodeValue());
                        int toAttackIndex = Integer.parseInt(action.getAttributes().getNamedItem("AttackedId").getNodeValue());
                        Creature user = camp == 0 ? gameHelper.getCalabash(userIndex) : gameHelper.getEvil(userIndex);
                        Creature toAttacked = camp == 1 ? gameHelper.getCalabash(toAttackIndex) : gameHelper.getEvil(toAttackIndex);
                        gameHelper.abilityRange = gameHelper.getDistanceHelper().getMovableDis(new BlockObject(user.getX(),user.getY(),null),user.getAbilityRange());
                        for (BlockObject b : abilityRange)b.setBlockType(BlockObject.B_TYPE.ABILITY);
                        abilityRange.removeIf(b -> b.isCollisionWith(user));
                        mObject.addAll(abilityRange);
                        setRect(user.getX(),user.getY());
                        try {
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        setRect(toAttacked.getX(),toAttacked.getY());
                        gameHelper.setStatus(GAME_STATUS.ABILITY);
                        gameHelper.abilityEvent(user,toAttacked);
                        mObject.removeAll(abilityRange);
                        break;
                    case FileHelper.SAVE_DEATH:
                        camp = Integer.parseInt(action.getAttributes().getNamedItem("Camp").getNodeValue());
                        int deathIndex = Integer.parseInt(action.getAttributes().getNamedItem("Id").getNodeValue());
                        Creature death = camp == 0 ? gameHelper.getCalabash(deathIndex) : gameHelper.getEvil(deathIndex);
                        setRect(death.getX(),death.getY());
                        gameHelper.deathEvent(death);
                        break;
                    case FileHelper.SAVE_STOP:
                        camp = Integer.parseInt(action.getAttributes().getNamedItem("Camp").getNodeValue());
                        index = Integer.parseInt(action.getAttributes().getNamedItem("Id").getNodeValue());
                        Creature stoper = camp == 0 ? gameHelper.getCalabash(index) : gameHelper.getEvil(index);
                        setRect(stoper.getX(),stoper.getY());
                        gameHelper.setStatus(GameHelper.GAME_STATUS.STOP);
                        gameHelper.stopEvent(stoper);
                        break;
                    case FileHelper.SAVE_WIN:
                        camp = Integer.parseInt(action.getAttributes().getNamedItem("Camp").getNodeValue());
                        gameHelper.winEvent(camp);
                        break;
                }

                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

    }

}
