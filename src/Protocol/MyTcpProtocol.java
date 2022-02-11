package Protocol;

import Controller.Result;
import Controller.TcpController;
import Test.RequestExampleHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.logging.Logger;

public class MyTcpProtocol {

    public static Logger logger = Logger.getLogger("My_TcpProtocol");
    public TcpController tcpController = new TcpController();

    public MyTcpProtocol(){}

    public String[] processMsg(String string) {
        return string.split(",", 2);
    }

    public Result fromClient(Socket client, String clientMessage, Map<String, String> map){
        // Request type
        String requestType = clientMessage.substring(0, clientMessage.indexOf(" "));
        // Request messages
        String msgContent = clientMessage.substring(clientMessage.indexOf(" ") + 1);

        // receive result from controller
        Result res = new Result();

        if (requestType.equalsIgnoreCase("PUT")) {
            // PUT request
            return tcpController.tcpPut(client, msgContent, map);
        }else if (requestType.equalsIgnoreCase("GET")) {
            // GET request
            return tcpController.tcpGet(client, msgContent, map);
        }else if (requestType.equalsIgnoreCase("DELETE")) {
            // DELETE request
            return tcpController.tcpDelete(client, msgContent, map);
        }else{
            logger.warning("Request error");
            System.out.println("Request error");
            return new Result(false, "N/A", "N/A", "FAIL", client);
        }
    }

    public void toClient(Socket client, String requestType, String key, String returnMsg){
        logger.fine("Operation is successful");
        logger.fine("Send message back to client...");
        System.out.println("Operation is successful");
        System.out.println("Send message back to client...");
        try {
            DataOutputStream outStream = new DataOutputStream(client.getOutputStream());
            if (!returnMsg.equals("") && requestType.equalsIgnoreCase("GET")) {
                outStream.writeUTF("Retrieved value with key (" + key + ") from server: " + returnMsg);
            } else {
                // send client feedback
                outStream.writeUTF(requestType + " with key: " + key + " is SUCCESS");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logger.fine("Sending is successful");
        System.out.println("Sending is successful");
        System.out.println("===========================================");
    }

    public void fromServer(Socket client){
        try {
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            String timeOut = RequestExampleHandler.getInstance().getProperty("CLIENT_SOCKET_TIMEOUT");
            client.setSoTimeout(Integer.parseInt(timeOut));
            String message = inputStream.readUTF();
            logger.config("Message from server:\n" + message);
            System.out.println("Message from server:\n" + message);
            System.out.println("===========================================");
        } catch (SocketTimeoutException e) {
            logger.warning("Timeout error");
            System.out.println("Timeout error");
            System.out.println("===========================================");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void toServer(String token, String hostName, int portNumber){
        String[] processMsg = processMsg(token);
        // get request type(PUT, GET, DEL)
        String type = processMsg[0];
        System.out.println(type);
        // get request content
        String content = processMsg[1];
        System.out.println(content);

        DataOutputStream outputStream = null;
        Socket client = null;
        try {
            // send message to Server
            client = new Socket(hostName, portNumber);
            outputStream = new DataOutputStream(client.getOutputStream());
            logger.config("Data sent from client: " + content);
            System.out.println("Data sent from client: " + content);
            // send right request type to server
            outputStream.writeUTF(type + " " + content);

            // get respond from server
            fromServer(client);

        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
