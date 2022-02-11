package Server;

import Controller.TcpController;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyTcpServer {

    public Map<String, String> map;
    TcpController controller;
    public MyTcpServer(TcpController controller, Map<String, String> map) {
        this.map = map;
        this.controller = controller;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        TcpController controller = new TcpController();
        MyTcpServer tcpServer = new MyTcpServer(controller, map);

        int portNumber = 8888;
        System.out.println("Server activated...\n");

        try {
            ServerSocket server = new ServerSocket(portNumber);
            //  keep detect client
            while (true) {
                Socket client = server.accept();
                DataInputStream input = new DataInputStream(client.getInputStream());
                String clientMessage = input.readUTF();
                System.out.println("Message from client: " + clientMessage);

                if (!clientMessage.equals("")) {
                    // Request type
                    String requestType = clientMessage.substring(0, clientMessage.indexOf(" "));
                    // Request messages
                    String msgContent = clientMessage.substring(clientMessage.indexOf(" ") + 1);

                    if (requestType.equalsIgnoreCase("PUT")) {
                        // PUT request
                        tcpServer.controller.tcpPut(client, msgContent, tcpServer.map);
                    }else if (requestType.equalsIgnoreCase("GET")) {
                        // GET request
                        tcpServer.controller.tcpGet(client, msgContent, tcpServer.map);
                    }else if (requestType.equalsIgnoreCase("DELETE")) {
                        // DELETE request
                        tcpServer.controller.tcpDelete(client, msgContent, tcpServer.map);
                    }else{
                        System.out.println("Request error");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
