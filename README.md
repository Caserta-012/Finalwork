# 葫芦娃大作业
## 基本玩法与截图
- 可以在`/out/artifacts/Client_jar`下运行指令`java -jar Client.jar`打开客户端，也可以直接通过运行`PlayerClient`的`main`函数加载客户端。
- 进入游戏，可以选择进行联网双人游戏或者读取录制的游戏文件。
![mainScreen.png](https://i.loli.net/2021/01/02/WuorCZDOmUgTtA5.png)
- 选择双人对战，则需一方提前启动`Sever`类下的`main`函数，作为服务器注册的事件
- 按照启动客户端顺序，默认第一个连接上服务器的玩家选择为葫芦娃阵营，第二个连接上服务器的玩家选定为妖精阵营
- 游戏玩法：本游戏参考火焰之纹章和英雄无敌等回合制战斗游戏玩法，每个玩家可以通过`WASD`键操纵游戏界面的选择光标移动，通过`J`/`K`键进行选取和取消选取。每个游戏角色拥有生命值、攻击力、防御力、移动力和能否使用技能几个属性。
![gameshow1.png](https://i.loli.net/2021/01/02/1iYvqlymGbeEZfn.png)
![gameshow2.png](https://i.loli.net/2021/01/02/82sxL6MwU5mHvqn.png)
![gameshow3.png](https://i.loli.net/2021/01/02/1ZIBtCR6GlzwMfr.png)
- 每个角色可以在自己能够移动的范围内进行移动，能够对敌人发动攻击（伤害公式为 `攻击者攻击力 - 被攻击者防御力`)，或是发动每场游戏只能发动一次的技能（对敌人造成2倍伤害，在计算敌人防御力前）。
- 当一个阵营里的全部角色全都被消灭时，操纵该阵营的玩家宣告失败，相反另一方玩家获得胜利
- 游戏可以随时保存，只需要点击界面右上角的保存图标，将会以`.xml`格式进行保存
- 游戏已经记录了一局完整的对战，对战文件保存在`/src/main/resources/test.xml`
## 类的设计
使用传统的c-s架构，设计了两大模块
- client 模块：实现了客户端，游戏UI，以及回合逻辑，与服务器通信，将对局可持久化输出等
- server 模块：实现了服务器端，相比于客户端更加轻量，主要负责对客户端的监听
### server模块
游戏的网络通信结构采用了较为简单的TCP/IP结构，服务器端保存有TCP端口号和用来转发客户端数据的UDP端口号，一个存有内部类`Client`的列表，用来保存当前连接上服务器的客户端的信息。
```java
	//内部类 Client的设计
	public class Client{
        String IP; // ip地址
        int UDP_PORT; // UDP端口号
        int id; // 客户端id

        public Client(String ipAddr, int UDP_PORT,int id){
            this.IP = ipAddr;
            this.UDP_PORT = UDP_PORT;
            this.id = id;
        }
    }
```
客户端成功连上服务器端后，服务器端尝试向客户端发送`player`编号（用于决定用户所选择的阵营)，以及用以接受消息的UDP端口号。
```java
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
```
客户端与服务器的通信通过独立的UDP线程，这样做实现了非同步非阻塞的输入输出，服务器可以时刻监视客户端的动作。
```java
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
```
### client 模块
client模块实现了游戏的大部分功能，通过传统的**MVC设计模式**，将UI设计和控制器以及模型分离，从而实现对游戏界面进行良好有序的控制。`PlayerClient`实现了客户端的入口函数，整个游戏的逻辑实现主要通过`gameHelper`类。
#### characters 包
主要保存了游戏中所有角色的对象，每个对象都是独立的，并且在初始化的时候加载了该角色的所有属性以及图片。
#### client 包
实现了客户端，`PlayerClient`作为游戏的入口，不直接与服务器进行连接，而是作为引用委托给`NetClient`类，进行对服务器的连接和接收并解析服务器传输的数据。
```java
	// NetClient的连接函数，将当前客户端连接上服务器并且获得客户端的ID号和UDP端口号
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
```
#### configuration 包
类`Configs`保存了游戏实现过程中的各类属性，采用了**单例模式**设计。
```java
 private static Configs configs = new Configs();

    // 懒汉式单例模式设计
    private Configs(){
    }

    public static Configs getInstance(){
        return configs;
    }
```
#### draw 包
实现了页面UI逻辑，其中抽象类`BaseScreen`使用了`Timeline`对象实现了对保存的各类UI模型进行循环刷新，注册了键盘事件，作为父类供其他界面继承。`MapScreen`继承自`BaseScrenn`对整个游戏界面进行初始化以及刷新展示。
```java
 // 使用Timeline对象实现刷新
    private void initTimeLine(){
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // 循环刷新
                draw(getGraphicsContext2D());
                update();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
    }
```
#### helper 包
这是游戏的核心包类，主要是实现了游戏逻辑的`GameHelper`类，和实现将游戏数据转化为文件进行输入输出的`FileHelper`类。
##### 如何实现线程同步 ？ 
在`GameHelper`中定义了枚举类`GAME_STATUS`
```java
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
```
一个`GameHelper`实例同时只能保存一种游戏状态的枚举变量，这样当客户端接收到服务器端发来的消息时，通过提前设置`GAME_STATUS`，将所有客户端的游戏状态设置为相同，从而实现线程同步。
##### 对游戏过程进行持久化输入输出
采用了`xml`的格式对游戏数据进行储存和输入输出，`xml`文件格式如下：
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<game>
	<ctime>2021-01-02 08:41:24</ctime>
	<round>
		<move Camp="0" Dx="4" Dy="1" Id="0">0</move>
		<stop Camp="0" Id="0">4</stop>
		<move Camp="0" Dx="5" Dy="2" Id="1">0</move>
		<stop Camp="0" Id="1">4</stop>
		<move Camp="0" Dx="3" Dy="3" Id="2">0</move>
		<stop Camp="0" Id="2">4</stop>
	</round>
</game>
```
`FileHelper`类提供了数据转化接口：
```java
	/**
     * 添加一次移动信息
     * @param camp 角色阵营
     * @param index 阵营序列角色所在下标
     * @param dx 目的地横坐标
     * @param dy 目的地纵坐标
     */
    public void addMove(int camp,int index,int dx,int dy);
    
    /**
     * 添加一次攻击信息
     * @param camp 攻击者阵营
     * @param attacker 攻击者所在阵营序列下标
     * @param attacked 被攻击者所在阵营序列下标
     */
    public void addAttack(int camp,int attacker,int attacked);
    ......
```
最后，`GameHelper`实现了内部类`AutoPlayer`，能够对成功读入的文件进行解析并演绎战斗过程。
#### mapObject 包
这里面主要实现了一些界面模块的模型，`BaseObject`作为所有`mapObject`的父类，可以作为引用载入到`BaseScreen`中，从而实现对界面的循环刷新。
#### protocol 包
实现了网络通信应用层的网络接口，接口`Msg`提供`send`和`parse`的方法，实现客户端和服务器端的数据传输。
#### utils 包
工具类包，实现了一个小的定时器类，主要用于实现人物进行一格格移动：
```java
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
```
## 测试
主要对类`DistanceHelper`寻找可移动方块的方法，以及`FileHelper`读入文件的方法进行测试。
```java
@Test
    public void testDisHelper(){
    DistanceHelper helper = new DistanceHelper();
    BlockObject curPlace = new BlockObject(0,0,null);
    ArrayList<BlockObject> res = helper.getMovableDis(curPlace,8);
    int[][] map = new int[8][15];
    System.out.println(res.size());
    for (BlockObject b : res){
        map[b.getVY()][b.getVX()] = 1;
    }
    for (int i = 0;i < 8;i++){
        for (int j = 0 ;j < 15; j++){
            System.out.print(map[i][j] + " ");
        }
        System.out.println();
        }
    }

    @Test
    public void testFileHelper(){
        String filePath = "../src/main/resources/test.xml";
        new GameHelper().getAutoPlayer().setFilePath(filePath);
    }
```
## 心得体会
这是我完成的第一个较为大型的项目，开发过程中遇到了许多坑，也有许多一直没有理解的地方，由于开发周期的原因，代码未来得及重构，所以可读性较差。
- 前面`JavaFx`界面的开发，由于是第一次接触，我在网上查找了一些设计模式的资料，但大部分都不太适合当前项目的开发，也花了一定时间理解整个`JavaFx`的架构。
- 关于网络通信的问题也困扰了我很久，感谢**余萍老师**提供的网络编程参考实例，让我这个还没有学过计算机网络的小白有了下手了地方。
- 最后一段时间在思考如何将游戏数据保存为文件进行持久化输出，查找了一些资料后使用了我较容易理解的`xml`文件输出方式。
- 整个游戏架构中还有一些线程同步的问题还没有解决，比如假如两个客户端同时发送了角色死亡的信息，服务器将优先处理谁。对此我选择忽略一个客户端发送的信息（在我的设计架构中，两个客户端发送的死亡信息应该是一样的），这就不太符合多线程编程的处理方法。
- 最后，感谢**曹春老师**、**余萍老师**还有助教**梁寓飞学长**，在这充实的一个学期学习中，我收获了许多**Java编程知识**和**设计模式知识**，虽然这是我的一门跨院系修选课，我也花费了不少于必修课的精力在这之上，能够较为完整地完成大作业，也让我收获感满满。