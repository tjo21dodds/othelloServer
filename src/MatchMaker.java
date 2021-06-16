import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import sun.nio.ch.Net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchMaker {

    final int serverPort = 1700;


    AtomicInteger nextPort = new AtomicInteger(1701);

    ServerSocket serverSocket;
    private ArrayList<MatchHandler> hosts = new ArrayList<>(); //List of sockets waiting for a game.

    private void handleHost(Socket socket) throws IOException {
        // Inform host of new room.
        // : Composing Message
        Integer port = nextPort.incrementAndGet();
        String msg = "ROOM " + port;
        ServerSocket serverSocket = new ServerSocket(port);
        NetworkHelper.write(msg, socket.getOutputStream());
        MatchHandler matchHandler = new MatchHandler(serverSocket, hosts);


    }

    public void handleClient(Socket socket) throws IOException {
        // Send list of hosts
        String msg = "ROOMS";
        for (Iterator it = hosts.iterator(); it.hasNext(); ) {
            MatchHandler matchHandler = (MatchHandler) it.next();
            msg = msg + " ";
            msg = msg + String.valueOf(matchHandler.serverSocket.getLocalPort());
        }
        NetworkHelper.write(msg, socket.getOutputStream());

    }

    public void handleServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverPort);
        while (!serverSocket.isClosed()) {

            Socket socket = serverSocket.accept();
            Thread thread = new Thread(() -> {
                try {
                    String msg = NetworkHelper.msgRead(socket.getInputStream());
                    String[] args = msg.split(" ");
                    switch (args[0]) {
                        case "HOST":
                            this.handleHost(socket);
                            break;
                        case "CLIENT":
                            this.handleClient(socket);
                            break;
                        default:
                            System.out.println("Handle server malformed message");
                            break;
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            thread.start();

        }
    }

    public static void main(String[] args) throws IOException{
        MatchMaker matchMaker = new MatchMaker();
        matchMaker.handleServer();
    }
}
