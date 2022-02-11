package Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.Objects;

public class UdpController{

    public UdpController(){}

    public UdpResult udpPut(DatagramSocket socket,
                    DatagramPacket clientPacket,
                    Map<String, String> map,
                    String msg) {
        System.out.println("\nInvoke tcp PUT operation...");
        // Message format should be
        // RequestType key, value
        // Example:
        // PUT name, Charles
        String msgContent = msg.substring(msg.indexOf(" ") + 1);

        if (!msgContent.equals("")) {
            String key = msgContent.substring(0, msgContent.indexOf(","));
            String value = msgContent.substring(msgContent.indexOf(",") + 1);
            System.out.println("key is " + key);
            System.out.println("value is " + value);

            if (!key.equals("")) {
                map.put(key, value);
                sendToClient(socket, clientPacket, "PUT", key, "");

            } else {
                String failureMsg = "Received a malformed request of length: " + clientPacket.getLength() + " from: "
                        + clientPacket.getAddress() + " at Port: " + clientPacket.getPort();
                sendFailureToClient(socket, clientPacket, failureMsg);
            }

        } else {
            String failureMsg = "The message content is not present.";
            sendFailureToClient(socket, clientPacket, failureMsg);
        }

    }

    public UdpResult udpGet(DatagramSocket socket,
                    DatagramPacket clientPacket,
                    Map<String, String> map,
                    String msg) {
        System.out.println("\nInvoke tcp GET operation...");
        // Message format should be
        // RequestType key
        // Example:
        // GET name
        String msgContent = msg.substring(msg.indexOf(" ") + 1);
        System.out.println("key is " + msgContent);
        if (!msgContent.equals("")) {
            if (map.containsKey(msgContent)) {
                String retrievedMsg = map.get(msgContent);
                sendToClient(socket, clientPacket, "GET", msgContent, retrievedMsg);
            } else {
                String failureMsg = "There is no key-value pair for key: " + msgContent;
                sendFailureToClient(socket, clientPacket, failureMsg);
            }
        } else {
            String failureMsg = "The message content is not present.";
            sendFailureToClient(socket, clientPacket, failureMsg);
        }

    }

    public UdpResult udpDelete(DatagramSocket socket,
                          DatagramPacket clientPacket,
                          Map<String, String> map,
                          String msg) {

        System.out.println("\nInvoke tcp DELETE operation...");
        // Message format should be
        // RequestType key
        // Example:
        // DELETE name
        String msgContent = msg.substring(msg.indexOf(" ") + 1);
        System.out.println("key is " + msgContent);

        if (!msgContent.equals("")) {
            if (map.containsKey(msgContent)) {
                String retrievedMsg = map.get(msgContent);
                sendToClient(socket, clientPacket, "DELETE", msgContent, retrievedMsg);
            } else {
                String failureMsg = "There is no key-value pair for key: " + msgContent;
                sendFailureToClient(socket, clientPacket, failureMsg);
                System.out.println("Operation is failed");
                System.out.println("===========================================");
            }
        } else {
            String failureMsg = "The message content is not present.";
            sendFailureToClient(socket, clientPacket, failureMsg);
        }
    }


    public void sendToClient(DatagramSocket socket,
                             DatagramPacket request,
                             String requestType, String key,
                             String returnMsg) {
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
    }

    public void sendFailureToClient(DatagramSocket socket, DatagramPacket request, String returnMsg) {
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
