package Protocol;

import Controller.Result;
import Controller.UdpController;
import Controller.UdpResult;
import Test.RequestExampleHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
        Result res = new Result();

        if (requestType.equalsIgnoreCase("PUT")) {
            return udpController.udpPut(socket, dataPacket, map, clientMessage);
        } else if (requestType.equalsIgnoreCase("GET")) {
            return udpController.udpGet(socket, dataPacket, map, clientMessage);
        } else if (requestType.equalsIgnoreCase("DELETE")) {
            return udpController.udpDelete(socket, dataPacket, map, clientMessage);
        } else {
            System.out.println("Request error");
            return new UdpResult(false, "N/A", "N/A", "FAIL", socket);
        }


    }

    public void toClient(Socket client, String requestType, String key, String returnMsg){

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
            String clientMsg = type + " " + token;
            DatagramPacket clientPacket = new DatagramPacket(clientMsg.getBytes(),
                                                                clientMsg.length(),
                                                                hostName,
                                                                portNumber);
            String sendMsg = new String(clientPacket.getData(), clientPacket.getOffset(), clientPacket.getLength());
            logger.config("Data sent from client: " + content);
            System.out.println("Data sent from client: " + content);
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
