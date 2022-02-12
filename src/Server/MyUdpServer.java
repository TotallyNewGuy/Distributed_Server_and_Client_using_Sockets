package Server;

import Controller.UdpController;
import Result.UdpResult;
import Protocol.MyUdpProtocol;
import TestData.RequestExampleHandler;

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
    public static MyUdpProtocol udpProtocol = new MyUdpProtocol();

    public MyUdpServer(UdpController controller, Map<String, String> map) {
        this.map = map;
        this.controller = controller;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        UdpController controller = new UdpController();
        MyUdpServer udpServer = new MyUdpServer(controller, map);

        String port = RequestExampleHandler.getInstance().getProperty("PORT");
        int portNumber = Integer.parseInt(port);

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

                UdpResult res = new UdpResult();

                if (!clientMessage.equals("")) {
                    res = udpProtocol.fromClient(socket, dataPacket, udpServer.map, clientMessage);
                }

                if (res.result) {
                    udpProtocol.toClient(socket,
                            dataPacket,
                            res.type, res.key,
                            res.returnMsg, true);
                } else {
                    udpProtocol.toClient(socket,
                            dataPacket,
                            res.type, res.key,
                            res.returnMsg, false);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
