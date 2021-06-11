import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MatchHandler {
    ServerSocket serverSocket;
    Socket host;
    Socket client;

    public MatchHandler(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        host = serverSocket.accept();
        client = serverSocket.accept();
        handleSocket();
    }

    public void handleSocket() throws IOException{
        NetworkHelper.write("HOST", host.getOutputStream());
        NetworkHelper.write("CLIENT", client.getOutputStream());
        while (!host.isClosed() && !client.isClosed()){
            String hostMove = NetworkHelper.msgRead(host.getInputStream());
            String[] hostArgs = hostMove.split(" ");
            if (hostArgs[0] == "CLOSE"){
                break;
            }
            NetworkHelper.write(hostMove, client.getOutputStream());

            String clientMove = NetworkHelper.msgRead(client.getInputStream());
            String[] clientArgs = clientMove.split(" ");
            if (clientArgs[0] == "CLOSE"){
                break;
            }
            NetworkHelper.write(clientMove,host.getOutputStream());
        }
        host.close();
        client.close();
    }
}
