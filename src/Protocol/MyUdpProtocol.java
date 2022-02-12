package Protocol;

import Result.TcpResult;
import Controller.UdpController;
import Result.UdpResult;
import TestData.RequestExampleHandler;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.logging.Logger;

public class MyUdpProtocol {

    public static Logger logger = Logger.getLogger("MyUdpProtocol");
    public static UdpController udpController = new UdpController();
    public MyUdpProtocol(){}

    public String[] processMsg(String string) {
        return string.split(",", 2);
    }

    public UdpResult fromClient(DatagramSocket socket,
                             DatagramPacket dataPacket,
                             Map<String, String> map,
                             String clientMessage){
        // Request type
        String requestType = clientMessage.substring(0, clientMessage.indexOf(" "));
        // Request messages
        String msgContent = clientMessage.substring(clientMessage.indexOf(" ") + 1);

        // receive result from controller
        TcpResult res = new TcpResult();

        if (requestType.equalsIgnoreCase("PUT")) {
            return udpController.udpPut(socket, dataPacket, map, msgContent);
        } else if (requestType.equalsIgnoreCase("GET")) {
            return udpController.udpGet(socket, dataPacket, map, msgContent);
        } else if (requestType.equalsIgnoreCase("DELETE")) {
            return udpController.udpDelete(socket, dataPacket, map, msgContent);
        } else {
            System.out.println("Request error");
            return new UdpResult(false, "N/A", "N/A", "FAIL", socket);
        }


    }

    public void toClient(DatagramSocket socket,
                         DatagramPacket request,
                         String requestType,
                         String key,
                         String returnMsg,
                         boolean isSuccessful){
        if (isSuccessful) {
            System.out.println("Operation is successful");
            System.out.println("Send message back to client...");
            try {
                String sendBack = "";
                if (!returnMsg.equals("") && requestType.equalsIgnoreCase("GET")) {
                    sendBack = "Retrieved value with key (" + key + ") from server: " + returnMsg;
                } else {
                    sendBack = requestType + " with key: " + key + " is SUCCESS";
                }
                DatagramPacket sendBackPacket = new DatagramPacket(sendBack.getBytes(),
                        sendBack.getBytes().length,
                        request.getAddress(),
                        request.getPort());
                // send client feedback
                socket.send(sendBackPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Sending is successful");
            System.out.println("===========================================");
        } else {
            try {
                byte[] ackMessage = new byte[500];
                ackMessage = ("Request FAILED due to: " + returnMsg).getBytes();
                DatagramPacket ackMsgPacket = new DatagramPacket(ackMessage, ackMessage.length, request.getAddress(),
                        request.getPort());
                socket.send(ackMsgPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fromServer(DatagramSocket client){
        try {
            // set time out error
            String timeOut = RequestExampleHandler.getInstance().getProperty("CLIENT_SOCKET_TIMEOUT");
            client.setSoTimeout(Integer.parseInt(timeOut));

            // get massage from server
            byte[] msgBuffer = new byte[1024];
            DatagramPacket returnPacket = new DatagramPacket(msgBuffer, msgBuffer.length);

            client.receive(returnPacket);
            msgBuffer = returnPacket.getData();
            int nlen = returnPacket.getLength();
            String message = new String(msgBuffer, 0, nlen, "ISO-8859-1");

            System.out.println("Message from server:\n" + message);
            System.out.println("===========================================");

        } catch (SocketTimeoutException e) {
            System.out.println("Timeout error");
            System.out.println("===========================================");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void toServer(String token, InetAddress hostName, int portNumber){
        String[] processMsg = processMsg(token);
        // get request type(PUT, GET, DEL)
        String type = processMsg[0];
        // get request content
        String content = processMsg[1];

        DatagramSocket client = null;
        try {
            // client send msg to server
            client = new DatagramSocket();
            String clientMsg = type + " " + content;
            DatagramPacket clientPacket = new DatagramPacket(clientMsg.getBytes(),
                                                                clientMsg.length(),
                                                                hostName,
                                                                portNumber);
            String sendMsg = new String(clientPacket.getData(), clientPacket.getOffset(), clientPacket.getLength());
            logger.config("Data sent from client: " + sendMsg);
            System.out.println("Data sent from client: " + sendMsg);
            // send PUT request to server
            client.send(clientPacket);

            // client receive msg from server
            fromServer(client);
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close server
            client.close();
        }
    }
}
