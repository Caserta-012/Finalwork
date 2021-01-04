import client.client.PlayerClient;
import server.Server;

public class Main{

    public static void main(String[] ags){
        Server.main(ags);
        PlayerClient.main(ags);
    }
}
