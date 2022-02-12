package Server;

import Result.TcpResult;
import Protocol.MyTcpProtocol;
import TestData.RequestExampleHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MyTcpServer {

    public static Logger logger = Logger.getLogger("Tcp Server");
    public static MyTcpProtocol tcpProtocol = new MyTcpProtocol();

    public Map<String, String> map;
    public MyTcpServer(Map<String, String> map) {
        this.map = map;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        MyTcpServer tcpServer = new MyTcpServer(map);

        String port = RequestExampleHandler.getInstance().getProperty("PORT");
        int portNumber = Integer.parseInt(port);

        logger.info("Server activated...\n");
        System.out.println("Server activated...\n");

        TcpResult res = new TcpResult();

        try {
            ServerSocket server = new ServerSocket(portNumber);
            //  keep detect client
            while (true) {
                Socket client = server.accept();
                DataInputStream input = new DataInputStream(client.getInputStream());
                String clientMessage = input.readUTF();
                logger.fine("Message from client: " + clientMessage);
                System.out.println("Message from client: " + clientMessage);

                if (!clientMessage.equals("")) {
                    res = tcpProtocol.fromClient(client, clientMessage, tcpServer.map);
                }

                // if operation is successful
                if (res.result) {
                    tcpProtocol.toClient(res.client, res.type, res.key, res.returnMsg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
