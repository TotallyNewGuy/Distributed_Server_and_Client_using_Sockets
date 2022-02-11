package Server;

import Controller.UdpController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyUdpServer {

    public static Logger LOGGER = Logger.getLogger("UdpServer");

    public Map<String, String> map;
    UdpController controller;
    public MyUdpServer(UdpController controller, Map<String, String> map) {
        this.map = map;
        this.controller = controller;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        UdpController controller = new UdpController();
        MyUdpServer udpServer = new MyUdpServer(controller, map);

        int portNumber = 8888;
        LOGGER.log(Level.WARNING, "Server activated...");
        System.out.println("Server activated...\n");

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(portNumber);

            byte[] msgBuffer = new byte[1024];
            while (true) {
                DatagramPacket dataPacket = new DatagramPacket(msgBuffer, msgBuffer.length);
                socket.receive(dataPacket);
                // receive message from client

                msgBuffer = dataPacket.getData();
                int nLen = dataPacket.getLength();
                String clientMessage = new String(msgBuffer, 0, nLen, "ISO-8859-1");
                System.out.println("Message from client: " + clientMessage);

                if (!clientMessage.equals("")) {
                    // Request type
                    String requestType = clientMessage.substring(0, clientMessage.indexOf(" "));
                    if (requestType.equalsIgnoreCase("PUT")) {
                        udpServer.controller.udpPut(socket, dataPacket, udpServer.map, clientMessage);
                    } else if (requestType.equalsIgnoreCase("GET")) {
                        udpServer.controller.udpGet(socket, dataPacket, udpServer.map, clientMessage);
                    } else if (requestType.equalsIgnoreCase("DELETE")) {
                        udpServer.controller.udpDelete(socket, dataPacket, udpServer.map, clientMessage);
                    } else {
                        System.out.println("Request error");
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
